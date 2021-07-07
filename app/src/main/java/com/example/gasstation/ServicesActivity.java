package com.example.gasstation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasstation.ui.home.HomeFragment;

import java.util.ArrayList;

public class ServicesActivity extends AppCompatActivity {
ListView list;
    ArrayList<CartShoppingModel> CartSho;
    DBLIGHT dblight=new DBLIGHT(this);
GetServices gg=new GetServices();
 ArrayList<Services> dd;
AdapterService adapterService;
   public static TextView count;
FrameLayout floating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        TextView txtNamegasstation;
        txtNamegasstation=findViewById(R.id.servprofile_services_gas_name);
        txtNamegasstation.setText(HomeFragment.gsName+" Services");
      floating =findViewById(R.id.floatingframeLayout);
        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartSho=new ArrayList<>(dblight.GetData());
                if(CartSho.isEmpty())
                {
                    Toast.makeText(ServicesActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
                else
                startActivity(new Intent(ServicesActivity.this, ShopingCartActivity.class));
            }
        });
        list=findViewById(R.id.lstServ);
        count=findViewById(R.id.count);
        count.setText(String.valueOf(dblight.getCount()));
        dd=new ArrayList<>(gg.GetData(this, StaticsClass.gasStationNumber));
        adapterService=new AdapterService(this,dd);
       list.setAdapter(adapterService);
//              list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//        }
//    });
    }
}