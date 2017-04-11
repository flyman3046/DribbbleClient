package com.fly.flyman3046.dribbbleclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by FZ on 4/9/2017.
 */
public class ShotActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shot_activity);

        ImageView shotImageView = (ImageView) findViewById(R.id.shot_image);

        String imageUrl = getIntent().getStringExtra(ShotRecylerViewAdapter.IMAGE_URL);
        Integer shotId = getIntent().getIntExtra(ShotRecylerViewAdapter.SHOT_ID, 0);

        if (imageUrl.endsWith(".gif")) {
            Glide.with(this)
                    .load(imageUrl)
                    .asGif()
                    .into(shotImageView);
        }
        else {
            Glide.with(this)
                    .load(imageUrl)
                    .into(shotImageView);
        }



    }








}
