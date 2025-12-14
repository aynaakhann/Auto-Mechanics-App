package com.example.map;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class oilFilterChangeFragment extends Fragment {

    Button sendReqbtn;
    String current_state="nothing_happen";
    FirebaseAuth auth;
    FirebaseUser mUser;
    DatabaseReference userreference,reqreference;
    public oilFilterChangeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_oil_filter_change, container, false);
//        TextView txt=view.findViewById(R.id.latlng);
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            Double lat = bundle.getDouble("mech latitude",37);
//            Double lng = bundle.getDouble("mech longitude",42);
//            // Do something with the value
//            txt.setText(String.valueOf(lat+" : "+lng));
//        }
//        final String userID=getIntent().getStringExtra("userkey");
        String userID="1";
        String ratings="4";
        String distance="20 km";
        userreference= FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        reqreference= FirebaseDatabase.getInstance().getReference().child("Requests");
        auth=FirebaseAuth.getInstance();
        mUser=auth.getCurrentUser();


        sendReqbtn=view.findViewById(R.id.sendRequest);
        sendReqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HashMap hashMap=new HashMap();
//                hashMap.put("status","pending");
//                if(current_state.equals("nothing_happen")){
//                    reqreference.child(mUser.getUid()).child(userID).child(mUser.getDisplayName()).child(ratings).child(distance).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
//                        @Override
//                        public void onComplete(@NonNull Task task) {
//                            if(task.isSuccessful()){
                                Toast.makeText(requireContext(), "You have sent Request", Toast.LENGTH_SHORT).show();
                                sendReqbtn.setText("Cancel Request");
                                sendReqbtn.setBackgroundColor(Color.RED);
                                current_state="req_pending";
//                            }
//                            else
//                            {
//                                Toast.makeText(requireContext(), ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            }
                        }
                    });
//                }
//                if(current_state.equals("req_pending")){
//                    reqreference.child(mUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener() {
//                        @Override
//                        public void onComplete(@NonNull Task task) {
//                            if(task.isSuccessful()){
//                                Toast.makeText(requireContext(), "You have cancelled request", Toast.LENGTH_SHORT).show();
//                                sendReqbtn.setText("Send Request");
//                                sendReqbtn.setBackgroundColor(Color.GREEN);
//                                current_state="nothing_happen";
//                            }
//                            else
//                            {
//                                Toast.makeText(requireContext(), ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }

//            }
//        });
        return view;
    }
}