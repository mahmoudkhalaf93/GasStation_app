package com.example.gasstation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;

public class RestPasswordActivity extends AppCompatActivity {
TextView name;
EditText pass;
EditText cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_password);
        name=findViewById(R.id.name_in_resetpassword);
        name.setText(  StaticsClass.nameCheckEmail);
        pass=findViewById(R.id.password_rest_password);
        cpass=findViewById(R.id.confirm_password_inrest);
    }

    public void reset(View view) {
        if(pass.getText().toString().equals(cpass.getText().toString()))
        {
            Database db=new Database();
            db.connect(this);
           String updat= db.Rundml("update Customer set Password='"+pass.getText()+"' WHERE Email='"+  StaticsClass.emailCheckEmail+"'");
            if(updat.equals("ok"))
            {
                Toast.makeText(this, "your password has been updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            }
            else
                Toast.makeText(this, ""+updat, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Password not match with confirm password", Toast.LENGTH_SHORT).show();
        }
    }
}