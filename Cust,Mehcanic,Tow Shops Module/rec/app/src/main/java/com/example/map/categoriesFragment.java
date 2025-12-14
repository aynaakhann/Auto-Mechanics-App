package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class categoriesFragment extends Fragment {

    ImageView oilFilter,newTire,BatteryReplace,ReplaceFilter,BrakeWork,TestingSystem,EngineTuneup,WheelBalance;
    public categoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_categories, container, false);
        oilFilter=view.findViewById(R.id.oilfilterchnage);
        newTire=view.findViewById(R.id.newtire);
        BatteryReplace=view.findViewById(R.id.batteryRep);
        ReplaceFilter=view.findViewById(R.id.replacefilter);
        BrakeWork=view.findViewById(R.id.brakefail);
        TestingSystem=view.findViewById(R.id.systemtesting);
        EngineTuneup=view.findViewById(R.id.etuneup);
        WheelBalance=view.findViewById(R.id.wheelbalance);

        oilFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MechMapsActivity.class);
                intent.putExtra("categoryName","Oil/filter changed");
                startActivity(intent);
            }
        });
        newTire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MechMapsActivity.class);
                intent.putExtra("categoryName","New tires");
                startActivity(intent);
            }
        });
        BrakeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MechMapsActivity.class);
                intent.putExtra("categoryName","Brake Work");
                startActivity(intent);

            }
        });
        BatteryReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MechMapsActivity.class);
                intent.putExtra("categoryName","Battery replacement");
                startActivity(intent);
            }
        });
        ReplaceFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MechMapsActivity.class);
                intent.putExtra("categoryName","Replace Air-filter");
                startActivity(intent);
            }
        });
        TestingSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MechMapsActivity.class);
                intent.putExtra("categoryName","Testing electrical & mechanical system");
                startActivity(intent);
            }
        });
        EngineTuneup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MechMapsActivity.class);
                intent.putExtra("categoryName","Engine tune-up");
                startActivity(intent);
            }
        });
        WheelBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),MechMapsActivity.class);
                intent.putExtra("categoryName","Wheels balance & alignment");
                startActivity(intent);
            }
        });
        return view;
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}