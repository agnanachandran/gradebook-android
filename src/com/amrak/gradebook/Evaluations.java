package com.amrak.gradebook;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Evaluations extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_evaluations, menu);
        return true;
    }
}
