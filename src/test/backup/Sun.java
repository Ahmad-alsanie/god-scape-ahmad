package com.godscape.system.themes.celestial.objects;

import java.awt.*;

/**
 * Represents the Sun in the solar system.
 */
public class Sun {
    private static final float SUN_DIAMETER_SCALE = 0.000005f; // Scaling factor for the Sun's size
    private static final float SUN_DIAMETER = 1392.7f; // Sun's diameter in thousands of kilometers

    /**
     * Draws the Sun at the center of the given panel.
     *
     * @param g2d    The Graphics2D context.
     * @param width  The width of the panel.
     * @param height The height of the panel.
     */
    public void draw(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.YELLOW);
        int sunSize = (int) (SUN_DIAMETER * SUN_DIAMETER_SCALE);
        int sunX = width / 2 - sunSize / 2;
        int sunY = height / 2 - sunSize / 2;
        g2d.fillOval(sunX, sunY, sunSize, sunSize);
    }
}
