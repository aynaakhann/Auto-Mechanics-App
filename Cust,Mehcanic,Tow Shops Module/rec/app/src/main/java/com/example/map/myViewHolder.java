package com.example.map;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class myViewHolder extends RecyclerView.ViewHolder  {
    TextView reqid,cust_name,ratings,distance,time_travel;
    Button viewCustProfile,towRejectCustReq;


    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        reqid=itemView.findViewById(R.id.reqid);
        cust_name=itemView.findViewById(R.id.custname);
//        ratings=itemView.findViewById(R.id.ratingsT);
        distance=itemView.findViewById(R.id.distance);
//        time_travel=itemView.findViewById(R.id.time_travel);
        viewCustProfile=itemView.findViewById(R.id.viewCustProfile);
//        towRejectCustReq=itemView.findViewById(R.id.towRejectCustReq);

    }

}
