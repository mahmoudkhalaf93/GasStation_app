package com.example.gasstation;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

class AdapterRecyclreviewView  extends RecyclerView.Adapter<AdapterRecyclreviewView.ViewHolder> {

    private ArrayList<RatingServcReview> listdata;
    private Context mContext;
    public AdapterRecyclreviewView(ArrayList<RatingServcReview> listdata) {
        this.listdata = listdata;
    }
    @Override
    public AdapterRecyclreviewView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    { LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_review, parent, false);
        AdapterRecyclreviewView.ViewHolder viewHolder = new AdapterRecyclreviewView.ViewHolder(listItem);
        mContext=parent.getContext();
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(AdapterRecyclreviewView.ViewHolder holder, int position) {
        final RatingServcReview myListData = listdata.get(position);

        holder.comntname.setText(myListData.getCustName());
        holder.comntdetials.setText(myListData.getComment());
        holder.comntrating.setRating(Float.parseFloat(myListData.getValue()));
    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       // TextView totalRequest;
        TextView comntname,comntdetials;
        RatingBar comntrating;
        //  public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.comntname = itemView.findViewById(R.id.comnt_name);
            this.comntdetials = itemView.findViewById(R.id.comnt_detlis);
            this.comntrating = itemView.findViewById(R.id.comnt_rating_bar);

        }
    }

}
