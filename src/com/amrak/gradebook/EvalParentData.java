package com.amrak.gradebook;

public class EvalParentData {
	String title = null;
	int mark = 0;
	
	public EvalParentData(String inputTitle, int inputMark) {
		// TODO Auto-generated constructor stub
		title = inputTitle;
		mark = inputMark;
	}
	//getMarkFromDatabase(); (have yet to write the function)
	
    public String getTitle() {
	    return title;
    }

    public int getMark() {
	    return mark;
    }
}


