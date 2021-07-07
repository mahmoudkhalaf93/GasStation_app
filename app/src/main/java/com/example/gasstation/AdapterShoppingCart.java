package com.example.gasstation;


import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterShoppingCart extends ArrayAdapter<CartShoppingModel> {
  //  String beforchange="0",afterchange="0";
    Context c;
    ArrayList<CartShoppingModel> ass;

    public AdapterShoppingCart(Context context, ArrayList<CartShoppingModel> cont) {
        super(context, R.layout.shopping_cartlist_layout,cont);
        c=context;
        ass=cont;
    }

    class Holder
    {

        ShapeableImageView imgCart;
        ImageView imgDelete;
        TextView  nameCart;
        TextView  totalPrice;
        TextView  servicePrice;
        EditText qty;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        CartShoppingModel data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.shopping_cartlist_layout, parent, false);
            viewHolder.imgCart=convertView.findViewById(R.id.shop_cart_image);
            viewHolder.nameCart=convertView.findViewById(R.id.shop_cart_name);
            viewHolder.totalPrice=convertView.findViewById(R.id.shop_cart_tot_price);
            viewHolder.servicePrice=convertView.findViewById(R.id.shop_cart_service_price);
            viewHolder.imgDelete=convertView.findViewById(R.id.shop_cart_delete);
            viewHolder.qty=convertView.findViewById(R.id.shop_cart_qty);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }

       Picasso.get().load(data.getServImg()).error(R.drawable.logfitt).
               resize(300,300).into(viewHolder.imgCart);
        viewHolder.nameCart.setText(data.getServName());
        viewHolder.totalPrice.setText("Total : "+data.getTotal()+" LE");
       viewHolder.servicePrice.setText("Price : " +data.getPrice()+" LE");
        viewHolder.qty.setText(data.getQty());
//        viewHolder.qty.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//              try {
//                  if (viewHolder.qty.hasFocus()) {
//                      DBLIGHT gg = new DBLIGHT(c);
//                      gg.changeqty(Double.parseDouble(viewHolder.qty.getText().toString()), Double.parseDouble(data.getPrice()), data.getID());
//                      data.setQty(viewHolder.qty.getText().toString());
//                      data.setTotal(String.valueOf(Double.parseDouble(viewHolder.qty.getText().toString()) * Double.parseDouble(data.getPrice())));
//                  }
//              }
//              catch (Exception e){
//                  Toast.makeText(c, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
//              }
//            }
//        });
        viewHolder.qty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    DBLIGHT gg=new DBLIGHT(c);
                    gg.changeqty(Double.parseDouble(viewHolder.qty.getText().toString()),Double.parseDouble(data.getPrice()), data.getID());
                    data.setQty(viewHolder.qty.getText().toString());
                    data.setTotal(String.valueOf(Double.parseDouble(viewHolder.qty.getText().toString())*Double.parseDouble(data.getPrice())));
                    //  Toast.makeText(c, "after edit"+data.getQty(), Toast.LENGTH_SHORT).show();
                    ShopingCartActivity sa=new ShopingCartActivity();
                    sa.UpdateSummary(c);
                    viewHolder.qty.clearFocus();
                    InputMethodManager imm =  (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    Toast.makeText(c, "done", Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        });


       viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBLIGHT mm=new DBLIGHT(c);
                mm.Delete(data.getID());
                AdapterShoppingCart.this.remove(getItem(position));
                AdapterShoppingCart.this.notifyDataSetChanged();
                ServicesActivity.count.setText(String.valueOf(mm.getCount()));
                Toast.makeText(c, "Deleted", Toast.LENGTH_SHORT).show();
                ShopingCartActivity sa=new ShopingCartActivity();
                        sa.UpdateSummary(c);
            }
        });

        // Return the completed view to render on screen
        return convertView;

    }


}
