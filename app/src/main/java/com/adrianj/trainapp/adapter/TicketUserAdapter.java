package com.adrianj.trainapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.models.Ticket;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

public class TicketUserAdapter extends ArrayAdapter<Ticket> {

    private Context context;
    private LayoutInflater layoutInflater;
    private String[] credentials;
    public TicketUserAdapter(@NonNull Context context, @NonNull List<Ticket> objects) {
        super(context, 0, objects);

        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        credentials = FileManager.getCredentials(context);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View customView = convertView;

        if (customView == null) {

            customView = layoutInflater.inflate(R.layout.ticket_user_view, parent, false);
        }

        TextView textName = customView.findViewById(R.id.textNameUserTicket);
        TextView textFrom = customView.findViewById(R.id.textFromTicketUser);
        TextView textTo = customView.findViewById(R.id.textToTicketUser);
        TextView departure = customView.findViewById(R.id.textDepartureTicketUser);
        TextView arrive = customView.findViewById(R.id.textArriveTicketUser);
        TextView trainNumber = customView.findViewById(R.id.textTrainNumberTicketUser);

        Ticket t = getItem(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        textName.setText(credentials[3] + " " + credentials[4]);
        textFrom.setText(t.getStartStops().getStationStop().getName());
        textTo.setText(t.getEndStops().getStationStop().getName());
        departure.setText(simpleDateFormat.format(t.getStartStops().getTime()));
        arrive.setText(simpleDateFormat.format(t.getEndStops().getTime()));
        trainNumber.setText(Integer.toString(t.getEndStops().getTrainStops().getNumber()));

        return customView;
    }
}
