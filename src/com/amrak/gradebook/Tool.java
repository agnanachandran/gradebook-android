package com.amrak.gradebook;

import java.util.Vector;

/**
 * 
 * @author Chronos
 * 
 */
public class Tool {

	/**
	 * 
	 * Does average, etc.
	 * 
	 * @param args
	 */
	public static double getAverage(Vector <Double> x) {
		double sum = 0;

		for (int i = 0; i < x.size(); i++) {
			sum += x.get(i);
		}

		return sum / x.size();

	}

}
