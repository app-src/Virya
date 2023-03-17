package com.scammer101.Virya.Utilities;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DateTimeOnline {


    private Activity activity;
    private String url = "https://www.timeapi.io/api/Time/current/coordinate?latitude=22.5726&longitude=88.3639" ;
    private RequestQueue requestQueue;



    public DateTimeOnline(Activity activity)
    {
        this.activity = activity;

        requestQueue = Volley.newRequestQueue(activity);

    }

    public void getDateTime(VolleyCallBack volleyCallBack)
    {
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    volleyCallBack.onGetDateTime(response.getString("date"), response.getString("time") );
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });

        requestQueue.add(request);

    }

    public interface VolleyCallBack
    {
        void onGetDateTime(String date, String time);
    }


}
