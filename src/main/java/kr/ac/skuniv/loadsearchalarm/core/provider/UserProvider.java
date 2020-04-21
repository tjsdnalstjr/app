package kr.ac.skuniv.loadsearchalarm.core.provider;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import kr.ac.skuniv.loadsearchalarm.core.domain.Alarm;


public class UserProvider {
    public List<Alarm> FatchUserList(){


        String url="http://192.168.43.233:8080/mysite/api/alarm?a=list";
        HttpRequest request=HttpRequest.get(url);

        request.contentType( HttpRequest.CONTENT_TYPE_JSON );
        request.accept( HttpRequest.CONTENT_TYPE_JSON );
        request.connectTimeout( 1000 );
        request.readTimeout( 3000 );
        int responseCode = request.code();
        if ( responseCode != HttpURLConnection.HTTP_OK  ) {
           throw new RuntimeException("HTTP Response Exception : "+responseCode);
        }
        JSONResultFatchUserList result=new GsonBuilder().create().fromJson(request.bufferedReader(),JSONResultFatchUserList.class);
        if("success".equals(result.getResult())==false){
            throw new RuntimeException("JSONResultFatchUserList Response Exception: "+result.getMessage());
        }
        return result.getData();

    }
    private class JSONResultFatchUserList extends JsonResult<List<Alarm>>{}
}
