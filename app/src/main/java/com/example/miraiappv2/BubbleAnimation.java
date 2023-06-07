package com.example.miraiappv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.example.miraiappv2.R;

import java.util.Random;

public class BubbleAnimation extends FrameLayout {
    public BubbleAnimation(Context context) {
        super(context);
        //Runs the init method
        init();
    }

    private void init() {
        //Grab the bubble_animation_layout.xml file into the view in our game page xml file
        LayoutInflater.from(getContext()).inflate(R.layout.bubble_animation_layout, this, true);
    }

    //Starts the bubble animation
    public void startAnimation() {
        //Gets animation settings from xml file in res/anim
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bubble_rise);
        //Run the startAnimation method and give it the parameters set in animation
        startAnimation(animation);
    }
}
