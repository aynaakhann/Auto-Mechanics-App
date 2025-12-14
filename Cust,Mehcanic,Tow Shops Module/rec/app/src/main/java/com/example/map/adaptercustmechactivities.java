package com.example.map;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

public class adaptercustmechactivities extends FirebaseRecyclerAdapter<HelperMech_complains, adaptercustmechactivities.myviewholder>
{
    private myviewholder holder;
    private int position;
    private HelperMech_complains modell;
    FirebaseAuth auth;

    Context context;

    public adaptercustmechactivities(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options, Context context) {
        super(options);
        this.context = context;
    }

    public adaptercustmechactivities(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options, myviewholder holder) {
        super(options);
        this.holder = holder;
    }
    public adaptercustmechactivities(@NonNull FirebaseRecyclerOptions<HelperMech_complains> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull  myviewholder holder, int position, @NonNull HelperMech_complains modell) {
        holder.mname.setText(modell.getMname());
        holder.serviceavailed.setText(modell.getCharges());
        holder.date.setText(modell.getDate());
        holder.time.setText(modell.getTime());
        String st= modell.getStatuscomplain();
        holder.status.setText(st);
        if(modell.getStatuscomplain().equals("sent")){
            holder.complainbtn.setVisibility(View.GONE);
        }


        holder.complainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText comp=new EditText(v.getContext());
                AlertDialog.Builder complainDialog= new AlertDialog.Builder(v.getContext());
                complainDialog.setTitle("Complain");
                complainDialog.setMessage("Enter your Complain");
                complainDialog.setView(comp);
                complainDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String tcomplain = comp.getText().toString();
                        String mcid = modell.getMid();
                        String ccid = modell.getCid();
                        String complainstatus = modell.getStatuscomplain();
                        String mcname = modell.getMname();
                        String mccnic = modell.getMcnic();
                        String mcphone = modell.getMphone();
                        String mcemail = modell.getMemail();
                        String mcpass = modell.getMpass();
                        String mcadd = modell.getAddress();
                        String mcimg = modell.getMimage();

                        Calendar calendar = Calendar.getInstance();
                        LocalDate date = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            date = LocalDate.now();
                        }
                        LocalTime time = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            time = LocalTime.now();
                        }
                        String key =  FirebaseDatabase.getInstance().getReference("Admin").child("complains_mechanic").push().getKey();

                        HelperMech_complains helper = new HelperMech_complains(mcid,ccid,mcname,mccnic,mcphone,mcadd,tcomplain,mcemail,mcpass,mcimg,date.toString(),time.toString(), modell.getCharges());
                        //statuscomplain me complain bheji hai
                        FirebaseDatabase.getInstance().getReference("Admin").child("complains_mechanic").child(key).setValue(helper);
                        DatabaseReference reff=FirebaseDatabase.getInstance().getReference("History_Mech");


                        Query q=reff.orderByChild("cid").equalTo(modell.getCid());
                        q.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for (DataSnapshot childs : snapshot.getChildren()){
                                    if(childs.child("cid").getValue().toString().equals(modell.getCid())){
                                        childs.child("statuscomplain").getRef().setValue("sent");
                                        modell.setStatuscomplain("sent");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                complainDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                complainDialog.create().show();
            }
        });
    }
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_mechactivities_layout,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView mname,serviceavailed,time,date,status;

        Button complainbtn;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
          //  img=(ImageView)itemView.findViewById(R.id.pictureid);
            mname=(TextView)itemView.findViewById(R.id.mechname);
            serviceavailed=(TextView)itemView.findViewById(R.id.aservice);
            time=(TextView)itemView.findViewById(R.id.stime);
            date=(TextView)itemView.findViewById(R.id.sdate);
            status=(TextView)itemView.findViewById(R.id.btnstatus);
            complainbtn=(Button) itemView.findViewById(R.id.compalain_btn);

            String mechname=mname.getText().toString();
            String savailed=serviceavailed.getText().toString();
            String timee=time.getText().toString();
            String datee=date.getText().toString();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mechname=mname.getText().toString();
                    String savailed=serviceavailed.getText().toString();
                    String timee=time.getText().toString();
                    String datee=date.getText().toString();
                }
            });
        }
    }
}








