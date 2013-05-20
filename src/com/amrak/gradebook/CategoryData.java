package com.amrak.gradebook;

import android.content.Context;
import android.database.Cursor;

public class CategoryData {
	
	Context context;
	
	int categoryID = -1;
	double weight = 0;
	String title = null;
	double mark = 0;
	int course = 0;
	int term = 0;
	int color = 0;
	double average = 100.00;
	CategoriesDBAdapter categoriesDB;
	
	public CategoryData(int inputCategoryID, String inputTitle, double inputWeight, int inputRefCourseID, int inputRefTermID, int inputColor, Context inputContext) {
		categoryID = inputCategoryID;
		title = inputTitle;
		weight = inputWeight;
		course = inputRefCourseID;
		term = inputRefTermID;
		color = inputColor;
		context = inputContext;
		categoriesDB = new CategoriesDBAdapter(context);
		mark = calcMarkFromDatabase(inputRefCourseID, inputCategoryID);
	}
	
	public double calcMarkFromDatabase (int course, int categoryID){
		double mark = 0;

		categoriesDB.open();
		Cursor c = categoriesDB.getCategory(categoryID);
		if (c.getCount() == 0)
			mark = 100.00;
		else 
			mark = c.getDouble(c.getColumnIndex("catAverage"));	

		categoriesDB.close();
		return mark;

	}
	
	public double getID(){
		return categoryID;
	}
	
	public String getTitle(){
		return title;
	}
	
	public double getWeight(){
		return weight;
	}
	
	public double getMark(){
		return mark;
	}
	
	public int getRefCourseID (){
		return course;
	}
	
	public int getRefTermID(){
		return term;
	}
	
	public int getColor() {
		return color;
	}
}
