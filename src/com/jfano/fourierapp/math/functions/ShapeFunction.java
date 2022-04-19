package com.jfano.fourierapp.math.functions;

import com.jfano.fourierapp.math.Complex;
import com.jfano.fourierapp.math.CubicCurveTimeEquation;
import com.jfano.fourierapp.math.LineTimeEquation;
import com.jfano.fourierapp.math.QuadCurveTimeEquation;
import com.jfano.fourierapp.path.JavaFX_Path2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates a function F from a shape such that F has domain [0, 1] and outputs
 * complex numbers representing points along the perimeter of the shape, fully
 * tracing the shape if every value of F were to be drawn at once.
 * <p>
 * <b>TODO:</b> Only supports lines and points at the moment.
 * 
 * @author Jacob Fano
 *
 */
public class ShapeFunction implements ComplexTimeFunction {

	private ComplexTimeFunction[] lines = new ComplexTimeFunction[0];

	/**
	 * Build this <code>ShapeFunction</code> from the given <code>PathIterator</code>.
	 * The generated function will trace the path of the <code>PathIterator</code>.
	 * <br><br>
	 * This is done by deconstructing the <code>PathIterator</code> into its component equations,
	 * then "gluing" those equations together to form a single function.
	 * @param points the <code>PathIterator</code> to model
	 */
	private void init(PathIterator points) {

		List<ComplexTimeFunction> dynLines = new ArrayList<ComplexTimeFunction>();

		float[] buffer = new float[6];
		Point2D last = new Point2D.Double(), current = new Point2D.Double();

		while (!points.isDone()) {

			switch (points.currentSegment(buffer)) {

			case PathIterator.SEG_CLOSE:

				// System.out.println("close");

				break;

			case PathIterator.SEG_MOVETO:

				// System.out.println("move to ["+buffer[0]+","+buffer[1]+"]");

				current = new Point2D.Double(buffer[0], buffer[1]);

				break;

			case PathIterator.SEG_LINETO:

				// System.out.println("line to ["+buffer[0]+","+buffer[1]+"]");

				current = new Point2D.Double(buffer[0], buffer[1]);

				dynLines.add(new LineTimeEquation(last.getX(), last.getY(), current.getX(), current.getY()));

				break;

			case PathIterator.SEG_QUADTO:

				current = new Point2D.Double(buffer[2], buffer[3]);

				dynLines.add(new QuadCurveTimeEquation(last.getX(), last.getY(), buffer[0], buffer[1], current.getX(),
						current.getY()));

				break;

			case PathIterator.SEG_CUBICTO:

				current = new Point2D.Double(buffer[4], buffer[5]);

				dynLines.add(new CubicCurveTimeEquation(last.getX(), last.getY(), buffer[0], buffer[1], current.getX(),
						current.getY(), buffer[2], buffer[3]));

				break;

			default:

				throw new IllegalStateException();

			}

			last = current;

			points.next();

		}

		lines = dynLines.toArray(lines);

	}

	/**
	 * Initialize this shape function from a <code>PathIterator</code>.
	 * The generated function will trace the path of the <code>PathIterator</code>.
	 *
	 * @param points the path to trace
	 */
	public ShapeFunction(PathIterator points) {

		init(points);

	}

	/**
	 * Initialize this shape function from a <code>Shape</code>.
	 * The generated function will trace the path of the <code>Shape</code>.
	 *
	 * @param s the shape to trace
	 */
	public ShapeFunction(Shape s) {
		this(s.getPathIterator(null));
	}

	/**
	 * Initialize this shape function from a SVG file.
	 * Configures the function such that it traces the path contained
	 * within the SVG file.
	 *
	 * @param svgpath the path to the target SVG file
	 * @throws IOException
	 */
	public ShapeFunction(String svgpath) throws IOException {

		String result = "";
		boolean found = false, done = false;

		try (BufferedReader lineReader = new BufferedReader(
				new InputStreamReader(ShapeFunction.class.getResourceAsStream(svgpath)))) {

			for (String line; (line = lineReader.readLine().replaceAll("\\s+", "")) != null && !done;) {

				if (!found && line.startsWith("<path")) {

					found = true;
					line = line.substring(line.indexOf("d=\"") + 3);

				}
				if (found) {

					if (line.endsWith("/>")) {

						done = true;
						line = line.substring(0, line.lastIndexOf("/>") - 1);

					}

					result += line;

				}

			}

		}

		// Generate a Path2D from the SVG using JavaFX's Path2D class
		JavaFX_Path2D svg = new JavaFX_Path2D();
		svg.appendSVGPath(result);

		// Create a transform matrix to adjust the origin point and scaling of the generated path
		Rectangle2D bounds = svg.getBounds2D();
		AffineTransform adjust = new AffineTransform();
		adjust.translate(-bounds.getWidth(), -bounds.getHeight());
		adjust.scale(2, 2);

		// Create a formula from the final path
		init(adjust.createTransformedShape(svg).getPathIterator(null));
		
	}

	@Override
	public Complex solveAtTime(double time) {

		int line = (int) (time * lines.length);

		return lines[line].solveAtTime((time - (double) line / lines.length) * lines.length);
	}

}
