package com.example.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class actFragment extends Fragment {
    public static actFragment newInstance() {
        return new actFragment();
    }
    public static final String[] dataa={"Mechanic Services","Towing Services"};
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_act, container, false);
        ViewPager2 viewPager=view.findViewById(R.id.pageview);   //viewpager
        TabLayout tabLayout=view.findViewById(R.id.tablayout);   //tablelayout
        pageservicesadapter adapter = new pageservicesadapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
        // Connect the TabLayout to the ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(dataa[position]);
            }
        }).attach();

        return view;
    }

    // Other Fragment methods...
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

}