package com.amrak.gradebook;

import java.text.DecimalFormat;

import android.content.Context;
import android.database.Cursor;

public class EvalData {

	//private
	Context context;
	
	// database
	CategoriesDBAdapter categoriesDB;
	
	String title = null;
	double mark = 0;
	double outOf = 0;
	double weight = 0;
	String date = null;
	int term = 0;
	int course = 0;
	int category = 0;
	

	public EvalData(String inputTitle, double inputMark, double inputOutOf,
			double inputWeight, String inputDate, int inputTerm, int inputCourse, int inputCategory, Context inputContext) {
		// TODO Auto-generated constructor stub
		title = inputTitle;
		mark = inputMark;
		outOf = inputOutOf;
		weight = inputWeight;
		date = inputDate;
		term = inputTerm;
		course = inputCourse;
		category = inputCategory;
		context = inputContext;
		categoriesDB = new CategoriesDBAdapter(context);
	}

	// calcFromDatabase(); (have yet to write the function)

	public String getTitle() {
		return title;
	}

	public double getMark() {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(mark));
	}
	
	public double getOutOf() {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(outOf));
	}
	
	public int getOutOfNoDecimal() {
		return (int) outOf;
	}
	
	public double getWeight() {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(weight));
	}
	
	public String getDate() {
		return date;
	}
	
	public int getTermRef() {
		return term;
	}
	
	public int getCourseRef() {
		return course;
	}
	
	public int getCategoryRef() {
		return category;
	}
	
	public String getCategory() {
		categoriesDB.open();
		Cursor cCategory = categoriesDB.getCategory(category);
		cCategory.moveToFirst();
		String categoryString = cCategory.getString(cCategory.getColumnIndex("catTitle"));
		categoriesDB.close();
		return categoryString;
	}
}
