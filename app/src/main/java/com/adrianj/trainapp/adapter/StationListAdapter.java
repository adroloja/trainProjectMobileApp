package com.adrianj.trainapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.models.Station;
import com.adrianj.trainapp.models.Stops;

import java.text.SimpleDateFormat;
import java.util.List;

public class StationListAdapter extends ArrayAdapter<Stops> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Stops> stopsList;
    public StationListAdapter(@NonNull Context context, int resource, @NonNull List<Stops> objects) {
        super(context, resource, objects);

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View customView = convertView;

        if (customView == null) {

            customView = layoutInflater.inflate(R.layout.station_adapter_view, parent, false);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Stops s = getItem(position);

        TextView trainText = customView.findViewById(R.id.textTrainNumberStation);
        TextView departureTime = customView.findViewById(R.id.textDepartureTimeStation);

        trainText.setText(Integer.toString(s.getTrainStops().getNumber()));
        departureTime.setText(simpleDateFormat.format(s.getTime()));

        return customView;
    }
}
