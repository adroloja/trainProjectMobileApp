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
import android.widget.Toast;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.controller.SignUpController;
import com.adrianj.trainapp.databinding.FragmentSignUpBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpFragment extends Fragment {

    private FragmentSignUpBinding binding;
    private TextView textBack;
    private EditText inputUsername;
    private EditText inputPassword;
    private EditText inputRepeatPassword;
    private EditText inputName;
    private EditText inputSurname;
    private EditText inputBirthDate;
    private Button signUpButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SignUpController sign = new SignUpController(getContext());
        
        inputName = binding.inputNameSU;
        inputBirthDate = binding.inputDateBirthSU;
        inputSurname = binding.inputSurnameSU;
        inputUsername = binding.inputUsernameSU;
        inputPassword = binding.inputPasswordSU;
        inputRepeatPassword = binding.inputRepeatPasswordSU;        
        textBack = binding.textBackSU;
        signUpButton = binding.buttonSignUpSU;
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String name = inputName.getText().toString();
                String surname = inputSurname.getText().toString();
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();
                String repeatPassword = inputRepeatPassword.getText().toString();
                String birthdate = inputBirthDate.getText().toString();
                
                if(!password.equals(repeatPassword)){

                    Toast.makeText(getContext(), "The password is different, please try again. Thanks.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("name", name);
                    jsonObject.put("surname", surname);
                    jsonObject.put("password", password);
                    jsonObject.put("dateBirth", birthdate);
                    jsonObject.put("employe", false);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                sign.signUp(jsonObject, getView());
            }
        });
        
        textBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.navigation_login);
            }
        });
        
        

        return root;
    }
}