package com.jfano.fourierapp.state;

import com.jfano.fourierapp.general.Drawable;
import com.jfano.fourierapp.math.Complex;
import com.jfano.fourierapp.math.functions.ComplexTimeFunction;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.function.Function;

/**
 * Class representing a set of spinners.
 * These spinners have lengths and periods derived from a Fourier Series.
 * The Fourier Series and Spinners are automatically calculated from various <code>ComplexTimeFunction</code>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Fourier_series">Fourier Series</a>
 * @see Spinner
 * @author Jacob Fano
 */
public class FourierSeriesSpinners implements Drawable {

	private Spinner[] series;
	private double time, penSize;
	private boolean hideSmallSpinners = true;
	
	private Complex currentSolution;

	// Common shapes for drawing
	private Font font = new Font("TimesNewRoman", Font.PLAIN, 10);
	private static final Line2D draw = new Line2D.Double();
	private static final Ellipse2D dot = new Ellipse2D.Double();
	private static final Ellipse2D pen = new Ellipse2D.Double();

	/**
	 * Constructs a simple, dummy Fourier series with some <code>Spinner</code>s of
	 * various lengths that are initially flat in the +real axis.
	 * 
	 * @param seriesLength
	 */
	public FourierSeriesSpinners(int seriesLength) {

		series = new Spinner[seriesLength];

		for (int i = -seriesLength; i < series.length; i++) {

			series[i] = new Spinner(new Complex(100 / (i + 1), 0), i);

		}
		
		currentSolution = solve();

	}

	/**
	 * Constructs a Fourier series with some <code>Spinner</code>s that, when
	 * solved, attempts a 1-to-1 approximation the input/outputs of the given
	 * <code>ComplexTimeFunction</code>.
	 *
	 * @param goal the function to approximate
	 * @param seriesLength the length of Fourier Series (and thus, the number of spinners)
	 * @param iterations the number of iterations used to approximate integrations
	 */
	public FourierSeriesSpinners(ComplexTimeFunction goal, int seriesLength, int iterations) {

		genSpinners(goal, seriesLength, iterations);

	}

	/**
	 * Constructs a Fourier series with some <code>Spinner</code>s that, when
	 * solved, attempts a 1-to-1 approximation the input/outputs of the given
	 * <code>ComplexTimeFunction</code>.
	 *
	 * @param goal the function to approximate
	 * @param seriesLength the length of Fourier Series (and thus, the number of spinners)
	 * @param iterations the number of iterations used to approximate integrations
	 */
	private void genSpinners(ComplexTimeFunction goal, int seriesLength, int iterations) {

		series = new Spinner[seriesLength];
		Spinner[] temp = new Spinner[seriesLength];

		// Generates the series such that the N value of the spinners in the array goes
		// [-seriesLength, ..., 0, ..., seriesLength - 1]
		// This order is simpler to generate, since the N value that must be provided to
		// deriveConstant and the Spinner object are advanced through linearly.

		for (int i = 0; i < seriesLength; i++) {

			int spinnerID = i - seriesLength / 2;
			temp[i] = new Spinner(deriveConstant(goal, spinnerID, iterations), spinnerID);

		}

		// Re-orders the series such that the N value of the spinners in the array goes
		// [0, 1, -1, 2, -2, 3, -3, ...]
		// This is simply a more visual appealing look to the series, as well as giving
		// a better intuitive sense of why each spinner has the settings it does just by
		// looking at the output.
		// (ex. 0 is at the function's average value; oblivious in this ordering.)

		// Spinner with N of 0 is halfway through the original array.
		series[0] = temp[seriesLength / 2];

		// Last spinner must be manually assigned, since it isn't in a pair.
		series[seriesLength - 1] = temp[seriesLength - 1];

		// temp[seriesLength / 2 + 1] = series[1],
		// temp[seriesLength / 2 - 1] = series[2],
		// temp[seriesLength / 2 + 2] = series[3],
		// temp[seriesLength / 2 - 2] = series[4],
		// etc.
		for (int pos = 1; pos < seriesLength / 2; ++pos) {
			series[pos * 2 - 1] = temp[seriesLength / 2 + pos];
			series[pos * 2] = temp[seriesLength / 2 - pos];
		}

		currentSolution = solve();

	}

	/**
	 * Derives the constant of the nth spinner in a Fourier series with the given
	 * <code>ComplexTimeFunction</code> goal function using the given number of
	 * iterations per integral.
	 * <p>
	 * <b>Assumption:</b> goal function's domain is [0, 1]
	 * 
	 * @param modelFunction the function the spinner should model
	 * @param termToSolveFor position of the spinner to solve for in its Fourier series
	 * @param iterations number of iterations to use to numerically approximate integrals
	 * @return the <code>Complex</code> constant of this spinner, encoding its base
	 *         position and length
	 */
	private static final Complex deriveConstant(ComplexTimeFunction modelFunction, int termToSolveFor, int iterations) {

		double radiansPerCycle = 2 * Math.PI * termToSolveFor;

		// Calculate ∫[0,1] (f(t) * e^Complex(0,-radians*t))
		return integrate(iterations, time ->
				modelFunction.solveAtTime(time)
				.getMult(Complex.eExponent(new Complex(0, -radiansPerCycle * time)))
		);

	}

	/**
	 * Perform an approximation of integration via summation on this Complex.
	 * In particular, the approximated integral is over the domain [0, 1].
	 *
	 * @param interations the number of iterations used. More iterations = more accuracy approximation
	 * @param perstep the function to integrate. Must take a time value in domain [0, 1] as input
	 * @return approximation of the perstep function's integral on the domain [0, 1]
	 */
	private static Complex integrate(int interations, Function<Double, Complex> perstep) {
		double deltaT = 1.0 / interations;
		return summation(interations, iteration -> perstep.apply(iteration * deltaT).getMult(deltaT));
	}

	/**
	 * Performs a summation on the given function.
	 *
	 * @param n the number of iterations
	 * @param perstep the function to be summed
	 * @return the sum
	 */
	private static Complex summation(int n, Function<Integer, Complex> perstep) {
		Complex ret = new Complex(0, 0);
		for(int i = 0; i < n; i++) ret.add(perstep.apply(i));
		return ret;
	}

	/**
	 * Calculates the solution to the Fourier Series represented by this object
	 * at the current time.
	 * @return the solution at the current time
	 */
	private Complex solve() {
		return summation(series.length, i -> series[i].solveAtTime(time));
	}

	public void setPenSize(double size) {

		penSize = size;

	}

	public void setTime(double t) {
		this.time = t;
		currentSolution = solve();
	}

	@Override
	public void draw(Graphics2D win) {

		AffineTransform oldTrans = win.getTransform();

		if(font.getSize() != (int) Math.round(penSize * 2)) {
			font = font.deriveFont((float) penSize * 2);
		}

		dot.setFrame(-penSize / 2, -penSize / 2, penSize, penSize);

		win.setFont(font);

		// Draw each spinner in the series
		for (int i = 0; i < series.length; i++) {

			// Calculate the spinner's current target position.
			Spinner spinner = series[i];
			Complex solution = spinner.solveAtTime(time);

			// SPINNER BODY
			// Draw a line from the last spinner's endpoint to the current spinner's endpoint
			win.setColor(Color.BLACK);
			draw.setLine(0, 0, solution.getReal(), solution.getImaginary());
			if (i > 0)
				win.draw(draw);
			
			// SPINNER HEAD
			// Move to the endpoint of the current spinner
			win.translate(solution.getReal(), solution.getImaginary());

			// Draw a dot to represent the head of the current spinner
			if (i == series.length - 1)
				win.setColor(Color.RED);
			else
				win.setColor(Color.ORANGE);
			win.fill(dot);
			
			// SPINNER LABEL
			// Draw a textual label for the spinner
			win.setColor(Color.BLACK);
			if (!hideSmallSpinners || hideSmallSpinners && solution.getLength() > 1 || i == series.length - 1)
				win.drawString(Integer.toString(i),
						(int) (Math.cos(Math.PI / 4 * i) * font.getSize() * 0.75 - font.getSize() / 4),
						(int) (-Math.sin(Math.PI / 4 * i) * font.getSize() + font.getSize() / 2));

		}

		// Restore the original transformation state after finishing
		win.setTransform(oldTrans);

	}

	public void drawCurrentPoint(Graphics2D win) {

		double x = currentSolution.getReal(), y = currentSolution.getImaginary();
		win.translate(x, y);

		pen.setFrame(-penSize / 8, -penSize / 8, penSize / 4, penSize / 4);
		win.fill(pen);

	}

	/**
	 * Solves the Fourier series this class contains at the last
	 * <code>setTime()</code>'s value. This is equivalent to finding the endpoint of
	 * every <code>Spinner</code> in the array and then adding them all together.
	 * 
	 * @return the solution to the Fourier series at the current time
	 */
	public Complex getSolution() {
		return currentSolution;
	}

	public int seriesLength() {
		return series.length;
	}

	public void hideSmallSpinners(boolean set) {
		hideSmallSpinners = set;
	}

}
