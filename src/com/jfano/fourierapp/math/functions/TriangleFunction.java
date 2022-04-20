package com.jfano.fourierapp.math.functions;

import com.jfano.fourierapp.math.Triangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class TriangleFunction extends ShapeFunction {

  private static final Shape trace;

  static {

    Shape temp = new Triangle(0, -100, 200, 200);
    AffineTransform rot = new AffineTransform();
    rot.rotate(Math.toRadians(35));
    trace = rot.createTransformedShape(temp);

  }

  public TriangleFunction() {
    super(trace);
  }

}
