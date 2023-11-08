package com.adrianj.trainapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.adrianj.trainapp.controller.LocationController;
import com.adrianj.trainapp.controller.LoginController;
import com.adrianj.trainapp.databinding.ActivityMainBinding;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.models.Ticket;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    public static List<Ticket> TICKET_LIST = new ArrayList<>();
    private ActivityMainBinding binding;
    private LocationController locationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (FileManager.getCredentials(this) != null) {

            String[] credentials = FileManager.getCredentials(this);
            LoginController login = new LoginController(this);
            JSONObject jsonObject = new JSONObject();

            try {

                jsonObject.put("username", credentials[0]);
                jsonObject.put("password", credentials[1]);

                login.login(jsonObject, getCurrentFocus(), 0);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_station, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        locationController = new LocationController(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLocationGPS();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
                navController.popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getLocationGPS() {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){

                    JSONObject jsonObject = new JSONObject();
                    String[] credentials = FileManager.getCredentials(getApplicationContext());

                    if(credentials == null){
                        credentials = new String[3];
                        credentials[2] = "0";
                    }

                    try {

                        jsonObject.put("lat", location.getLatitude());
                        jsonObject.put("lng", location.getLongitude());
                        jsonObject.put("id", credentials[2]);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    locationController.sendLocation(jsonObject);
                }
            }
        });
    }

}