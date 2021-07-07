package com.example.gasstation;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetRatingServcReview {

    public List<RatingServcReview> GetData(Context c, String ServiceNO)
    {
        List<RatingServcReview> pp=new ArrayList<>();
        Database db=new Database();
        db.connect(c);
        ResultSet yy=db.GetData("SELECT * FROM ratingview WHERE ServiceNo='"+ServiceNO+"';");
        try {
            while (yy.next())
            {//String  Value, 	Comment ,	CustName ,	ServiceNo ;
                RatingServcReview xx=new RatingServcReview();
                xx.setValue(yy.getString(1));
                xx.setComment(yy.getString(2));
                xx.setCustName(yy.getString(3));
                xx.setServiceNo(yy.getString(4));
                pp.add(xx);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pp;
    }
}
