package com.example.teamproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfessorStatusActivity extends AppCompatActivity {
    private  int id;
    private String data;
    private String URL, stdId, stdName, stdStatus;
    private RequestQueue requestQueue;
    private JSONArray resData;
    private ArrayList<ProfessorStatusModel> models = new ArrayList<>();

    RecyclerView recycler_view;
    ProfessorStatusAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_status);


        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        id = Integer.parseInt(intent.getExtras().getString("id"));
        String class_name = intent.getExtras().getString("class_name");
        String position = intent.getExtras().getString("position");
        String email = intent.getExtras().getString("email");
        data = intent.getExtras().getString("date");
        String jwt = intent.getExtras().getString("jwt");
        token(jwt);
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("date", data);
            final String requestBody = jsonBody.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("--------------"+jsonBody);
        statusGet(jsonBody);

        recycler_view = findViewById(R.id.recycler_view);

//        try {
//            setRecyclerView();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setRecyclerView() throws JSONException {

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProfessorStatusAdapter(this,getList());
        recycler_view.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private List<ProfessorStatusModel> getList() throws JSONException {
        List<ProfessorStatusModel> status_list = new ArrayList<>();

        for( int i = 0 ; i < models.size(); i++ ) {
            System.out.println(models.size());
            status_list.add(models.get(i));

        }
        return  status_list;
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
                        resData = response.getJSONArray("resData");

                        System.out.println(resData);
                        System.out.println(resData.length());
                        for(int i=0; i< resData.length(); i++){
                            for(int j=0; j< resData.getJSONArray(i).length(); j++){
                                stdId = Objects.toString(resData.getJSONArray(i).get(5));
                                stdName = Objects.toString(resData.getJSONArray(i).get(6));
                                stdStatus = Objects.toString(resData.getJSONArray(i).get(4));
                                Log.i("data", stdId + ", " + stdName + ", " + stdStatus);
                            }
                            models.add(i, new ProfessorStatusModel(stdId, stdName, stdStatus));
                        }
                        System.out.println(models.toString());


                        try {
                            setRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
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