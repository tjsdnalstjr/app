package kr.ac.skuniv.loadsearchalarm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;
import java.util.Calendar;

import kr.ac.skuniv.loadsearchalarm.core.AlarmNotifyReceiver;
import kr.ac.skuniv.loadsearchalarm.core.network.SafeAsyncTask;

import static kr.ac.skuniv.loadsearchalarm.R.id.startdeparture;

public class InputForm extends AppCompatActivity{

    static public String way=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_form);

        Spinner spinner=(Spinner)findViewById(R.id.way) ;
        String[] waylist={"도보","대중교통","차"};
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.spinner,R.layout.spinner_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                way =(String)parent.getItemAtPosition(position);
                System.out.println("----------------------"+way);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((Button)findViewById(R.id.reserve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FatchUserListAsyncTask().execute();
                int year,month,day,hour,minute;
                String pt=((TextView)findViewById(R.id.promiseday)).getText().toString();
                String st=((TextView)findViewById(R.id.starttime)).getText().toString();
                int a=pt.indexOf("년");
                int b=pt.indexOf("월");
                int c=pt.indexOf("일");
                int d=st.indexOf("시");
                int e=st.indexOf("분");
                year=Integer.parseInt(pt.substring(0,a));
                month=Integer.parseInt(pt.substring(a+1,b));
                day=Integer.parseInt(pt.substring(b+1,c));
                hour=Integer.parseInt(st.substring(0,d));
                minute=Integer.parseInt(st.substring(d+1,e));
                startAlarm(year,month,day,hour,minute);
                finish();
                Intent intent=new Intent(InputForm.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void startAlarm(int year,int month,int day,int hour,int minute){
        Calendar cal = Calendar.getInstance();
        cal.set( Calendar.YEAR, year );
        cal.set( Calendar.MONTH, month );
        cal.set( Calendar.DATE, day );
        cal.set( Calendar.HOUR, hour );
        cal.set( Calendar.MINUTE, minute );
        cal.getTimeInMillis();
        System.out.println("-------------------------->cal"+cal.getTimeInMillis());


        AlarmManager manager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent;
        PendingIntent pendingIntent;
        intent=new Intent(InputForm.this, AlarmNotifyReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+5000,pendingIntent);
        System.out.println("+==================================+"+SystemClock.elapsedRealtime());
    }
    private class FatchUserListAsyncTask extends SafeAsyncTask<String> {
        @Override
        public String call() throws Exception {
            String sd=((EditText)findViewById(startdeparture)).getText().toString();
            System.out.println("---------------------->>>>"+sd);
            String ed=((EditText)findViewById(R.id.enddeparture)).getText().toString();
            String pd=((TextView)findViewById(R.id.promiseday)).getText().toString();
            String pt=((TextView)findViewById(R.id.promisetime)).getText().toString();
            String st=((TextView)findViewById(R.id.starttime)).getText().toString();

            String url="http://192.168.43.233:8080/mysite/api/alarm?a=alarminsert";
            String query="sd="+sd+"&ed="+ed+"&pd="+pd+"&pt="+pt+"&st="+st+"&way="+InputForm.way;
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
    public void dialogDatePicker(View view){
        Calendar calendar=Calendar.getInstance();

        DatePickerDialog dpd=new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ((TextView)findViewById(R.id.promiseday)).setText(year+"년"+(monthOfYear+1)+"월"+dayOfMonth+"일");
            }
        },calendar.get(calendar.YEAR),calendar.get(calendar.MONTH),calendar.get(calendar.DATE)
        );
        dpd.show();
    }

    public void startdialogTimePicker(View view){
        Calendar calendar=Calendar.getInstance();
        TimePickerDialog tpd=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                ((TextView)findViewById(R.id.starttime)).setText(hourOfDay+"시"+minute+"분");
            }
        },calendar.get(calendar.HOUR),calendar.get(calendar.MINUTE),true);
        tpd.show();

    }
}
