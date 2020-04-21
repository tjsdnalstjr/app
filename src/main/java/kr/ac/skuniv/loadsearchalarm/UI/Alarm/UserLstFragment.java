package kr.ac.skuniv.loadsearchalarm.UI.Alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import kr.ac.skuniv.loadsearchalarm.InputForm;
import kr.ac.skuniv.loadsearchalarm.MainActivity;
import kr.ac.skuniv.loadsearchalarm.R;
import kr.ac.skuniv.loadsearchalarm.core.domain.Alarm;
import kr.ac.skuniv.loadsearchalarm.core.network.SafeAsyncTask;
import kr.ac.skuniv.loadsearchalarm.core.provider.UserProvider;

/**
 * Created by cs618 on 2017-07-18.
 */

public class UserLstFragment extends ListFragment {
    private UserListArrayAdapter userListArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        userListArrayAdapter=new UserListArrayAdapter(getActivity());
        setListAdapter( userListArrayAdapter );

        return inflater.inflate(R.layout.fragment_user_list,container,false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new FatchUserListAsyncTask().execute();
        getActivity().findViewById(R.id.addalarmbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), InputForm.class);
                startActivity(intent);
            }
        });

        ListView lv=((ListView)getView().findViewById(android.R.id.list));

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity()).setTitle("삭제하시겠습니까?").setIcon(android.R.drawable.ic_delete).setView(R.layout.delete_dialog).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteAlarm().execute();
                        FragmentTransaction ft=getFragmentManager().beginTransaction();
                        ft.detach(UserLstFragment.this).attach(UserLstFragment.this).commit();
                    }
                }).show();

                return true;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String sd=((TextView)view.findViewById(R.id.sd)).getText().toString();
                String ed=((TextView)view.findViewById(R.id.ed)).getText().toString();
                String way=((TextView)view.findViewById(R.id.realway)).getText().toString();
                double sdlat=0;
                double sdlon=0;
                double edlat=0;
                double edlon=0;
                StringBuffer result = null;
                Geocoder coder=new Geocoder(getContext());
                List<Address> sdlist=null;
                List<Address> edlist=null;
                try {
                    sdlist=coder.getFromLocationName(sd, 1);
                    edlist=coder.getFromLocationName(ed,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }// 몇개 까지의 주소를 원하는지 지정 1~5개 정도가 적당
                if (sd != null&&ed!=null) {
                    for (int i = 0; i < edlist.size(); i++) {
                        Address startlating = sdlist.get(i);
                        Address endlating = edlist.get(i);
                        sdlat = startlating.getLatitude(); // 위도가져오기
                        sdlon = startlating.getLongitude(); // 경도가져오기
                        edlat = endlating.getLatitude(); // 위도가져오기
                        edlon = endlating.getLongitude(); // 경도가져오기
                    }
                }
                if("도보".equals(way)) {
                    result = new StringBuffer("daummaps://route?sp=");
                    result.append(sdlat + "," + sdlon + "&ep=" + edlat + "," + edlon + "&by=FOOT");
                }else if("대중교통".equals(way)){
                    result = new StringBuffer("daummaps://route?sp=");
                    result.append(sdlat + "," + sdlon + "&ep=" + edlat + "," + edlon + "&by=PUBLICTRANSIT");
                }else if("차".equals(way)){
                    result = new StringBuffer("daummaps://route?sp=");
                    result.append(sdlat + "," + sdlon + "&ep=" + edlat + "," + edlon + "&by=CAR");
                }
                Uri uri=Uri.parse(result.toString());
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                startActivity(intent);
            }
        });

    }
    private class DeleteAlarm extends SafeAsyncTask<String> {
        @Override
        public String call() throws Exception {
            String no=((TextView)getActivity().findViewById(R.id.no)).getText().toString();
            System.out.println("---------------------------------"+no);
            String url="http://192.168.43.233:8080/mysite/api/alarm?a=alarmdelete";
            String query="no="+no;
            HttpRequest request=HttpRequest.post(url);

            request.accept( HttpRequest.CONTENT_TYPE_JSON );
            request.connectTimeout( 1000 );
            request.readTimeout( 3000 );
            request.send(query);

            int responseCode = request.code();
            if ( responseCode != HttpURLConnection.HTTP_OK  ) {
                    /* 에러 처리 */
                System.out.println("---------------------ERROR");
                return null;
            }
            String result=new GsonBuilder().create().fromJson(request.bufferedReader(),String.class);
            return null;
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            super.onException(e);
            System.out.println("----------->exception: "+e);
        }
        @Override
        protected void onSuccess(String result) throws Exception {
            super.onSuccess(result);
            System.out.println("------------------------------------------->>>>");
        }
    }
    private class FatchUserListAsyncTask extends SafeAsyncTask<List<Alarm>> {

        @Override
        public List<Alarm> call() throws Exception {
            List<Alarm> alarms=new UserProvider().FatchUserList();
            return alarms;
        }

        @Override
        protected void onException(Exception e) throws RuntimeException {
            super.onException(e);


            Log.e("FatchUserListAsyncTask","arror"+e);
        }

        @Override
        protected void onSuccess(List<Alarm> alarms) throws Exception {
            super.onSuccess(alarms);
            for(Alarm user:alarms){
                System.out.println(user);
            }
            userListArrayAdapter.add(alarms);
            getView().findViewById(R.id.progress).setVisibility(View.GONE);
        }
    }
}
