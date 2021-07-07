package com.example.gasstation;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String TAG = "Database";
    Connection conn=null;
    public Connection connect(Context c){

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
             conn= DriverManager.getConnection("jdbc:jtds:sqlserver://sql5104.site4now.net/db_a74796_gasstationmody","db_a74796_gasstationmody_admin","142536qw!");
            Log.e(TAG,"Connection Done");
        } catch (ClassNotFoundException e) {
          //  e.printStackTrace();
            Log.e(TAG,"class not found except"+ e);
        } catch (SQLException throwables) {
           // throwables.printStackTrace();
            Log.e(TAG,"sql exception " +throwables);
        }
       // if (conn==null)
           // NoInternetConnection(c);

      return conn;
    }

    public String Rundml(String st){
        try {
            Statement hh=conn.createStatement();
            hh.executeUpdate(st);
            return "ok";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return throwables.getMessage();
        }
    }

    public ResultSet GetData(String st){
           ResultSet rs=null;
        try {
            Statement hh=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rs=hh.executeQuery(st);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rs;
    }

//    public  void NoInternetConnection(Context c){
//        AlertDialog.Builder  builder = new AlertDialog.Builder(c,R.style.MyDialogThemelight);
//
//        //Uncomment the below code to Set the message and title from the strings.xml file
//        builder.setMessage("choose wifi or mobile data") .setTitle("No Internet Connection");
//        //Setting message manually and performing action on button click
//        builder.setMessage("please choose wifi or mobile data.")
//                .setCancelable(false)
//                .setPositiveButton("wifi", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Intent str=new Intent();
//                        c.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//
//                    }
//                })
//                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //  Action for 'NO' Button
//                        dialog.cancel();
//                    }
//                }).setNegativeButton("mobile data", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                //  Action for 'NO' Button
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName(
//                        "com.android.settings",
//                        "com.android.settings.Settings$DataUsageSummaryActivity"));
//                c.startActivity(intent);
//            }
//        });
//        //Creating dialog box
//        AlertDialog alert = builder.create();
//        //Setting the title manually
//        alert.setTitle("No Internet Connection");
//        alert.show();
//
//    }
}
