package ca.projectkarma.gradetrackr;

import java.util.List;

public class Utils {

    /**
     * 
     * finds average of a list of doubles
     * 
     * @param x
     */
    public static double getAverage(List<Double> x) {
        double sum = 0;

        for (int i = 0; i < x.size(); i++)
            sum += x.get(i);

        return sum / x.size();
    }

    public static String toProperDate(String date) {
        int dash = date.indexOf("-");
        String year = date.substring(0, dash);
        String month = date.substring(dash + 1, date.indexOf("-", dash + 1));
        String day = date.substring(date.indexOf("-", dash + 1) + 1, date.length());
        return year + "/" + month + "/" + day;
    }

    /**
     * 
     * @param date
     * @return a String representation of the full name of the month and year, ex. "January 2013"
     */
    public static String toMonthYear(String date) {
        date = Utils.toProperDate(date);
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
            default:
                realMonth = "Dec";
                break;
        }

        return realMonth + " " + Integer.toString(year);

    }

    /**
     * 
     * @param date
     * @return a String representation of the full name of the month and year, ex. "January 2013"
     */
    public static String toFullMonthYear(String date) {
        date = Utils.toProperDate(date);
        int month = Integer.parseInt(date.substring(5, 7));
        int year = Integer.parseInt(date.substring(0, 4));
        String realMonth = null;
        switch (month)
        {
            case 1:
                realMonth = "January";
                break;
            case 2:
                realMonth = "February";
                break;
            case 3:
                realMonth = "March";
                break;
            case 4:
                realMonth = "April";
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
                realMonth = "August";
                break;
            case 9:
                realMonth = "September";
                break;
            case 10:
                realMonth = "October";
                break;
            case 11:
                realMonth = "November";
                break;
            case 12:
                realMonth = "December";
                break;
        }
        return realMonth + " " + Integer.toString(year);
    }

}
