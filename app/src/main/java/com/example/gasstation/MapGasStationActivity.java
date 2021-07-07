package com.example.gasstation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.gasstation.ui.home.HomeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapGasStationActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mMap;
    Database db = new Database();
    Button but;
    Float latOnMap, lngOnMap;
    List<String> categories;
    ArrayList<LatLng> gaslocation;
    Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    TextView showphone;
    int check = 0;
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //window.setNavigationBarColor(@ColorInt int color)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_gas_station);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
            //window.setNavigationBarColor(@ColorInt int color)
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapgas);
        mapFragment.getMapAsync(this);
        spinner = (Spinner) findViewById(R.id.spinnerdistanceg);
        spinner.setOnItemSelectedListener(this);
        gaslocation = new ArrayList<LatLng>();
        categories = new ArrayList<String>();
        showphone = findViewById(R.id.showgasphone);

//        showphone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getGasStationLocations(mMap, getUserLocation());
        db.connect(MapGasStationActivity.this);
        ResultSet gasStations = db.GetData("select GasStationNo,Name,Phone from GasStation");
        // db.Rundml("Insert INTO Services (Name,Details,Logo,Cost,GasStationNo) VALUES ('Change oil',N'ثلاثة آلاف كيلو سعة 1 لتر','https://cdn5.vectorstock.com/i/1000x1000/26/44/oil-change-icon-logo-silhouette-oil-canister-vector-28012644.jpg',36,9);");
        //Toast.makeText(this, "name: ", Toast.LENGTH_SHORT).show();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                boolean hasGasStationID;
                try {
                    Double.parseDouble(marker.getTitle());
                    hasGasStationID = true;
                } catch (NumberFormatException e) {
                    hasGasStationID = false;
                    if (showphone.getVisibility() == View.VISIBLE) {
                        showphone.setVisibility(View.GONE);
                    }
                }

                if (hasGasStationID) {
                    try {
                        //   ResultSet  gasStations = db.GetData("select * from GasStation WHERE GasStationNo='" + marker.getTitle() + "';");

                        while (gasStations.next()) {
                            //gasStations.findColumn()
                            if (Integer.parseInt(gasStations.getString("GasStationNo")) == Integer.parseInt(marker.getTitle())) {
                                String gasnum = gasStations.getString("GasStationNo");
                                String gasname = gasStations.getString("Name");
                                Toast.makeText(MapGasStationActivity.this, "" + marker.getTitle(), Toast.LENGTH_SHORT).show();
                                showphone.setText(gasStations.getString("Name") + "\n\n" + "phone : " + gasStations.getString("Phone") + "\n\nClick Here to Go To Gas Station");
                                showphone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        StaticsClass.gasStationNumber = gasnum;
                                        HomeFragment.gsName = gasname;
                                        startActivity(new Intent(MapGasStationActivity.this, ServicesActivity.class));
                                    }
                                });
                                showphone.setVisibility(View.VISIBLE);
                                gasStations.beforeFirst();

                                Toast.makeText(MapGasStationActivity.this, gasStations.getString("Name"), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (showphone.getVisibility() == View.VISIBLE) {
                    showphone.setVisibility(View.GONE);
                }
            }
        });
        //XXXXXXXXXXXXXXXXXxx use gps to get location
        but = findViewById(R.id.gpsgas);
        but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (showphone.getVisibility() == View.VISIBLE) {
                    showphone.setVisibility(View.GONE);
                }
                // Do something in response to button click
                LocationManager lm = (LocationManager) MapGasStationActivity.this.getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                }
                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch (Exception ex) {
                }
                //
                if (checkLocationPermission()) {
                    if (!gps_enabled && !network_enabled) {
                        // notify user
                        new android.app.AlertDialog.Builder(MapGasStationActivity.this, R.style.MyDialogThemeDark)
                                .setMessage("gps network not enabled")
                                .setPositiveButton("open location settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        MapGasStationActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    } else {
                        mMap.clear();
                        GPSTracker gps = new GPSTracker(MapGasStationActivity.this);
                        latOnMap = (float) gps.getLatitude();
                        lngOnMap = (float) gps.getLongitude();
                        //make custom color on it
                        LatLng us = new LatLng(latOnMap, lngOnMap);
                        float gpsloc = (float) 0.0;
                        if (!latOnMap.equals(gpsloc)) {
                            getGasStationLocations(mMap, us);
                        } else {
                            Toast.makeText(MapGasStationActivity.this, "try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(MapGasStationActivity.this, "App Need Location Permission to continue", Toast.LENGTH_SHORT).show();
                }
////
            }
        });
    }

    private void getGasStationLocations(GoogleMap googlemap, LatLng userlocation) {
        db.connect(MapGasStationActivity.this);
        ResultSet gasStations = db.GetData("select * from GasStation ;");
        LatLng marker;
        double dis;
        int coun = 1;
        DecimalFormat df2 = new DecimalFormat("#.##");
        if (!(dataAdapter == null))
            dataAdapter.clear();
        gaslocation.clear();
        try {
            while (gasStations.next()) {
                marker = new LatLng(gasStations.getDouble("Latitude"), gasStations.getDouble("Longitude"));
                dis = CalculationByDistance(userlocation, marker);
                if (dis < 4) {
                    // Log.e( "XXXXXXXXXXXXgasss","coun:"+coun+"dis="+dis+" Langlnt:"+marker.longitude+"/"+marker.latitude);
                    categories.add("station." + coun + " distant : " + df2.format(dis) + "km from you");
                    gaslocation.add(marker);
                    googlemap.addMarker(new MarkerOptions().position(marker).title(gasStations.getString("GasStationNo")));
                    googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
                    coun++;
                }
            }

            int strokcolor = 0xffff0000;
            int shadecolor = 0x44ff0000;
            Circle circle = googlemap.addCircle(new CircleOptions().center(new LatLng(userlocation.latitude, userlocation.longitude))
                    .radius(200).strokeColor(strokcolor).fillColor(shadecolor));
          //  BitmapDescriptor bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            googlemap.addMarker(new MarkerOptions().position(userlocation).title("my Current location"));
            googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 14));


            dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
            check = 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (++check > 1) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gaslocation.get(i), 14));
        }
    }


    private LatLng getUserLocation() {
        LatLng usr = null;
        db.connect(MapGasStationActivity.this);
        ResultSet user = db.GetData("select Latitude,Longitude from Customer WHERE CustomerNo='" + StaticsClass.IDLogin + "';");
        try {
            if (user.next()) {
                usr = new LatLng(user.getDouble("Latitude"), user.getDouble("Longitude"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return usr;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this, R.style.MyDialogThemeDark)
                        .setTitle("location permission")
                        .setMessage("we need location permission to continue")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapGasStationActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}