package com.adrianj.trainapp.controller;

import android.content.Context;
import android.util.Log;

import com.adrianj.trainapp.adapter.TicketUserAdapter;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.general.Global;
import com.adrianj.trainapp.models.Passenger;
import com.adrianj.trainapp.models.Schedule;
import com.adrianj.trainapp.models.Station;
import com.adrianj.trainapp.models.Stops;
import com.adrianj.trainapp.models.Ticket;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketUserController {

    private Context context;
    private OnReceivedTickets listener;

    public TicketUserController(Context context, final OnReceivedTickets listener){

        this.context = context;
        this.listener = listener;
    }

    public void getTickerByUser(){

        String[] credentials = FileManager.getCredentials(context);

        String url = Global.BASEURL + "getTicket/" + credentials[2];        // this is Id

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        List<Ticket> ticketList = new ArrayList<>();

                        try{

                            for(int i = 0; i < response.length(); i++){

                                JSONObject item = response.getJSONObject(i);

                                Stops startStops = new Stops();
                                Stops endStops = new Stops();
                                Station startStation = new Station();
                                Station endStation = new Station();
                                Train train = new Train();
                                Schedule schedule = new Schedule();
                                Passenger passenger = new Passenger();

                                JSONObject startStopsObject = item.getJSONObject("startStops");

                                JSONObject stationStartObject = startStopsObject.getJSONObject("stationStop");
                                startStation.setName(stationStartObject.getString("name"));
                                startStation.setId(stationStartObject.getLong("id"));

                                JSONObject trainObject = startStopsObject.getJSONObject("trainStops");
                                train.setId(trainObject.getLong("id"));
                                train.setNumber(trainObject.getInt("number"));
                                train.setSeats(trainObject.getInt("seats"));

                                JSONObject scheduleObject = startStopsObject.getJSONObject("schedule");
                                schedule.setId(scheduleObject.getLong("id"));
                                schedule.setTrain(train);

                                String time = startStopsObject.getString("time");
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                Date timeDate = simpleDateFormat.parse(time);

                                startStops.setId(startStopsObject.getLong("id"));
                                startStops.setStationStop(startStation);
                                startStops.setTrainStops(train);
                                startStops.setSchedule(schedule);
                                startStops.setTime(timeDate);

                                JSONObject endStopsObject = item.getJSONObject("endStops");

                                JSONObject stationEndObject = endStopsObject.getJSONObject("stationStop");
                                endStation.setName(stationEndObject.getString("name"));
                                endStation.setId(stationEndObject.getLong("id"));

                                String endTime = endStopsObject.getString("time");
                                Date timeEndDate = simpleDateFormat.parse(endTime);

                                endStops.setId(endStopsObject.getLong("id"));
                                endStops.setStationStop(endStation);
                                endStops.setTrainStops(train);
                                endStops.setSchedule(schedule);
                                endStops.setTime(timeEndDate);

                                JSONObject passengerObject = item.getJSONObject("passenger");
                                passenger.setUsername(passengerObject.getString("username"));

                                int seats = item.getInt("seat");

                                Ticket ticket = new Ticket();
                                ticket.setStartStops(startStops);
                                ticket.setEndStops(endStops);
                                ticket.setSeat(seats);
                                ticket.setPassenger(passenger);
                                ticketList.add(ticket);
                            }

                            if(listener != null){

                                listener.onReceived(ticketList);
                            }

                        }catch (Exception e){

                            Log.e("hola", e.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

    }

    public interface OnReceivedTickets{
        void onReceived(List<Ticket> ticketList);
    }
}
