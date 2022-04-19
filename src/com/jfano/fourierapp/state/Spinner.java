package com.jfano.fourierapp.state;

import com.jfano.fourierapp.math.Complex;
import com.jfano.fourierapp.math.functions.ComplexTimeFunction;

/**
 * Object representing a spinner. A spinner is a time function which
 * traces a circle in complex space.
 * <br><br>
 * Each spinner is defined its length and speed (aka period).
 *
 * @author Jacob Fano
 */
public class Spinner implements ComplexTimeFunction {
	
	private Complex length, basePow;

	/**
	 * Generates corresponding to a particular sequence in a Fourier Series
	 * The period of the spinner is determined by its position in the series.
	 * @param length
	 * @param n
	 */
	public Spinner(Complex length, int n) {
		
		this.length = length;
		this.basePow = new Complex(0, n * 2 * Math.PI);
		
	}

	/**
	 * Calculates the complex number representing this spinner at a given point in its period.
	 * @param time the time
	 * @return the complex number representing the spinner
	 */
	public Complex solveAtTime(double time) {
		
		return length.getMult(Complex.eExponent(basePow.getMult(time)));
		
	}

}
