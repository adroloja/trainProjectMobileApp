package com.adrianj.trainapp.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.controller.BuyController;
import com.adrianj.trainapp.controller.TicketController;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.models.Ticket;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

public class TicketListAdapter  extends ArrayAdapter<Ticket> {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Ticket> ticketList;
    private TicketController.OnReceivedListener onReceivedListener;
    public TicketListAdapter(@NonNull Context context, @NonNull List<Ticket> objects) {
        super(context, 0, objects);

        layoutInflater = LayoutInflater.from(context);
        this.ticketList = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View customView = convertView;

        if(customView == null){

            customView = layoutInflater.inflate(R.layout.tickets_adapter_view, parent, false);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Ticket t = getItem(position);

        String stationStartName = t.getStartStops().getStationStop().getName();
        String stationEndName = t.getEndStops().getStationStop().getName();
        String departureT = simpleDateFormat.format(t.getStartStops().getTime());
        String arriveT = simpleDateFormat.format(t.getEndStops().getTime());
        int trainN = t.getEndStops().getTrainStops().getNumber();

        View headerView = customView.findViewById(R.id.constraintLayoutHeader);
        TextView stationFrom = headerView.findViewById(R.id.textFromAT);
        TextView stationTo = headerView.findViewById(R.id.textToAT);
        TextView departureTime = customView.findViewById(R.id.textDataDeparture);
        TextView arriveTime = customView.findViewById(R.id.textArrive);
        TextView trainNumber = customView.findViewById(R.id.textTrainNumberAT);
        Button buyTicketButton = customView.findViewById(R.id.buttonBuyTicketAT);


        stationFrom.setText(stationStartName);
        stationTo.setText(stationEndName);
        departureTime.setText(departureT);
        arriveTime.setText(arriveT);
        trainNumber.setText(Integer.toString(trainN));

        buyTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FileManager.getCredentials(context) == null){

                    Toast.makeText(context, "You must be registred. Thanks", Toast.LENGTH_SHORT).show();

                }else{

                    String[] credentials = FileManager.getCredentials(context);
                    JSONObject jsonObject = new JSONObject();
                    BuyController buy = new BuyController(getContext());

                    try{

                        jsonObject.put("trainNumber", trainN);
                        jsonObject.put("idStartStops", t.getStartStops().getId());
                        jsonObject.put("idEndStops", t.getEndStops().getId());
                        jsonObject.put("idPassenger", credentials[2]);

                        buy.buy(jsonObject);

                    }catch (Exception e){

                        Log.d("hola", e.toString());
                    }
                }
            }
        });

        return customView;
    }
}
