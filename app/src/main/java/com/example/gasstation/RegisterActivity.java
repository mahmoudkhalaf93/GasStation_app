package com.example.gasstation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Connection;

public class RegisterActivity extends AppCompatActivity {
EditText name;
EditText email;
EditText password;
EditText confirmPassword;
EditText phone;
ImageView imgprofile;
    ConstraintLayout conreg;
    Uri filePath;
    String path;
    private int  PICK_IMAGE_REQUEST=90;
boolean cp=true,p=true,e=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //swipe
        conreg =  findViewById(R.id.registswip);
        conreg.setOnTouchListener(new OnSwipeTouchListener(RegisterActivity.this) {
            public void onSwipeTop() {
                // Toast.makeText(LoginActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                //    Toast.makeText(LoginActivity.this, "right", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                         //new windo     //currnt windo
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
            public void onSwipeLeft() {
                //    Toast.makeText(LoginActivity.this, "left", Toast.LENGTH_SHORT).show();


            }
            public void onSwipeBottom() {
                //     Toast.makeText(LoginActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });
        //swipe

        ImageView img=findViewById(R.id.dot_register);
        Animation anim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rot);
        img.startAnimation(anim);
        imgprofile=findViewById(R.id.register_profile_img);
        name=findViewById(R.id.register_name);
        email=findViewById(R.id.register_email);
        password=findViewById(R.id.register_passowrd);
        confirmPassword=findViewById(R.id.register_confirm_password);
        phone=findViewById(R.id.register_phone);

        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false) {
                    if (!(password.getText().toString().equals(confirmPassword.getText().toString()))) {
                        confirmPassword.setError("the password not match");
                        cp=false;
                        Toast.makeText(RegisterActivity.this,confirmPassword.getError(),Toast.LENGTH_LONG).show();
                    }
                    else  cp=true;

                } }});
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b==false) {
                    String st="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
                    if(!password.getText().toString().matches(st))
                    { password.setError("the password required 8 input at least one letter and one number");
                        p=false;
                    }
                    else p=true;
                }
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String st="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                if(!email.getText().toString().matches(st))
                { email.setError("invalid email");
                    e=false;
                }
                else e=true;
            }
        });

    }


    public void reg(View view) {

        if(name.getText().toString().isEmpty())
        {
            name.setError("Enter Name");
            name.requestFocus();
        }
        else{
            if(email.getText().toString().isEmpty())
            {

                email.setError("Enter Email");
                email.requestFocus();
            }
            else
            {
                if(password.getText().toString().isEmpty())
                {
                    password.setError("Enter Password");
                    password.requestFocus();
                }
                else
                {
                    if(confirmPassword.getText().toString().isEmpty())
                    {
                        confirmPassword.setError("Enter Confirm Password");
                        confirmPassword.requestFocus();
                    }
                    else {
                        if(phone.getText().toString().isEmpty())
                        {
                            phone.setError("Enter Phone");
                            phone.requestFocus();
                        }
                        else
                        {
                            if(!(cp&&p&&e)){
                                Toast.makeText(this, "fix error", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                insertIntoCustomer();
//                                Database db = new Database();
//                                Connection conn = db.connect(RegisterActivity.this);
//                                if (conn == null) {
//                                    Toast.makeText(RegisterActivity.this, "no internet Connection", Toast.LENGTH_SHORT).show();
//                                    // NoInternetConnection();
//                                } else {
//                                    String ss = db.Rundml("INSERT INTO Customer(CustName,Password,Email,Phone,Image) VALUES (N'" + name.getText().toString() + "','" + password.getText().toString() + "','" + email.getText().toString() + "','" + phone.getText().toString() + "','" + path + "');");
//                                }
                                }
                        }
                    }
                }
            }
        }

    }

    public void getimagefromgallary(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Image"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            //uploadImage();
            try {
                Bitmap sirisha = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), filePath);
                imgprofile.setImageBitmap(sirisha);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void insertIntoCustomer() {
        try {
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.MyDialogThemeDark);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
    final StorageReference ref = storageReference.child("userprofilephoto/" + StaticsClass.nameLogin);
    ref.putFile(filePath)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            path = downloadUrl.toString();
                            Database db = new Database();
                            Connection conn = db.connect(RegisterActivity.this);
                            if (conn == null) {
                                Toast.makeText(RegisterActivity.this, "no internet Connection", Toast.LENGTH_SHORT).show();
                                // NoInternetConnection();
                            } else {
                                String ss = db.Rundml("INSERT INTO Customer(CustName,Password,Email,Phone,Image) VALUES (N'" + name.getText().toString() + "','" + password.getText().toString() + "','" + email.getText().toString() + "','" + phone.getText().toString() + "','" + path + "');");
                                if (ss.equals("ok")) {
                                    AlertDialog.Builder m = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogThemeDark)
                                            .setTitle("Register...")
                                            .setMessage("Your account has been created")
                                            .setIcon(R.drawable.logfitt)
                                            .setPositiveButton("Thanks", null)
                                            .setNegativeButton("Goto Login", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();
                                                }
                                            });
                                    m.create().show();
                                } else if (ss.contains("unPhone"))
                                    Toast.makeText(RegisterActivity.this, "this phone is used", Toast.LENGTH_SHORT).show();
                                else if (ss.contains("unEmail"))
                                    Toast.makeText(RegisterActivity.this, "this email is used", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(RegisterActivity.this, "" + ss, Toast.LENGTH_SHORT).show();

                            }
                            progressDialog.dismiss();

                            //////////////////////
                        }
                    });

                }
            })
            .addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                }
            });
}
catch (Exception e){

}
        if(filePath==null){ Database db = new Database();
            Connection conn = db.connect(RegisterActivity.this);
            if (conn == null) {
                Toast.makeText(RegisterActivity.this, "no internet Connection", Toast.LENGTH_SHORT).show();
                // NoInternetConnection();
            } else {
                String ss = db.Rundml("INSERT INTO Customer(CustName,Password,Email,Phone) VALUES (N'" + name.getText().toString() + "','" + password.getText().toString() + "','" + email.getText().toString() + "','" + phone.getText().toString() + "');");
                if (ss.equals("ok")) {
                    AlertDialog.Builder m = new AlertDialog.Builder(RegisterActivity.this, R.style.MyDialogThemeDark)
                            .setTitle("Register...")
                            .setMessage("Your account has been created")
                            .setIcon(R.drawable.logfitt)
                            .setPositiveButton("Thanks", null)
                            .setNegativeButton("Goto Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });
                    m.create().show();
                } else if (ss.contains("unPhone"))
                    Toast.makeText(RegisterActivity.this, "this phone is used", Toast.LENGTH_SHORT).show();
                else if (ss.contains("unEmail"))
                    Toast.makeText(RegisterActivity.this, "this email is used", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RegisterActivity.this, "" + ss, Toast.LENGTH_SHORT).show();

            }}

    }

}