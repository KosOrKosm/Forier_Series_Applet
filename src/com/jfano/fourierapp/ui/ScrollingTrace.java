package com.jfano.fourierapp.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.function.Consumer;

/**
 * Object representing a scrolling image in which a path is traced over time.
 * <br><br>
 * The trace is drawn into a dedicated <code>BufferedImage</code>. It can later be copied into any
 * other <code>Graphics2D</code> to display the trace.
 *
 * @author Jacob Fano
 */
public class ScrollingTrace {

  private static final Color clear = new Color(0, 0, 0, 0);
  private final BufferedImage input;
  private final BufferedImage buffer;
  private Graphics2D inputCurrentGraphics, bufferCurrentGraphics;

  /**
   * Creates the scrolling trace object with the given canvas size.
   *
   * @param width
   * @param height
   */
  public ScrollingTrace(int width, int height) {

    input = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    buffer = new BufferedImage(width + 5, height, BufferedImage.TYPE_INT_ARGB);

    Graphics2D win;

    win = (Graphics2D) input.getGraphics();
    win.setBackground(clear);
    win.clearRect(0, 0, width, height);
    win.dispose();

    win = (Graphics2D) buffer.getGraphics();
    win.setBackground(clear);
    win.clearRect(0, 0, width, height);
    win.dispose();

  }

  /**
   * Helper function to copy one <code>BufferedImage</code> into another.
   * <br><br>
   * <a href="https://stackoverflow.com/a/2826123">Code pulled from here</a>
   *
   * @param src source <code>BufferedImage</code>
   * @param dst destination <code>BufferedImage</code>
   * @param dx  x offset of the copy
   * @param dy  y offset of the copy
   */
  private static void copySrcIntoDstAt(final BufferedImage src,
      final BufferedImage dst, final int dx, final int dy) {
    int[] srcbuf = ((DataBufferInt) src.getRaster().getDataBuffer()).getData();
    int[] dstbuf = ((DataBufferInt) dst.getRaster().getDataBuffer()).getData();
    int width = src.getWidth();
    int height = src.getHeight();
    int dstoffs = dx + dy * dst.getWidth();
    int srcoffs = 0;
    for (int y = 0; y < height; y++, dstoffs += dst.getWidth(), srcoffs += width) {
      System.arraycopy(srcbuf, srcoffs, dstbuf, dstoffs, width);
    }
  }

  public int getWidth() {

    return input.getWidth();

  }

  public int getHeight() {

    return input.getHeight();

  }

  /**
   * Get the current contents of this trace.
   *
   * @return a <code>BufferedImage</code> containing the trace
   */
  public BufferedImage getTrace() {
    return input;
  }

  public void beginFrame() {

    inputCurrentGraphics = (Graphics2D) input.getGraphics();
    bufferCurrentGraphics = (Graphics2D) buffer.getGraphics();

  }

  public void endFrame() {

    inputCurrentGraphics.dispose();
    bufferCurrentGraphics.dispose();

  }

  /**
   * Advance the scrolling of the image by copying it, then draw the newest trace frame.
   *
   * @param frame function to draw the shape that will be traced
   */
  public void drawFrameAndAdvance(Consumer<Graphics2D> frame) {

    // Move the entire image by 1 pixel to create the scrolling effect
    copySrcIntoDstAt(input, buffer, 1, 0);

    // Draw the current trace frame
    Graphics2D win = inputCurrentGraphics;
    win.setBackground(clear);
    win.clearRect(0, 0, input.getWidth(), input.getHeight());
    frame.accept(win);

    // Add the original image back, on top of the latest trace
    win.drawImage(buffer, 0, 0, null);

  }

}
