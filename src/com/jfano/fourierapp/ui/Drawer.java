package com.jfano.fourierapp.ui;

import com.jfano.fourierapp.general.Counter;
import com.jfano.fourierapp.general.Driver;
import com.jfano.fourierapp.general.TextTools;
import com.jfano.fourierapp.math.Complex;
import com.jfano.fourierapp.math.functions.ComplexTimeFunction;
import com.jfano.fourierapp.math.functions.ShapeFunction;
import com.jfano.fourierapp.state.FourierSeriesSpinners;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Drawer extends Driver {

	// Drawer Configuration
    private static final int
            STEPS_PER_FRAME = 18,
            STEPS_PER_CYCLE = 9000,
            INTEGRATION_ACCURACY = 800,
            WAVE_TRACKER_WIDTH = 512,
            WAVE_TRACKER_COUNT = 2,
            WAVE_TRACKER_BUFFER_FRACTION = 16;

    // Drawables
    private final double ZOOM_FACTOR = 2;
    private final Stroke ZOOM_COMPENSATED = new BasicStroke(4 / (float) ZOOM_FACTOR);
    private final Font BASIC = new Font("TimesNewRoman", Font.PLAIN, 20), BOLD = new Font("TimesNewRoman", Font.BOLD, 20);
    private final Ellipse2D PEN = new Ellipse2D.Double();

	private final Counter time = new Counter(STEPS_PER_CYCLE);
    private final ComplexTimeFunction func;
    private final FourierSeriesSpinners spinners;
    private final BufferedImage pathTraceBuffer, pathGoalBuffer;
    private final ScrollingTrace xWaveTracker, yWaveTracker, xIdealTracker, yIdealTracker;
    private final int mainWindowWidth;

    private double
			maxReal = -Double.MAX_VALUE,
			minReal = Double.MAX_VALUE,
			maxImg = -Double.MAX_VALUE,
			minImg = Double.MAX_VALUE;

    public Drawer() throws IOException {
        super(60, 60, "Fourier Series Generator", true);

        this.width = 512 * 5 / 2;
        this.height = 512 * 3 / 2;
        this.mainWindowWidth = width - WAVE_TRACKER_WIDTH;

        func = new ShapeFunction("/sample_shapes/H.svg");

        final int seriesLength = 20;
        assert seriesLength % 2 == 0 : "Length not a multiple of 2";

        spinners = new FourierSeriesSpinners(func, seriesLength, INTEGRATION_ACCURACY);
        spinners.setPenSize(12 / ZOOM_FACTOR);

        xWaveTracker = new ScrollingTrace(STEPS_PER_CYCLE / STEPS_PER_FRAME, this.height / WAVE_TRACKER_COUNT);
        yWaveTracker = new ScrollingTrace(STEPS_PER_CYCLE / STEPS_PER_FRAME, this.height / WAVE_TRACKER_COUNT);
        xIdealTracker = new ScrollingTrace(STEPS_PER_CYCLE / STEPS_PER_FRAME, this.height / WAVE_TRACKER_COUNT);
        yIdealTracker = new ScrollingTrace(STEPS_PER_CYCLE / STEPS_PER_FRAME, this.height / WAVE_TRACKER_COUNT);

        pathTraceBuffer = new BufferedImage(this.mainWindowWidth, this.height, BufferedImage.TYPE_4BYTE_ABGR);
        pathGoalBuffer = new BufferedImage(this.mainWindowWidth, this.height, BufferedImage.TYPE_4BYTE_ABGR);

        // Tracing the original function to generate a "goal" path to display

        // Create the traceBuffer to store the trace, so the screen can be cleared without
        // having to re-trace the whole path every frame
        Graphics2D traceBuffer = (Graphics2D) pathGoalBuffer.getGraphics();
        zoom_compensate(traceBuffer);
        traceBuffer.setColor(Color.GREEN);

        // Perform the trace, storing results into the traceBuffer.
        for (int i = 0; i < STEPS_PER_CYCLE; i++) {

            Complex solution = func.solveAtTime(i / (double) STEPS_PER_CYCLE);

            if (maxReal < solution.getReal()) maxReal = solution.getReal();
            if (minReal > solution.getReal()) minReal = solution.getReal();
            if (maxImg < solution.getImaginary()) maxImg = solution.getImaginary();
            if (minImg > solution.getImaginary()) minImg = solution.getImaginary();

            PEN.setFrame(solution.getReal() - 0.5 * ZOOM_FACTOR, solution.getImaginary() - 0.5 * ZOOM_FACTOR,
                    5 / ZOOM_FACTOR, 5 / ZOOM_FACTOR);

            traceBuffer.fill(PEN);
        }

        traceBuffer.dispose();

    }

    @Override
    public void draw(Graphics2D win) {

        AffineTransform oldTrans = win.getTransform();

        // TRACED PATHS

        win.drawImage(pathGoalBuffer, 0, 0, null);
        win.drawImage(pathTraceBuffer, 0, 0, null);

        // SPINNERS

        zoom_compensate(win);
        win.setColor(Color.BLACK);

        spinners.draw(win);

        win.setTransform(oldTrans);

        // -----------------------------------------------UI--------------------------------------------------

        win.translate(this.width, 0);

        // WAVEFORM TRACKERS
        oldTrans = win.getTransform();
        win.scale(-WAVE_TRACKER_WIDTH / (double) xWaveTracker.getWidth(), 1);
        win.drawImage(xIdealTracker.getTrace(), 0, 0, null);
        win.drawImage(xWaveTracker.getTrace(), 0, 0, null);
        win.setTransform(oldTrans);

        win.translate(0, getWaveTrackerHeight());
        oldTrans = win.getTransform();
        win.scale(-WAVE_TRACKER_WIDTH / (double) yWaveTracker.getWidth(), 1);
        win.drawImage(yIdealTracker.getTrace(), 0, 0, null);
        win.drawImage(yWaveTracker.getTrace(), 0, 0, null);
        win.setTransform(oldTrans);

        win.translate(-WAVE_TRACKER_WIDTH, -getWaveTrackerHeight());

        // The most expensive rendering operations are performed without antialiasing for performance.
        win.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // WAVEFORM TRACKER TEXT AND DIVIDERS
        win.setColor(Color.WHITE);
        win.fillRect(0, -5, TextTools.getTextWidth(win.getFontRenderContext(), BASIC, "X Waveform Comparison") + 10,
                BASIC.getSize() + 10);
        win.fillRect(0, getWaveTrackerHeight() - 5,
                TextTools.getTextWidth(win.getFontRenderContext(), BASIC, "Y Waveform Comparison") + 10,
                BASIC.getSize() + 10);
        win.setColor(Color.BLACK);
        win.setFont(BASIC);
        win.drawString("X Waveform Comparison", 5, BASIC.getSize() + 2);
        win.drawString("Y Waveform Comparison", 5, getWaveTrackerHeight() + BASIC.getSize() + 2);

        win.translate(-this.mainWindowWidth, 0);
        win.drawLine(this.mainWindowWidth, 0, this.mainWindowWidth, this.height);
        win.drawLine(this.mainWindowWidth, getWaveTrackerHeight(), this.width, getWaveTrackerHeight());

        // Numeric Settings Box
        win.setFont(BASIC);
        win.translate(this.mainWindowWidth / 32, this.height / 32);
        win.setColor(Color.WHITE);
        win.fillRect(0, 0, this.mainWindowWidth / 3, this.height / 6);
        win.setColor(Color.BLACK);
        win.drawRect(0, 0, this.mainWindowWidth / 3, this.height / 6);

        // Numeric Settings Box Text
        win.drawString("# of Spinners: " + spinners.seriesLength(), 5, BASIC.getSize() + 2);
        win.translate(0, BASIC.getSize() + 5);
        win.drawString("Iterations per Integral: " + INTEGRATION_ACCURACY, 5, BASIC.getSize() + 2);
        win.translate(0, BASIC.getSize() + 5);
        win.drawString("Steps per Cycle: " + STEPS_PER_CYCLE, 5, BASIC.getSize() + 2);
        win.translate(0, BASIC.getSize() + 5);
        win.drawString("Steps per Frame: " + STEPS_PER_FRAME, 5, BASIC.getSize() + 2);
        win.translate(0, BASIC.getSize());
        win.drawString(
                "Current Time (0 to 1): " + Math.round((double) time.getVal() / STEPS_PER_CYCLE * 1000.0) / 1000.0, 5,
                BASIC.getSize() + 8);

        // NEAT LITTLE LABELS
        win.setFont(BOLD);
        win.setTransform(new AffineTransform());
        win.translate(0, this.height - BASIC.getSize() * 4);
        win.setColor(Color.GREEN);
        win.drawString("GOAL PATH", 5, BASIC.getSize());
        win.translate(0, BASIC.getSize() + 5);
        win.setColor(Color.RED);
        win.drawString("FOURIER APPROXIMATION PATH", 5, BASIC.getSize());

    }

    @Override
    public void update() {

        // TRACING THE PATH OF THE SPINNERS

        Graphics2D buffer = (Graphics2D) pathTraceBuffer.getGraphics();
        buffer.setColor(Color.RED);
        zoom_compensate(buffer);
        xWaveTracker.beginFrame();
        yWaveTracker.beginFrame();
        xIdealTracker.beginFrame();
        yIdealTracker.beginFrame();
        AffineTransform restore = buffer.getTransform();

        for (int i = 0; i < STEPS_PER_FRAME; i++) {

            buffer.setTransform(restore);
            spinners.drawCurrentPoint(buffer);

            time.update();
            spinners.setTime(time.getVal() / (double) STEPS_PER_CYCLE);

        }

        xWaveTracker.drawFrameAndAdvance(draw -> {

            Complex solution = spinners.getSolution();

            draw.setColor(Color.RED);
            PEN.setFrame(0, -this.height / 2 * (solution.getReal() + minReal) / (maxReal - minReal + 2 * this.height / WAVE_TRACKER_BUFFER_FRACTION)
                    + this.height / WAVE_TRACKER_BUFFER_FRACTION + 32, 4, 4);

            draw.fill(PEN);

        });

        xIdealTracker.drawFrameAndAdvance(draw -> {

            Complex solution = func.solveAtTime(time.getVal() / (double) STEPS_PER_CYCLE);

            draw.setColor(Color.GREEN);
            PEN.setFrame(0, -this.height / 2 * (solution.getReal() + minReal) / (maxReal - minReal + 2 * this.height / WAVE_TRACKER_BUFFER_FRACTION)
                    + this.height / WAVE_TRACKER_BUFFER_FRACTION + 32, 8, 8);

            draw.fill(PEN);

        });

        yWaveTracker.drawFrameAndAdvance(draw -> {

            Complex solution = spinners.getSolution();

            draw.setColor(Color.RED);
            PEN.setFrame(0, -this.height / 2 * (solution.getImaginary() + minImg) / (maxImg - minImg + 2 * this.height / WAVE_TRACKER_BUFFER_FRACTION)
                    + this.height / WAVE_TRACKER_BUFFER_FRACTION + 32, 4, 4);

            draw.fill(PEN);

        });

        yIdealTracker.drawFrameAndAdvance(draw -> {

            Complex solution = func.solveAtTime(time.getVal() / (double) STEPS_PER_CYCLE);

            draw.setColor(Color.GREEN);
            PEN.setFrame(0, -this.height / 2 * (solution.getImaginary() + minImg) / (maxImg - minImg + 2 * this.height / WAVE_TRACKER_BUFFER_FRACTION)
                    + this.height / WAVE_TRACKER_BUFFER_FRACTION + 32, 8, 8);

            draw.fill(PEN);

        });

        buffer.dispose();
        xWaveTracker.endFrame();
        yWaveTracker.endFrame();
        xIdealTracker.endFrame();
        yIdealTracker.endFrame();

    }

    private void zoom_compensate(Graphics2D win) {

        win.translate(this.mainWindowWidth / 2, this.height / 2);
        win.scale(ZOOM_FACTOR, ZOOM_FACTOR);
        win.setStroke(ZOOM_COMPENSATED);

    }

    private int getWaveTrackerHeight() {
        return this.height / WAVE_TRACKER_COUNT;
    }

	public static void main(String[] args) {

		try {
			new Drawer().start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
