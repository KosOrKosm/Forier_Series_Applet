package com.jfano.fourierapp.general;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public final class TextTools {

	// Gets the outline of a given string in a given font using GlyphVector.
	public static final Shape getTextOutline(FontRenderContext frc, Font font, String text, double x, double y) {

		GlyphVector outlineRaw = font.createGlyphVector(frc, text);
		Rectangle2D box = outlineRaw.getVisualBounds();
		double xOff = x - box.getX();
		double yOff = y - box.getY();
		Shape outline = outlineRaw.getOutline((float) xOff, (float) yOff);

		return outline;

	}
	
	public static final Shape getTextOutline(FontRenderContext frc, Font font, char text, double x, double y) {

		GlyphVector outlineRaw = font.createGlyphVector(frc, new char[] {text});
		Rectangle2D box = outlineRaw.getVisualBounds();
		double xOff = x - box.getX();
		double yOff = y - box.getY();
		Shape outline = outlineRaw.getOutline((float) xOff, (float) yOff);

		return outline;

	}

	// Using an int for y is fairly common...
	public static final Shape getTextOutline(FontRenderContext frc, Font font, String text, double x, int y) {

		GlyphVector outlineRaw = font.createGlyphVector(frc, text);
		Rectangle2D box = outlineRaw.getVisualBounds();
		double xOff = x - box.getX();
		double yOff = y - box.getY();
		Shape outline = outlineRaw.getOutline((float) xOff, (float) yOff);

		return outline;

	}

	public static final Shape getTextOutlineCentered(FontRenderContext frc, Font font, String text, double screenWidth,
			double y) {

		GlyphVector outlineRaw = font.createGlyphVector(frc, text);
		Rectangle2D box = outlineRaw.getVisualBounds();
		double yOff = y - box.getY();
		Shape outline = outlineRaw.getOutline((float) box.getX(), (float) yOff);
		AffineTransform mover = new AffineTransform();
		mover.translate((screenWidth - outline.getBounds().getWidth())/2, 0);
		
		return mover.createTransformedShape(outline);

	}

	// Gets the width of a block of text in standard x scale
	public static final int getTextWidth(FontRenderContext frc, Font font, String text) {

		GlyphVector outlineRaw = font.createGlyphVector(frc, text);
		Rectangle2D box = outlineRaw.getVisualBounds();

		return (int) box.getWidth();

	}

	// Gets the height of block of text in standard y scale
	public static final int getTextHeight(FontRenderContext frc, Font font, String text) {

		GlyphVector outlineRaw = font.createGlyphVector(frc, text);
		Rectangle2D box = outlineRaw.getVisualBounds();

		return (int) box.getHeight();

	}

	// Draws a block of text such that it is centered to the window
	public static void drawTextCentered(Graphics2D win, String text, double y, int screenWidth) {

		win.drawString(text, (screenWidth - getTextWidth(win.getFontRenderContext(), win.getFont(), text)) / 2,
				(int) y);

	}

	// Draw a centered text inside a generic box of arbitrary colo.
	public static void drawTextCenteredBox(Graphics2D win, String text, int y, int screenWidth, Color textColor,
			Color boxColor) {

		Color oldColor = win.getColor();

		int textWidth = getTextWidth(win.getFontRenderContext(), win.getFont(), text);
		int textHeight = getTextHeight(win.getFontRenderContext(), win.getFont(), text);

		win.setColor(boxColor);
		win.fillRect((screenWidth - textWidth) / 2 - 5, y - textHeight - 5, textWidth + 15, textHeight + 15);
		win.setColor(textColor);
		win.drawString(text, (screenWidth - textWidth) / 2, y);

		win.setColor(oldColor);
	}

}
