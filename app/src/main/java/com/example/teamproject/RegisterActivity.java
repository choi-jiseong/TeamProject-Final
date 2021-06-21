package com.example.teamproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private EditText email, password, password2, id, name, phone;
    private Spinner class_name;
    private RadioGroup radioGroup;
    private RadioButton rd;
    private String URL;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email = (EditText)findViewById(R.id.etRemail);
        password = (EditText)findViewById(R.id.etRpassword);
        password2 = (EditText)findViewById(R.id.etRpassword2);
        name = (EditText)findViewById(R.id.etRname);
        phone = (EditText)findViewById(R.id.etRphone);
        id = (EditText)findViewById(R.id.etRnumberId);
        class_name = findViewById(R.id.spinnerClass);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);


        //가입 버튼
        Button successBtn = findViewById(R.id.successBtn);
        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rd = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                if(!password.getText().toString().equals(password2.getText().toString()) ) {
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_LONG).show();
                    return;
                }else
                 if(phone.length() != 11) {
                    Toast.makeText(RegisterActivity.this, "올바른 전화번호를 입력해 주세요", Toast.LENGTH_LONG).show();
                }else if(name.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "이름을 입력해 주세요", Toast.LENGTH_LONG).show();
                } else if(id.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, "학번을 입력해 주세요", Toast.LENGTH_LONG).show();
                }else{
                    JSONObject jsonBody = new JSONObject();

                    try {
                        jsonBody.put("id", Integer.parseInt(id.getText().toString()));
                        jsonBody.put("name", name.getText().toString());
                        jsonBody.put("password", password.getText().toString());
                        jsonBody.put("email", email.getText().toString());
                        jsonBody.put("class_name", class_name.getSelectedItem().toString());
                        jsonBody.put("position", rd.getText().toString());
                        jsonBody.put("phone", phone.getText().toString());
                        final String requestBody = jsonBody.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    registerSubmit(jsonBody);
                }
            }

        });


        //중복확인 버튼
        Button checkBtn = findViewById(R.id.btnCheck);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonCheck = new JSONObject();

                try {
                    jsonCheck.put("email", email.getText().toString());

                    final String requestBody = jsonCheck.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                emailCheck(jsonCheck);
            }
        });

        // Spinner
        Spinner yearSpinner = (Spinner)findViewById(R.id.spinnerClass);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.classChoose, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);



    }


    //가입 POST
    private void registerSubmit(JSONObject data){

        URL = "";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.i("VOLLEY", String.valueOf(response));
                    Boolean success = response.getBoolean("success");
                    if(success){
                        Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        RegisterActivity.this.startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_LONG).show();
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

    //중복확인 POST
    private void emailCheck(JSONObject data){


        URL = "";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, data, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("VOLLEY", String.valueOf(response));
                    Boolean success = response.getBoolean("success");
                    if(success){
                        AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                        dialog=builder.setMessage("사용할 수 있는 아이디입니다.")
                                .setPositiveButton("확인",null)
                                .create();
                        dialog.show();
                    }else{
                        AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                        dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                .setNegativeButton("확인",null)
                                .create();
                        dialog.show();
                        email.setText("");
                    }
                }catch (JSONException e){
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
