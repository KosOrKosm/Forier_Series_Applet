package com.jfano.fourierapp.math;

/**
 * Represents a <b>complex number</b>. This class also contains various methods for performing
 * mathmatical operations involving complex numbers.
 * <br><br>
 * A complex number is a type of number with two components; real and imaginary. Complex numbers
 * occupy complex number space, a 2D field.
 *
 * @author Jacob Fano
 */
public class Complex {

  private double real, img;

  /**
   * Create a complex number with the given components
   *
   * @param real the real component of the complex number
   * @param img  the imaginary component of the complex number
   */
  public Complex(double real, double img) {
    this.real = real;
    this.img = img;
  }

  /**
   * Calculates e^pow, where pow is a complex number and
   * <a href="https://en.wikipedia.org/wiki/E_(mathematical_constant)">e is Euler's Number</a>
   *
   * @param pow the complex exponent
   * @return the result of raising e to the power of pow
   */
  public static Complex eExponent(Complex pow) {
    double scale = Math.exp(pow.real);
    return new Complex(scale * Math.cos(pow.img), scale * Math.sin(pow.img));
  }

  /**
   * Add two complex numbers, storing the sum in this complex number
   *
   * @param other the complex number to add
   */
  public void add(Complex other) {
    this.real += other.real;
    this.img += other.img;
  }

  /**
   * Add two complex numbers, creating a new complex number to store the sum
   *
   * @param other the complex number to add
   * @return the sum
   */
  public Complex getAdd(Complex other) {
    return new Complex(
        this.real + other.real,
        this.img + other.img
    );
  }

  /**
   * Multiplies two complex numbers, storing the product in this complex number
   *
   * @param other the complex number to multiply
   */
  public void mult(Complex other) {
    this.real = real * other.real - img * other.img;
    this.img = img * other.real + real * other.img;
  }

  /**
   * Multiplies two complex numbers, creating a new complex number to store the product
   *
   * @param other the complex number to multiply
   * @return the product
   */
  public Complex getMult(Complex other) {
    return new Complex(
        real * other.real - img * other.img,
        img * other.real + real * other.img
    );
  }

  /**
   * Multiplies this complex number by a real number
   *
   * @param factor the real multiplier
   */
  public void mult(double factor) {
    this.real *= factor;
    this.img *= factor;
  }

  /**
   * Multiplies this complex number by a real number, creating a new complex number to store the
   * product
   *
   * @param factor the real multiplier
   * @return the product
   */
  public Complex getMult(double factor) {
    return new Complex(this.real * factor, this.img * factor);
  }

  /**
   * Get the length of this complex number in imaginary space.
   *
   * @return the length
   */
  public double getLength() {
    return Math.hypot(real, img);
  }

  /**
   * Get the real component of this complex number
   *
   * @return the real component
   */
  public double getReal() {
    return real;
  }

  /**
   * Get the imaginary component of this complex number
   *
   * @return the imaginary component
   */
  public double getImaginary() {
    return img;
  }

  @Override
  public String toString() {
    return "Complex [real=" + real + ", img=" + img + "]";
  }

}
