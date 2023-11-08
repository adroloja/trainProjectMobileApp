package com.adrianj.trainapp.controller;

import android.content.Context;
import android.util.Log;

import com.adrianj.trainapp.general.Global;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LocationController {

    private Context context;

    public LocationController(Context context){

        this.context = context;
    }

    public void sendLocation(JSONObject jsonObject){

        String url = Global.BASEURL + "insertLocation";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("hola", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
