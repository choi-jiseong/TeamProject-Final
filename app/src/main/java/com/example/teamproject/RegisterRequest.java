package com.example.teamproject;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "";
    private Map<String, String> parameters;

    public RegisterRequest (String email, String password, String name, int id, String phone, String position, String class_room, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("name", name);
        parameters.put("password", password);
        parameters.put("id", id +"");
        parameters.put("phone", phone);
        parameters.put("position", position);
        parameters.put("class_room", class_room);

    }


    public Map<String, String> getParameters() {
        return parameters;
    }
}
