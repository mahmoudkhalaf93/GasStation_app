package com.example.gasstation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    EditText emailphone;
    EditText password;
    CheckBox rememberme;
    TextView forgetpassword;
    ConstraintLayout con;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //swipe
        con =  findViewById(R.id.maincontn);
        con.setOnTouchListener(new OnSwipeTouchListener(LoginActivity.this) {
            public void onSwipeTop() {
               // Toast.makeText(LoginActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
            //    Toast.makeText(LoginActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
            //    Toast.makeText(LoginActivity.this, "left", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(R.anim.fedinlin, R.anim.fedolin);
                
            }
            public void onSwipeBottom() {
           //     Toast.makeText(LoginActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
        //swipe

        SharedPreferences shar= getSharedPreferences("gasStation",MODE_PRIVATE);
       String sharstring = shar.getString("name",null);
   if(sharstring!=null) {
       Database db=new Database();
       if(!(db.connect(this)==null)){

           StaticsClass.IDLogin=shar.getString("id",null);
           StaticsClass.lat=shar.getString("lat",null);
           StaticsClass.lng=shar.getString("lng",null);
           StaticsClass.imgProfile=shar.getString("img",null);
           StaticsClass.emailLogin=shar.getString("email",null);
           StaticsClass.nameLogin=shar.getString("name",null);
       startActivity(new Intent(this, MainHomeActivity.class));
       finish();}
else
           Toast.makeText(this, "open wifi then press back button to login", Toast.LENGTH_LONG).show();

   }
else {

       ImageView img = findViewById(R.id.dotlogin);
       Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rot);
       img.startAnimation(anim);
       emailphone = findViewById(R.id.loginEmailphone);
       password = findViewById(R.id.logintextPassword);
       rememberme = findViewById(R.id.RememberMe);
       forgetpassword = findViewById(R.id.ForgetPassword);

   }
    }

    public void Login(View view) {

        if(emailphone.getText().toString().isEmpty())
        {
            emailphone.setError("enter your email / phone");
            emailphone.requestFocus();
        }
        else {
            if(password.getText().toString().isEmpty())
            {
                password.setError("enter password");
                password.requestFocus();
            }
            else {
                Database db=new Database();
                Connection con=db.connect(this);
          if(con==null)
              {
                  Toast.makeText(this, "no internet Connection", Toast.LENGTH_SHORT).show();
              }
          else
               {
                   ResultSet rs=db.GetData("select * from Customer where (Email='"+emailphone.getText()+"' or Phone='"+emailphone.getText()+"') and Password='"+password.getText()+"'");
                   try {
                       if(rs.next())
                       {
                           StaticsClass.IDLogin=rs.getString("CustomerNo");
                           StaticsClass.lat=rs.getString("Latitude");
                           StaticsClass.lng=rs.getString("Longitude");
                           StaticsClass.imgProfile=rs.getString("Image");
                           StaticsClass.emailLogin=rs.getString("Email");
                           StaticsClass.nameLogin=rs.getString("CustName");
                           if(rememberme.isChecked()) {
                               getSharedPreferences("gasStation", MODE_PRIVATE).edit()
                                       .putString("id",StaticsClass.IDLogin )
                                       .putString("name",StaticsClass.nameLogin )
                                       .putString("lat",StaticsClass.lat )
                                       .putString("lng",StaticsClass.lng )
                                       .putString("img",StaticsClass.imgProfile)
                                       .putString("email",StaticsClass.emailLogin)
                                       .apply();
                           }
                         //  Toast.makeText(LoginActivity.this, "id="+StaticsClass.IDLogin, Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(this,MainHomeActivity.class));
                       finish();

                       }
                       else
                       {
                           Toast.makeText(this, "wrong email or password", Toast.LENGTH_SHORT).show();
                       }
                   } catch (SQLException throwables) {
                       throwables.printStackTrace();
                   }
               }
            }
        }

    }

    public void CreateNewUser(View view) {
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    public void onBackPressed(){
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void ForgetPassword(View view) {
        startActivity(new Intent(this,CheckEmailActivity.class));
    }

//    public void gotovideo(View view) {
//startActivity(new Intent(this,MainActivity2.class));
//    }
}

