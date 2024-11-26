// File: com/godscape/system/themes/sandbox/scenes/SandboxMidground.java
package com.godscape.system.themes.sandbox.scenes;

import java.awt.*;

public class SandboxMidground {
    private int width;
    private int height;

    public SandboxMidground(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(20, 20, 20));
        int[] xPoints = {0, 150, 300, 450, 600, width};
        int[] yPoints = {height - 50, height - 70, height - 90, height - 70, height - 50, height - 100};
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);
    }
}
