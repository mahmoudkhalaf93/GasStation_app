package com.example.gasstation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBLIGHT  extends SQLiteOpenHelper {

    public DBLIGHT(@Nullable Context context ) {
        super(context, "GasStation", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
     sqLiteDatabase.execSQL("CREATE TABLE Orders ( ID INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, servNo TEXT, servName TEXT, qty NUMERIC, price NUMERIC, total NUMERIC ,servImg TEXT );");
    }
    public void changeqty(Double qtys,Double price,String id){
        SQLiteDatabase gg=getWritableDatabase();
        gg.execSQL("UPDATE Orders SET qty = "+qtys+",total ="+qtys*price+" WHERE ID='"+id+"';");
    }
    public void Delete(String id){
        SQLiteDatabase gg=getWritableDatabase();
        gg.execSQL("DELETE FROM Orders WHERE ID='"+id+"';");
    }
    public void savecart(String servNo,String servName,double qty,double price,String servImg){
        SQLiteDatabase gg= getWritableDatabase();
        ContentValues cc=new ContentValues();
        cc.put("servNo",servNo);
        cc.put("servName",servName);
        cc.put("qty",qty);
        cc.put("price",price);
        cc.put("total",(qty*price));
        cc.put("servImg",servImg);
        gg.insert("Orders",null,cc);
    }
public int getCount(){
    SQLiteDatabase gg= getReadableDatabase();
    Cursor cr=gg.rawQuery("SELECT COUNT(*) FROM Orders",null);
    if(cr.moveToFirst()){
        return cr.getInt(0);
    }
    return 0;
}
    public Double getTotalPriceCart(){
        SQLiteDatabase gg= getReadableDatabase();
        Cursor cr=gg.rawQuery("SELECT SUM(total) FROM Orders",null);
        if(cr.moveToFirst()){
            return cr.getDouble(0);
        }
        return 0.0;
    }
    public  void DeleteAll()
    {
        SQLiteDatabase gg=getWritableDatabase();
        gg.execSQL("delete from Orders");
    }
    public List<CartShoppingModel> GetData()
    {
        List<CartShoppingModel> pp=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor cr=db.rawQuery("SELECT * FROM Orders",null);

            while (cr.moveToNext())
            {
                CartShoppingModel xx=new CartShoppingModel();

                xx.setID(cr.getString(0));
                xx.setServNo(cr.getString(1));
                xx.setServName(cr.getString(2));
                xx.setQty(cr.getString(3));
                xx.setPrice(cr.getString(4));
                xx.setTotal(cr.getString(5));
                xx.setServImg(cr.getString(6));
                pp.add(xx);
            }

        return pp;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
