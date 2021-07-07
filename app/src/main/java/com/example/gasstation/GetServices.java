package com.example.gasstation;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetServices {

    public List<Services> GetData(Context c,String gasStationNO)
    {
        List<Services> pp=new ArrayList<>();
        Database db=new Database();
        db.connect(c);
        ResultSet yy=db.GetData("SELECT * FROM Services WHERE GasStationNo='"+gasStationNO+"';");
        try {
          while (yy.next())
          {
              Services xx=new Services();
              xx.setServiceNO(yy.getString(1));
              xx.setName(yy.getString(2));
              xx.setCost(yy.getString(3));
              xx.setLogo(yy.getString(4));
              xx.setGasStationNo(yy.getString(5));
              xx.setDetails(yy.getString(6));
              pp.add(xx);
          }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pp;
    }
}
