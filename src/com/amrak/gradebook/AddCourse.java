package com.amrak.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddCourse extends Activity {

	// database
	TermsDBAdapter termsDB = new TermsDBAdapter(this);
	CoursesDBAdapter coursesDB = new CoursesDBAdapter(this);

	// context
	Context context = this;
	
	// views
	EditText etCourseTitle;
	EditText etCourseCode;
	EditText etCourseNotes;
	EditText etCourseUnits;
	Button bCourseDone;
	
	// variables
	final private String TAG = "AddCourse";
	int refIDGet_Term;
	int idGet_Mode; // mode 0: add, mode 1: edit
	int idEditGet_Item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_course);
		
		Intent iAddCourse = getIntent();
		refIDGet_Term = iAddCourse.getIntExtra("refID_Term", -1);
		idGet_Mode = iAddCourse.getIntExtra("id_Mode", 0); // mode 0: add, mode 1: edit
		idEditGet_Item = iAddCourse.getIntExtra("idEdit_Item", -1);
		
		etCourseTitle = (EditText) findViewById(R.id.etCourseTitle);
		etCourseCode = (EditText) findViewById(R.id.etCourseCode);
		etCourseUnits = (EditText) findViewById(R.id.etCourseUnits);
		etCourseNotes = (EditText) findViewById(R.id.etCourseNotes);
		bCourseDone = (Button) findViewById(R.id.bCourseDone);
		
		termsDB.open();
		Cursor cTerm = termsDB.getTerm(refIDGet_Term);
		cTerm.moveToFirst();
		if (idGet_Mode == 0) {
			setTitle("Add Course to " + cTerm.getString(cTerm.getColumnIndex("termTitle")));
		} else if (idGet_Mode == 1) {
			setTitle("Edit Course in " + cTerm.getString(cTerm.getColumnIndex("termTitle")));
			bCourseDone.setText(R.string.doneEditCourse);
		}
		termsDB.close();
		
		if (idGet_Mode == 1) {
			coursesDB.open();
			Cursor cCourse = coursesDB.getCourse(idEditGet_Item);
			cCourse.moveToFirst();
			etCourseTitle.setText(cCourse.getString(cCourse.getColumnIndex("courseTitle")));
			etCourseCode.setText(cCourse.getString(cCourse.getColumnIndex("courseCode")));
			etCourseUnits.setText(cCourse.getString(cCourse.getColumnIndex("courseUnits")));
			etCourseNotes.setText(cCourse.getString(cCourse.getColumnIndex("notes")));
			coursesDB.close();
		}
		
		bCourseDone.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addCourse(v);
			}
		});
	}

	public void addCourse(View v) {
		
		Log.d(TAG, "Adding or Editing Course.");
		
		if (etCourseTitle.getText().toString().trim().equals("") || etCourseCode.getText().toString().trim().equals("") || etCourseUnits.getText().toString().trim().equals("")) {
			//TODO check if units is numeric.
			//TODO notes are not required values
			
		    new AlertDialog.Builder(this)
		    .setMessage("Make sure all fields are entered.")
		    .setPositiveButton("OK", null)
		    .show();
			
		} else {
			
			String courseTitle = etCourseTitle.getText().toString();
			String courseCode = etCourseCode.getText().toString();
			double courseUnits = 0.0;
			String courseNotes = etCourseNotes.getText().toString();
			
		    try {
		    	courseUnits = Double.parseDouble(etCourseUnits.getText().toString());  
		    } catch(NumberFormatException nfe) {  
		    	Log.d(TAG, nfe.getMessage());
		    }
		    
		    if (idGet_Mode == 0) {
		    	coursesDB.open();
				coursesDB.createCourse(courseTitle, courseCode, courseUnits, courseNotes, refIDGet_Term);
				coursesDB.close();

				Toast toast = Toast.makeText(context, "Course " + courseTitle + " was added successfully.", Toast.LENGTH_SHORT);
				try {
					//center toast
					((TextView)((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
				} catch (ClassCastException cce) {
					Log.d(TAG, cce.getMessage());
				}
				toast.show();
				Log.d(TAG, "Added Course Successfully.");
				finish();
		    } else if (idGet_Mode == 1) {
		    	//editing data in database
		    	coursesDB.open();
				coursesDB.updateCourse(idEditGet_Item, courseTitle, courseCode, courseUnits, courseNotes, refIDGet_Term);
				coursesDB.close();

				Toast toast = Toast.makeText(context, "Course " + courseTitle + " was edited successfully.", Toast.LENGTH_SHORT);
				try {
					//center toast
					((TextView)((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
				} catch (ClassCastException cce) {
					Log.d(TAG, cce.getMessage());
				}
				toast.show();
				Log.d(TAG, "Added Course Successfully.");
				finish();	
			}
			
		}
		
		
	}

}
