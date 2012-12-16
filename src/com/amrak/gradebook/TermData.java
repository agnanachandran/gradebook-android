package com.amrak.gradebook;

import java.text.DecimalFormat;

public class TermData {

	int termID = -1;
	String title = null;
	String dateStart = null;
	String dateEnd = null;
	double mark = 0;

	public TermData(int inputTermID, String inputTitle, String inputDateStart,
			String inputDateEnd) {
		termID = inputTermID;
		title = inputTitle;
		dateStart = inputDateStart;
		dateEnd = inputDateEnd;
		mark = calcMarkFromDatabase(inputTermID);
	}

	public double calcMarkFromDatabase(int termID) {
		// TODO Write function
		double mark = 0;

		return mark;
	}

	public double getID() {
		return termID;
	}

	public String getTitle() {
		return title;
	}

	public double getMark() {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(mark));
	}

	public String getDateStart() {
		return dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

}
