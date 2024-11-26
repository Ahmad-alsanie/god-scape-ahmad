// File: com/godscape/system/themes/ThemeObject.java
package com.godscape.system.themes;

import java.awt.Graphics2D;

/**
 * Abstract class representing a generic theme object.
 */
public abstract class ThemeObject {
    protected double x, y, speed;

    /**
     * Parameterized constructor to initialize the theme object.
     *
     * @param x     The x-coordinate of the object.
     * @param y     The y-coordinate of the object.
     * @param speed The speed at which the object moves.
     */
    public ThemeObject(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    /**
     * Abstract method to move the object.
     */
    public abstract void move();

    /**
     * Abstract method to draw the object.
     *
     * @param g2d Graphics2D object.
     */
    public abstract void draw(Graphics2D g2d);
}
