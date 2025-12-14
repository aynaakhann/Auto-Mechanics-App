package com.example.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class towBillFragment extends Fragment {

    Button billpaid;
    TextView cust_name,total2tow,timetow,distanceinMetersTow,addressTow,servicesTow,chargesofServicesTow,chargesperKMTow,infoLineTow;
    String cID,distance,availedAddress,availedServices;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://automechanics-fd582-default-rtdb.firebaseio.com/");
    double charges;
    String time;
    double distanceParse;
    FirebaseAuth auth;
    String currentLoginUser;
    private static final double CHARGE_RATE_PER_KM = 20.0; // 20 rs per kilometer
    private static final double METERS_IN_ONE_KM = 1000.0; // 1000 meters in 1 kilometer

    public towBillFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_tow_bill, container, false);
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();

//        total1tow=view.findViewById(R.id.totalChargesatBilltow);
        cust_name=view.findViewById(R.id.cust_nameT);
        total2tow=view.findViewById(R.id.totalBill2Tow);
        servicesTow=view.findViewById(R.id.servicesAvailedAtBillTow);
        chargesofServicesTow=view.findViewById(R.id.chargesofServicesAtBillTow);
        distanceinMetersTow=view.findViewById(R.id.distanceAtBillTow);
        chargesperKMTow=view.findViewById(R.id.perKmAtBillTow);
        timetow=view.findViewById(R.id.timeinMinAtBillTow);
        addressTow=view.findViewById(R.id.addressofAvailedServiceAtTow);
        infoLineTow=view.findViewById(R.id.infoLinetow);
        billpaid=view.findViewById(R.id.TowbillPaid);

        Bundle bundle=this.getArguments();
        cID =bundle.getString("cID");
        distance =bundle.getString("distance1");
        availedAddress =bundle.getString("availedAddress");
        availedServices =bundle.getString("availedServices");
        charges=bundle.getDouble("charges",0);
        time=bundle.getString("timeBill");

        myRef.child("user").child(cID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String unamee=snapshot.child("username").getValue(String.class);
                    cust_name.setText(unamee);
                    distanceinMetersTow.setText(distance+" meters");
                    timetow.setText(time+"");
                    addressTow.setText(availedAddress);
                    chargesofServicesTow.setText(charges+" Rs");

                    servicesTow.setText(availedServices);

                    distanceParse=Double.parseDouble(distance);
                    double estimatedDistanceKm = distanceParse / METERS_IN_ONE_KM;
                    double estimatedCharges = (estimatedDistanceKm * CHARGE_RATE_PER_KM)*2;

                    // Add service charges to the estimated charges
                    estimatedCharges += charges;
                    // Cut off the decimal part and keep only the whole number charges
                    int wholeNumberCharges = (int) Math.floor(estimatedCharges);


                    myRef.child("History").child(cID).child(currentLoginUser).child("Total Charges").setValue(wholeNumberCharges);

                    myRef.child("History_Tow").child(cID).child(currentLoginUser).child("Total Charges").setValue(wholeNumberCharges);

//        long totalcharge= (long) ((distanceParse*20)+charges);
//        total1tow.setText(wholeNumberCharges+" Rs");
                    total2tow.setText(wholeNumberCharges+" Rs");
                    infoLineTow.setText("You travelled " +distance+" meters in "+time+" min");

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        billpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                replaceFragment(new towFeedbackForCustFragment());
                AppCompatActivity activity=(AppCompatActivity)getContext();
                towFeedbackForCustFragment towfeedback=new towFeedbackForCustFragment();

                Bundle bundle1=new Bundle();
                bundle1.putString("cID",cID);
                towfeedback.setArguments(bundle1);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.containertow,towfeedback).addToBackStack(null).commit();
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
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
                builder.setTitle("Exit Alert")
                        .setIcon(R.drawable.ic_alert_error_msg)
                        .setMessage("You cannot exit!")  ;
                builder.setNegativeButton("OK", null);
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}