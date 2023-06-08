package com.example.stockify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    TextView tv;
    LottieAnimationView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tv=findViewById(R.id.splashText);
        lv=findViewById(R.id.animationView);
        tv.animate().translationY(-1000).setDuration(2000).setStartDelay(4000);
        lv.animate().translationY(2000).setDuration(2500).setStartDelay(4000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this,Login.class));


            }
        },5650);


    }
}