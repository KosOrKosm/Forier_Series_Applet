package com.jfano.fourierapp.math.functions;

import com.jfano.fourierapp.math.Complex;

public class LemniscateEquation implements ComplexTimeFunction {

  @Override
  public Complex solveAtTime(double time) {
    double cos = Math.cos(time * Math.PI * 2);
    double sin = Math.sin(time * Math.PI * 2);
    return new Complex(
        100 * cos / (1 + Math.pow(sin, 2)),
        100 * sin * cos / (1 + Math.pow(sin, 2))
    );
  }

}
