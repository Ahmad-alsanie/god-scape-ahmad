package com.godscape.system.interfaces.mark;

import java.awt.Graphics2D;

/**
 * Interface for drawable objects in the celestial theme.
 */
public interface Drawable {
    /**
     * Draws the object using the given Graphics2D context.
     *
     * @param g2d The graphics context to draw the object.
     */
    void draw(Graphics2D g2d);

    /**
     * Checks if the object is currently active.
     *
     * @return true if the object is active, otherwise false.
     */
    boolean isActive();
}
