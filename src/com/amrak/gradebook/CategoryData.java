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
	DecimalFormat twoDForm = new DecimalFormat("0.00");
	
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
		
		//default mark is 100.00
		if (c.getCount() == 0)
			return 100.00;
		
		if (c.moveToFirst()) {
			do {
				totalWeight += c.getInt(c.getColumnIndex("evalWeight"));
			} while(c.moveToNext());
		}
		
		//When the user leaves weight blank
		if (totalWeight == 0) {
			int count = c.getCount();
			if (c.moveToFirst()) {
				do {
					mark += 100*c.getDouble(c.getColumnIndex("evalMark"))/c.getDouble(c.getColumnIndex("evalOutOf"))/count;
				} while(c.moveToNext());
			}
		}
		else {			
			if (c.moveToFirst()) {
				do {
					double weightFraction = c.getDouble(c.getColumnIndex("evalWeight"))/totalWeight;
					mark += 100*weightFraction*c.getDouble(c.getColumnIndex("evalMark"))/c.getDouble(c.getColumnIndex("evalOutOf"));
				} while(c.moveToNext());
			}			
		}
		
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
		return Double.valueOf(twoDForm.format(mark));
	}
	
	public int getRefCourseID (){
		return course;
	}
	
	public int getRefTermID(){
		return term;
	}
}
