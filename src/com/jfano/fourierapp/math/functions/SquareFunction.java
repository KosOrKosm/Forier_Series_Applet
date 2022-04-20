package com.jfano.fourierapp.math.functions;

import com.jfano.fourierapp.math.Complex;

public class SquareFunction implements ComplexTimeFunction {

  @Override
  public Complex solveAtTime(double time) {

    if (time < 0.25) {

      return new Complex(time * 8 - 1, 1).getMult(100);

    } else if (time < 0.5) {

      return new Complex(1, -(time - 0.25) * 8 + 1).getMult(100);

    } else if (time < 0.75) {

      return new Complex(-(time - 0.75) * 8 - 1, -1).getMult(100);

    } else {

      return new Complex(-1, (time - 0.5) * 8 - 3).getMult(100);

    }

  }

}