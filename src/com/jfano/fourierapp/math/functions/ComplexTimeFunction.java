package com.jfano.fourierapp.math.functions;

import com.jfano.fourierapp.math.Complex;

/**
 * A function which takes a time value as input and returns a <code>Complex</code>
 *
 * @author Jacob Fano
 * @see Complex
 * @see ShapeFunction
 */
public interface ComplexTimeFunction {

  /**
   * Solves this <code>ComplexTimeFunction</code> at the given time
   *
   * @param time the time value used as input to the function
   * @return the <code>Complex</code> solution
   * @see Complex
   */
  Complex solveAtTime(double time);

}
