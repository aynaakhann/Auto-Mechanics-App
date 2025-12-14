package com.example.map;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myMechViewHolder extends RecyclerView.ViewHolder {

    TextView reqid,cust_name,ratings,distance,time_travel;
    Button viewCustProfile,towRejectCustReq;

    public myMechViewHolder(@NonNull View itemView) {
        super(itemView);

        reqid=itemView.findViewById(R.id.custidforMech);
        cust_name=itemView.findViewById(R.id.custnameforMech);
//        ratings=itemView.findViewById(R.id.ratingsM);
        distance=itemView.findViewById(R.id.custdistanceforMech);
//        time_travel=itemView.findViewById(R.id.time_travel);
        viewCustProfile=itemView.findViewById(R.id.viewCustProfileforMech);
//        towRejectCustReq=itemView.findViewById(R.id.towRejectCustReqforMech);
    }
}
