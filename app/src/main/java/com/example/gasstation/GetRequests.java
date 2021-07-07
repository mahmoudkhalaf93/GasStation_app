package com.example.gasstation;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetRequests {

    public List<Requests> GetData(Context c){

        List<Requests> gglist=new ArrayList<>();
        Database db=new Database();
        db.connect(c);
        ResultSet rs=db.GetData("SELECT * FROM salesreq  WHERE CustomerNo='"+StaticsClass.IDLogin+"';");
        try {while (rs.next())
        {// String RequestDate ,CustomerNo ,RequestNo ,	TotalRequest ,	ServiceNo ,	QTY ,	Total ,	Price ,	Status ,RatedorNot,Logo,Name,Details;
            Requests gg=new Requests();
            gg.setRequestDate(rs.getString(1));
            gg.setCustomerNo(rs.getString(2));
            gg.setRequestNo(rs.getString(3));
            gg.setTotalRequest(rs.getString(4));
            gg.setServiceNo(rs.getString(5));
            gg.setQTY(rs.getString(6));
            gg.setTotal(rs.getString(7));
            gg.setPrice(rs.getString(8));
            gg.setStatus(rs.getString(9));
            gg.setRatedorNot(rs.getString(10));
            gg.setLogo(rs.getString(11));
            gg.setName(rs.getString(12));
            gg.setDetails(rs.getString(13));
                gglist.add(gg);
        }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return gglist;
    }
}
