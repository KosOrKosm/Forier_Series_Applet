package com.jfano.fourierapp.math;

import com.jfano.fourierapp.math.functions.ComplexTimeFunction;

public class QuadCurveTimeEquation implements ComplexTimeFunction {

  private Complex startPoint, controlPoint, endPoint;

  public QuadCurveTimeEquation(double cPx, double cPy, double p1x, double p1y, double p2x,
      double p2y) {
    startPoint = new Complex(cPx, cPy);
    controlPoint = new Complex(p1x, p1y);
    endPoint = new Complex(p2x, p2y);
  }

  @Override
  public Complex solveAtTime(double time) {

    Complex solution = controlPoint;
    solution.add(startPoint.getAdd(controlPoint.getMult(-1)).getMult((1 - time) * (1 - time)));
    solution.add(endPoint.getAdd(controlPoint.getMult(-1)).getMult(time * time));

    return solution;

  }

}
