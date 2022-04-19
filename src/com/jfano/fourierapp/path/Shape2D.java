package com.jfano.fourierapp.path;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Shape2D {
	
	private Shape oShape;
	protected double curX = 0, curY = 0;
	
	public Shape2D(Shape in) {
		
		oShape = in;
		
	}
	
	protected void setShape(Shape in) {
		
		oShape = in;
		
	}
	
	public Shape scale(double factorX, double factorY) {
		
		AffineTransform scaler = new AffineTransform();
		scaler.scale(factorX, factorY);
		
		oShape = scaler.createTransformedShape(oShape);
		return oShape;
		
	}
	
	public Shape translate(double dx, double dy) {
		
		AffineTransform mover = new AffineTransform();
		mover.translate(dx, dy);
		curX += dx;
		curY += dy;
		
		oShape = mover.createTransformedShape(oShape);
		return oShape;
		
	}
	
	protected void setPos(Point2D pos) {
		
		AffineTransform mover = new AffineTransform();
		mover.translate(pos.getX() - curX, pos.getY() - curY);
		curX = pos.getX();
		curY = pos.getY();
		
		oShape = mover.createTransformedShape(oShape);
		
	}
	
	public Shape setPosGetShape(double x, double y) {
		
		AffineTransform mover = new AffineTransform();
		mover.translate(x - curX, y - curY);
		curX = x;
		curY = y;
		
		oShape = mover.createTransformedShape(oShape);
		return oShape;
		
	}
	
	public Shape rotate(double rot) {
		
		AffineTransform mover = new AffineTransform();
		mover.rotate(rot);
		
		oShape = mover.createTransformedShape(oShape);
		return oShape;
		
	}
	
	public Shape rotateAboutOrigin(double rot) {
		
		AffineTransform mover = new AffineTransform();
		mover.rotate(rot, -curX, -curY);
		
		oShape = mover.createTransformedShape(oShape);
		return oShape;
		
	}
	
	public Shape rotateAboutCenter(double rot) {
		
		double originalX = curX; double originalY = curY;
		
		setPos(new Point2D.Double(-oShape.getBounds2D().getWidth()/2, -oShape.getBounds2D().getHeight()/2));
		rotate(rot);
		setPos(new Point2D.Double(originalX, originalY));
		
		return oShape;
		
	}
	
	public Shape get() {
		
		return oShape;
		
	}
	
	public double getX() {
		
		return curX;
		
	}
	
	public double getY() {
		
		return curY;
		
	}
	
	public void setX(double x) {

		setPos(new Point2D.Double(x, curY));

	}

	public void setY(double y) {

		setPos(new Point2D.Double(curX, y));
		
	}
	
}
