package com.amrak.gradebook;

import android.content.Context;
import android.database.Cursor;

public class TermData {

	int termID = -1;
	String title = null;
	String dateStart = null;
	String dateEnd = null;
	double mark = 0;
	
	Context context;
	CoursesDBAdapter coursesDB;

	public TermData(int inputTermID, String inputTitle, String inputDateStart,
			String inputDateEnd, Context inputContext) {
		termID = inputTermID;
		title = inputTitle;
		dateStart = inputDateStart;
		dateEnd = inputDateEnd;
		context = inputContext;
		coursesDB = new CoursesDBAdapter(context);
		mark = calcMarkFromDatabase(inputTermID);
	}

	public double calcMarkFromDatabase(int termID) {
		double mark = 0;
		double totalWeight = 0; //courseUnits can be doubles
		CourseData courseData;
		
		coursesDB.open();
		Cursor c = coursesDB.getCoursesOfTerm(termID);
		
		//default mark is 100.00
		if (c.getCount() == 0)
			return 100.00;
		
		if (c.moveToFirst()) {
			do {
				totalWeight += c.getDouble(c.getColumnIndex("courseUnits"));
			} while(c.moveToNext());
		}
		
		// when user has a course that doesn't count for anything
		if (totalWeight == 0)
			return 100.00;		
		else {
			if (c.moveToFirst()) {
				do {
					double weightFraction = c.getDouble(c.getColumnIndex("courseUnits"))/totalWeight;
					courseData = new CourseData(c.getInt(c.getColumnIndex("_id")), 
							c.getString(c.getColumnIndex("courseTitle")), 
							c.getString(c.getColumnIndex("courseCode")), 
							c.getInt(c.getColumnIndex("courseUnits")), 
							c.getString(c.getColumnIndex("notes")), 
							c.getInt(c.getColumnIndex("termRef")),
							context);
					mark += weightFraction*courseData.getMark();
				} while(c.moveToNext());
			}			
		}
		
		coursesDB.close();

		return mark;
	}

	public double getID() {
		return termID;
	}

	public String getTitle() {
		return title;
	}

	public double getMark() {
		return mark;
	}

	public String getDateStart() {
		return dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

}
