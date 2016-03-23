package com.samsunguet.sev_user.mycloud.IntroApp.example.introApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.samsunguet.sev_user.mycloud.LoginActivity;
import com.samsunguet.sev_user.mycloud.R;

/**
 * Created by Lil'Knight on 7/17/2015.
 */
public class Intro extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(SampleSlide.newInstance(R.layout.intro));
        addSlide(SampleSlide.newInstance(R.layout.intro2));
        addSlide(SampleSlide.newInstance(R.layout.intro3));
        addSlide(SampleSlide.newInstance(R.layout.intro4));
    }

    @Override
    public void onSkipPressed() {
        Intent main = new Intent(getBaseContext(),LoginActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void onDonePressed() {
        Intent main = new Intent(getBaseContext(),LoginActivity.class);
        startActivity(main);
        finish();
    }

    public void getStarted(View v){
        Intent main = new Intent(getBaseContext(),LoginActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
}

