package com.adrianj.trainapp.ui.search;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.adrianj.trainapp.MainActivity;
import com.adrianj.trainapp.R;
import com.adrianj.trainapp.adapter.TicketListAdapter;
import com.adrianj.trainapp.databinding.FragmentSearchBinding;
import com.adrianj.trainapp.databinding.FragmentSearchListBinding;
import com.adrianj.trainapp.models.Ticket;

import java.util.List;

public class SearchListFragment extends Fragment {

    List<Ticket> ticketList;

    private FragmentSearchListBinding binding;
    ListView listView;

    public SearchListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ticketList = MainActivity.TICKET_LIST;
        listView = binding.listViewTicket;
        
        if(ticketList.size() == 0){
            Toast.makeText(getContext(), "There isn´t any result. Thanks", Toast.LENGTH_SHORT).show();
        }
        TicketListAdapter adapter = new TicketListAdapter(getContext(), ticketList);
        listView.setAdapter(adapter);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("hola", "entro");
        switch (item.getItemId()) {
            case android.R.id.home:
                NavController navController = Navigation.findNavController(getView());
                navController.popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}