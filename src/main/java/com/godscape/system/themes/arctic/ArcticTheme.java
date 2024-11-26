package com.godscape.system.themes.arctic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * ArcticTheme.java
 *
 * This class creates a dynamic and immersive Arctic scene featuring falling snowflakes,
 * drifting icebergs, shimmering auroras, wandering polar bears, and other ice-related elements.
 * The scene auto-adapts to the canvas size.
 */
public class ArcticTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<ArcticObject> arcticObjects;
    private Random rand = new Random();
    private int frameCount = 0;

    public ArcticTheme() {
        arcticObjects = new ArrayList<>();
        initArcticObjects();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initArcticObjects(); // Reinitialize arctic objects based on new size
            }
        });
    }

    /**
     * Initializes arctic objects based on the current panel size.
     */
    private void initArcticObjects() {
        arcticObjects.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Static objects
        arcticObjects.add(new Mountain(width / 4.0, height * 2 / 3.0, width, height));
        arcticObjects.add(new Mountain(width * 3 / 4.0, height * 2 / 3.0, width, height));
        arcticObjects.add(new Iceberg(width / 2.0, height * 3 / 4.0, width, height));

        // Snowflakes
        for (int i = 0; i < 200; i++) {
            arcticObjects.add(new Snowflake(rand.nextInt(width), rand.nextInt(height), rand.nextDouble() + 0.5, width, height));
        }

        // Polar bears
        for (int i = 0; i < 3; i++) {
            arcticObjects.add(new PolarBear(rand.nextInt(width), height - 50 - rand.nextInt(50), 1 + rand.nextDouble(), width, height));
        }

        // Penguins
        for (int i = 0; i < 5; i++) {
            arcticObjects.add(new Penguin(rand.nextInt(width), height - 30 - rand.nextInt(20), 1 + rand.nextDouble(), width, height));
        }

        // Seals
        for (int i = 0; i < 4; i++) {
            arcticObjects.add(new Seal(rand.nextInt(width), height - 40 - rand.nextInt(30), 1 + rand.nextDouble(), width, height));
        }

        // Aurora
        arcticObjects.add(new Aurora(width, height));

        // Clouds
        for (int i = 0; i < 5; i++) {
            arcticObjects.add(new Cloud(rand.nextInt(width), rand.nextInt(height / 2), 0.5 + rand.nextDouble(), width, height));
        }

        // Birds
        for (int i = 0; i < 10; i++) {
            arcticObjects.add(new Bird(-rand.nextInt(width), rand.nextInt(height / 2), 1 + rand.nextDouble(), width, height));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        drawBackground(g2d, width, height);
        for (ArcticObject obj : arcticObjects) {
            obj.move();
            obj.draw(g2d);
        }

        g2d.dispose();
    }

    /**
     * Draws the Arctic background with gradient sky, auroras, and ground.
     */
    private void drawBackground(Graphics2D g2d, int width, int height) {
        // Draw gradient background for the sky
        GradientPaint gp = new GradientPaint(0, 0, new Color(25, 25, 112), // Midnight blue
                0, height, new Color(0, 0, 0)); // Black
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Draw ground (snow)
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, height - 100, width, 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all arctic objects
    abstract class ArcticObject {
        double x, y, speed;
        int panelWidth, panelHeight;

        ArcticObject(double x, double y, double speed, int width, int height) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.panelWidth = width;
            this.panelHeight = height;
        }

        abstract void move();

        abstract void draw(Graphics2D g2d);
    }

    // Mountain class (static object)
    class Mountain extends ArcticObject {

        Mountain(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            // Static object
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(169, 169, 169)); // Dark gray
            Polygon mountain = new Polygon();
            mountain.addPoint((int) x, (int) y);
            mountain.addPoint((int) (x - 150), (int) panelHeight - 100);
            mountain.addPoint((int) (x + 150), (int) panelHeight - 100);
            g2d.fill(mountain);
            // Add snow cap
            g2d.setColor(Color.WHITE);
            Polygon snowCap = new Polygon();
            snowCap.addPoint((int) x, (int) y);
            snowCap.addPoint((int) (x - 50), (int) y + 50);
            snowCap.addPoint((int) (x + 50), (int) y + 50);
            g2d.fill(snowCap);
        }
    }

    // Iceberg class
    class Iceberg extends ArcticObject {
        double driftSpeed;

        Iceberg(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
            driftSpeed = 0.1 + rand.nextDouble() * 0.2;
        }

        @Override
        void move() {
            x += driftSpeed;
            if (x > panelWidth + 200) x = -200;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(176, 224, 230)); // Powder blue
            Polygon iceberg = new Polygon();
            iceberg.addPoint((int) x, (int) y);
            iceberg.addPoint((int) (x - 50), (int) y + 30);
            iceberg.addPoint((int) (x + 50), (int) y + 30);
            g2d.fill(iceberg);
        }
    }

    // Snowflake class
    class Snowflake extends ArcticObject {

        Snowflake(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            y += speed;
            x += Math.sin(y / 20.0) * 1;
            if (y > panelHeight) {
                y = -10;
                x = rand.nextInt(panelWidth);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.WHITE);
            int size = 2 + rand.nextInt(3);
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    // PolarBear class
    class PolarBear extends ArcticObject {

        PolarBear(double x, double y, double speed, int width, int height) {
            super(x, y, speed * (rand.nextBoolean() ? 1 : -1), width, height);
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth || x < -50) {
                speed = -speed;
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.WHITE);
            // Simplified polar bear drawing
            g2d.fillOval((int) x, (int) y - 20, 40, 20); // Body
            g2d.fillOval((int) x + 25, (int) y - 30, 15, 15); // Head
            g2d.setColor(Color.BLACK);
            g2d.fillOval((int) x + 35, (int) y - 25, 3, 3); // Eye
            g2d.fillOval((int) x + 38, (int) y - 20, 4, 4); // Nose
        }
    }

    // Penguin class
    class Penguin extends ArcticObject {

        Penguin(double x, double y, double speed, int width, int height) {
            super(x, y, speed * (rand.nextBoolean() ? 1 : -1), width, height);
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth || x < -20) {
                speed = -speed;
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            // Body
            g2d.setColor(Color.BLACK);
            g2d.fillOval((int) x, (int) y - 20, 15, 30);

            // Belly
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) x + 3, (int) y - 15, 10, 20);

            // Beak
            g2d.setColor(Color.ORANGE);
            g2d.fillPolygon(new int[]{(int) x + 7, (int) x + 12, (int) x + 7}, new int[]{(int) y - 20, (int) y - 18, (int) y - 15}, 3);

            // Eyes
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) x + 5, (int) y - 18, 3, 3);
            g2d.setColor(Color.BLACK);
            g2d.fillOval((int) x + 6, (int) y - 17, 1, 1);
        }
    }

    // Seal class
    class Seal extends ArcticObject {

        Seal(double x, double y, double speed, int width, int height) {
            super(x, y, speed * (rand.nextBoolean() ? 1 : -1), width, height);
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth || x < -50) {
                speed = -speed;
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(112, 128, 144)); // Slate gray
            // Body
            g2d.fillOval((int) x, (int) y - 10, 30, 20);
            // Head
            g2d.fillOval((int) x + 25, (int) y - 15, 15, 15);
            // Flippers
            g2d.fillOval((int) x + 10, (int) y + 5, 10, 5);
            g2d.fillOval((int) x + 20, (int) y + 5, 10, 5);
        }
    }

    // Aurora class (static but changing)
    class Aurora extends ArcticObject {

        Aurora(int width, int height) {
            super(0, 0, 0, width, height);
        }

        @Override
        void move() {
            // Static but can animate colors
        }

        @Override
        void draw(Graphics2D g2d) {
            // Simulate aurora with gradient
            GradientPaint gp = new GradientPaint(0, 0, new Color(0, 255, 127, 50), // Spring green with transparency
                    0, panelHeight / 2, new Color(0, 0, 0, 0)); // Transparent
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, panelWidth, panelHeight / 2);
        }
    }

    // Cloud class
    class Cloud extends ArcticObject {

        Cloud(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth + 50) {
                x = -50;
                y = rand.nextInt(panelHeight / 2);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) x, (int) y, 40, 20);
            g2d.fillOval((int) x + 10, (int) y - 10, 30, 30);
            g2d.fillOval((int) x + 20, (int) y, 40, 20);
        }
    }

    // Bird class
    class Bird extends ArcticObject {

        Bird(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(x / 50.0) * 1;
            if (x > panelWidth + 20) x = -20;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.BLACK);
            g2d.drawArc((int) x, (int) y, 10, 5, 0, 180);
            g2d.drawArc((int) x + 10, (int) y, 10, 5, 0, 180);
        }
    }

    /**
     * Main method to test the ArcticTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Incredible Arctic Theme");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ArcticTheme arcticTheme = new ArcticTheme();
            frame.add(arcticTheme);
            frame.setSize(new Dimension(800, 600)); // Initial size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
