package com.jfano.fourierapp.math;

import com.jfano.fourierapp.math.functions.ComplexTimeFunction;

/**
 * Creates an equation that traces out the given line with domain [0, 1].
 * 
 * @author Jacob Fano
 *
 */
public class LineTimeEquation implements ComplexTimeFunction {
	
	private double x1, y1, xDif, yDif;

	public LineTimeEquation(double x1, double y1, double x2, double y2) {
		
		this.x1 = x1;
		this.y1 = y1;
		this.xDif = x2 - x1;
		this.yDif = y2 - y1;
		
	}
	
	@Override
	public Complex solveAtTime(double time) {
		return new Complex(x1 + time * xDif, y1 + time * yDif);
	}

}
