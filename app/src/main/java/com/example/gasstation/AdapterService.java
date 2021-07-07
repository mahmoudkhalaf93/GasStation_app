package com.example.gasstation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AdapterService extends ArrayAdapter<Services> {
    Context c;
    ArrayList<Services> ass;
    public AdapterService(Context context, ArrayList<Services> cont) {
        super(context, R.layout.servclayout,cont);
        c=context;
        ass=cont;
    }
    class Holder
    {
        ShapeableImageView imgservc;
        TextView txtNameserv;
        TextView  txtDetailsservc;
        TextView  txtPriceservc;
        ImageView addtocard;
        LinearLayout onclikliner;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Services data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        AdapterService.Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new AdapterService.Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.servclayout, parent, false);
            viewHolder.onclikliner=convertView.findViewById(R.id.onclick_liner_servlayout);
            viewHolder.addtocard=convertView.findViewById(R.id.addToCard);
            viewHolder.imgservc=convertView.findViewById(R.id.servceimage);
            viewHolder.txtNameserv=convertView.findViewById(R.id.servprofile_servcname);
            viewHolder.txtDetailsservc=convertView.findViewById(R.id.servprofile_servcdetails);
            viewHolder.txtPriceservc=convertView.findViewById(R.id.servprofile_servcprice);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (AdapterService.Holder) convertView.getTag();
        }

        Picasso.get().load(data.getLogo()).error(R.drawable.logfitt).
                resize(300,300).into(viewHolder.imgservc);
        viewHolder.txtNameserv.setText(data.getName());
        viewHolder.txtDetailsservc.setText("Details : "+data.getDetails());
        viewHolder.txtPriceservc.setText("Price : "+data.getCost()+" LE");
        viewHolder.onclikliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticsClass.serviceProfileId=data.getServiceNO();
              c.startActivity(new Intent(c, ActivityServiceProfile.class));
            }
        });
        viewHolder.addtocard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // model=dd.get(i);
                LayoutInflater inflater=LayoutInflater.from(c);
                View vv=inflater.inflate(R.layout.bottomsheetdialog,null);

                ShapeableImageView detlsimage = vv.findViewById(R.id.imagedetails);
                TextView detlsname = vv.findViewById(R.id.namedetails);
                TextView detlsdetls = vv.findViewById(R.id.details);
                TextView detlsprice = vv.findViewById(R.id.price);
                detlsname.setText(data.getName());
                detlsdetls.setText("Details : "+data.getDetails());
                detlsprice.setText("Price : "+data.getCost()+" LE");
                final BottomSheetDialog bt=new BottomSheetDialog(c,R.style.AppBottomSheetDialogTheme);
                Picasso.get().load(data.getLogo()).error(R.drawable.logfitt).
                        resize(300,300).into(detlsimage);
                EditText detlsqty = vv.findViewById(R.id.qty);
                Button detladdtocard = vv.findViewById(R.id.addtocart);
                DBLIGHT dblight=new DBLIGHT(c);
                detladdtocard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            dblight.savecart(data.getServiceNO(), data.getName(), Double.parseDouble(detlsqty.getText().toString()), Double.parseDouble(data.getCost()), data.getLogo());
                            ServicesActivity.count.setText(String.valueOf(dblight.getCount()));
                            Toast.makeText(c, "done", Toast.LENGTH_SHORT).show();
                            bt.dismiss();
                        }
                        catch (NumberFormatException e){
                            Toast.makeText(c, "please enter qty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                bt.setContentView(vv);
                bt.show();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
