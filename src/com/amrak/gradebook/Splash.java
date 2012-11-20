package com.amrak.gradebook;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.os.Handler;

/**
 * Splash screen activity
 *
 * 
 */

public class Splash extends Activity {
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.activity_splash);
 
        Handler handler = new Handler();
 
        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
 
            public void run() {
 
                finish(); //closes splash screen
                // start the home screen
 
                Intent intent = new Intent(Splash.this, Courses.class);
                Splash.this.startActivity(intent);
 
            }
 
        }, 2000); // time in milliseconds until the run() method will be called
 
    }
 
}