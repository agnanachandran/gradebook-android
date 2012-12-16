package com.amrak.gradebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddCategory extends Activity {

	// database
	CoursesDBAdapter coursesDB = new CoursesDBAdapter(this);
	CategoriesDBAdapter categoriesDB = new CategoriesDBAdapter(this);

	// context
	Context context = this;

	// views
	EditText etCatName;
	EditText etCatWeight;

	// variables
	final private String TAG = "AddCat";
	int[] refID;
	int selectedRefID;
	int refIDGet_Term;
	int refIDGet_Course;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_category);

		etCatName = (EditText) findViewById(R.id.etCatName);
		etCatWeight = (EditText) findViewById(R.id.etCatWeight);

		Intent iAddCat = getIntent();
		refIDGet_Term = iAddCat.getIntExtra("refID_Term", -1);
		refIDGet_Course = iAddCat.getIntExtra("refID_Course", -1);	

		coursesDB.open();
		Cursor cCourse = coursesDB.getCourse(refIDGet_Course);
		cCourse.moveToFirst();
		setTitle("Add a Category to " + cCourse.getString(cCourse.getColumnIndex("courseTitle")));
		coursesDB.close();
	}
	
	public void addCat(View v) {
		
		Log.d(TAG, "Adding Category.");
		
		if (etCatName.getText().toString().trim().equals("") || etCatWeight.getText().toString().trim().equals("")) {
			
		    new AlertDialog.Builder(this)
		    .setMessage("Please enter data for all the fields.")
		    .setPositiveButton("OK", null)
		    .show();
			
		} else {
			
			String catName = etCatName.getText().toString();
			double catWeight = 0.0;
			
		     try {  
		    	 catWeight = Double.parseDouble(etCatWeight.getText().toString());  
		      } catch(NumberFormatException nfe) {  
		    	  Log.d(TAG, nfe.getMessage());
		      }
		     
			categoriesDB.open();
			categoriesDB.createCategory(catName, catWeight, refIDGet_Term, refIDGet_Course);
			categoriesDB.close();
			
			Toast toast = Toast.makeText(context, "Category " + catName + " added successfully.", Toast.LENGTH_SHORT);
			try {
				//center toast
				((TextView)((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
			} catch (ClassCastException cce) {
				Log.d(TAG, cce.getMessage());
			}
			toast.show();
			
			Log.d(TAG, "Added Category Successfully.");
			finish();
			
		}
		
	}
}
