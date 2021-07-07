package com.example.gasstation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VerficationActivity extends AppCompatActivity {
TextView welcom;
EditText verfication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfication);
        welcom=findViewById(R.id.welcomever);
        welcom.setText(StaticsClass.nameCheckEmail);
    verfication=findViewById(R.id.verfication_code);
    }

    public void ResetPassword(View view) {
        if(Integer.parseInt(verfication.getText().toString())==StaticsClass.vCodeCheckEmail)
        {   Database db=new Database();
            db.connect(this);
            db.Rundml("update Customer set EmailVerified=1 WHERE Email='"+ StaticsClass.emailCheckEmail+"'");
            startActivity(new Intent(this,RestPasswordActivity.class));
            finish();
        }
    }
}