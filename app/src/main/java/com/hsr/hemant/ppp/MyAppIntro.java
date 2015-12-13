package com.hsr.hemant.ppp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class MyAppIntro extends AppIntro {
    @Override
    public void init(Bundle bundle) {
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        askForPermissions(new String[]{Manifest.permission.VIBRATE}, 1);

        addSlide(AppIntroFragment.newInstance("Nearby Photo Grid", "Touch Up to enlarge image of interest, click floating action button to get next set of image, Long press to download image from grid", R.drawable.firsst, Color.parseColor("#4dffb8")));
        addSlide(AppIntroFragment.newInstance("Photos On Map", "Wait for the Snackbar to see the images in the grid on the map, which can be reached from sliding menu", R.drawable.ready, Color.parseColor("#4dffb8")));
        addSlide(AppIntroFragment.newInstance("Route to the picture", "Click on the bottom two icons to see the route to the photo marker you clicked", R.drawable.map, Color.parseColor("#4dffb8")));
        // OPTIONAL METHODS
        // Override bar/separator color
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));
        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        // Hide Skip/Done button
        showSkipButton(true);


        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permesssion in Manifest
        setVibrate(true);
        setVibrateIntensity(20);
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, gSignIn.class);
        startActivity(intent);
    }
}
