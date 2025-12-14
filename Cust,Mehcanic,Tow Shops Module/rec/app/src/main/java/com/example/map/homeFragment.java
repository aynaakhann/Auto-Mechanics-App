package com.example.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;


public class homeFragment extends Fragment {

    ImageSlider imageslider,is;
    public homeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container,false);
        imageslider=view.findViewById(R.id.image_slider);
        ArrayList<SlideModel> imagelist= new ArrayList<>();
        imagelist.add(new SlideModel(R.drawable.s4, null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.slider5,null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.s6, null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.slider3, null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.s7, null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.s1, null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.s3, null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.slider6, null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.slider2,null, ScaleTypes.FIT));
        imagelist.add(new SlideModel(R.drawable.slider4,null, ScaleTypes.FIT));

        imageslider.setImageList(imagelist);
        //return inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
}