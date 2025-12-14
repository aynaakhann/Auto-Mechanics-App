package com.example.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;


public class towingcategory extends Fragment {

    LinearLayout towingClick;
    public towingcategory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_towingcategory, container, false);
        towingClick=view.findViewById(R.id.towingCategory);

        towingClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MapsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}