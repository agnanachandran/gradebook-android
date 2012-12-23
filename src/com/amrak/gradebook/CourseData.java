package com.amrak.gradebook;

import java.text.DecimalFormat;

import android.content.Context;
import android.database.Cursor;

public class CourseData {

	int courseID = -1;
	String title = null;
	String code = null;
	double units = 0;
	double mark = 0;
	String notes = null;
	int refTermID = -1;
	
	Context context;
	CategoriesDBAdapter categoriesDB;

	public CourseData(int inputCourseID, String inputTitle, String inputCode,
			double inputUnits, String inputNotes, int inputRefTermID, Context inputContext) {
		courseID = inputCourseID;
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
		// TODO Write function
		double mark = 0;
		int totalWeight = 0;
		CategoryData categoryData;
		
		categoriesDB.open();
		Cursor c = categoriesDB.getCategoriesOfCourse(courseID);
		
		if (c.moveToFirst()) {
			do {
				totalWeight += c.getInt(c.getColumnIndex("catWeight"));
			} while(c.moveToNext());
		}
		
		if (c.moveToFirst()) {
			do {
				double weightFraction = (double)c.getInt(c.getColumnIndex("catWeight"))/totalWeight;
				categoryData = new CategoryData(c.getInt(c.getColumnIndex("_id")),
						c.getString(c.getColumnIndex("catTitle")),
						c.getInt(c.getColumnIndex("catWeight")),
						c.getInt(c.getColumnIndex("courseRef")),
						c.getInt(c.getColumnIndex("termRef")),
						this.context);
				mark += weightFraction*(double)categoryData.getMark();
			} while(c.moveToNext());
		}
		
		categoriesDB.close();

		return mark;
	}

	public double getID() {
		return courseID;
	}

	public String getTitle() {
		return title;
	}

	public String getCode() {
		return code;
	}

	public double getUnits() {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(units));
	}

	public double getMark() {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(mark));
	}

	public String getNotes() {
		return notes;
	}

	public double getRefTermID() {
		return refTermID;
	}

}
