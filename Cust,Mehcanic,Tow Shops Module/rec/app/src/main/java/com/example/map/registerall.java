package com.example.map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class registerall extends AppCompatActivity {
    public static final String[] data={"Customer","Mechanic","Towing"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerall);
        ViewPager2 viewPager=findViewById(R.id.pageview);   //viewpager
        TabLayout tabLayout=findViewById(R.id.tablayout);   //tablayout
        pageadapter adapter=new pageadapter(this);  //pageadapter java class
        viewPager.setAdapter(adapter);                             //setadapter
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(data[position]);
            }
        }).attach();
    }
}
