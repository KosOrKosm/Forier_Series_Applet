package com.jfano.fourierapp.math;

import com.jfano.fourierapp.math.functions.ComplexTimeFunction;

public class CubicCurveTimeEquation implements ComplexTimeFunction {

	private Complex[] points;

	public CubicCurveTimeEquation(double cPx, double cPy, double p1x, double p1y, double p2x, double p2y, double p3x,
			double p3y) {
		
		Complex startPoint = new Complex(cPx, cPy), startPointControl = new Complex(p1x, p1y),
				endPoint = new Complex(p2x, p2y), endPointControl = new Complex(p3x, p3y);
		
		points = new Complex[] {startPoint, startPointControl, endPointControl, endPoint};
		
	}

	@Override
	public Complex solveAtTime(double time) {

		Complex solution = new Complex(0, 0);
		
		for(int i = 0; i < points.length; i++)
			solution.add(points[i].getMult(deriveMultiplier(i, time)));

		return solution;

	}

	//

	/**
	 * Derives the multiplier for the ith point of the 4 points that compose a cubic Bezier curve.
	 * The solution to the cubic Bezier cube at a given time is...
	 * 		startPoint * (1 - t)^3 + startControlPoint * 3 * (1 - t)^2 * t +
	 * 		endControlPoint * 3 * (1 - t) + t^2 + endPoint * t^3
	 *
	 * @param i the point to solve for
	 * @param time the time to solve for
	 * @return the appropriate multiplier for the given point
	 */
	private static double deriveMultiplier(int i, double time) {
		boolean isControlPoint = (i > 0 && i < 3);
		return (isControlPoint ? 3 : 1) * Math.pow(1 - time, 3 - i) * Math.pow(time, i);
	}

}
