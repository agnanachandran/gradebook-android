package com.amrak.gradebook.activity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

import com.amrak.gradebook.EditMode;
import com.amrak.gradebook.R;
import com.amrak.gradebook.activity.DatePickerDialogFragment.DatePickedListener;
import com.amrak.gradebook.activity.TimePickerDialogFragment.TimePickedListener;
import com.amrak.gradebook.db.adapter.TaskDBAdapter;

public class AddTask extends FragmentActivity implements TimePickedListener, DatePickedListener {

    // database
    TaskDBAdapter tasksDB = new TaskDBAdapter(this);

    // context
    Context context = this;

    // views
    EditText etTaskTitle;
    Button bTaskPickDate;
    Button bTaskPickTime;
    Button bTaskDone;

    // date
    private int mEYear;
    private int mEMonth;
    private int mEDay;
    static final String STATE_EYEAR = "selectedEYear";
    static final String STATE_EMONTH = "selectedEMonth";
    static final String STATE_EDAY = "selectedEDay";
    static final int DATE_END_DIALOG_ID = 1;

    // time
    private int mEHour;
    private int mEMin;
    static final String STATE_EHOUR = "selectedEHour";
    static final String STATE_EMIN = "selectedEMin";
    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat parseFormat = new SimpleDateFormat("h:mm a");

    NumberFormat twoDigit;

    // variables
    final private String TAG = "AddTask";
    int[] refID;
    int selectedRefID;
    String selectedEDate;
    String selectedETime;
    int idGet_Mode; // use with EditMode constants
    int idEditGet_Item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // initialize time to current time
        final Calendar c = Calendar.getInstance();
        mEHour = c.get(Calendar.HOUR_OF_DAY);
        mEMin = c.get(Calendar.MINUTE);

        Intent iAddTask = getIntent();
        idGet_Mode = iAddTask.getIntExtra("id_Mode", EditMode.ADD_MODE);
        // edit
        idEditGet_Item = iAddTask.getIntExtra("idEdit_Item", -1);
        
        etTaskTitle = (EditText) findViewById(R.id.etTaskTitle);
        bTaskPickDate = (Button) findViewById(R.id.bTaskPickDate);
        bTaskPickTime = (Button) findViewById(R.id.bTaskPickTime);
        bTaskDone = (Button) findViewById(R.id.bTaskDone);

        bTaskPickTime.setText(readableTime(mEHour, mEMin));
        if (idGet_Mode == EditMode.ADD_MODE)
        {
            setTitle("Add Task");
        }
        else if (idGet_Mode == EditMode.EDIT_MODE)
        {
            setTitle("Edit Task");
            bTaskDone.setText(R.string.doneEditTask);
        }

        bTaskPickTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putInt("curHour", mEHour);
                b.putInt("curMin", mEMin);
                // show the time picker dialog
                DialogFragment newFragment = new TimePickerDialogFragment();
                newFragment.setArguments(b);
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        bTaskPickDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putInt("curYear", mEYear);
                b.putInt("curMonth", mEMonth);
                b.putInt("curDay", mEDay);
                // show the time picker dialog
                DialogFragment newFragment = new DatePickerDialogFragment();
                newFragment.setArguments(b);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        tasksDB.open();
        Cursor cTask = tasksDB.getTask(idEditGet_Item);
        cTask.moveToFirst();
        Log.d(TAG, String.valueOf(cTask.getCount()));
        // set the date
        if (savedInstanceState != null)
        {
            // recreate date from state
            mEYear = savedInstanceState.getInt(STATE_EYEAR);
            mEMonth = savedInstanceState.getInt(STATE_EMONTH);
            mEDay = savedInstanceState.getInt(STATE_EDAY);
            mEHour = savedInstanceState.getInt(STATE_EHOUR);
            mEMin = savedInstanceState.getInt(STATE_EMIN);
        }
        else if (idGet_Mode == EditMode.ADD_MODE)
        {
            // set date to today
            final Calendar cal = Calendar.getInstance();
            mEYear = cal.get(Calendar.YEAR);
            mEMonth = cal.get(Calendar.MONTH);
            mEDay = cal.get(Calendar.DAY_OF_MONTH);
            mEHour = cal.get(Calendar.HOUR_OF_DAY);
            mEMin = cal.get(Calendar.MINUTE);
        }
        else if (idGet_Mode == EditMode.EDIT_MODE)
        {
            // set date from database
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date eDate = new Date();
            Date eTime = new Date();
            try
            {
                eDate = format.parse(cTask.getString(cTask.getColumnIndex("taskDateDue")));
                eTime = parseFormat.parse(cTask.getString(cTask.getColumnIndex("taskDateDueTime")));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat hourFormat = new SimpleDateFormat("h");
            SimpleDateFormat minFormat = new SimpleDateFormat("mm");
            SimpleDateFormat ampmFormat = new SimpleDateFormat("a");

            mEYear = Integer.parseInt(yearFormat.format(eDate));
            mEMonth = Integer.parseInt(monthFormat.format(eDate)) - 1;
            mEDay = Integer.parseInt(dayFormat.format(eDate));
            mEHour = Integer.parseInt(hourFormat.format(eTime));
            mEMin = Integer.parseInt(minFormat.format(eTime));
            String x = ampmFormat.format(eTime);
            Log.d(TAG, x);
        }
        tasksDB.close();

        // set format
        twoDigit = NumberFormat.getInstance();
        twoDigit.setMinimumIntegerDigits(2);
        twoDigit.setMaximumIntegerDigits(2);
        twoDigit.setMinimumFractionDigits(0);
        twoDigit.setMaximumFractionDigits(0);

        selectedETime = readableTime(mEHour, mEMin);
        selectedEDate = mEYear + "-" + twoDigit.format(mEMonth + 1) + "-" + twoDigit.format(mEDay);

        bTaskPickDate.setText(selectedEDate);
        bTaskPickTime.setText(selectedETime);
        bTaskDone.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                addTask(v);
            }
        });

        if (idGet_Mode == EditMode.EDIT_MODE)
        {
            etTaskTitle.setText(cTask.getString(cTask.getColumnIndex("taskTitle")));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        // Save the user's current state
        savedInstanceState.putInt(STATE_EYEAR, mEYear);
        savedInstanceState.putInt(STATE_EMONTH, mEMonth);
        savedInstanceState.putInt(STATE_EDAY, mEDay);
        savedInstanceState.putInt(STATE_EHOUR, mEHour);
        savedInstanceState.putInt(STATE_EMIN, mEMin);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_task, menu);
        return true;
    }

    public void addTask(View v) {

        Log.d(TAG, "Adding or Editing Task.");

        if (etTaskTitle.getText().toString().trim().equals(""))
        {

            new AlertDialog.Builder(this).setMessage("Make sure all fields are entered.")
                    .setPositiveButton("OK", null).show();

        }
        else
        {

            {

                String taskTitle = etTaskTitle.getText().toString();

                if (idGet_Mode == EditMode.ADD_MODE)
                {
                    // adding data to database
                    // get data from form and input to database
                    tasksDB.open();
                    tasksDB.createTask(taskTitle, selectedEDate, selectedETime);
                    tasksDB.close();

                    Toast toast = Toast.makeText(context, "Task " + taskTitle
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
                    Log.d(TAG, "Added Task Successfully.");
                    finish();
                }
                else if (idGet_Mode == EditMode.EDIT_MODE)
                {
                    // editing data in database
                    tasksDB.open();
                    tasksDB.updateTask(idEditGet_Item, taskTitle, selectedEDate, selectedETime);
                    tasksDB.close();

                    Toast toast = Toast.makeText(context, "Task " + taskTitle
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
                    Log.d(TAG, "Edited Task Successfully.");
                    finish();
                }
            }
        }

    }

    @Override
    public void onTimePicked(Calendar time) {
        mEHour = time.get(Calendar.HOUR_OF_DAY);
        mEMin = time.get(Calendar.MINUTE);
        selectedETime = readableTime(mEHour, mEMin);
        bTaskPickTime.setText(selectedETime);
    }

    public String readableTime(int hour, int min) {

        Date date = null;
        try
        {
            date = displayFormat.parse(hour + ":" + min);
        }
        catch (ParseException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return parseFormat.format(date);
    }

    @Override
    public void onDatePicked(Calendar date) {
        mEYear = date.get(Calendar.YEAR);
        mEMonth = date.get(Calendar.MONTH);
        mEDay = date.get(Calendar.DAY_OF_MONTH);
        selectedEDate = mEYear + "-" + twoDigit.format(mEMonth + 1) + "-" + twoDigit.format(mEDay);
        bTaskPickDate.setText(selectedEDate);
    }

}