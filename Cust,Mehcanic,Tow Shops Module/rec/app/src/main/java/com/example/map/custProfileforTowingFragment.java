package com.example.map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class custProfileforTowingFragment extends Fragment {
    String current_tow;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    Button acceptreq,rejectreq;
    FloatingActionButton callTowing;
    String custId,custphh,custImgg;
    String addressLine;
    FirebaseAuth auth;
    SwitchCompat switchCompat;
    public custProfileforTowingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cust_profilefor_towing, container, false);
        acceptreq=view.findViewById(R.id.acceptrequesttowing);
        rejectreq=view.findViewById(R.id.rejectrequesttowing);
        callTowing=view.findViewById(R.id.callCustByTowing);
        switchCompat=getActivity().findViewById(R.id.switchbtn);

//        TextView showId=view.findViewById(R.id.custId);
        TextView showName=view.findViewById(R.id.custNameforReq);
        TextView rating=view.findViewById(R.id.rating);
        TextView showDist=view.findViewById(R.id.cust_dist);
        TextView showAddress=view.findViewById(R.id.cust_address);
        TextView showph=view.findViewById(R.id.custPh);
//        TextView showRatings=view.findViewById(R.id.custRatings);
        ImageView pimage=view.findViewById(R.id.cust_img);

        current_tow=auth.getInstance().getCurrentUser().getUid();
        Bundle bundle=this.getArguments();
         custId=bundle.getString("custId");
        String name=bundle.getString("custName");
        String passedDist=bundle.getString("custDist");
//        String passedRatings=bundle.getString("custRatings");

        switchCompat.setChecked(true);


        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(32.5101, 74.5431, 1);//model town
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
           addressLine = address.getAddressLine(0);
            String city = address.getLocality();
            String state = address.getAdminArea();
            String country = address.getCountryName();
            String postalCode = address.getPostalCode();
            String knownName = address.getFeatureName();
//            Toast.makeText(requireContext(), ""+addressLine, Toast.LENGTH_SHORT).show();
        }

        myRef.child("user").child(custId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if(snapshot.exists()) {
                    custphh = snapshot.child("phone").getValue(String.class);
                    custImgg = snapshot.child("image").getValue(String.class);
                 showName.setText(name);
                 showDist.setText(passedDist);
                 showAddress.setText(addressLine);
                 showph.setText(custphh);
//                 showRatings.setText(passedRatings);
                 Picasso.get().load(custImgg).into(pimage); //to retireve image

             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        acceptreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity=(AppCompatActivity)getContext();
                towAcceptReqFragment tow=new towAcceptReqFragment();
                Bundle bundle1=new Bundle();
                bundle1.putString("cIdT",custId);
                bundle1.putString("addressLineT",addressLine);
                tow.setArguments(bundle1);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containertow,tow).addToBackStack(null).commit();
            //  requireContext().finish();

            }
        });

        rejectreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("TowingDecline").child(custId).child(current_tow).child("status").setValue("decline");
                myRef.child("requests").child(current_tow).child(custId).removeValue();
                replaceFragment(new tmapFragment());
            }
        });

        callTowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + custphh));
                startActivity(callIntent);
            }
        });
        Query q=FirebaseDatabase.getInstance().getReference("Ratings");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("final_customer_rating").child(custId).exists()) {
                    String ratings=snapshot.child("final_customer_rating").child(custId).child("rating").getValue().toString();
                    if(TextUtils.isEmpty(ratings)){
                        rating.setText("N/A");
                    }else{
                        rating.setText(ratings);
                    }
                }
                else{
                    rating.setText("N/A");
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return view;
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentmanager =requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentmanager.beginTransaction();
        fragmentTransaction.replace(R.id.containertow,fragment);
        fragmentTransaction.commit();
    }
}