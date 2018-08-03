package com.attlds.chinmay.ant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1800;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView img = (ImageView)findViewById(R.id.splashscreen);
        AnimationSet as = new AnimationSet(true);
        Animation anime = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide);
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        shake.setStartOffset(1000);
        as.addAnimation(anime);
        //as.addAnimation(shake);
        //img.startAnimation(anime);
        img.startAnimation(as);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, Home.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
