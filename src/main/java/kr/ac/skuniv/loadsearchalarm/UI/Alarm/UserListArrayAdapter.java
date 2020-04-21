package kr.ac.skuniv.loadsearchalarm.UI.Alarm;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kr.ac.skuniv.loadsearchalarm.R;
import kr.ac.skuniv.loadsearchalarm.UI.DaumMap;
import kr.ac.skuniv.loadsearchalarm.core.domain.Alarm;


public class UserListArrayAdapter extends ArrayAdapter<Alarm> {
    private LayoutInflater layoutInflater;
    public UserListArrayAdapter(@NonNull Context context) {
        super(context, R.layout.row_user_list);
        layoutInflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view=convertView;
        if(view==null){
            view=layoutInflater.inflate(R.layout.row_user_list,parent,false);
        }
        Alarm user=getItem(position);

        ((TextView)view.findViewById(R.id.no)).setText(user.getNo()+"");
        ((TextView)view.findViewById(R.id.sd)).setText(user.getStartDeparture());
        ((TextView)view.findViewById(R.id.ed)).setText(user.getEndDeparture());
        ((TextView)view.findViewById(R.id.st)).setText(user.getStartTime());
        ((TextView)view.findViewById(R.id.realway)).setText(user.getWay());
        if("도보".equals(user.getWay())){
            ((ImageView)view.findViewById(R.id.imageway)).setImageDrawable(getContext().getDrawable(R.drawable.ic_work));
        }else if("대중교통".equals(user.getWay())){
            ((ImageView)view.findViewById(R.id.imageway)).setImageDrawable(getContext().getDrawable(R.drawable.ic_bus));
        }else if("차".equals(user.getWay())){
            ((ImageView)view.findViewById(R.id.imageway)).setImageDrawable(getContext().getDrawable(R.drawable.ic_car));
        }
        return view;
    }
    public  void add(List<Alarm> list){
        if(list==null){
            return;
        }
        for(Alarm alarm:list){
            add(alarm);
        }
    }
}
