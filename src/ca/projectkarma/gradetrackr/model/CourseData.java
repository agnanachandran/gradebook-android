package ca.projectkarma.gradetrackr.model;

import ca.projectkarma.gradetrackr.db.adapter.CategoriesDBAdapter;

import android.content.Context;
import android.database.Cursor;

public class CourseData {

    int databaseID = -1;
    String title = null;
    String code = null;
    double units = 0;
    double mark = 0;
    String notes = null;
    int refTermID = -1;

    Context context;
    CategoriesDBAdapter categoriesDB;

    public CourseData(int inputCourseID, String inputTitle, String inputCode, double inputUnits,
            String inputNotes, int inputRefTermID, Context inputContext) {
    	databaseID = inputCourseID;
        title = inputTitle;
        code = inputCode;
        units = inputUnits;
        notes = inputNotes;
        refTermID = inputRefTermID;
        context = inputContext;
        categoriesDB = new CategoriesDBAdapter(context);
        mark = calcMarkFromDatabase(inputCourseID);
    }

    public double calcMarkFromDatabase(int courseID) {
        double mark = 0;
        int totalWeight = 0;
        double categoryMark = 0;

        categoriesDB.open();
        Cursor c = categoriesDB.getCategoriesOfCourse(courseID);

        // default mark is 100.00
        if (c.getCount() == 0)
        {
            categoriesDB.close();
            return 100.00;
        }

        if (c.moveToFirst())
        {
            do
            {
                totalWeight += c.getInt(c.getColumnIndex("catWeight"));
            }
            while (c.moveToNext());
        }

        // When the user has a category that doesn't count for anything
        if (totalWeight == 0)
        {
            categoriesDB.close();
            return 100.00;
        }

        else
        {
            if (c.moveToFirst())
            {
                do
                {
                    double weightFraction = (double) c.getInt(c.getColumnIndex("catWeight"))
                            / totalWeight;
                    categoryMark = c.getDouble(c.getColumnIndex("catAverage"));
                    mark += weightFraction * categoryMark;
                }
                while (c.moveToNext());
            }
        }

        categoriesDB.close();

        return mark;
    }

    public int getDatabaseID() {
        return databaseID;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public double getUnits() {
        return units;
    }

    public double getMark() {
        return mark;
    }

    public String getNotes() {
        return notes;
    }

    public double getRefTermID() {
        return refTermID;
    }

}
