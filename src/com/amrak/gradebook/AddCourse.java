package com.amrak.gradebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCourse extends Activity {

	DBAdapter db = new DBAdapter(this); 
	Button bDone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcourse);
	   	bDone = (Button)findViewById(R.id.bDone);
	   	bDone.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				addCourse(v);
				
			}
		});
	}
	public void addCourse(View v)
	{
		Log.d("test", "adding");
	   	//get data from form
	   	EditText nameTxt = (EditText)findViewById(R.id.etCourseName);
	   	EditText dateTxt = (EditText)findViewById(R.id.etTerm);
	   	EditText notesTxt = (EditText)findViewById(R.id.etNotes);
	    
	   	db.open();
	    long id = db.insertRecord(nameTxt.getText().toString(), dateTxt.getText().toString(), "Term", notesTxt.getText().toString());        
	    db.close();
	       
	    nameTxt.setText("");
	    dateTxt.setText("");
	    notesTxt.setText("");
        Toast.makeText(AddCourse.this,"Course Added", Toast.LENGTH_LONG).show();
    	Intent i = new Intent(this, Courses.class);
    	startActivity(i);
	}

}

