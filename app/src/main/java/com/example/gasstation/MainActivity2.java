package com.example.gasstation;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {

//EditText loca;
//StorageReference videoRef;
//    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        loca=findViewById(R.id.location);
//
//       // String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//         storageRef= FirebaseStorage.getInstance().getReference();
//        videoRef=storageRef.child("/video/dyto.mp4");

    }

    public void getvideo(View view) {
//        if(!loca.getText().toString().isEmpty()){
//            videoRef=storageRef.child("/video/"+loca.getText().toString()+".mp4");
//        }
//        else
//        { videoRef=storageRef.child("/video/dyto.mp4");}
//try {
//    final File localfile = File.createTempFile("userVideo", "mp4");
//    videoRef.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//        @Override
//        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//            Toast.makeText(MainActivity2.this, "done", Toast.LENGTH_SHORT).show();
//       final VideoView videoView=findViewById(R.id.videoView);
//       videoView.setVideoURI(Uri.fromFile(localfile));
//       videoView.start();
//        }
//    });
//} catch (IOException e) {
//    e.printStackTrace();
//}

    }
}