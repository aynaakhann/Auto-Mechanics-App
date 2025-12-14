package com.example.map;

import static com.example.map.registerall.data;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class pageadapter extends FragmentStateAdapter {


    public pageadapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);}
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new register_customer(data[position]);
                case 1:
                    return new register_mechanic(data[position]);
                case 2:
                    return new register_towing(data[position]);
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return data.length;
        }
    }


