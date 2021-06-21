package com.example.teamproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StudentStatusActivity extends AppCompatActivity {
    private TextView date, am, pm, leave;
    private String URL;
    private RequestQueue requestQueue;
    int id = 0;
    String data = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_status);
        date = findViewById(R.id.date);
        am = findViewById(R.id.am);
        pm = findViewById(R.id.pm);
        leave = findViewById(R.id.leave);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        id = Integer.parseInt(intent.getExtras().getString("id"));
        String class_name = intent.getExtras().getString("class_name");
        String position = intent.getExtras().getString("position");
        String email = intent.getExtras().getString("email");
        data = intent.getExtras().getString("date");
        String jwt = intent.getExtras().getString("jwt");
        token(jwt);
        date.setText(data);

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("id", id);
            jsonBody.put("date", data);
            final String requestBody = jsonBody.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("--------------"+jsonBody);
        statusGet(jsonBody);
    }
    private void token(String data){

        String URL = "";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("VOLLEY", String.valueOf(response));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + data);

                Log.i("VOLLEY", String.valueOf(params));
                return params;
            }
        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

    }

    private void statusGet (JSONObject data){

        URL = "";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, data, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.i("VOLLEY", String.valueOf(response));


                    Boolean success = response.getBoolean("success");
                    if(success){
                        JSONArray resData = response.getJSONArray("resData");

                        System.out.println(resData);
                        for(int i=0; i< resData.length(); i++) {
                            for(int j=0; j< resData.getJSONArray(i).length(); j++){
                                am.setText(Objects.toString(resData.getJSONArray(i).get(2)));
                                pm.setText(Objects.toString(resData.getJSONArray(i).get(1)));
                                leave.setText(Objects.toString(resData.getJSONArray(i).get(3)));
                            }
                        }

                    }else{
                        return;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);

    }
}