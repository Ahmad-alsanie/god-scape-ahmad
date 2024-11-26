package com.godscape.system.interfaces.mark;

import java.awt.Dimension;

/**
 * The Themable interface defines the contract for all theme classes.
 * It ensures that each theme can initialize its objects, manage canvas size,
 * and handle the start/stop of animations or timers.
 */
public interface Themeable {
    /**
     * Initializes all objects specific to the theme.
     */
    void initializeObjects();

    /**
     * Sets the canvas size.
     *
     * @param width  The width of the canvas.
     * @param height The height of the canvas.
     */
    void setCanvasSize(int width, int height);

    /**
     * Gets the current canvas size.
     *
     * @return The Dimension object representing the canvas size.
     */
    Dimension getCanvasSize();

    /**
     * Starts any necessary animations or timers.
     */
    void start();

    /**
     * Stops any ongoing animations or timers.
     */
    void stop();
}
