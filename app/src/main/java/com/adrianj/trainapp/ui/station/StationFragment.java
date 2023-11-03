package com.adrianj.trainapp.ui.station;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.adapter.StationListAdapter;
import com.adrianj.trainapp.controller.StationController;
import com.adrianj.trainapp.controller.StopsController;
import com.adrianj.trainapp.databinding.FragmentStationBinding;
import com.adrianj.trainapp.models.Station;
import com.adrianj.trainapp.models.Stops;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StationFragment extends Fragment implements StationController.OnStationsReceivedListener, StopsController.OnStopsReceivedListener {

    private Spinner spinnerStations;
    private Spinner spinnerDay;

    private List<String> listDays;
    private ListView listView;
    private List<Station> listStation;
    private Button searchButton;


    private FragmentStationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = binding.listViewStopsStation;
        spinnerStations = binding.spinnerStationStation;
        spinnerDay = binding.spinnerDayStation;

        StationController stationController = new StationController(getContext());
        stationController.getAllStations((StationController.OnStationsReceivedListener) this);

        JSONObject jsonObject = new JSONObject();
        StopsController stopsController = new StopsController(getContext(), (StopsController.OnStopsReceivedListener) this);

        listDays = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String currentDay = dateFormat.format(calendar.getTime());
        listDays.add(currentDay);

        for (int i = 1; i <= 7; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            String day = dateFormat.format(calendar.getTime());
            listDays.add(day);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_style, listDays);
        spinnerDay.setAdapter(adapter);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String day = (String) parent.getSelectedItem();

                try{
                    jsonObject.put("day", day);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerStations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Station s = (Station) parent.getSelectedItem();

                try {
                    jsonObject.put("name", s.getName());

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchButton = binding.buttonSearchStation;
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopsController.getStops(jsonObject);
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

        this.listStation = stationList;
        stationList.sort((a, b) -> a.getName().compareTo(b.getName()));
        ArrayAdapter<Station> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_style, stationList);
        spinnerStations.setAdapter(adapter);
    }

    @Override
    public void onStopsReceived(List<Stops> stops) {
        if(stops.size() == 0){
            Toast.makeText(getContext(), "There isn´t any Stop for this day. Thanks", Toast.LENGTH_SHORT).show();
        }
        StationListAdapter adapter = new StationListAdapter(getContext(), 0, stops);
        listView.setAdapter(adapter);
    }
}