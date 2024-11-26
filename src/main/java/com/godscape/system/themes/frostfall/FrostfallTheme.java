package com.godscape.system.themes.frostfall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * FrostfallTheme.java
 *
 * This class creates a dynamic and immersive winter scene featuring falling snowflakes,
 * snow-covered pine trees, snowmen, aurora borealis, and additional Christmas-themed
 * elements like Christmas trees with twinkling lights, Santa Claus with a flying sleigh,
 * reindeer animations, falling presents, and twinkling stars.
 */
public class FrostfallTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<WinterElement> winterElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public FrostfallTheme() {
        winterElements = new ArrayList<>();
        initWinterElements();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initWinterElements(); // Reinitialize winter elements based on new size
            }
        });
    }

    /**
     * Initializes winter elements based on the current panel size.
     */
    private void initWinterElements() {
        winterElements.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Add falling snowflakes
        for (int i = 0; i < 150; i++) { // Increased number for denser snowfall
            winterElements.add(new Snowflake(rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble()));
        }

        // Add snow-covered pine trees
        for (int i = 0; i < 7; i++) { // Increased number for a denser forest
            winterElements.add(new PineTree(rand.nextInt(width), height - 150 + rand.nextInt(50)));
        }

        // Add snowmen
        for (int i = 0; i < 3; i++) { // Increased number of snowmen
            winterElements.add(new Snowman(100 + i * (width / 4), height - 120));
        }

        // Add aurora borealis
        winterElements.add(new AuroraBorealis());

        // Add Christmas trees with twinkling lights
        for (int i = 0; i < 5; i++) {
            winterElements.add(new ChristmasTree(rand.nextInt(width), height - 150 + rand.nextInt(50)));
        }

        // Add Santa Claus with a flying sleigh
        winterElements.add(new SantaSleigh(-100, 100 + rand.nextInt(height / 2), 2 + rand.nextDouble()));

        // Add Reindeer
        for (int i = 0; i < 3; i++) {
            winterElements.add(new Reindeer(-200 - i * 100, 150 + i * 30, 1.5 + rand.nextDouble()));
        }

        // Add Falling Presents
        for (int i = 0; i < 20; i++) {
            winterElements.add(new Present(rand.nextInt(width), -50, 1 + rand.nextDouble()));
        }

        // Add Twinkling Stars
        for (int i = 0; i < 50; i++) {
            winterElements.add(new TwinklingStar(rand.nextInt(width), rand.nextInt(height / 2)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int currentWidth = getWidth();
        int currentHeight = getHeight();
        drawWinterBackground(g2d, currentWidth, currentHeight);

        // Draw all winter elements
        for (WinterElement element : winterElements) {
            element.move(currentWidth, currentHeight, frameCount);
            element.draw(g2d);
        }

        g2d.dispose();
    }

    /**
     * Draws the winter background with gradient sky and ground.
     */
    private void drawWinterBackground(Graphics2D g2d, int width, int height) {
        // Gradient background for sky
        GradientPaint skyGradient = new GradientPaint(0, 0, new Color(0, 0, 102),
                0, height, new Color(173, 216, 230));
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, width, height);

        // Draw the ground (snow)
        g2d.setColor(new Color(240, 248, 255));
        g2d.fillRect(0, height - 100, width, 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all FrostfallTheme elements
    abstract class WinterElement {
        double x, y, speed;

        /**
         * Move the element. frameCount can be used for animations.
         *
         * @param canvasWidth  Current width of the canvas
         * @param canvasHeight Current height of the canvas
         * @param frameCount   Current frame count
         */
        abstract void move(int canvasWidth, int canvasHeight, int frameCount);

        abstract void draw(Graphics2D g2d);
    }

    // Snowflake class for falling snow
    class Snowflake extends WinterElement {
        int size;

        Snowflake(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.size = 5 + rand.nextInt(5);
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            y += speed;
            x += Math.sin(frameCount / 10.0) * 0.5;
            if (y > canvasHeight) {
                y = -size;
                x = rand.nextInt(canvasWidth);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    // PineTree class representing snow-covered pine trees
    class PineTree extends WinterElement {
        PineTree(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            // Static tree; no movement
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(34, 139, 34));
            int[] xPoints = {(int) x, (int) x - 20, (int) x + 20};
            int[] yPoints = {(int) y, (int) y + 50, (int) y + 50};
            g2d.fillPolygon(xPoints, yPoints, 3);

            g2d.setColor(new Color(240, 248, 255));
            g2d.fillPolygon(
                    new int[]{(int) x, (int) x - 15, (int) x + 15},
                    new int[]{(int) y, (int) y + 35, (int) y + 35},
                    3
            );
        }
    }

    // Snowman class representing snowmen
    class Snowman extends WinterElement {
        Snowman(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            // Static snowman; no movement
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255));
            g2d.fillOval((int) x - 15, (int) y - 30, 30, 30); // Head
            g2d.fillOval((int) x - 20, (int) y, 40, 40); // Body

            // Draw eyes and buttons
            g2d.setColor(Color.BLACK);
            g2d.fillOval((int) x - 5, (int) y - 20, 3, 3);
            g2d.fillOval((int) x + 2, (int) y - 20, 3, 3);
            g2d.fillOval((int) x - 2, (int) y + 10, 3, 3);

            // Draw the nose
            g2d.setColor(Color.ORANGE);
            g2d.fillPolygon(
                    new int[]{(int) x, (int) x + 10, (int) x},
                    new int[]{(int) y - 15, (int) y - 12, (int) y - 10},
                    3
            );
        }
    }

    // AuroraBorealis class representing the Northern Lights
    class AuroraBorealis extends WinterElement {
        AuroraBorealis() {
            // Aurora is stationary, no specific coordinates needed
            this.speed = 0;
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            // Static aurora; no movement
        }

        @Override
        void draw(Graphics2D g2d) {
            // Draw aurora with a translucent wavy pattern
            g2d.setColor(new Color(0, 255, 127, 100));
            Path2D.Double auroraPath = new Path2D.Double();
            auroraPath.moveTo(0, 50);
            int currentWidth = getWidth(); // Use current width
            for (int i = 0; i < currentWidth; i += 50) {
                double yOffset = 30 * Math.sin((frameCount / 20.0) + i / 50.0);
                auroraPath.lineTo(i, 50 + yOffset);
            }
            auroraPath.lineTo(currentWidth, 80);
            auroraPath.lineTo(0, 80);
            auroraPath.closePath();

            g2d.fill(auroraPath);
        }
    }

    // ChristmasTree class with twinkling lights
    class ChristmasTree extends WinterElement {
        private ArrayList<Color> lights;
        private ArrayList<Point> lightPositions;
        private int lightCount = 20;
        private double twinkleRate;

        ChristmasTree(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
            this.twinkleRate = rand.nextDouble() * 0.1 + 0.05;

            // Initialize lights
            lights = new ArrayList<>();
            lightPositions = new ArrayList<>();
            for (int i = 0; i < lightCount; i++) {
                lights.add(randomChristmasLightColor());
                // Position lights randomly on the tree
                double angle = rand.nextDouble() * Math.PI;
                double radius = 30 + rand.nextDouble() * 20;
                int lightX = (int) (x + radius * Math.cos(angle));
                int lightY = (int) (y - radius * Math.sin(angle));
                lightPositions.add(new Point(lightX, lightY));
            }
        }

        private Color randomChristmasLightColor() {
            Color[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.WHITE};
            return colors[rand.nextInt(colors.length)];
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            // No movement; static tree with twinkling lights
        }

        @Override
        void draw(Graphics2D g2d) {
            // Draw the tree
            g2d.setColor(new Color(34, 139, 34));
            int[] xPoints = {(int) x, (int) x - 25, (int) x + 25};
            int[] yPoints = {(int) y, (int) y + 60, (int) y + 60};
            g2d.fillPolygon(xPoints, yPoints, 3);

            g2d.setColor(new Color(139, 69, 19));
            g2d.fillRect((int) x - 5, (int) y + 60, 10, 15); // Tree trunk

            // Draw twinkling lights
            for (int i = 0; i < lightCount; i++) {
                Point p = lightPositions.get(i);
                // Change brightness to simulate twinkling
                float brightness = (float) (0.5 + 0.5 * Math.sin(frameCount * twinkleRate + i));
                brightness = Math.max(0.3f, Math.min(1.0f, brightness));
                Color twinkleColor = new Color(
                        lights.get(i).getRed(),
                        lights.get(i).getGreen(),
                        lights.get(i).getBlue(),
                        (int) (brightness * 255)
                );
                g2d.setColor(twinkleColor);
                g2d.fillOval(p.x - 3, p.y - 3, 6, 6);
            }
        }
    }

    // SantaSleigh class representing Santa Claus with a flying sleigh
    class SantaSleigh extends WinterElement {
        private double theta; // Angle for flight path

        SantaSleigh(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.theta = 0;
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            x += speed;
            theta += 0.02;
            y = 100 + 50 * Math.sin(theta);
            if (x > canvasWidth + 100) {
                x = -150;
                y = 100 + rand.nextInt(100);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            // Draw the sleigh
            g2d.setColor(new Color(139, 69, 19)); // Brown sleigh
            g2d.fillRect((int) x, (int) y, 60, 20);
            g2d.fillRect((int) x + 10, (int) y - 10, 40, 20);

            // Draw the runners
            g2d.setColor(Color.BLACK);
            g2d.fillRect((int) x, (int) y + 20, 60, 5);
            g2d.fillRect((int) x + 10, (int) y + 15, 40, 5);

            // Draw Santa
            g2d.setColor(Color.RED);
            g2d.fillOval((int) x + 25, (int) y - 20, 20, 20); // Head
            g2d.fillRect((int) x + 20, (int) y, 30, 10); // Body

            // Draw beard
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) x + 25, (int) y - 10, 20, 15);
        }
    }

    // Reindeer class
    class Reindeer extends WinterElement {
        private double theta; // Angle for movement pattern

        Reindeer(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.theta = 0;
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            x += speed;
            theta += 0.03;
            y += Math.sin(theta) * 0.5;
            if (x > canvasWidth + 50) {
                x = -50;
                y = 150 + rand.nextInt(100);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            // Draw the reindeer
            g2d.setColor(new Color(160, 82, 45)); // Brown color
            g2d.fillOval((int) x, (int) y, 30, 15); // Body
            g2d.fillOval((int) x + 20, (int) y - 10, 20, 20); // Head

            // Draw antlers
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawLine((int) x + 25, (int) y - 10, (int) x + 20, (int) y - 20);
            g2d.drawLine((int) x + 25, (int) y - 10, (int) x + 30, (int) y - 20);

            // Draw legs
            g2d.setColor(Color.BLACK);
            g2d.drawLine((int) x + 5, (int) y + 15, (int) x + 5, (int) y + 25);
            g2d.drawLine((int) x + 25, (int) y + 15, (int) x + 25, (int) y + 25);
        }
    }

    // Present class for falling presents
    class Present extends WinterElement {
        private Color color;

        Present(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.color = randomPresentColor();
        }

        private Color randomPresentColor() {
            Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.ORANGE};
            return colors[rand.nextInt(colors.length)];
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            y += speed;
            if (y > canvasHeight) {
                y = -20;
                x = rand.nextInt(canvasWidth);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillRect((int) x, (int) y, 10, 10); // Simple square present
            g2d.setColor(Color.WHITE);
            g2d.drawLine((int) x, (int) y + 5, (int) x + 10, (int) y + 5); // Ribbon horizontal
            g2d.drawLine((int) x + 5, (int) y, (int) x + 5, (int) y + 10); // Ribbon vertical
        }
    }

    // TwinklingStar class
    class TwinklingStar extends WinterElement {
        private float alpha;
        private boolean increasing;

        TwinklingStar(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
            this.alpha = rand.nextFloat() * 0.5f + 0.5f;
            this.increasing = rand.nextBoolean();
        }

        @Override
        void move(int canvasWidth, int canvasHeight, int frameCount) {
            // Twinkling effect by adjusting alpha
            if (increasing) {
                alpha += 0.005f;
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    increasing = false;
                }
            } else {
                alpha -= 0.005f;
                if (alpha <= 0.3f) {
                    alpha = 0.3f;
                    increasing = true;
                }
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, (int) (alpha * 255)));
            g2d.fillOval((int) x, (int) y, 3, 3);
        }
    }

    /**
     * Main method to test the FrostfallTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Frostfall Theme - Winter Wonderland");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            FrostfallTheme frostfallTheme = new FrostfallTheme();
            frame.add(frostfallTheme);
            frame.setSize(new Dimension(1000, 800)); // Increased initial size for better visibility
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
