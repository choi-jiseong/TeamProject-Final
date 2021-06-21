package com.example.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         final EditText etEmail = (EditText)findViewById(R.id.etEmail);
         final EditText etPassword = (EditText)findViewById(R.id.etPassword);
         Button loginBtn = findViewById(R.id.btnLogin);


         //회원가입 버튼
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
//                Toast.makeText(MainActivity.this, "회원가입", Toast.LENGTH_LONG).show();
              Intent in = new Intent(MainActivity.this, RegisterActivity.class);
              startActivity(in);
        });

        //로그인 버튼
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            final String email = etEmail.getText().toString();
            final String password = etPassword.getText().toString();
            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("email", email);
                jsonBody.put("password", password);
                final String requestBody = jsonBody.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("--------------"+jsonBody);
            loginSubmit(jsonBody);


        });
    }
    private void loginSubmit(JSONObject data){

        URL = "s";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.i("VOLLEY", String.valueOf(response));


                    Boolean success = response.getBoolean("success");
                    if(success){
                        JSONObject resData = response.getJSONObject("resData");
                        //StudentActivity 로 데이터 보내기
                        String id = resData.get("id").toString();
                        String email = resData.get("email").toString();
                        String class_name = resData.get("class_name").toString();
                        String position = resData.get("position").toString();
                        System.out.println(position);
                        String jwt = resData.get("jwt").toString();
                        String name = resData.get("name").toString();
                        Intent in2 = new Intent(MainActivity.this, StudentActivity.class);
                        Intent in3 = new Intent(MainActivity.this, ProfessorActivity.class);

                        if(position.equals("학생") ){
                            in2.putExtra("email", email);
                            in2.putExtra("id", id);
                            in2.putExtra("name", name);
                            in2.putExtra("class_name", class_name);
                            in2.putExtra("position", position);
                            in2.putExtra("jwt", jwt);
                            startActivity(in2);
                        }else if (position.equals("교수")){
                            in3.putExtra("email", email);
                            in3.putExtra("id", id);
                            in3.putExtra("name", name);
                            in3.putExtra("class_name", class_name);
                            in3.putExtra("position", position);
                            in3.putExtra("jwt", jwt);
                            startActivity(in3);
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_LONG).show();
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
