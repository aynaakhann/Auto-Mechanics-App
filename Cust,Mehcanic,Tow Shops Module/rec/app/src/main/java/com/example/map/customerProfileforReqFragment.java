package com.example.map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class customerProfileforReqFragment extends Fragment {
    Button accept,decline;
    FloatingActionButton callMechanic;
    String custId,currentUser;
    String addressLine,custph,custImg;
    double latt,longitude;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    FirebaseAuth auth;
    SwitchCompat switchCompat;
    public customerProfileforReqFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customer_profilefor_req, container, false);
        accept=view.findViewById(R.id.acceptrequest);
        decline=view.findViewById(R.id.rejectrequest);
        callMechanic=view.findViewById(R.id.callMechanic);
        switchCompat=getActivity().findViewById(R.id.switchbtn);
        TextView rating=view.findViewById(R.id.rating);
        TextView showName=view.findViewById(R.id.custNameForMechReq);
        TextView showDist=view.findViewById(R.id.custDistforMechReq);
        TextView showAddress=view.findViewById(R.id.cust_addressMech);
//        TextView showRatings=view.findViewById(R.id.custRatingsforMechReq);
        TextView showph=view.findViewById(R.id.custPhMech);
        ImageView pimage=view.findViewById(R.id.cust_imgMech);

        currentUser =auth.getInstance().getCurrentUser().getUid();

        Bundle bundle=this.getArguments();
        custId=bundle.getString("custIdforMech");
        String name=bundle.getString("custNameforMech");
        String passedDist=bundle.getString("custDistforMech");
//        String passedRatings=bundle.getString("custRatingsforMech");


        switchCompat.setChecked(true);

        myRef.child("mech_requests").child(currentUser).child(custId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String slatt=snapshot.child("latitude").getValue().toString();
                    String slong=snapshot.child("longitude").getValue().toString();
                    latt=Double.parseDouble(slatt);
                    longitude=Double.parseDouble(slong);
                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(latt, longitude, 1);
//            addresses = geocoder.getFromLocation(32.5101, 74.5431, 1);//khadim Ali Road
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        addressLine = address.getAddressLine(0);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        myRef.child("user").child(custId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    custph = snapshot.child("phone").getValue(String.class);
                    custImg = snapshot.child("image").getValue(String.class);
                    showName.setText(name);
                    showDist.setText(passedDist);
                    showAddress.setText(addressLine);
                    showph.setText(custph);
//                    showRatings.setText(passedRatings);
                    Picasso.get().load(custImg).into(pimage); //to retireve image
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity=(AppCompatActivity)getContext();
                mechAcceptReqFragment mech=new mechAcceptReqFragment();
                Bundle bundle1=new Bundle();
                bundle1.putString("cIdm",custId);
                bundle1.putString("addressLinem",addressLine);
                mech.setArguments(bundle1);

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,mech).addToBackStack(null).commit();
//                replaceFragment(new mechAcceptReqFragment());
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("MechDecline").child(custId).child(currentUser).child("status").setValue("decline");
                myRef.child("mech_requests").child(currentUser).child(custId).removeValue();
                replaceFragment(new MapFragment());
            }
        });
        callMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + custph));
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
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.commit();
    }
}