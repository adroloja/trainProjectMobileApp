package com.adrianj.trainapp.ui.search;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adrianj.trainapp.MainActivity;
import com.adrianj.trainapp.R;
import com.adrianj.trainapp.controller.StationController;
import com.adrianj.trainapp.controller.TicketController;
import com.adrianj.trainapp.databinding.FragmentSearchBinding;
import com.adrianj.trainapp.models.Station;
import com.adrianj.trainapp.models.Ticket;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends Fragment implements StationController.OnStationsReceivedListener, TicketController.OnReceivedListener {

    private FragmentSearchBinding binding;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private Spinner spinnerDay;
    private StationController stationController;
    private TicketController ticketController;
    private List<Station> stationList;
    private List<String> listDays;
    private Button searchButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinnerDay = binding.spinnerSearchDay;
        spinnerFrom = binding.spinnerSearchFrom;
        spinnerTo = binding.spinnerSearchTo;
        searchButton = binding.buttonSearchSearch;

        stationList = new ArrayList<>();

        ticketController = new TicketController(getContext(), (TicketController.OnReceivedListener) this);
        stationController = new StationController(getContext());
        stationController.getAllStations((StationController.OnStationsReceivedListener) this);

        listDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDay = dateFormat.format(calendar.getTime());
        listDays.add(currentDay);

        for (int i = 1; i <= 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String day = dateFormat.format(calendar.getTime());
            listDays.add(day);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_style, listDays);
        spinnerDay.setAdapter(adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Station stationStart = (Station) spinnerFrom.getSelectedItem();
               Station stationEnd = (Station) spinnerTo.getSelectedItem();
               String day = spinnerDay.getSelectedItem().toString();
               String startDay = day + "T00:01";
               String endDay = day + "T23:59";

               JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("stationId1", stationStart.getId());
                    jsonObject.put("stationId2", stationEnd.getId());
                    jsonObject.put("startTime", startDay);
                    jsonObject.put("endTime", endDay);   //2023-10-15T11:01

                    ticketController.getTicketForStationAtoB(jsonObject);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStationReceived(List<Station> stationList) {
        this.stationList = stationList;
        this.stationList.sort((a,b) -> a.getName().compareTo(b.getName()));

        ArrayAdapter<Station> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_style, this.stationList );
        spinnerFrom.setAdapter(arrayAdapter);
        spinnerTo.setAdapter(arrayAdapter);
    }

    @Override
    public void onReceivedTicketList(List<Ticket> ticketList) {

        MainActivity.TICKET_LIST = ticketList;
        if(ticketList.size() == 0){
            Toast.makeText(getContext(), "There isn´t any result. Thanks", Toast.LENGTH_SHORT).show();
            return;
        }
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.navigation_searchlist);
    }
}