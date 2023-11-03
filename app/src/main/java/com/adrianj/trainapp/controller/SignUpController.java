package com.adrianj.trainapp.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.general.CustomJsonArrayRequest;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.general.Global;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpController {

    private Context context;

    public SignUpController(Context context){

        this.context = context;
    }

    public void signUp(JSONObject jsonObject, View view){

        String url = Global.BASEURL + "registrer";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest customJsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("hola", response.toString());

                        try {

                            String username = response.getString("username");
                            String password = response.getString("password");
                            String name = response.getString("name");
                            String surname = response.getString("surname");
                            String birth = response.getString("dateBirth");
                            String id = Integer.toString(response.getInt("id"));
                            FileManager.saveCredentials(context, username, password, id, name, surname, birth);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                        NavController navController = Navigation.findNavController(view);
                        navController.navigate(R.id.navigation_login);
                        Toast.makeText(context, "Sign Up success. Thanks.", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("hola", error.toString());
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String body = new String(error.networkResponse.data);
                            Toast.makeText(context, body, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        requestQueue.add(customJsonArrayRequest);
    }
}
