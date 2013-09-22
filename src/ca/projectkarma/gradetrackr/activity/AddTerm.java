package ca.projectkarma.gradetrackr.activity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.projectkarma.gradetrackr.EditMode;
import ca.projectkarma.gradetrackr.activity.DatePickerDialogFragment.DatePickedListener;
import ca.projectkarma.gradetrackr.db.adapter.TermsDBAdapter;

import ca.projectkarma.gradetrackr.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddTerm extends FragmentActivity implements DatePickedListener {

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
    int idGet_Mode; // use with EditMode constants
    int idEditGet_Item;
    
    enum DatePickers {START, END};
    DatePickers datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        Intent iAddTerm = getIntent();
        idGet_Mode = iAddTerm.getIntExtra("id_Mode", EditMode.ADD_MODE);
                                                         // edit
        idEditGet_Item = iAddTerm.getIntExtra("idEdit_Item", -1);

        etTermTitle = (EditText) findViewById(R.id.etTermTitle);
        bTermPickStartDate = (Button) findViewById(R.id.bTermPickStartDate);
        bTermPickEndDate = (Button) findViewById(R.id.bTermPickEndDate);
        bTermDone = (Button) findViewById(R.id.bTermDone);

        if (idGet_Mode == EditMode.ADD_MODE)
        {
            setTitle("Add Term");
        }
        else if (idGet_Mode == EditMode.EDIT_MODE)
        {
            setTitle("Edit Term");
            bTermDone.setText(R.string.doneEditTerm);
        }

        bTermPickStartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //showDialog(DATE_START_DIALOG_ID);
                datePicker = DatePickers.START;
                Bundle b = new Bundle();
                b.putInt("curYear", mSYear);
                b.putInt("curMonth", mSMonth);
                b.putInt("curDay", mSDay);
                // show the time picker dialog
                DialogFragment newFragment = new DatePickerDialogFragment();
                newFragment.setArguments(b);
                newFragment.show(getSupportFragmentManager(), "datePickerS");
            }
        });

        bTermPickEndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //showDialog(DATE_END_DIALOG_ID);
                datePicker = DatePickers.END;
                Bundle b = new Bundle();
                b.putInt("curYear", mEYear);
                b.putInt("curMonth", mEMonth);
                b.putInt("curDay", mEDay);
                // show the time picker dialog
                DialogFragment newFragment = new DatePickerDialogFragment();
                newFragment.setArguments(b);
                newFragment.show(getSupportFragmentManager(), "datePickerE");
            }
        });

        termsDB.open();
        Cursor cTerm = termsDB.getTerm(idEditGet_Item);
        cTerm.moveToFirst();
        // set the date
        if (savedInstanceState != null)
        {
            // recreate date from state
            mSYear = savedInstanceState.getInt(STATE_SYEAR);
            mSMonth = savedInstanceState.getInt(STATE_SMONTH);
            mSDay = savedInstanceState.getInt(STATE_SDAY);
            mEYear = savedInstanceState.getInt(STATE_EYEAR);
            mEMonth = savedInstanceState.getInt(STATE_EMONTH);
            mEDay = savedInstanceState.getInt(STATE_EDAY);
        }
        else if (idGet_Mode == EditMode.ADD_MODE)
        {
            // set date to today
            final Calendar cal = Calendar.getInstance();
            mSYear = cal.get(Calendar.YEAR);
            mSMonth = cal.get(Calendar.MONTH);
            mSDay = cal.get(Calendar.DAY_OF_MONTH);
            mEYear = cal.get(Calendar.YEAR);
            mEMonth = cal.get(Calendar.MONTH);
            mEDay = cal.get(Calendar.DAY_OF_MONTH);
        }
        else if (idGet_Mode == EditMode.EDIT_MODE)
        {
            // set date from database
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date sDate = new Date();
            Date eDate = new Date();
            try
            {
                sDate = format.parse(cTerm.getString(cTerm.getColumnIndex("termStartDate")));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            try
            {
                eDate = format.parse(cTerm.getString(cTerm.getColumnIndex("termEndDate")));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);

            mSYear = Integer.parseInt(yearFormat.format(sDate));
            mSMonth = Integer.parseInt(monthFormat.format(sDate)) - 1;
            mSDay = Integer.parseInt(dayFormat.format(sDate));
            mEYear = Integer.parseInt(yearFormat.format(eDate));
            mEMonth = Integer.parseInt(monthFormat.format(eDate)) - 1;
            mEDay = Integer.parseInt(dayFormat.format(eDate));
        }
        termsDB.close();

        // set format
        twoDigit = NumberFormat.getInstance();
        twoDigit.setMinimumIntegerDigits(2);
        twoDigit.setMaximumIntegerDigits(2);
        twoDigit.setMinimumFractionDigits(0);
        twoDigit.setMaximumFractionDigits(0);

        selectedSDate = mSYear + "-" + twoDigit.format(mSMonth + 1) + "-" + twoDigit.format(mSDay);
        bTermPickStartDate.setText(selectedSDate);
        selectedEDate = mEYear + "-" + twoDigit.format(mEMonth + 1) + "-" + twoDigit.format(mEDay);
        bTermPickEndDate.setText(selectedEDate);

        bTermDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addTerm(v);
            }
        });

        if (idGet_Mode == EditMode.EDIT_MODE)
        {
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
        savedInstanceState.putInt(STATE_EYEAR, mEYear);
        savedInstanceState.putInt(STATE_EMONTH, mEMonth);
        savedInstanceState.putInt(STATE_EDAY, mEDay);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_term, menu);
        return true;
    }

    public void addTerm(View v) {

        Log.d(TAG, "Adding of Editing Term.");

        if (etTermTitle.getText().toString().trim().equals(""))
        {
            // TODO Check: start date must be before end date

            new AlertDialog.Builder(this).setMessage("Make sure all fields are entered.")
                    .setPositiveButton("OK", null).show();

        }
        else
        {

            if (mSYear > mEYear || (mSYear == mEYear && mSMonth > mEMonth)
                    || (mSYear == mEYear && mSMonth == mEMonth && mSDay > mEDay))
            {
                // end date is before start date
                new AlertDialog.Builder(this).setMessage("Start date must be before end date.")
                        .setPositiveButton("OK", null).show();

            }
            else
            {

                String termTitle = etTermTitle.getText().toString();

                if (idGet_Mode == EditMode.ADD_MODE)
                {
                    // get data from form and input to database
                    termsDB.open();
                    termsDB.createTerm(termTitle, selectedSDate, selectedEDate);
                    termsDB.close();

                    Toast toast = Toast.makeText(context, "Term " + termTitle
                            + " was added successfully.", Toast.LENGTH_SHORT);
                    try
                    {
                        // center toast
                        ((TextView) ((LinearLayout) toast.getView()).getChildAt(0))
                                .setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                    catch (ClassCastException cce)
                    {
                        Log.d(TAG, cce.getMessage());
                    }
                    toast.show();
                    Log.d(TAG, "Added Term Successfully.");
                    finish();
                }
                else if (idGet_Mode == EditMode.EDIT_MODE)
                {
                    // editing data in database
                    termsDB.open();
                    termsDB.updateTerm(idEditGet_Item, termTitle, selectedSDate, selectedEDate);
                    termsDB.close();

                    Toast toast = Toast.makeText(context, "Term" + termTitle
                            + " was edited successfully.", Toast.LENGTH_SHORT);
                    try
                    {
                        // center toast
                        ((TextView) ((LinearLayout) toast.getView()).getChildAt(0))
                                .setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                    catch (ClassCastException cce)
                    {
                        Log.d(TAG, cce.getMessage());
                    }
                    toast.show();
                    Log.d(TAG, "Edited Term Successfully.");
                    finish();
                }
            }
        }

    }

	@Override
	public void onDatePicked(Calendar date) {
		if (datePicker == DatePickers.START) {
			mSYear = date.get(Calendar.YEAR);
			mSMonth = date.get(Calendar.MONTH);
			mSDay = date.get(Calendar.DAY_OF_MONTH);
            selectedSDate = mSYear + "-" + twoDigit.format(mSMonth + 1) + "-"
                    + twoDigit.format(mSDay);
            bTermPickStartDate.setText(selectedSDate);
		} else if (datePicker == DatePickers.END) {
			mEYear = date.get(Calendar.YEAR);
			mEMonth = date.get(Calendar.MONTH);
			mEDay = date.get(Calendar.DAY_OF_MONTH);
            selectedEDate = mEYear + "-" + twoDigit.format(mEMonth + 1) + "-"
                    + twoDigit.format(mEDay);
            bTermPickEndDate.setText(selectedEDate);
		}
		
	}
	
}
