package com.example.gasstation.ui.requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gasstation.AdapterRecyclViewRequests;
import com.example.gasstation.GetRequests;
import com.example.gasstation.R;
import com.example.gasstation.Requests;

import java.util.ArrayList;

public class RequestsFragment extends Fragment {

    GetRequests gg=new GetRequests();
    ArrayList<Requests> dd;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_requests, container, false);

        dd=new ArrayList<>(gg.GetData(getActivity()));
        AdapterRecyclViewRequests adapter = new AdapterRecyclViewRequests(dd);
        RecyclerView recyclerView =  root.findViewById(R.id.servprofile_requests_recycl_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



        return root;
    }
}