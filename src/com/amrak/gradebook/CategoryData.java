package com.amrak.gradebook;

import java.text.DecimalFormat;

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
	EvaluationsDBAdapter evalsDB;
	
	public CategoryData(int inputCategoryID, String inputTitle, int inputWeight, int inputRefCourseID, int inputRefTermID, Context inputContext) {
		categoryID = inputCategoryID;
		title = inputTitle;
		weight = inputWeight;
		course = inputRefCourseID;
		term = inputRefTermID;
		context = inputContext;
		evalsDB = new EvaluationsDBAdapter(context);
		mark = calcMarkFromDatabase(inputRefCourseID, inputCategoryID);
	}
	
	public double calcMarkFromDatabase (int course, int categoryID){
		double mark = 0;
		int totalWeight = 0;
		
		evalsDB.open();
		Cursor c = evalsDB.getEvaluationsOfCategory(categoryID);
		if (c.moveToFirst()) {
			do {
				totalWeight += c.getInt(c.getColumnIndex("evalWeight"));
			} while(c.moveToNext());
		}
		
		if (c.moveToFirst()) {
			do {
				double weightFraction = (double)c.getInt(c.getColumnIndex("evalWeight"))/totalWeight;
				mark += 100*weightFraction*(double)c.getInt(c.getColumnIndex("evalMark"))/c.getInt(c.getColumnIndex("evalOutOf"));
			} while(c.moveToNext());
		}
		//System.out.println(mark);
		
		evalsDB.close();
		
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
