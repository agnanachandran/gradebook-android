package com.amrak.gradebook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

	public class AddCourse extends Activity {
	    EditText etAddCourse;
	    Button bDone;
		
		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_addcourse);
	        etAddCourse = (EditText) findViewById(R.id.etCourseName);
	        bDone = (Button) findViewById(R.id.bDone);
	        
	    }
			
	}