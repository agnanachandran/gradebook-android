package com.amrak.gradebook;

public class EvalChildData {
	String data1 = null;
	String data2 = null;
	
	//can add other data
	
	public EvalChildData(String inputCourse, String inputDate) {
		// TODO Auto-generated constructor stub
		data1 = inputCourse;
		data2 = inputDate;
	}

    public String getData1() {
	    return data1;
    }

    public String getData2() {
	    return data2;
    }
	
}
