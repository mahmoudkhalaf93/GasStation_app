package com.example.gasstation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Rating;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.gasstation.ui.home.HomeFragment.ddgt;

public class ActivityServiceProfile extends AppCompatActivity {
    ShapeableImageView profServImg;
    TextView profServName,profServprice,profServDetails;
    RatingBar profServRating;
    GetRatingServcReview ggr=new GetRatingServcReview();
    ArrayList<RatingServcReview> dd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_profile);
        profServImg=findViewById(R.id.servprofile_servceprofileimage);
        profServName = findViewById(R.id.servprofile_servcname);
        profServprice = findViewById(R.id.servprofile_servcprice);
        profServDetails = findViewById(R.id.servprofile_servcdetails);
        profServRating = findViewById(R.id.servprofile_servcratingbar);
        try {
            Database db = new Database();
           db.connect(this);
            ResultSet rs = db.GetData("select * from Services where ServiceNO='" + StaticsClass.serviceProfileId + "';");
            rs.beforeFirst();
        if (rs.next()){
            Picasso.get().load(rs.getString("Logo")).error(R.drawable.logfitt).
                    resize(300,300).into(profServImg);
            profServName.setText("Name : "+rs.getString("Name"));
            profServprice.setText("Price : "+rs.getString("Cost")+" LE");
            profServDetails.setText("Details : "+rs.getString("Details"));
           //set rating
            double rate=0,count=0;
            Iterator<GetRateGasStation> iter = ddgt.iterator();
            while(iter.hasNext()){
                GetRateGasStation xx=iter.next();
                if(xx.getServiceNo().equals(StaticsClass.serviceProfileId)){
                    rate=rate+Double.parseDouble(xx.getValue());
                    count++;
                }
            }
            if(count!=0)
                rate=rate/count;
            profServRating.setRating((float) rate);
            //test
          //  ResultSet rat = db.GetData(" SELECT AVG(Value) FROM Rating where  ServiceNo='" + StaticsClass.serviceProfileId + "';");
       //   if (rat.next())
         //   profServRating.setRating(rat.getFloat(1));
            //test
         //end of set rating
//Review
            dd=new ArrayList<>(ggr.GetData(this,StaticsClass.serviceProfileId));
            AdapterRecyclreviewView adapter = new AdapterRecyclreviewView(dd);
            RecyclerView recyclerView =  findViewById(R.id.reviews_profileserv);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
 //endreviews

        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();
        }

    }
}