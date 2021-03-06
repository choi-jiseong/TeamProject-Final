package com.example.teamproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfessorActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ProfessorHome professorHome;
    private ProfessorCalendar professorCalendar;
    private ProfessorMy professorMy;
    private Bundle bundle;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        professorHome = new ProfessorHome();
        professorCalendar = new ProfessorCalendar();
        professorMy = new ProfessorMy();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String position = intent.getStringExtra("position");
        String class_name = intent.getStringExtra("class_name");
        String jwt = intent.getStringExtra("jwt");

        token(jwt);

        bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("class_name", class_name);
        bundle.putString("email", email);
        bundle.putString("id", id);
        bundle.putString("position", position);
        bundle.putString("jwt", jwt);
        professorHome.setArguments(bundle);
        professorCalendar.setArguments(bundle);
        professorMy.setArguments(bundle);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        setFrag(0);
                        break;
                    case R.id.navigation_calendar:
                        setFrag(1);
                        break;
                    case R.id.navigation_identity:
                        setFrag(2);
                        break;

                }
                return true;
            }
        });


        setFrag(0); //??? ??????????????? ????????? ???????????? ???????????? ????????? ??????
    }

    //??????????????? ????????? ???????????? ?????????
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.frameLayout, professorHome);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.frameLayout, professorCalendar);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.frameLayout, professorMy);
                ft.commit();
                break;

        }
    }
    //token auth
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
}