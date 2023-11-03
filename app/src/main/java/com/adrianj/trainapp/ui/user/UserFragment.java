package com.adrianj.trainapp.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.adrianj.trainapp.R;
import com.adrianj.trainapp.adapter.TicketUserAdapter;
import com.adrianj.trainapp.controller.TicketUserController;
import com.adrianj.trainapp.databinding.FragmentUserBinding;
import com.adrianj.trainapp.general.FileManager;
import com.adrianj.trainapp.models.Ticket;

import java.util.List;

public class UserFragment extends Fragment implements TicketUserController.OnReceivedTickets{

    private Button logOutButton;
    private TextView textUsername;
    private TextView textName;
    private TextView textSurname;
    private TextView textBirth;
    private ListView listViewTicket;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(FileManager.getCredentials(getContext()) == null || FileManager.getToken(getContext()) == null){
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.navigation_login);
        }
    }

    private FragmentUserBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String[] credentials = FileManager.getCredentials(getContext());

        if(credentials != null){

            textUsername = binding.textUsernameUser;
            textName = binding.textNameUser;
            textSurname = binding.textSurnameUser;
            textBirth = binding.textBirthUser;

            textUsername.setText(credentials[0]);
            textName.setText(credentials[3]);
            textSurname.setText(credentials[4]);
            textBirth.setText(credentials[5]);
        }

        logOutButton = binding.buttonLogOut;
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileManager.deleteToken(getContext());
                FileManager.deleteCredentials(getContext());

                NavController navController = Navigation.findNavController(getView());
                navController.navigate(R.id.navigation_login);
            }
        });

        TicketUserController ticket = new TicketUserController(getContext(), (TicketUserController.OnReceivedTickets) this);

        Button button = binding.button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ticket.getTickerByUser();

            }
        });

        listViewTicket = binding.listView;


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onReceived(List<Ticket> ticketList) {

        TicketUserAdapter adapter = new TicketUserAdapter(getContext(), ticketList);
        listViewTicket.setAdapter(adapter);
    }
}