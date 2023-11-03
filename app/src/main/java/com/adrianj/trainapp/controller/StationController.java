package com.adrianj.trainapp.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.adrianj.trainapp.general.Global;
import com.adrianj.trainapp.models.Station;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StationController {

    private Context context;

    public StationController(Context context){

        this.context = context;
    }

    public void getAllStations(final OnStationsReceivedListener listener){

        String url = Global.BASEURL + "getStations";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Station> stationList = new ArrayList<>();

                        try {

                            for(int i = 0; i < response.length(); i++){

                                JSONObject jsonObject = response.getJSONObject(i);
                                long id = jsonObject.getLong("id");
                                String name = jsonObject.getString("name");

                                Station station = new Station();
                                station.setId(id);
                                station.setName(name);
                                
                                stationList.add(station);
                            }

                            if(listener != null){
                                listener.onStationReceived(stationList);
                            }
                            
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("hola", error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);

    }

    public interface OnStationsReceivedListener{
        void onStationReceived(List<Station> stationList);
    }
}
