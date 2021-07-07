package com.example.gasstation;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapEditProfileActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Database db=new Database();
    Button but;
    Float latOnMap,lngOnMap,latDatabase,lngDatabase;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapprof);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng marker = null;
       Connection conn= db.connect(MapEditProfileActivity.this);
        ResultSet user=null;
        if(conn==null)
        { super.onBackPressed();}
        else
        { user = db.GetData("select * from Customer WHERE CustomerNo='"+ StaticsClass.IDLogin +"';");}


                try {
                if (user.next()) {
                    //XXXXXXXXXXXXXX show my last location on map
                    latDatabase=Float.parseFloat(user.getString("Latitude"));
                    lngDatabase=Float.parseFloat(user.getString("Longitude"));
                    marker = new LatLng(latDatabase,lngDatabase);
                    mMap.addMarker(new MarkerOptions().position(marker).title("my Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));

                    //XXXXXXXXXXXXX//set on map my loaction
                    mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            mMap.clear();
                            latOnMap= (float) latLng.latitude;
                            lngOnMap= (float) latLng.longitude;
                            //make custom color on it
                            int strokcolor=0xffff0000;
                            int shadecolor=0x44ff0000;
                            Circle circle=mMap.addCircle(new CircleOptions().center(new LatLng(latOnMap,lngOnMap))
                                    .radius(200).strokeColor(strokcolor).fillColor(shadecolor));
                            BitmapDescriptor bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                            //end of color code the color will apply on next mark
                            LatLng  marker = new LatLng(latOnMap,lngOnMap);
                            mMap.addMarker(new MarkerOptions().position(marker).title("my location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
                        }
                    });


                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //end of on click map

            //XXXXXXXXXXXXXXXXXxx use gps to get location
            but=findViewById(R.id.gpsprof);
            but.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//                    // Do something in response to button click
//                    LocationManager lm = (LocationManager) MapEditProfileActivity.this.getSystemService(Context.LOCATION_SERVICE);
//                    boolean gps_enabled = false;
//                    boolean network_enabled = false;
//                    try {
//                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//                    } catch(Exception ex) {}
//                    try {
//                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//                    } catch(Exception ex) {}
//
//                    if(!gps_enabled && !network_enabled) {
//                        // notify user
//                        new android.app.AlertDialog.Builder(MapEditProfileActivity.this)
//                                .setMessage("gps network not enabled")
//                                .setPositiveButton("open location settings", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                                        MapEditProfileActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                                    }
//                                })
//                                .setNegativeButton("Cancel",null)
//                                .show();
//                    }
//                    else {
//                        GPSTracker gps=new GPSTracker(MapEditProfileActivity.this);
//                        mMap.clear();
//                        latOnMap= (float) gps.getLatitude();
//                        lngOnMap= (float) gps.getLongitude();
//                        //make custom color on it
//                        int strokcolor=0xffff0000;
//                        int shadecolor=0x44ff0000;
//                        Circle circle=mMap.addCircle(new CircleOptions().center(new LatLng(latOnMap,lngOnMap))
//                                .radius(200).strokeColor(strokcolor).fillColor(shadecolor));
//                        BitmapDescriptor bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//                        LatLng  marker = new LatLng(latOnMap,lngOnMap);
//                        mMap.addMarker(new MarkerOptions().position(marker).title("my location"));
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
//                    }


                    // Do something in response to button click
                    LocationManager lm = (LocationManager) MapEditProfileActivity.this.getSystemService(Context.LOCATION_SERVICE);
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
                    if (checkLocationPermission())
                    {
                        if (!gps_enabled && !network_enabled) {
                            // notify user

                            new android.app.AlertDialog.Builder(MapEditProfileActivity.this)
                                    .setMessage("gps network not enabled")
                                    .setPositiveButton("open location settings", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                            MapEditProfileActivity.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        } else {
                            mMap.clear();
                            GPSTracker gps = new GPSTracker(MapEditProfileActivity.this);
                            latOnMap = (float) gps.getLatitude();
                            lngOnMap = (float) gps.getLongitude();
                            //make custom color on it
                            LatLng us = new LatLng(latOnMap, lngOnMap);
                            float gpsloc = (float) 0.0;
                            if (!latOnMap.equals(gpsloc)) {
                                ///xxx
//                                int strokcolor=0xffff0000;
//                                int shadecolor=0x44ff0000;
//                                Circle circle=mMap.addCircle(new CircleOptions().center(new LatLng(latOnMap,latOnMap))
//                                        .radius(200).strokeColor(strokcolor).fillColor(shadecolor));
//                                BitmapDescriptor bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//                                mMap.addMarker(new MarkerOptions().position(us).title("my Current location"));
//                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(us, 14));
                                //xx
                                int strokcolor=0xffff0000;
                        int shadecolor=0x44ff0000;
                        Circle circle=mMap.addCircle(new CircleOptions().center(new LatLng(latOnMap,lngOnMap))
                                .radius(200).strokeColor(strokcolor).fillColor(shadecolor));
                        BitmapDescriptor bitmap = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                        LatLng  marker = new LatLng(latOnMap,lngOnMap);
                        mMap.addMarker(new MarkerOptions().position(marker).title("my location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
                                //getGasStationLocations(mMap, us);
                            } else {
                                Toast.makeText(MapEditProfileActivity.this, "try again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    else { Toast.makeText(MapEditProfileActivity.this, "App Need Location Permission to continue", Toast.LENGTH_SHORT).show();}
////
                }
            });







    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("location permission")
                        .setMessage("we need location permission to continue")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapEditProfileActivity.this,
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
    @Override
    public void onBackPressed() {

            if(!(latOnMap == null)){

            AlertDialog.Builder buil=new AlertDialog.Builder(this,R.style.MyDialogThemelight)
                    .setTitle("Location")
                    .setMessage("do you want to save this location as your location ?")
                    .setIcon(R.drawable.ic_baseline_add_location_37)
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Connection conntds= db.connect(MapEditProfileActivity.this);
                            if(conntds==null)
                            { MapEditProfileActivity.super.onBackPressed();}
                            else
                            {   db.Rundml("UPDATE Customer SET Latitude='" + latOnMap + "',Longitude='" + lngOnMap + "' WHERE CustomerNo=" + StaticsClass.IDLogin + ";");}

                            SharedPreferences shar=getSharedPreferences("gasStation",MODE_PRIVATE);

                            shar.edit().putString("lat",String.valueOf(latOnMap)).
                                    putString("lng",String.valueOf(lngOnMap)).
                                    apply();
                            StaticsClass.lat=shar.getString("lat",null);
                            StaticsClass.lng=shar.getString("lng",null);

                            Toast.makeText(MapEditProfileActivity.this, "locaion updated", Toast.LENGTH_SHORT).show();
                            MapEditProfileActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MapEditProfileActivity.super.onBackPressed();
                        }
                    }).setNeutralButton("Cancel",null);
                    buil.create().show();
            }
            else
            {Toast.makeText(this, "it's same location", Toast.LENGTH_SHORT).show();
                super.onBackPressed();}


    }



//    public void go(View view) {
//        startActivity(new Intent(this,MainHomeActivity.class));
//    }

}