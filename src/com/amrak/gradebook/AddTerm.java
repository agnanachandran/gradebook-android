package com.amrak.gradebook;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddTerm extends Activity {
	
	// database
	TermsDBAdapter termsDB = new TermsDBAdapter(this);
	
	// context
	Context context = this;
	
	// views
	EditText etTermTitle;
	Button bTermPickStartDate;
	Button bTermPickEndDate;
	Button bTermDone;
	
	// date and time
	private int mSYear;
	private int mSMonth;
	private int mSDay;
	private int mEYear;
	private int mEMonth;
	private int mEDay;
	static final String STATE_SYEAR = "selectedSYear";
	static final String STATE_SMONTH = "selectedSMonth";
	static final String STATE_SDAY = "selectedSDay";
	static final String STATE_EYEAR = "selectedEYear";
	static final String STATE_EMONTH = "selectedEMonth";
	static final String STATE_EDAY = "selectedEDay";
	static final int DATE_START_DIALOG_ID = 0;
	static final int DATE_END_DIALOG_ID = 1;
	NumberFormat twoDigit;
	
	// variables
	final private String TAG = "AddTerm";
	int[] refID;
	int selectedRefID;
	String selectedSDate;
	String selectedEDate;
	int idGet_Mode; // mode 0: add, mode 1: edit
	int idEditGet_Item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_term);
		
		Intent iAddTerm = getIntent();
		idGet_Mode = iAddTerm.getIntExtra("id_Mode", 0); // mode 0: add, mode 1: edit
		idEditGet_Item = iAddTerm.getIntExtra("idEdit_Item", -1);
		
		etTermTitle = (EditText) findViewById(R.id.etTermTitle);
		bTermPickStartDate = (Button) findViewById(R.id.bTermPickStartDate);
		bTermPickEndDate = (Button) findViewById(R.id.bTermPickEndDate);
		bTermDone = (Button) findViewById(R.id.bTermDone);
		
		if (idGet_Mode == 0) {
			setTitle("Add a Term");
		} else if (idGet_Mode == 1) {
			setTitle("Edit a Term");
			bTermDone.setText(R.string.doneEditTerm);
		}
		
		bTermPickStartDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_START_DIALOG_ID);
			}
		});

		bTermPickEndDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_END_DIALOG_ID);
			}
		});
		
		termsDB.open();
		Cursor cTerm = termsDB.getTerm(idEditGet_Item);
		cTerm.moveToFirst();
		// set the date
	    if (savedInstanceState != null) {
	    	// recreate date from state
	        mSYear = savedInstanceState.getInt(STATE_SYEAR);
	        mSMonth = savedInstanceState.getInt(STATE_SMONTH);
	        mSDay = savedInstanceState.getInt(STATE_SDAY);
	        mEYear = savedInstanceState.getInt(STATE_EYEAR);
	        mEMonth = savedInstanceState.getInt(STATE_EMONTH);
	        mEDay = savedInstanceState.getInt(STATE_EDAY);
	    } else if (idGet_Mode == 0) {
	    	// set date to today
	    	final Calendar cal = Calendar.getInstance();
			mSYear = cal.get(Calendar.YEAR);
			mSMonth = cal.get(Calendar.MONTH);
			mSDay = cal.get(Calendar.DAY_OF_MONTH);
			mEYear = cal.get(Calendar.YEAR);
			mEMonth = cal.get(Calendar.MONTH);
			mEDay = cal.get(Calendar.DAY_OF_MONTH);
	    } else if (idGet_Mode == 1) {
	    	//set date from database
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        Date sDate =  new Date();
	        Date eDate =  new Date();
	        try {
	        	sDate = format.parse(cTerm.getString(cTerm.getColumnIndex("termStartDate")));
	        } catch (ParseException e) {
	          e.printStackTrace();
	        }
	        
	        try {
	        	eDate = format.parse(cTerm.getString(cTerm.getColumnIndex("termEndDate")));
	        } catch (ParseException e) {
	          e.printStackTrace();
	        }
	        
	        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
	        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
	        
			mSYear = Integer.parseInt(yearFormat.format(sDate));
			mSMonth = Integer.parseInt(monthFormat.format(sDate)) -1;
			mSDay = Integer.parseInt(dayFormat.format(eDate));
			mEYear = Integer.parseInt(yearFormat.format(eDate));
			mEMonth = Integer.parseInt(monthFormat.format(eDate)) -1;
			mEDay = Integer.parseInt(dayFormat.format(eDate));
		}
	    termsDB.close();
	    
	    //set format
	    twoDigit = NumberFormat.getInstance();
		twoDigit.setMinimumIntegerDigits(2);
		twoDigit.setMaximumIntegerDigits(2);
		twoDigit.setMinimumFractionDigits(0);
		twoDigit.setMaximumFractionDigits(0);
	    
		selectedSDate = mSYear + "-" + twoDigit.format(mSMonth + 1) + "-" + twoDigit.format(mSDay);
		bTermPickStartDate.setText(mSYear + "-" + twoDigit.format(mSMonth + 1) + "-" + twoDigit.format(mSDay));
		selectedEDate = mEYear + "-" + twoDigit.format(mEMonth + 1) + "-" + twoDigit.format(mEDay);
		bTermPickEndDate.setText(mEYear + "-" + twoDigit.format(mEMonth + 1) + "-" + twoDigit.format(mEDay));
	    
		bTermDone.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addTerm(v);
			}
		});
		
		if (idGet_Mode == 1) {
			etTermTitle.setText(cTerm.getString(cTerm.getColumnIndex("termTitle")));
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	    // Save the user's current state
	    savedInstanceState.putInt(STATE_SYEAR, mSYear);
	    savedInstanceState.putInt(STATE_SMONTH, mSMonth);
	    savedInstanceState.putInt(STATE_SDAY, mSDay);
	    savedInstanceState.putInt(STATE_SYEAR, mEYear);
	    savedInstanceState.putInt(STATE_SMONTH, mEMonth);
	    savedInstanceState.putInt(STATE_SDAY, mEDay);
	    
	    super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_START_DIALOG_ID:
			return new DatePickerDialog(this, mSDateSetListener, mSYear, mSMonth,
					mSDay);
		case DATE_END_DIALOG_ID:
			return new DatePickerDialog(this, mEDateSetListener, mEYear, mEMonth,
					mEDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_START_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mSYear, mSMonth, mSDay);
			break;
		case DATE_END_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mEYear, mEMonth, mEDay);
			break;
		}
	}

	private DatePickerDialog.OnDateSetListener mSDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mSYear = year;
			mSMonth = monthOfYear;
			mSDay = dayOfMonth;
			selectedSDate = mSYear + "-" + twoDigit.format(mSMonth + 1) + "-" + twoDigit.format(mSDay);
			bTermPickStartDate.setText(mSYear + "-" + twoDigit.format(mSMonth + 1) + "-" + twoDigit.format(mSDay));
		}
	};
	
	private DatePickerDialog.OnDateSetListener mEDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mEYear = year;
			mEMonth = monthOfYear;
			mEDay = dayOfMonth;
			selectedEDate = mEYear + "-" + twoDigit.format(mEMonth + 1) + "-" + twoDigit.format(mEDay);
			bTermPickEndDate.setText(mEYear + "-" + twoDigit.format(mEMonth + 1) + "-" + twoDigit.format(mEDay));
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_term, menu);
		return true;
	}

	public void addTerm(View v)
	{
		
		Log.d(TAG, "Adding of Editing Term.");
		
		if (etTermTitle.getText().toString().trim().equals("")) {
			//TODO Check: start date must be before end date
			
		    new AlertDialog.Builder(this)
		    .setMessage("Make sure all fields are entered.")
		    .setPositiveButton("OK", null)
		    .show();
			
		} else {
			
			if (mSYear > mEYear || (mSYear == mEYear && mSMonth > mEMonth) || (mSYear == mEYear && mSMonth == mEMonth && mSDay > mEDay)) {
				// end date is before start date
			    new AlertDialog.Builder(this)
			    .setMessage("Start date must be before end date.")
			    .setPositiveButton("OK", null)
			    .show();
			    
			} else {
			
			String termTitle = etTermTitle.getText().toString();
			
			if (idGet_Mode == 0) {
				// get data from form and input to database
				termsDB.open();
				termsDB.createTerm(termTitle, selectedSDate, selectedEDate);        
				termsDB.close();
		       
				Toast toast = Toast.makeText(context, "Term " + termTitle + " was added successfully.", Toast.LENGTH_SHORT);
				try {
					//center toast
					((TextView)((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
				} catch (ClassCastException cce) {
					Log.d(TAG, cce.getMessage());
				}
				toast.show();
				Log.d(TAG, "Added Term Successfully.");
				finish();
			} else if (idGet_Mode == 1) {
		    	//editing data in database
				termsDB.open();
				termsDB.updateTerm(idEditGet_Item, termTitle, selectedSDate, selectedEDate);
				termsDB.close();

				Toast toast = Toast.makeText(context, "Term " + termTitle + " was edited successfully.", Toast.LENGTH_SHORT);
				try {
					//center toast
					((TextView)((LinearLayout) toast.getView()).getChildAt(0)).setGravity(Gravity.CENTER_HORIZONTAL);
				} catch (ClassCastException cce) {
					Log.d(TAG, cce.getMessage());
				}
				toast.show();
				Log.d(TAG, "Added Term Successfully.");
				finish();	
			}
			}
		}
		
	}
	
}
