package com.adrianj.trainapp.controller;

import android.content.Context;
import android.util.Log;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.general.CustomJsonArrayRequest;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.general.Global;
import com.adrianj.trainapp.models.Schedule;
import com.adrianj.trainapp.models.Station;
import com.adrianj.trainapp.models.Stops;
import com.adrianj.trainapp.models.Train;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StopsController {

    private Context context;
    private StopsController.OnStopsReceivedListener listener;


    public StopsController(Context context, StopsController.OnStopsReceivedListener onStopsReceivedListener){

        this.context = context;
        this.listener = onStopsReceivedListener;
    }

    public void getStops(JSONObject jsonObject){

        String url = Global.BASEURL + "checktrainByDay";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        CustomJsonArrayRequest jsonArrayRequest = new CustomJsonArrayRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Stops> stopsList = new ArrayList<>();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        for(int i = 0; i < response.length(); i++){

                            try {
                                Stops s = new Stops();
                                Train t = new Train();
                                Station station = new Station();
                                Schedule schedule = new Schedule();
                                JSONObject jsonObject = response.getJSONObject(i);
                                s.setId(jsonObject.getLong("id"));
                                s.setTime(simpleDateFormat.parse(jsonObject.getString("time")));

                                JSONObject train = jsonObject.getJSONObject("trainStops");
                                t.setId(train.getLong("id"));
                                t.setNumber(train.getInt("number"));
                                t.setSeats(train.getInt("seats"));
                                s.setTrainStops(t);

                                JSONObject st = new JSONObject();
                                st = jsonObject.getJSONObject("stationStop");
                                station.setId(st.getLong("id"));
                                station.setName(st.getString("name"));
                                s.setStationStop(station);

                                JSONObject timeTable = jsonObject.getJSONObject("schedule");
                                schedule.setId(timeTable.getLong("id"));
                                schedule.setTrain(t);
                                s.setSchedule(schedule);

                                stopsList.add(s);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        if(listener != null){

                            listener.onStopsReceived(stopsList);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("hola", error.toString());
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

        requestQueue.add(jsonArrayRequest);
    }

    public interface OnStopsReceivedListener{

        void onStopsReceived(List<Stops> stops);
    }
}
