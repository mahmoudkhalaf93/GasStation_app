package com.example.gasstation;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetGasStations {

    public List<GasStations> GetData(Context c){
        double dis;
        LatLng gasstation,userlocation;
        List<GasStations> gglist=new ArrayList<>();
        Database db=new Database();
        db.connect(c);
        ResultSet rs=db.GetData("SELECT * FROM GasStation ");
        try {while (rs.next())
        {
            GasStations gg=new GasStations();
            gg.setGasStationNo(rs.getString("GasStationNo"));
            gg.setArea(rs.getString("Area"));
            gg.setCity(rs.getString("City"));
            gg.setName(rs.getString("Name"));
            gg.setLogo(rs.getString("Logo"));
            gg.setLongitude(rs.getString("Longitude"));
            gg.setLatitude(rs.getString("Latitude"));
            gg.setPhone(rs.getString("Phone"));
            gasstation=new LatLng(Double.parseDouble(gg.getLatitude()),Double.parseDouble(gg.getLongitude()));
            userlocation=new LatLng(Double.parseDouble(StaticsClass.lat),Double.parseDouble(StaticsClass.lng));
            dis = CalculationByDistance(userlocation, gasstation);
            if(dis<4){
            gglist.add(gg);}
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
return gglist;
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

}//endclass
