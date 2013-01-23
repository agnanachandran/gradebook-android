package com.amrak.gradebook;

import java.util.Vector;

/**
 * 
 * @author Chronos
 * 
 */
public class Tool {

    /**
     * 
     * Does average, etc.
     * 
     * @param args
     */
    public static double getAverage(Vector<Double> x) {
	double sum = 0;

	for (int i = 0; i < x.size(); i++)
	{
	    sum += x.get(i);
	}

	return sum / x.size();

    }

    public static String toProperDate(String date) {
	int dash = date.indexOf("-");
	String year = date.substring(0, dash);
	String month = date.substring(dash + 1, date.indexOf("-", dash + 1));
	String day = date.substring(date.indexOf("-", dash + 1) + 1, date.length());
	return year + "/" + month + "/" + day;
    }

    public static String toMonthYear(String date) {
	date = Tool.toProperDate(date);
	int month = Integer.parseInt(date.substring(5, 7));
	int year = Integer.parseInt(date.substring(0, 4));
	String realMonth = null;
	switch (month)
	{
	case 1:
	    realMonth = "Jan";
	    break;
	case 2:
	    realMonth = "Feb";
	    break;
	case 3:
	    realMonth = "Mar";
	    break;
	case 4:
	    realMonth = "Apr";
	    break;
	case 5:
	    realMonth = "May";
	    break;
	case 6:
	    realMonth = "June";
	    break;
	case 7:
	    realMonth = "July";
	    break;
	case 8:
	    realMonth = "Aug";
	    break;
	case 9:
	    realMonth = "Sept";
	    break;
	case 10:
	    realMonth = "Oct";
	    break;
	case 11:
	    realMonth = "Nov";
	    break;
	case 12:
	    realMonth = "Dec";
	    break;
	}

	return realMonth + " " + Integer.toString(year);

    }

}
