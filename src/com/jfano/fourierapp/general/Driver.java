package com.jfano.fourierapp.general;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Optional;

public abstract class Driver extends Canvas implements Runnable, KeyListener {

    private int FPS, UPS;
    protected int width, height;
    protected JFrame frame;
    protected boolean[] keys;

    private boolean exit = false, fullScreen = false;

    private static final long MtoN = 1000000L, StoN = 1000L*MtoN;

    /**
     * Create the game driver and bind it to a JFrame
     *
     * @param width width of the driver's JFrame
     * @param height height of the driver's JFrame
     * @param FPS target (maximum) frames drawn per second
     * @param UPS target (maximum) updates per second
     * @param title title of the driver's JFrame
     * @param showTitle flag controlling the visibility of the driver's JFrame
     */
    public Driver(int width, int height, int FPS, int UPS, String title, boolean showTitle) {
        this.width = width;
        this.height = height;
        this.FPS = FPS;
        this.UPS = UPS;

        this.frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(this);
        frame.setUndecorated(!showTitle);
        frame.pack();

        this.addKeyListener(this);

        // Support all key codes 0 - 600
        this.keys = new boolean[600];

    }

    /**
     * Create the game driver and bind it to a JFrame.
     * FPS and UPS are set to their default values of 60 and 100 respectively.
     *
     * @param width width of the driver's JFrame
     * @param height height of the driver's JFrame
     * @param title title of the driver's JFrame
     * @param showTitle flag controlling the visibility of the driver's JFrame
     */
    public Driver(int width, int height, String title, boolean showTitle) {
        // Default FPS: 60
        // Default UPS: 100
        this(width, height, 60, 100, title, showTitle);
    }

    /**
     * Create the game driver and bind it to a JFrame.
     * FPS and UPS are set to their default values of 60 and 100 respectively.
     * Width and Height are set to their default values of 1024 and 756 respectively.
     *
     * @param title
     * @param showTitle
     */
    public Driver(String title, boolean showTitle) {
        // Default Width: 1046
        // Default Height: 756
        this(1046, 756, title, showTitle);
    }

    /**
     *
     */
    public Driver() {
        this("Driver", true);
    }

    /**
     *
     */
    public void start() {

        exit = false;

        if(fullScreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            frame.setSize(width, height);
        }

        frame.setLocation(0, 0);
        frame.setVisible(true);

        Thread driverThread = new Thread(this, "driver");
        driverThread.start();

    }

    /**
     *
     */
    public abstract void update();

    /**
     *
     * @param context
     */
    public abstract void draw(Graphics2D context);

    /**
     *  Call the driver's draw function with the driver's JFrame as the target
     */
    private void render() {

        BufferStrategy buffs = Optional
            .ofNullable(this.getBufferStrategy())
            .orElseGet(() -> {
                this.createBufferStrategy(3);
                return this.getBufferStrategy();
        });
        Graphics2D g = (Graphics2D) buffs.getDrawGraphics();

        g.setColor(Color.WHITE);
        g.fill(new Rectangle(0, 0, this.width, this.height));
        draw(g);
        g.dispose();
        buffs.show();

    }

    @Override
    public void run() {

        this.setFocusable(true);

        // Calculate update/draw rate in terms of nanoseconds
        final long updateRate = StoN / UPS, drawRate = StoN / FPS;

        long processingStartTime, lastUpdateTime, updateDiff;
        lastUpdateTime = System.nanoTime();

        while(!exit) {

            long curTime = System.nanoTime();
            processingStartTime = curTime;
            updateDiff = curTime - lastUpdateTime;

            // If an entire second worth of updates are missed, just drop them
            // The driver will never catch back up otherwise
            if (updateDiff > StoN) {
                lastUpdateTime = curTime;
                updateDiff = curTime - lastUpdateTime;
            }

            // Calculate the number of updates that need to occur to meet
            // the UPS rate and perform those updates.
            int updatesNeeded = (int) (updateDiff / updateRate);
            for (int i = 0; i < updatesNeeded; ++i) {
                update();
                lastUpdateTime += updateRate;
            }

            render();

            long processingTime = System.nanoTime() - processingStartTime;

            // If this frame was completed before the draw rate
            if (processingTime < drawRate) {
                try {
                    Thread.sleep((int) (drawRate - processingTime) / MtoN);
                } catch (InterruptedException e) {
                    System.err.format("Thread Interrupted: %s", e.getMessage());
                }
            }

        }

        frame.setVisible(false);

    }

    public void exit() {
        exit = true;
    }

    @Override
    public void keyTyped(KeyEvent ev) {
        // STUB
    }

    @Override
    public final void keyPressed(KeyEvent ev) {
        try {
            keys[ev.getKeyCode()] = true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.format("Unexpected key stroke (%s) recieved. " +
                            "Index of %d exceeded capacity of %d",
                    ev.getKeyChar(),
                    ev.getKeyChar(),
                    keys.length);
        }
    }

    @Override
    public final void keyReleased(KeyEvent ev) {
        try {
            keys[ev.getKeyCode()] = false;
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.format("Unexpected key stroke (%s) recieved. " +
                            "Index of %d exceeded capacity of %d",
                    ev.getKeyChar(),
                    ev.getKeyChar(),
                    keys.length);
        }

    }
}
