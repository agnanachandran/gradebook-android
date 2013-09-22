package ca.projectkarma.gradetrackr.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {
    int mHour;
    int mMinute;
    private TimePickedListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // use the current time as the default values for the picker

        if (savedInstanceState == null)
        {
            mHour = (Integer) this.getArguments().get("curHour");
            mMinute = (Integer) this.getArguments().get("curMin");
        }

        else
        {
            mHour = savedInstanceState.getInt("setHour");
            mMinute = savedInstanceState.getInt("setMinute");
        }
        // create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, mHour, mMinute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("setHour", mHour);
        outState.putInt("setMinute", mMinute);
    }

    @Override
    public void onAttach(Activity activity) {
        // when the fragment is initially shown (i.e. attached to the activity),
        // cast the activity to the callback interface type
        super.onAttach(activity);
        try
        {
            mListener = (TimePickedListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement "
                    + TimePickedListener.class.getName());
        }
    }

    public static interface TimePickedListener {
        public void onTimePicked(Calendar time);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // when the time is selected, send it to the activity via its callback
        // interface method
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        mHour = hourOfDay;
        mMinute = minute;

        mListener.onTimePicked(c);
    }
}
