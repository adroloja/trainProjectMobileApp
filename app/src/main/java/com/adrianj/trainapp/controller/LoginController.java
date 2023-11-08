package com.adrianj.trainapp.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.general.Global;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginController {

    private Context context;

    public LoginController(Context context){

        this.context = context;
    }

    public void login(JSONObject jsonObject, View view, int mode){

        String url = Global.BASEURL + "login";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject passenger = response.getJSONObject("passenger");
                            String token = response.getString("token");
                            String username = passenger.getString("username");
                            String password = passenger.getString("password");
                            String id = passenger.getString("id");
                            String name = passenger.getString("name");
                            String surname = passenger.getString("surname");
                            String birth = passenger.getString("dateBirth");

                            FileManager.saveCredentials(context, username, password, id, name, surname, birth);
                            FileManager.saveToken(context, token);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if(mode == 1){
                            NavController navController = Navigation.findNavController(view);
                            navController.navigate(R.id.navigation_user);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

               // Log.d("hola", error.toString()  );
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String body = new String(error.networkResponse.data);
                    JSONObject bodyJson;
                    try {
                        bodyJson = new JSONObject(body);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if(bodyJson != null){

                        try {
                            Toast.makeText(context, bodyJson.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }

            }
        }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
