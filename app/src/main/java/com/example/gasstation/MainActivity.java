package com.example.gasstation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gv;
    GasStations model;
    GetGasStations gg=new GetGasStations();
    AdapterGasStation adapterGasStation;
    ArrayList<GasStations> dd;
    public  static String gsNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv=findViewById(R.id.vvgasstations);
        dd=new ArrayList<>(gg.GetData(this));
        adapterGasStation=new AdapterGasStation(this,dd);
        gv.setAdapter(adapterGasStation);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.e("RRRRRRRRRRRRRRRRRRRRR","home fragment on click testRRRRRRR");
                model=dd.get(i);
                gsNo=model.getGasStationNo();
                Toast.makeText(MainActivity.this, "gasstations :"+model.getGasStationNo(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ServicesActivity.class));
            }});

        SwipeRefreshLayout swp=findViewById(R.id.swipp);
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gv=findViewById(R.id.vvgasstations);
                dd=new ArrayList<>(gg.GetData(MainActivity.this));
                adapterGasStation=new AdapterGasStation(MainActivity.this,dd);
                gv.setAdapter(adapterGasStation);
                swp.setRefreshing(false);
            }
        });


    }
}