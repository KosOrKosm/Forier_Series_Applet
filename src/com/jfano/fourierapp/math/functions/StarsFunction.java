package com.jfano.fourierapp.math.functions;

import com.jfano.fourierapp.path.Polygon2D;
import java.awt.Shape;

public class StarsFunction extends ShapeFunction {

  private static final Shape trace;

  static {

    double[] x = new double[]{-5, -1, 0, 1, 5, 5, 6, 8.5, 6, 5, 4, 1.5, 4, 5, 5, 1, 0, -1, -5};
    double[] y = new double[]{0, 1, 5, 1, 0, 2, 4.25, 5.5, 6.75, 9, 6.75, 5.5, 4.25, 2, 0, -1, -5,
        -1, 0};

    for (int i = 0; i < x.length; i++) {

      y[i] *= -1;
      x[i] -= 1;
      y[i] += 2;
      x[i] *= 22;
      y[i] *= 22;

    }

    trace = new Polygon2D(x, y, x.length);

  }

  public StarsFunction() {
    super(trace);
  }

}
