package com.adrianj.trainapp.ui.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.controller.LoginController;
import com.adrianj.trainapp.databinding.FragmentLoginBinding;
import com.adrianj.trainapp.general.FileManager;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private EditText inputUsername;
    private EditText inputPassword;
    private TextView textSignUp;
    private Button loginButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inputPassword = binding.inputPassword;
        inputUsername = binding.inputUsername;
        loginButton = binding.buttonLogin;
        textSignUp = binding.textRegistredLoginF;

        String[] data = FileManager.getCredentials(getContext());

        if(data != null){

            inputUsername.setText(data[0]);
            inputPassword.setText(data[1]);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                LoginController login = new LoginController(getContext());
                login.login(jsonObject, getView(), 1);
            }
        });

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.navigation_signUp);
            }
        });

        return root;
    }
}