package com.example.gasstation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.hbb20.CountryCodePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyProfileActivity extends AppCompatActivity  {
   private int  PICK_IMAGE_REQUEST=90;
    EditText namep;
    EditText emailp;
    EditText phonep;
   Uri filePath;
  //  CountryCodePicker country;

    boolean e=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ImageView img = findViewById(R.id.dot_registerp);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rot);
        img.startAnimation(anim);

        namep = findViewById(R.id.register_namep);
        emailp = findViewById(R.id.register_emailp);
        phonep = findViewById(R.id.register_phonep);
        try {
            Database db = new Database();
            Connection con = db.connect(this);
            if (con == null) {
                onBackPressed();
                Toast.makeText(this, "no internet Connection", Toast.LENGTH_SHORT).show();
            }
            else {
            ResultSet rs = db.GetData("select * from Customer WHERE CustomerNo='" + StaticsClass.IDLogin + "'");
            if (rs.next()) {
                // Toast.makeText(this, rs.getString(1)+" "+StaticsClass.IDLogin+" "+rs.getString("CustName"), Toast.LENGTH_SHORT).show();

                namep.setText(rs.getString("CustName"));
                emailp.setText(rs.getString("Email"));
                phonep.setText(rs.getString("Phone"));
            }
        }
        }

        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        emailp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String st = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                if (!emailp.getText().toString().matches(st)) {
                    emailp.setError("invalid email");
                    e = false;
                } else e = true;
            }
        });



    }

    public void Update(View view) {
        Database db = new Database();
        Connection conn = db.connect(this);
        if(namep.getText().toString().isEmpty())
        {
            namep.setError("Enter Name");
            namep.requestFocus();
        }
        else{
            if(emailp.getText().toString().isEmpty())
            {

                emailp.setError("Enter Email");
                emailp.requestFocus();
            }
            else
            {
                        if(phonep.getText().toString().isEmpty())
                        {
                            phonep.setError("Enter Phone");
                            phonep.requestFocus();
                        }
                        else
                        {

                            boolean state;
                            try {
                                ResultSet rs=null;
                                if(conn==null){onBackPressed();Toast.makeText(this, "no internet Connection", Toast.LENGTH_SHORT).show();}
                                else {rs = db.GetData("select * from Customer WHERE CustomerNo='" + StaticsClass.IDLogin + "'");}
                                rs.next();
                                 state = namep.getText().toString().equals(rs.getString("CustName")) && emailp.getText().toString().equals(rs.getString("Email")) && phonep.getText().toString().equals(rs.getString("Phone"));


                            } catch (SQLException throwables) {
                                state=false;
                                throwables.printStackTrace();
                            }
                            if(state)
                            {
                                Toast.makeText(this, "This data is already updated", Toast.LENGTH_SHORT).show();
                        }

                            else {
                                if (!(e)) {
                                    Toast.makeText(this, "fix error", Toast.LENGTH_SHORT).show();
                                } else {

                                    if (conn == null) {
                                        Toast.makeText(this, "no internet Connection", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String ss = db.Rundml("update Customer set CustName=N'" + namep.getText().toString() + "',Email='" + emailp.getText().toString() + "',Phone='" + phonep.getText().toString() + "' WHERE CustomerNo='" + StaticsClass.IDLogin + "';");
                                       StaticsClass.nameLogin=namep.getText().toString();
                                       StaticsClass.emailLogin=emailp.getText().toString();
                                        getSharedPreferences("gasStation", MODE_PRIVATE).edit()
                                                .putString("name",StaticsClass.nameLogin )
                                                .putString("email",StaticsClass.emailLogin)
                                                .apply();
                                        if (ss.equals("ok")) {
                                            AlertDialog.Builder m = new AlertDialog.Builder(MyProfileActivity.this, R.style.MyDialogThemeDark)
                                                    .setTitle("Updated...")
                                                    .setMessage("Your account has been Updated")
                                                    .setIcon(R.drawable.logfitt)
                                                    .setNegativeButton("Goto HomePage", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            startActivity(new Intent(MyProfileActivity.this, MainHomeActivity.class));
                                                            finish();
                                                        }
                                                    });
                                            m.create().show();
                                            startActivity(new Intent(MyProfileActivity.this, MainHomeActivity.class));
                                            finish();
                                        } else
                                            Toast.makeText(this, "" + ss, Toast.LENGTH_SHORT).show();

                                    }

                                }
                                //
                            }




                        }
            }
        }

    }

    public void uploadimage(View view){
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
            uploadImage();
//            try {
//                Bitmap sirisha = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), filePath);
//
//            //    img.setImageBitmap(sirisha);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }


    public void SetLocation(View view) {

                        startActivity(new Intent(MyProfileActivity.this, MapEditProfileActivity.class));
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
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
                                progressDialog.dismiss();
                                String path = downloadUrl.toString();
                                Database db = new Database();
                                Connection conn = db.connect(MyProfileActivity.this);

                                if (conn == null)
                                    Toast.makeText(MyProfileActivity.this, "Check internet access", Toast.LENGTH_SHORT).show();
                                else {                                       //WHERE CustomerNo='" + StaticsClass.IDLogin + "'"
                                    //Toast.makeText(MyProfileActivity.this, ""+path, Toast.LENGTH_SHORT).show();
//                                    UPDATE Customers
//                                    SET ContactName = 'Alfred Schmidt', City= 'Frankfurt'
//                                    WHERE CustomerID = 1;
                                    String msg = db.Rundml("UPDATE Customer set Image='"+ path + "' where CustomerNo=" + StaticsClass.IDLogin + ";");
                                    if (msg.equals("ok")) {
                                        StaticsClass.imgProfile=path;
                                        getSharedPreferences("gasStation", MODE_PRIVATE).edit()
                                                .putString("img",StaticsClass.imgProfile);

                                        AlertDialog.Builder mg = new AlertDialog.Builder(MyProfileActivity.this)
                                                .setTitle("Events...")
                                                .setMessage("profile image has been uploaded")
                                                .setIcon(R.drawable.logfitt)
                                                .setNegativeButton("Goto HomePage", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        startActivity(new Intent(MyProfileActivity.this, MainHomeActivity.class));
                                                        finish();
                                                    }
                                                });
                                        mg.create();
                                        mg.show();
                                        startActivity(new Intent(MyProfileActivity.this, MainHomeActivity.class));
                                        finish();
                                    } else
                                        Toast.makeText(MyProfileActivity.this, "" + msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(MyProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
}