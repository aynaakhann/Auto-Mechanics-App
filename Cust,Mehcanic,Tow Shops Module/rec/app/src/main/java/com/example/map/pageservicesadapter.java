package com.example.map;

import static com.example.map.actFragment.dataa;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class pageservicesadapter extends FragmentStateAdapter {


    public pageservicesadapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public pageservicesadapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public pageservicesadapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new currentmechanicactivities();

            case 1:
                return new currenttowactivities();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return dataa.length;
    }
}

