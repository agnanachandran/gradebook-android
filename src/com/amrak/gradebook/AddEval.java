package com.amrak.gradebook;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddEval extends Activity {

	// database
	CoursesDBAdapter coursesDB = new CoursesDBAdapter(this);
	CategoriesDBAdapter categoriesDB = new CategoriesDBAdapter(this);
	EvaluationsDBAdapter evaluationsDB = new EvaluationsDBAdapter(this);

	// context
	Context context = this;

	// views
	EditText etEvalName;
	EditText etEvalMark;
	EditText etEvalOutOf;
	Spinner sEvalCat;
	Button bEvalPickDate;
	EditText etWeight;
	EditText etEvalNotes;
	Button bEvalDone;

	// date and time
	private int mYear;
	private int mMonth;
	private int mDay;
	static final String STATE_YEAR = "selectedYear";
	static final String STATE_MONTH = "selectedMonth";
	static final String STATE_DAY = "selectedDay";
	static final int DATE_DIALOG_ID = 0;
	NumberFormat twoDigit;

	// variables
	final private String TAG = "AddEval";
	int[] refID;
	int selectedRefID;
	String selectedDate;
	int refIDGet_Term;
	int refIDGet_Course;
	int idGet_Mode; // mode 0: add, mode 1: edit
	int idEditGet_Item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_eval);

		etEvalName = (EditText) findViewById(R.id.etEvalName);
		etEvalMark = (EditText) findViewById(R.id.etEvalMark);
		etEvalOutOf = (EditText) findViewById(R.id.etEvalOutOf);
		sEvalCat = (Spinner) findViewById(R.id.sEvalCat);
		bEvalPickDate = (Button) findViewById(R.id.bPickEvalDate);
		etWeight = (EditText) findViewById(R.id.etEvalWeight);
		etEvalNotes = (EditText) findViewById(R.id.etEvalNotes);
		bEvalDone = (Button) findViewById(R.id.bEvalDone);

		Intent iAddEval = getIntent();
		refIDGet_Term = iAddEval.getIntExtra("refID_Term", -1);
		refIDGet_Course = iAddEval.getIntExtra("refID_Course", -1);
		idGet_Mode = iAddEval.getIntExtra("id_Mode", 0); // mode 0: add, mode 1: edit
		idEditGet_Item = iAddEval.getIntExtra("idEdit_Item", -1);

		coursesDB.open();
		Cursor cCourse = coursesDB.getCourse(refIDGet_Course);
		cCourse.moveToFirst();
		if (idGet_Mode == 0) {
			setTitle("Add Evaluation to " + cCourse.getString(cCourse.getColumnIndex("courseTitle")));
		} else if (idGet_Mode == 1) {
			setTitle("Edit Evaluation in " + cCourse.getString(cCourse.getColumnIndex("courseTitle")));
			bEvalDone.setText(R.string.doneEditEval);
		}
		coursesDB.close();
		
		bEvalPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		evaluationsDB.open();
		// set the date
		Cursor cEvaluation = evaluationsDB.getEvaluation(idEditGet_Item);
		cEvaluation.moveToFirst();
	    if (savedInstanceState != null) {
	    	// recreate date from state
	        mYear = savedInstanceState.getInt(STATE_YEAR);
	        mMonth = savedInstanceState.getInt(STATE_MONTH);
	        mDay = savedInstanceState.getInt(STATE_DAY);
	    } else if (idGet_Mode == 0) {
	    	// set date to today
			final Calendar cal = Calendar.getInstance();
			mYear = cal.get(Calendar.YEAR);
			mMonth = cal.get(Calendar.MONTH);
			mDay = cal.get(Calendar.DAY_OF_MONTH);
	    } else if (idGet_Mode == 1) {
	    	//set date from database
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        Date date =  new Date();
	        try {
	          date = format.parse(cEvaluation.getString(cEvaluation.getColumnIndex("evalDate")));
	        } catch (ParseException e) {
	          e.printStackTrace();
	        }
	        
	        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
			mYear = Integer.parseInt(yearFormat.format(date));
			mMonth = Integer.parseInt(monthFormat.format(date)) -1;
			mDay = Integer.parseInt(dayFormat.format(date));
		}
	    evaluationsDB.close();
	    
	    //set format
	    twoDigit = NumberFormat.getInstance();
		twoDigit.setMinimumIntegerDigits(2);
		twoDigit.setMaximumIntegerDigits(2);
		twoDigit.setMinimumFractionDigits(0);
		twoDigit.setMaximumFractionDigits(0);
		
		selectedDate = mYear + "-" + twoDigit.format(mMonth + 1) + "-" + twoDigit.format(mDay);
		bEvalPickDate.setText(mYear + "-" + twoDigit.format(mMonth + 1) + "-" + twoDigit.format(mDay));
	    
		List<String> categories = new ArrayList<String>();

		// read data from database
		categoriesDB.open();
		Cursor c = categoriesDB.getCategoriesOfCourse(refIDGet_Course);
		int i = 0;
		refID = new int[c.getCount()];
		if (c.moveToFirst()) {
			do {
				refID[i] = c.getInt(0); // get ids of each.
				categories.add(c.getString(c.getColumnIndex("catTitle")));
				i++;
			} while (c.moveToNext());
		}
		categoriesDB.close();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sEvalCat.setAdapter(dataAdapter);
		sEvalCat.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
				//String selecteditem = adapter.getItemAtPosition(i).toString();
				//Toast.makeText(context, "Selected item: " + selecteditem + "\n" + "ID of selected item: " + refID[i], Toast.LENGTH_SHORT).show();
				selectedRefID = refID[i];
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		
		if (idGet_Mode == 1) {
			for (int j = 0; j < c.getCount(); j++) {
				if (cEvaluation.getInt(cEvaluation.getColumnIndex("catRef")) == refID[j]) {
					sEvalCat.setSelection(j);
				}
			}	
			
			//set other data
			etEvalName.setText(cEvaluation.getString(cEvaluation.getColumnIndex("evalTitle")));
			etEvalMark.setText(cEvaluation.getString(cEvaluation.getColumnIndex("evalMark")));
			etEvalOutOf.setText(cEvaluation.getString(cEvaluation.getColumnIndex("evalOutOf")));
			etWeight.setText(cEvaluation.getString(cEvaluation.getColumnIndex("evalWeight")));
			//etEvalNotes.setText(cEvaluation.getString(cEvaluation.getColumnIndex("evalNotes")));
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	    // Save the user's current state
	    savedInstanceState.putInt(STATE_YEAR, mYear);
	    savedInstanceState.putInt(STATE_MONTH, mMonth);
	    savedInstanceState.putInt(STATE_DAY, mDay);
	    
	    super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			selectedDate = mYear + "-" + twoDigit.format(mMonth + 1) + "-" + twoDigit.format(mDay);
			bEvalPickDate.setText(mYear + "-" + twoDigit.format(mMonth + 1) + "-" + twoDigit.format(mDay));
		}
	};
	
	
	public void addEval(View v) {
	
		Log.d(TAG, "Adding or Editing Evaluation.");
		
		if (etEvalName.getText().toString().trim().equals("") || etEvalMark.getText().toString().trim().equals("") || etEvalOutOf.getText().toString().trim().equals("")) {
			//TODO check if Mark, OutOf, and Weight are numeric.
			//TODO weight and notes are not required values
			
		    new AlertDialog.Builder(this)
		    .setMessage("Make sure all fields are entered.")
		    .setPositiveButton("OK", null)
		    .show();
			
		}
		
		else if((Double.parseDouble(etEvalOutOf.getText().toString().trim())<=0.0) || ((Double.parseDouble(etEvalMark.getText().toString().trim())<0.0))) {
			 new AlertDialog.Builder(this)
			    .setMessage("Make sure all fields are entered correctly.")
			    .setPositiveButton("OK", null)
			    .show();
		}
		
			else {
			
			String evalName = etEvalName.getText().toString();
			double evalMark = 0.0;
			double evalOutOf = 0.0;
			double evalWeight = 0.0;
			String evalNotes = etEvalNotes.getText().toString();
			
		    try {
		    	evalMark = Double.parseDouble(etEvalMark.getText().toString());  
		    } catch(NumberFormatException nfe) {  
		    	Log.d(TAG, nfe.getMessage());
		    }
		    
		    try {
		    	evalOutOf = Double.parseDouble(etEvalOutOf.getText().toString());  
		    } catch(NumberFormatException nfe) {  
		    	Log.d(TAG, nfe.getMessage());
		    }
			
			try {
		    	evalWeight = Double.parseDouble(etWeight.getText().toString());  
		    } catch(NumberFormatException nfe) {  
		    	Log.d(TAG, nfe.getMessage());
		    }
		    
			if (idGet_Mode == 0) {
				//adding data to database
			    evaluationsDB.open();
				evaluationsDB.createEvaluation(evalName, evalMark, evalOutOf, evalWeight, selectedDate, refIDGet_Term, refIDGet_Course, selectedRefID);
				evaluationsDB.close();
				Toast toast = Toast.makeText(context, "Evaluation " + evalName + " was added successfully.", Toast.LENGTH_SHORT);
				try {
					//center toast
					((TextView)((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
				} catch (ClassCastException cce) {
					Log.d(TAG, cce.getMessage());
				}
				toast.show();
				Log.d(TAG, "Edited Evaluation Successfully.");
				finish();	
		    } else if (idGet_Mode == 1) {
		    	//editing data in database
			    evaluationsDB.open();
			    evaluationsDB.updateEvaluation(idEditGet_Item, evalName, evalMark, evalOutOf, evalWeight, selectedDate, refIDGet_Term, refIDGet_Course, selectedRefID);
				evaluationsDB.close();
				Toast toast = Toast.makeText(context, "Evaluation " + evalName + " was edited successfully.", Toast.LENGTH_SHORT);
				try {
					//center toast
					((TextView)((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
				} catch (ClassCastException cce) {
					Log.d(TAG, cce.getMessage());
				}
				toast.show();
				Log.d(TAG, "Added Evaluation Successfully.");
				finish();	
			}
		}
	}
}