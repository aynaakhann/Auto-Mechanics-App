package com.example.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adaptermechservicehistory extends FirebaseRecyclerAdapter<HelperMech_complains, adaptermechservicehistory.myviewholder>
{
    private myviewholder holder;
    private int position;
    private HelperMech_complains modell;
    FirebaseAuth auth;
    String cname;
    Context context;

    public adaptermechservicehistory(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options, Context context) {
        super(options);
        this.context = context;
    }

    public adaptermechservicehistory(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options, myviewholder holder) {
        super(options);
        this.holder = holder;
    }
    public adaptermechservicehistory(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperMech_complains modell) {
        String cid= modell.getCid();
        DatabaseReference re= FirebaseDatabase.getInstance().getReference("user");
        Query cu = re.orderByChild("uid").equalTo(cid);
        cu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                cname=snapshot.child(cid).child("username").getValue().toString();
                holder.custname.setText(cname);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.date.setText(modell.getDate());
        holder.time.setText(modell.getTime());
        holder.servicename.setText("Mechanic Service");
        holder.scharges.setText(modell.getCharges());

    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.towing_history_layout,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView custname,date,time,servicename,scharges;
        TextView uid,email,password,imgpath,rimgpath;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            custname=(TextView)itemView.findViewById(R.id.cname);
            date=(TextView)itemView.findViewById(R.id.sdate);
            time=(TextView)itemView.findViewById(R.id.stime);
            servicename=(TextView)itemView.findViewById(R.id.servicename);
            scharges=(TextView)itemView.findViewById(R.id.scharges);


            String cname=custname.getText().toString();
            String cdate=date.getText().toString();
            String ctime=time.getText().toString();
            String servname=servicename.getText().toString();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String cname=custname.getText().toString();
                    String cdate=date.getText().toString();
                    String ctime=time.getText().toString();
                    String servname=servicename.getText().toString();
                }
            });
        }
    }
}








