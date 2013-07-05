package com.amrak.gradebook.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import android.content.Context;

public class TaskData implements Comparator<TaskData> {

    int taskID = -1;
    String title = null;
    String dateDue = null;
    String dateDueTime = null;
    Context context;
    SimpleDateFormat parseFormat = new SimpleDateFormat("h:mm a");

    public TaskData() {
    }

    public TaskData(int inputTaskID, String inputTitle, String inputDateDue,
            String inputDateDueTime, Context inputContext) {
        taskID = inputTaskID;
        title = inputTitle;
        dateDue = inputDateDue;
        dateDueTime = inputDateDueTime;
        context = inputContext;
    }

    public double getID() {
        return taskID;
    }

    public String getTitle() {
        return title;
    }

    public String getDateDue() {
        return dateDue;
    }

    public String getDateDueTime() {
        return dateDueTime;
    }

    @Override
    public int compare(TaskData lhs, TaskData rhs) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date leDate = new Date();
        Date leTime = new Date();
        Date reDate = new Date();
        Date reTime = new Date();
        try
        {
            leDate = format.parse(lhs.getDateDue());
            leTime = parseFormat.parse(lhs.getDateDueTime());
            reDate = format.parse(rhs.getDateDue());
            reTime = parseFormat.parse(rhs.getDateDueTime());
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

        int lYear = Integer.parseInt(yearFormat.format(leDate));
        int lMonth = Integer.parseInt(monthFormat.format(leDate)) - 1;
        int lDay = Integer.parseInt(dayFormat.format(leDate));
        int lHour = Integer.parseInt(hourFormat.format(leTime));
        int lMin = Integer.parseInt(minFormat.format(leTime));
        int rYear = Integer.parseInt(yearFormat.format(reDate));
        int rMonth = Integer.parseInt(monthFormat.format(reDate)) - 1;
        int rDay = Integer.parseInt(dayFormat.format(reDate));
        int rHour = Integer.parseInt(hourFormat.format(reTime));
        int rMin = Integer.parseInt(minFormat.format(reTime));
        if (lYear < rYear)
        {
            return -1;
        }
        else if (lYear == rYear)
        {
            if (lMonth < rMonth)
            {
                return -1;
            }
            else if (lMonth == rMonth)
            {
                if (lDay < rDay)
                {
                    return -1;
                }
                else if (lDay == rDay)
                {
                    if (lHour < rHour)
                    {
                        return -1;
                    }
                    else if (lHour == rHour)
                    {

                        if (lMin < rMin)
                        {
                            return -1;
                        }
                        else if (lMin == rMin)
                        {
                            return 0;
                        }
                    }
                }

            }
        }
        return 1;
    }

}