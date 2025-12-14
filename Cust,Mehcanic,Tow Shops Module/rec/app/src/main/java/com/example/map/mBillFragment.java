package com.example.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class mBillFragment extends Fragment {

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

    public mBillFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_m_bill, container, false);
        currentLoginUser = auth.getInstance().getCurrentUser().getUid();

//        total1tow=view.findViewById(R.id.totalChargesatBillMech);
        cust_name=view.findViewById(R.id.cust_nameM);
        total2tow=view.findViewById(R.id.totalBill2Mech);
        servicesTow=view.findViewById(R.id.servicesAvailedAtBillMech);
        chargesofServicesTow=view.findViewById(R.id.chargesofServicesAtBillMech);
        distanceinMetersTow=view.findViewById(R.id.distanceAtBillMech);
        chargesperKMTow=view.findViewById(R.id.perKmAtBillMech);
        timetow=view.findViewById(R.id.timeinMinAtBillMech);
        addressTow=view.findViewById(R.id.addressofAvailedServiceAtMech);
        infoLineTow=view.findViewById(R.id.infoLineMech);

        billpaid=view.findViewById(R.id.MechbillPaid);

        Bundle bundle=this.getArguments();
        cID =bundle.getString("cIDM");
        distance =bundle.getString("distanceM");
        availedAddress =bundle.getString("availedAddressM");
        availedServices =bundle.getString("availedServicesM");
        charges=bundle.getDouble("chargesM",0);
        time=bundle.getString("timeBillM");

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
                    double estimatedCharges = estimatedDistanceKm * CHARGE_RATE_PER_KM;

                    // Add service charges to the estimated charges
                    estimatedCharges += charges;
                    // Cut off the decimal part and keep only the whole number charges
                    int wholeNumberCharges = (int) Math.floor(estimatedCharges);


                    myRef.child("HistoryM").child(cID).child(currentLoginUser).child("Total Charges").setValue(wholeNumberCharges);

                    myRef.child("History_Mech").child(cID).child(currentLoginUser).child("Total Charges").setValue(wholeNumberCharges);

//        long totalcharge= (long) ((distanceParse*20)+charges);
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
                AppCompatActivity activity=(AppCompatActivity)getContext();
                MechFeedbackforCustFragment mechfeedback=new MechFeedbackforCustFragment();

                Bundle bundle1=new Bundle();
                bundle1.putString("cIdM",cID);
                mechfeedback.setArguments(bundle1);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container,mechfeedback).addToBackStack(null).commit();

//                replaceFragment(new MechFeedbackforCustFragment());
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
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Exit Alert")
                        .setIcon(R.drawable.ic_alert_error_msg)
                        .setMessage("You cannot exit!")  ;
                builder.setNegativeButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}