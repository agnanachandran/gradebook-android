package com.amrak.gradebook;

import java.text.DecimalFormat;

import android.content.Context;

public class CourseData {

	int courseID = -1;
	String title = null;
	String code = null;
	double units = 0;
	double mark = 0;
	String notes = null;
	int refTermID = -1;
	
	Context context;
	EvaluationsDBAdapter evalsDB;

	public CourseData(int inputCourseID, String inputTitle, String inputCode,
			double inputUnits, String inputNotes, int inputRefTermID, Context inputContext) {
		courseID = inputCourseID;
		title = inputTitle;
		code = inputCode;
		units = inputUnits;
		mark = calcMarkFromDatabase(inputCourseID);
		notes = inputNotes;
		refTermID = inputRefTermID;
		context = inputContext;
		evalsDB = new EvaluationsDBAdapter(context);

	}

	public double calcMarkFromDatabase(int courseID) {
		// TODO Write function
		double mark = 0;

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
