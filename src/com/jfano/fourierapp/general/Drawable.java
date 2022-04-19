package com.jfano.fourierapp.general;

import java.awt.Graphics2D;

/**
 * Generic interface representing all "drawable" objects.
 * "Drawable" objects have code to render to a <code>Graphics2D</code> object.
 *
 * @author Jacob Fano
 */
public interface Drawable {

    /**
     * Performs this object's drawing routine using the given <code>Graphics2D</code>
     * @param context the draw context
     */
    void draw(Graphics2D context);

}
