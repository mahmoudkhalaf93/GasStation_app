package com.example.gasstation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.gasstation.AdapterGasStation;
import com.example.gasstation.Database;
import com.example.gasstation.GasStations;
import com.example.gasstation.GetGasStations;
import com.example.gasstation.GetRateGasStation;
import com.example.gasstation.GetServices;
import com.example.gasstation.R;
import com.example.gasstation.Services;
import com.example.gasstation.ServicesActivity;
import com.example.gasstation.StaticsClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment   {

     GridView gv;
GasStations model;
 public static   ArrayList<GetRateGasStation> ddgt=new ArrayList<>();
GetGasStations gg=new GetGasStations();
AdapterGasStation adapterGasStation;
ArrayList<GasStations> dd;
    public  static String gsNo,gsName;
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Database db=new Database();
       db.connect(getActivity());
       ResultSet rs= db.GetData("select * from gasstationrating ");
       try{ while (rs.next())
        {
            GetRateGasStation xx=new GetRateGasStation();
            xx.setGasStationNo(rs.getString(1));
            xx.setServiceNo(rs.getString(2));
            xx.setValue(rs.getString(3));
            ddgt.add(xx);
        }} catch (SQLException throwables) {
           throwables.printStackTrace();
       }

        gv=root.findViewById(R.id.vvgasstations);
       dd=new ArrayList<>(gg.GetData(getActivity()));
       adapterGasStation=new AdapterGasStation(getActivity(),dd);
       gv.setAdapter(adapterGasStation);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {

        model=dd.get(i);
            gsName=model.getName();
           StaticsClass.gasStationNumber=model.getGasStationNo();
        startActivity(new Intent(getActivity(), ServicesActivity.class));
        }});


        SwipeRefreshLayout swp=root.findViewById(R.id.swipp);
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gv=root.findViewById(R.id.vvgasstations);
                dd=new ArrayList<>(gg.GetData(getActivity()));
                adapterGasStation=new AdapterGasStation(getActivity(),dd);
                gv.setAdapter(adapterGasStation);
                swp.setRefreshing(false);
            }
        });

        return root;
    }

}












