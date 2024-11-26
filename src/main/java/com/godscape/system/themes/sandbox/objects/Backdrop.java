// File: com/godscape/system/themes/sandbox/objects/Backdrop.java
package com.godscape.system.themes.sandbox.objects;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Random;

/**
 * Backdrop for Halloween Shadowfest-themed scene with mystical and eerie elements.
 */
public class Backdrop {
    private int width;
    private int height;
    private Random rand = new Random();

    public Backdrop(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Draws the Halloween Shadowfest scene onto the provided Graphics2D context.
     */
    public void draw(Graphics2D g2d) {
        drawNightSky(g2d);
        drawMoon(g2d);
        drawMistyLandscape(g2d);
        drawFloatingOrbs(g2d);
        drawFoggyGround(g2d);
    }

    private void drawNightSky(Graphics2D g2d) {
        GradientPaint nightSky = new GradientPaint(0, 0, new Color(20, 20, 60), 0, height, Color.BLACK);
        g2d.setPaint(nightSky);
        g2d.fillRect(0, 0, width, height);
    }

    private void drawMoon(Graphics2D g2d) {
        int moonSize = 80;
        int x = width - 200;
        int y = 100;
        g2d.setColor(new Color(240, 240, 220, 200));
        g2d.fillOval(x, y, moonSize, moonSize);

        for (int i = 0; i < 3; i++) {
            g2d.setColor(new Color(240, 240, 220, 100 - (i * 30)));
            g2d.drawOval(x - i * 10, y - i * 10, moonSize + i * 20, moonSize + i * 20);
        }
    }

    private void drawMistyLandscape(Graphics2D g2d) {
        g2d.setColor(new Color(40, 40, 50, 150));
        g2d.fillRect(0, height - 120, width, 120);

        g2d.setColor(new Color(30, 30, 40, 180));
        int[] xPoints = {0, 200, 400, 600, 800, width};
        int[] yPoints = {height - 80, height - 100, height - 120, height - 100, height - 80, height - 120};
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);

        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(20, 20, 20, 180));
        Path2D.Double tree = new Path2D.Double();
        tree.moveTo(150, height - 150);
        tree.lineTo(160, height - 190);
        tree.lineTo(170, height - 150);
        tree.lineTo(180, height - 200);
        tree.lineTo(190, height - 150);
        g2d.draw(tree);
    }

    private void drawFloatingOrbs(Graphics2D g2d) {
        for (int i = 0; i < 20; i++) {
            int size = rand.nextInt(6) + 5;
            int x = rand.nextInt(width);
            int y = rand.nextInt(height - 100);
            g2d.setColor(new Color(255, 255, 220, 100));
            g2d.fill(new Ellipse2D.Double(x, y, size, size));
        }
    }

    private void drawFoggyGround(Graphics2D g2d) {
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(width);
            int y = height - rand.nextInt(100);
            int size = 20 + rand.nextInt(30);
            g2d.setColor(new Color(180, 180, 180, 50));
            g2d.fillOval(x, y, size, size / 2);
        }
    }
}
