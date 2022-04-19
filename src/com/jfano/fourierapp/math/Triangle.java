package com.jfano.fourierapp.math;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Triangle implements Shape {

	private Point2D top, left, right;
	private Path2D iterator;

	public Triangle(double topX, double topY, double sizeX, double sizeY) {

		top = new Point2D.Double(topX, topY);
		left = new Point2D.Double(topX + sizeX / 2, topY + sizeY);
		right = new Point2D.Double(topX - sizeX / 2, topY + sizeY);

		iterator = new Path2D.Double();

		iterator.reset();
		iterator.moveTo(top.getX(), top.getY());
		iterator.lineTo(left.getX(), left.getY());
		iterator.lineTo(right.getX(), right.getY());
		iterator.lineTo(top.getX(), top.getY());
		iterator.closePath();

	}

	@Override
	public boolean contains(Point2D arg0) {
		return false;
	}

	@Override
	public boolean contains(Rectangle2D arg0) {
		return false;
	}

	@Override
	public boolean contains(double arg0, double arg1) {
		return false;
	}

	@Override
	public boolean contains(double arg0, double arg1, double arg2, double arg3) {
		return false;
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}

	@Override
	public Rectangle2D getBounds2D() {
		return null;
	}

	@Override
	public PathIterator getPathIterator(AffineTransform arg0) {
		return iterator.getPathIterator(arg0);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform arg0, double arg1) {
		return iterator.getPathIterator(arg0, arg1);
	}

	@Override
	public boolean intersects(Rectangle2D arg0) {
		return false;
	}

	@Override
	public boolean intersects(double arg0, double arg1, double arg2, double arg3) {
		return false;
	}

}
