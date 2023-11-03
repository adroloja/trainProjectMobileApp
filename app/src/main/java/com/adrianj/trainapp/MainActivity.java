package com.adrianj.trainapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.adrianj.trainapp.controller.LoginController;
import com.adrianj.trainapp.databinding.ActivityMainBinding;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.models.Ticket;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static List<Ticket> TICKET_LIST = new ArrayList<>();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(FileManager.getCredentials(this) != null){

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


}