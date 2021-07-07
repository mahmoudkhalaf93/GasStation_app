package com.example.gasstation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gasstation.ui.home.HomeFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ShopingCartActivity extends AppCompatActivity {
    ListView list;
    CartShoppingModel model;
    DBLIGHT gg=new DBLIGHT(this);
    AdapterShoppingCart adapterShoppingCart;
    ArrayList<CartShoppingModel> dd;
    Button confirmOrder;
static TextView itemsPrice,totalPrice,deliver,cashFees;

    static double  deliverValue=25,cashFeesValue=10;
    static CheckBox deliverCheck,cashFeesCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_cart);
        itemsPrice=findViewById(R.id.shop_cart_items_price);
                totalPrice=findViewById(R.id.shop_cart_total_price_sumery);
        deliver=findViewById(R.id.shop_cart_deliver);
                cashFees=findViewById(R.id.shop_cart_cash_fees);
        deliverCheck=findViewById(R.id.shop_cart_deliver_check);
                cashFeesCheck=findViewById(R.id.shop_cart_cash_check);
        UpdateSummary(this);
        deliverCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSummary(ShopingCartActivity.this);
            }
        });
        cashFeesCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSummary(ShopingCartActivity.this);
            }
        });
        list=findViewById(R.id.shop_cart_list);
        dd=new ArrayList<>(gg.GetData());
//        if(dd.isEmpty())
//        {Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this,MainHomeActivity.class));
//        }
        adapterShoppingCart=new AdapterShoppingCart(this,dd);
        list.setAdapter(adapterShoppingCart);

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(event.getKeyCode()==KeyEvent.KEYCODE_VOLUME_UP)
//        {
//            Toast.makeText(this, "valuem up", Toast.LENGTH_SHORT).show();
//        }
//        return true;
//    }

    public static void  UpdateSummary(Context c){
  //  itemsPrice,totalPrice,deliver,cashFees;
    // deliverCheck,cashFeesCheck;
    //deliverValue=25,cashFeesValue=10;
    DBLIGHT dblight=new DBLIGHT(c);
    itemsPrice.setText("Items Price : "+String.valueOf(dblight.getTotalPriceCart())+" LE");
    if(!deliverCheck.isChecked()&&!cashFeesCheck.isChecked()){
        totalPrice.setText("Total Price : "+String.valueOf(dblight.getTotalPriceCart())+" LE");
    }
    else if(!deliverCheck.isChecked()&&cashFeesCheck.isChecked()){
        totalPrice.setText("Total Price : "+String.valueOf(dblight.getTotalPriceCart()+cashFeesValue)+" LE");
    }
    else if(deliverCheck.isChecked()&&!cashFeesCheck.isChecked()){
        totalPrice.setText("Total Price : "+String.valueOf(dblight.getTotalPriceCart()+deliverValue)+" LE");
    }
    else if(deliverCheck.isChecked()&&cashFeesCheck.isChecked()){
        totalPrice.setText("Total Price : "+String.valueOf(dblight.getTotalPriceCart()+cashFeesValue+deliverValue)+" LE");
    }
    }

    public void ConfirmOrder(View view) {

        Database db=new Database();
        Connection c=db.connect(this);
      //  if(! (c==null)){}
        String insertQuerry="no";
double totl=0;
        if(!deliverCheck.isChecked()&&!cashFeesCheck.isChecked()){
            insertQuerry=db.Rundml("insert into Request(RequestDate,CustomerNo,TotalRequest) values ('"+ Calendar.getInstance().getTime()+"','"+StaticsClass.IDLogin+"','"+gg.getTotalPriceCart()+"')");
        }
        else if(!deliverCheck.isChecked()&&cashFeesCheck.isChecked()){totl=gg.getTotalPriceCart()+cashFeesValue;
            insertQuerry=db.Rundml("insert into Request(RequestDate,CustomerNo,Cashfees,TotalRequest) values ('"+ Calendar.getInstance().getTime()+"','"+StaticsClass.IDLogin+"','"+cashFeesValue+"','"+totl+"')");
        }
        else if(deliverCheck.isChecked()&&!cashFeesCheck.isChecked()){totl=gg.getTotalPriceCart()+deliverValue;
            insertQuerry=db.Rundml("insert into Request(RequestDate,CustomerNo,Deliveryfees,TotalRequest) values ('"+ Calendar.getInstance().getTime()+"','"+StaticsClass.IDLogin+"','"+deliverValue+"','"+totl+"')");
        }
        else if(deliverCheck.isChecked()&&cashFeesCheck.isChecked()){totl=gg.getTotalPriceCart()+cashFeesValue+deliverValue;
            insertQuerry=db.Rundml("insert into Request(RequestDate,CustomerNo,Cashfees,Deliveryfees,TotalRequest) values ('"+ Calendar.getInstance().getTime()+"','"+StaticsClass.IDLogin+"','"+cashFeesValue+"','"+deliverValue+"','"+totl+"')");
        }
     //   String mm=db.Rundml("insert into Request(RequestDate,CustomerNo,Cashfees,Deliveryfees,Total) values ('"+ Calendar.getInstance().getTime()+"','"+StaticsClass.IDLogin+"','"+gg.getTotalPriceCart()+"')");
        if(insertQuerry.equals("ok")){
            ResultSet r=db.GetData("select max(RequestNo) as max from Request where CustomerNo='"+StaticsClass.IDLogin+"'");
            try {
                if(r.next()) {
                    String max = r.getString(1);
                    for (int x = 0; x < dd.size(); x++) {
                        model = dd.get(x);
                        insertQuerry = db.Rundml("insert into Sales(RequestoNo,ServiceNo,QTY,Price,Total) values ('" + max + "','" + model.getServNo() + "','" + model.getQty() + "','" + model.getPrice() + "','" + model.getTotal() + "')");
                    }
                    if (insertQuerry.equals("ok")) {
                        Toast.makeText(this, "Request sent", Toast.LENGTH_SHORT).show();
                        gg.DeleteAll();
                        dd = new ArrayList<>(gg.GetData());
                        adapterShoppingCart = new AdapterShoppingCart(this, dd);
                        list.setAdapter(adapterShoppingCart);
                        UpdateSummary(this);
                        DBLIGHT mm = new DBLIGHT(this);
                        ServicesActivity.count.setText(String.valueOf(mm.getCount()));
                        startActivity(new Intent(this, MainHomeActivity.class));
                    } else
                    {
                        Toast.makeText(this, "sales error , Some Services duplicate we send one of them only DB mesg:" + insertQuerry, Toast.LENGTH_LONG).show();
                        gg.DeleteAll();
                        dd=new ArrayList<>(gg.GetData());
                        adapterShoppingCart=new AdapterShoppingCart(this,dd);
                        list.setAdapter(adapterShoppingCart);
                        UpdateSummary(this);
                        DBLIGHT mm=new DBLIGHT(this);
                        ServicesActivity.count.setText(String.valueOf(mm.getCount()));
                        startActivity(new Intent(this,MainHomeActivity.class));
                }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(this, "Request error : "+insertQuerry, Toast.LENGTH_SHORT).show();
    }



}