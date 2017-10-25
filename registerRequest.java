package kr.co.company.myapp;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asdrt11 on 2017-09-19.
 */

public class registerRequest extends StringRequest {
    final static private String URL = "http://blak29.cafe24.com/RoomRegister.php";
    private Map<String, String> parameters;

    public registerRequest(String roomID, String roomPassword, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("roomID", roomID);
        parameters.put("roomPassword", roomPassword);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}