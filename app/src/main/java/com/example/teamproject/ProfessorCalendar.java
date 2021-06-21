package com.example.teamproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfessorCalendar extends Fragment {
    private View view;
    private static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.professor_calendar, container, false);


        //받아온 데이터
        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String position = bundle.getString("position");
        String class_name = bundle.getString("class_name");
        String jwt = bundle.getString("jwt");
        token(jwt);
        if(bundle != null){


    }
        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = i + "-" + (i1 + 1) + "-" + i2;
                Log.d(TAG, "onSelectedDayChange: mm/dd/yyyy: " + date);
                Intent intent = new Intent(getActivity(), ProfessorStatusActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("class_name", class_name);
                intent.putExtra("position", position);
                intent.putExtra("email", email);
                intent.putExtra("jwt", jwt);
                intent.putExtra("date", date);
                startActivity(intent);

            }
        });
        return view;
    }
    private void token(String data){

        String URL = "";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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

