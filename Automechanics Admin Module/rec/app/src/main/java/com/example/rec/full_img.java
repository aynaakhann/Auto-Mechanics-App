package com.example.rec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class full_img extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_img);
        int imageResId = getIntent().getIntExtra("image_res_id",0);

        // Find the ImageView in the layout and set its image resource
        imageView = findViewById(R.id.fullimg);
        imageView.setImageResource(imageResId);
    }
}