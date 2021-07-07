package com.example.gasstation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.gasstation.ui.home.HomeFragment.ddgt;

public class AdapterGasStation extends ArrayAdapter<GasStations> {
//adapter
    Context c;
    ArrayList<GasStations> ass;
public static String gasNo;
    public AdapterGasStation(Context context, ArrayList<GasStations> cont) {
        super(context, R.layout.gasstationlayout,cont);
        c=context;
        ass=cont;
    }

    class Holder
    {
        //ImageView imggas;
        ShapeableImageView imggas;
        TextView  txtnamegas;
        TextView  txtareagas;
        TextView  txtphonegas;
        RatingBar ratingBar;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        GasStations data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.gasstationlayout, parent, false);

            viewHolder.imggas=convertView.findViewById(R.id.imggassation);
            viewHolder.txtnamegas=convertView.findViewById(R.id.txtnamegasstation);
            viewHolder.txtareagas=convertView.findViewById(R.id.txtareagasstation);
            viewHolder.txtphonegas=convertView.findViewById(R.id.txtphonegasstations);
            viewHolder.ratingBar=convertView.findViewById(R.id.servprofile_servcratingbar);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Holder) convertView.getTag();
        }
       Picasso.get().load(data.getLogo()).error(R.drawable.logfitt).
               resize(300,300).into(viewHolder.imggas);
        viewHolder.txtnamegas.setText(data.getName());
        viewHolder.txtareagas.setText(data.getArea());
       viewHolder.txtphonegas.setText(data.getPhone());
        double rate=0,count=0;
        Iterator<GetRateGasStation> iter = ddgt.iterator();
        while(iter.hasNext()){
            GetRateGasStation xx=iter.next();
            if(xx.getGasStationNo().equals(data.getGasStationNo())){
                rate=rate+Double.parseDouble(xx.getValue());
                count++;
            }
        }
        if(count!=0)
        rate=rate/count;
        viewHolder.ratingBar.setRating((float) rate);


//       viewHolder.imggas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gasNo = data.getGasStationNo();
//                Intent cc=new Intent(c,ServicesActivity.class);
//                c.startActivity(cc);
//            }
//        });

        // Return the completed view to render on screen
        return convertView;
    }


}
