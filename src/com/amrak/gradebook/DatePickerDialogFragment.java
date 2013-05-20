package com.amrak.gradebook;

import java.util.Calendar;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerDialogFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	int mYear;
	int mMonth;
	int mDay;
	private DatePickedListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// use the current date as the default values for the picker

		if (savedInstanceState == null)
		{
			mYear = (Integer) this.getArguments().get("curYear");
			mMonth = (Integer) this.getArguments().get("curMonth");
			mDay = (Integer) this.getArguments().get("curDay");
		}

		else
		{
			mYear = savedInstanceState.getInt("setYear");
			mMonth = savedInstanceState.getInt("setMonth");
			mDay = savedInstanceState.getInt("setDay");
		}
		// create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("setYear", mYear);
		outState.putInt("setMonth", mMonth);
		outState.putInt("setDay", mDay);
	}

	@Override
	public void onAttach(Activity activity) {
		// when the fragment is initially shown (i.e. attached to the activity),
		// cast the activity to the callback interface type
		super.onAttach(activity);
		try
		{
			mListener = (DatePickedListener) activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(activity.toString() + " must implement "
					+ DatePickedListener.class.getName());
		}
	}

	public static interface DatePickedListener {
		public void onDatePicked(Calendar date);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, monthOfYear);
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;
		mListener.onDatePicked(c);
	}
}
