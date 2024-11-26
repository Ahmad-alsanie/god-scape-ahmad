// File: com/godscape/system/themes/ThemeAbstraction.java
package com.godscape.system.themes;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Abstract class providing common functionalities for all themes.
 */
public abstract class ThemeAbstraction extends JPanel implements ActionListener {
    protected ArrayList<ThemeObject> themeObjects;
    protected Dimension canvasSize;
    private Timer timer;
    protected int frameCount = 0;

    /**
     * Constructor to set up the theme abstraction with screen size.
     */
    public ThemeAbstraction() {
        this(Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
    }

    /**
     * Constructor to set up the theme abstraction with specified size.
     *
     * @param width  The width of the canvas.
     * @param height The height of the canvas.
     */
    public ThemeAbstraction(int width, int height) {
        this.canvasSize = new Dimension(width, height);
        setPreferredSize(canvasSize);
        this.themeObjects = new ArrayList<>();
        setDoubleBuffered(true); // Enable double buffering for smooth rendering
        initializeObjects();      // Initialize theme-specific objects
        initializeTimer();        // Set up the animation timer
    }

    /**
     * Initialize theme-specific objects. To be implemented by subclasses.
     */
    public abstract void initializeObjects();

    /**
     * Draw the background. To be implemented by subclasses.
     *
     * @param g2d Graphics2D object.
     */
    protected abstract void drawBackground(Graphics2D g2d);

    /**
     * Initialize the animation timer.
     */
    private void initializeTimer() {
        timer = new Timer(30, this); // Approximately 33 FPS
        timer.start();
    }

    /**
     * Handle timer events to repaint the panel.
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        frameCount++;
        repaint();
    }

    /**
     * Override paintComponent to draw background and theme objects.
     *
     * @param g Graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        drawBackground(g2d);
        for (ThemeObject obj : themeObjects) {
            obj.move();
            obj.draw(g2d);
        }
        g2d.dispose();
    }

    /**
     * Get the current canvas size.
     *
     * @return Dimension object.
     */
    public Dimension getCanvasSize() {
        return canvasSize;
    }

    /**
     * Start the animation timer.
     */
    protected void start() {
        timer.start();
    }

    /**
     * Stop the animation timer.
     */
    protected void stop() {
        timer.stop();
    }
}
