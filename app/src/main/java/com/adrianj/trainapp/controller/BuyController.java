package com.adrianj.trainapp.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.general.Global;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuyController {

    private Context context;

    public BuyController(Context context){

        this.context = context;
    }

    public void buy(JSONObject jsonObject){

        String url = Global.BASEURL + "buyTicket";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "The ticket has been bought successfully. Thanks", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String body = new String(error.networkResponse.data);
                            try {
                                JSONObject jsonObject1 = new JSONObject(body);
                                Toast.makeText(context, jsonObject1.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders(){

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + FileManager.getToken(context));
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
