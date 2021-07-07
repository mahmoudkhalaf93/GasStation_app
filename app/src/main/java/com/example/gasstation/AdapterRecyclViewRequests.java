package com.example.gasstation;

import android.content.Context;
import android.content.Intent;
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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gasstation.ui.requests.RequestsFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterRecyclViewRequests extends RecyclerView.Adapter<AdapterRecyclViewRequests.ViewHolder>
       {
    //private MyListData[] listdata;
    private ArrayList<Requests> listdata;
    private Context mContext;
    // RecyclerView recyclerView;
    public AdapterRecyclViewRequests(ArrayList<Requests> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    { LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_requests, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        mContext=parent.getContext();
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Requests myListData = listdata.get(position);

        holder.totalRequest.setText("Total : "+myListData.getTotalRequest()+" LE");
        holder.reqNumber.setText("ReqNo : "+myListData.getRequestNo());
        holder.dateRequest.setText("Date : "+myListData.getRequestDate());
        Picasso.get().load(myListData.getLogo()).error(R.drawable.logfitt).
                resize(300,300).into(holder.imglogo);
        holder.price.setText("Price : "+myListData.getPrice()+" LE");
        holder.total.setText("Total : "+myListData.getTotal()+" LE");
        holder.qty.setText("Qty : "+myListData.getQTY());
        holder.status.setText("Status : "+myListData.getStatus());
        //check if delivered
        if(myListData.getStatus().equals("Delivered")){
            holder.status.setTextColor(Color.YELLOW);
            if(myListData.getRatedorNot().equals("0")){
                holder.reviewbutton.setBackgroundResource(R.drawable.solid);
                holder.reviewbutton.setText("Review Service");
                //on click listener for review button
                holder.reviewbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //show sheet dialog when click review
                        LayoutInflater inflater=LayoutInflater.from(view.getContext());
                        View vv=inflater.inflate(R.layout.review_service,null);
                        //set items view and data in view sheet dialog
                        ShapeableImageView reviewImage=vv.findViewById(R.id.service_request_image_review);
                        TextView namebtShet=vv.findViewById(R.id.name_req_review);
                        TextView deltsbtShet=vv.findViewById(R.id.delt_req_review);
                        RatingBar rtingbtShet=vv.findViewById(R.id.review_servic_request);
                        EditText comntbtShet=vv.findViewById(R.id.review_comment_request);
                        Button sendreview=vv.findViewById(R.id.send_review);
                        //set data in items in view sheet dialog
                        Picasso.get().load(myListData.getLogo()).error(R.drawable.logfitt).
                                resize(300,300).into(reviewImage);
                        namebtShet.setText("Name : "+myListData.getName());
                        deltsbtShet.setText("Details : "+myListData.getDetails());
                        //create sheet dialog
                        final BottomSheetDialog bt=new BottomSheetDialog(view.getContext(),R.style.AppBottomSheetDialogTheme);
                        //change enter buttom in keyboard to send action buttom in edittext comment
                        comntbtShet.setImeOptions(EditorInfo.IME_ACTION_SEND);
                        comntbtShet.setRawInputType(InputType.TYPE_CLASS_TEXT);
                        //set action buttom
                        comntbtShet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_SEND) {
//(View view,Requests myListData,ViewHolder holder,EditText comntbtShet,RatingBar rtingbtShet,BottomSheetDialog bt)
                                    sendreview(view,myListData,holder,comntbtShet,rtingbtShet,bt);
//                                    Database db=new Database();
//                                    Connection c=db.connect(view.getContext());
//                                    String insertQuerry;
//                                    if(comntbtShet.getText().toString().matches("^\\s*$")){
//                                        insertQuerry=db.Rundml("insert into RatingServcReview(Date,Value,RequestoNo,ServiceNo) values('"+ Calendar.getInstance().getTime()+"','"+rtingbtShet.getRating()+"','"+myListData.getRequestNo()+"','"+myListData.getServiceNo()+"');");
//                                      db.Rundml("update Sales SET RatedorNot=1 where RequestoNo='"+myListData.getRequestNo()+"' and ServiceNo='"+myListData.getServiceNo()+"';");
//
//                                        if(insertQuerry.equals("ok"))
//                                      {holder.reviewbutton.setBackgroundResource(R.drawable.editotesxt);
//                                          holder.reviewbutton.setText("Reviewed");
//                                          myListData.setRatedorNot("1");
//                                          Toast.makeText(view.getContext(), "Review Sent", Toast.LENGTH_SHORT).show();
//                                      }
//                                         else
//                                    {Toast.makeText(view.getContext(), ""+insertQuerry, Toast.LENGTH_SHORT).show();}}
//                                    else
//                                    {
//                                        insertQuerry=db.Rundml("insert into RatingServcReview values('"+ Calendar.getInstance().getTime()+"','"+rtingbtShet.getRating()+"',N'"+comntbtShet.getText().toString()+"','"+myListData.getRequestNo()+"','"+myListData.getServiceNo()+"');");
//                                        db.Rundml("update Sales SET RatedorNot=1 where RequestoNo='"+myListData.getRequestNo()+"' and ServiceNo='"+myListData.getServiceNo()+"';");
//                                        holder.reviewbutton.setBackgroundResource(R.drawable.editotesxt);
//                                        holder.reviewbutton.setText("Reviewed");
//                                        myListData.setRatedorNot("1");
//                                        Toast.makeText(view.getContext(), "Review Sent", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                    bt.dismiss();
                                    return true;
                                }
                                return false;
                            }
                        });
                        sendreview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendreview(view,myListData,holder,comntbtShet,rtingbtShet,bt);
                            }
                        });
                        bt.setContentView(vv);
                        bt.show();
                    }


                });

            }
            else {
                holder.reviewbutton.setBackgroundResource(R.drawable.editotesxt);
                holder.reviewbutton.setText("Reviewed");
                holder.reviewbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
//                viewHolder.reviewbutton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        StaticsClass.servcNoToRewview=data.getServiceNo();
//                        Toast.makeText(c, "rate or not : "+ data.getRatedorNot(), Toast.LENGTH_SHORT).show();
//                        //startactiviy to all request item
//                    }
//                });
            }

        }
        else {holder.status.setTextColor(Color.WHITE);
            holder.reviewbutton.setText("Review Service");
            holder.reviewbutton.setBackgroundResource(R.drawable.editotesxt);
            holder.reviewbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return listdata.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView totalRequest;
        TextView  reqNumber;
        TextView  dateRequest;
        ShapeableImageView imglogo;
        TextView price;
        TextView  total;
        TextView  qty;
        TextView  status;
        Button reviewbutton;
      //  public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.totalRequest = itemView.findViewById(R.id.request_total_price);
            this.reqNumber = itemView.findViewById(R.id.req_number);
            this.dateRequest = itemView.findViewById(R.id.request_date);
            this.imglogo = itemView.findViewById(R.id.service_request_image);
            this.price = itemView.findViewById(R.id.srvc_price);
            this.total = itemView.findViewById(R.id.service_total);
            this.qty = itemView.findViewById(R.id.servc_qty);
            this.status = itemView.findViewById(R.id.status_request);
            this.reviewbutton = itemView.findViewById(R.id.review_request);

          //  relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
public void sendreview(View view,Requests myListData,ViewHolder holder,EditText comntbtShet,RatingBar rtingbtShet,BottomSheetDialog bt)
{
//    TextView namebtShet=vv.findViewById(R.id.name_req_review);
//    TextView deltsbtShet=vv.findViewById(R.id.delt_req_review);
//    RatingBar rtingbtShet=vv.findViewById(R.id.review_servic_request);
//    EditText comntbtShet=vv.findViewById(R.id.review_comment_request);
    Database db=new Database();
    Connection c=db.connect(view.getContext());
    String insertQuerry;
    if(comntbtShet.getText().toString().matches("^\\s*$")){

        insertQuerry=db.Rundml("insert into Rating(Date,Value,RequestoNo,ServiceNo) values('"+ Calendar.getInstance().getTime()+"','"+rtingbtShet.getRating()+"','"+myListData.getRequestNo()+"','"+myListData.getServiceNo()+"');");

        db.Rundml("update Sales SET RatedorNot=1 where RequestoNo='"+myListData.getRequestNo()+"' and ServiceNo='"+myListData.getServiceNo()+"';");

        if(insertQuerry.equals("ok"))
        { holder.reviewbutton.setBackgroundResource(R.drawable.editotesxt);
            holder.reviewbutton.setText("Reviewed");
            holder.reviewbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            myListData.setRatedorNot("1");
            Toast.makeText(view.getContext(), "Review Sent", Toast.LENGTH_SHORT).show();
        }
        else
        {Toast.makeText(view.getContext(), ""+insertQuerry, Toast.LENGTH_SHORT).show();}}
    else
    {
        insertQuerry=db.Rundml("insert into Rating values('"+ Calendar.getInstance().getTime()+"','"+rtingbtShet.getRating()+"',N'"+comntbtShet.getText().toString()+"','"+myListData.getRequestNo()+"','"+myListData.getServiceNo()+"');");
        db.Rundml("update Sales SET RatedorNot=1 where RequestoNo='"+myListData.getRequestNo()+"' and ServiceNo='"+myListData.getServiceNo()+"';");

        if(insertQuerry.equals("ok")) {
            holder.reviewbutton.setBackgroundResource(R.drawable.editotesxt);
            holder.reviewbutton.setText("Reviewed");
            holder.reviewbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            myListData.setRatedorNot("1");
            Toast.makeText(view.getContext(), "Review Sent", Toast.LENGTH_SHORT).show();
        }
    }

    bt.dismiss();
}
}
