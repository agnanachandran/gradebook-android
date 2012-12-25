package com.amrak.gradebook;

import android.content.Context;

public class TaskData {

	int taskID = -1;
	String title = null;
	String dateMade = null;
	String dateDue = null;
	String dateDueTime = null;
	Context context;	

	public TaskData(int inputTaskID, String inputTitle, String inputDateMade,
			String inputDateDue, String inputDateDueTime, Context inputContext) {
		taskID = inputTaskID;
		title = inputTitle;
		dateMade = inputDateMade;
		dateDue = inputDateDue;
		dateDueTime = inputDateDueTime;
		context = inputContext;
	}

	public double getID() {
		return taskID;
	}

	public String getTitle() {
		return title;
	}

	public String getDateMade() {
		return dateMade;
	}

	public String getDateDue() {
		return dateDue;
	}
	
	public String getDateDueTime() {
		return dateDueTime;
	}

}