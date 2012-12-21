package com.amrak.gradebook;

import java.text.DecimalFormat;

import android.content.Context;

public class CategoryData {
	
	Context context;
	
	int categoryID = -1;
	double weight = 0;
	String title = null;
	double mark = 0;
	int course = 0;
	int term = 0;
	
	public CategoryData(int inputCategoryID, String inputTitle, int inputWeight, int inputRefCourseID, int inputRefTermID, Context inputContext) {
		categoryID = inputCategoryID;
		title = inputTitle;
		weight = inputWeight;
		mark = calcMarkFromDatabase(inputRefCourseID, inputCategoryID);
		course = inputRefCourseID;
		term = inputRefTermID;
		context = inputContext;

	}
	
	public double calcMarkFromDatabase (int course, int categoryID){
		double mark = 0;
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
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(mark));
	}
	
	public int getRefCourseID (){
		return course;
	}
	
	public int getRefTermID(){
		return term;
	}
}
