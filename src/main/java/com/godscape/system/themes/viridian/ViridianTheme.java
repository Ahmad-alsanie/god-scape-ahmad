package com.godscape.system.themes.viridian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * ViridianTheme.java
 *
 * This class creates an extraordinary forest scene with swaying trees, falling leaves, fluttering birds,
 * wandering animals, and dynamic lighting effects like sun rays filtering through the canopy.
 * The scene adapts dynamically to the canvas size.
 */
public class ViridianTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<ForestObject> forestObjects;
    private Random rand = new Random();
    private int frameCount = 0;

    public ViridianTheme() {
        forestObjects = new ArrayList<>();
        initForestObjects();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initForestObjects(); // Reinitialize forest objects based on new size
            }
        });
    }

    /**
     * Initializes forest objects based on the current panel size.
     */
    private void initForestObjects() {
        forestObjects.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Trees
        for (int i = 0; i < 20; i++) {
            double xPos = i * (width / 20.0);
            forestObjects.add(new Tree(xPos, height - 100, width, height));
        }

        // Falling leaves
        for (int i = 0; i < 100; i++) {
            forestObjects.add(new Leaf(rand.nextInt(width), rand.nextInt(height), rand.nextDouble() + 0.5, width, height));
        }

        // Animals
        for (int i = 0; i < 5; i++) {
            forestObjects.add(new Deer(rand.nextInt(width), height - 80 - rand.nextInt(50), 1 + rand.nextDouble(), width, height));
        }
        for (int i = 0; i < 7; i++) {
            forestObjects.add(new Rabbit(rand.nextInt(width), height - 40 - rand.nextInt(30), 1 + rand.nextDouble(), width, height));
        }

        // Birds
        for (int i = 0; i < 15; i++) {
            forestObjects.add(new Bird(-rand.nextInt(width), rand.nextInt(height / 2), 1 + rand.nextDouble(), width, height));
        }

        // Sun rays
        forestObjects.add(new SunRays(width, height));

        // Butterflies
        for (int i = 0; i < 20; i++) {
            forestObjects.add(new Butterfly(rand.nextInt(width), rand.nextInt(height / 2), 1 + rand.nextDouble(), width, height));
        }

        // Flowers
        for (int i = 0; i < 30; i++) {
            forestObjects.add(new Flower(rand.nextInt(width), height - 20 - rand.nextInt(20), width, height));
        }

        // Grass
        for (int i = 0; i < width; i += 5) {
            forestObjects.add(new Grass(i, height - 10, width, height));
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
        for (ForestObject obj : forestObjects) {
            obj.move();
            obj.draw(g2d);
        }

        g2d.dispose();
    }

    /**
     * Draws the forest background with gradient sky and distant trees.
     */
    private void drawBackground(Graphics2D g2d, int width, int height) {
        // Draw gradient background for the sky
        GradientPaint gp = new GradientPaint(0, 0, new Color(135, 206, 235), // Sky blue
                0, height, new Color(34, 139, 34)); // Forest green
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Distant trees silhouettes
        g2d.setColor(new Color(34, 139, 34, 50));
        for (int i = 0; i < 50; i++) {
            int x = rand.nextInt(width);
            int h = 50 + rand.nextInt(100);
            g2d.fillRect(x, height - h, 5, h);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all forest objects
    abstract class ForestObject {
        double x, y, speed;
        int panelWidth, panelHeight;

        ForestObject(double x, double y, double speed, int width, int height) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.panelWidth = width;
            this.panelHeight = height;
        }

        abstract void move();

        abstract void draw(Graphics2D g2d);
    }

    // Tree class with swaying animation
    class Tree extends ForestObject {
        double swayOffset = rand.nextDouble() * Math.PI * 2;
        Color trunkColor = new Color(139, 69, 19); // Saddle brown
        Color leafColor = new Color(34, 139, 34);  // Forest green

        Tree(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            // Swaying motion handled in draw method
        }

        @Override
        void draw(Graphics2D g2d) {
            double sway = Math.sin((frameCount / 50.0) + swayOffset) * 5;

            // Draw trunk
            g2d.setColor(trunkColor);
            g2d.fillRect((int) x, (int) y - 60, 10, 60);

            // Draw leaves (canopy)
            g2d.setColor(leafColor);
            g2d.fillOval((int) x - 20 + (int) sway, (int) y - 100, 50, 50);
        }
    }

    // Leaf class with falling animation
    class Leaf extends ForestObject {
        Color color;
        double angle;

        Leaf(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
            color = new Color(rand.nextFloat(), rand.nextFloat(), 0); // Shades of yellow to red
            angle = rand.nextDouble() * 360;
        }

        @Override
        void move() {
            y += speed;
            x += Math.sin(y / 20.0) * 1;
            angle += speed;
            if (y > panelHeight) {
                y = -10;
                x = rand.nextInt(panelWidth);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(angle));
            g2d.setColor(color);
            g2d.fillOval(-5, -5, 10, 10);
            g2d.setTransform(old);
        }
    }

    // Deer class
    class Deer extends ForestObject {

        Deer(double x, double y, double speed, int width, int height) {
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
            // Simplified deer drawing
            g2d.setColor(new Color(160, 82, 45)); // Sienna
            g2d.fillRect((int) x, (int) y - 20, 30, 20); // Body
            g2d.fillRect((int) x + 20, (int) y - 30, 10, 10); // Head
            g2d.setColor(Color.BLACK);
            g2d.drawLine((int) x + 25, (int) y - 30, (int) x + 25, (int) y - 40); // Antler
            g2d.drawLine((int) x + 25, (int) y - 35, (int) x + 20, (int) y - 38); // Antler branch
        }
    }

    // Rabbit class
    class Rabbit extends ForestObject {

        Rabbit(double x, double y, double speed, int width, int height) {
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
            // Simplified rabbit drawing
            g2d.setColor(Color.GRAY);
            g2d.fillOval((int) x, (int) y - 10, 15, 10); // Body
            g2d.fillOval((int) x + 10, (int) y - 15, 7, 7); // Head
            g2d.setColor(Color.PINK);
            g2d.drawLine((int) x + 13, (int) y - 15, (int) x + 13, (int) y - 25); // Ear
        }
    }

    // Bird class
    class Bird extends ForestObject {

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

    // SunRays class (dynamic lighting effect)
    class SunRays extends ForestObject {

        SunRays(int width, int height) {
            super(0, 0, 0, width, height);
        }

        @Override
        void move() {
            // Static but can animate intensity
        }

        @Override
        void draw(Graphics2D g2d) {
            // Simulate sun rays filtering through the canopy
            g2d.setColor(new Color(255, 255, 0, 30));
            for (int i = 0; i < 10; i++) {
                int rayWidth = panelWidth / 10;
                int x = i * rayWidth;
                Polygon ray = new Polygon();
                ray.addPoint(x, 0);
                ray.addPoint(x + rayWidth / 2, panelHeight);
                ray.addPoint(x + rayWidth, 0);
                g2d.fill(ray);
            }
        }
    }

    // Butterfly class
    class Butterfly extends ForestObject {
        Color color;

        Butterfly(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
            color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        }

        @Override
        void move() {
            x += Math.sin(frameCount / 10.0) * 2;
            y += Math.cos(frameCount / 10.0) * 2;
            if (x > panelWidth) x = -10;
            if (x < -10) x = panelWidth;
            if (y > panelHeight) y = -10;
            if (y < -10) y = panelHeight;
        }

        @Override
        void draw(Graphics2D g2d) {
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.setColor(color);
            g2d.fillOval(-5, -5, 10, 10);
            g2d.setColor(color.darker());
            g2d.drawLine(0, 0, -10, -10);
            g2d.drawLine(0, 0, 10, -10);
            g2d.setTransform(old);
        }
    }

    // Flower class
    class Flower extends ForestObject {
        Color color;

        Flower(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
            color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        }

        @Override
        void move() {
            // Static
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(color);
            // Draw petals
            for (int i = 0; i < 5; i++) {
                double angle = i * Math.PI * 2 / 5;
                int petalX = (int) (x + Math.cos(angle) * 5);
                int petalY = (int) (y + Math.sin(angle) * 5);
                g2d.fillOval(petalX, petalY, 5, 5);
            }
            // Draw center
            g2d.setColor(Color.YELLOW);
            g2d.fillOval((int) x, (int) y, 5, 5);
        }
    }

    // Grass class
    class Grass extends ForestObject {

        Grass(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            // Swaying motion handled in draw method
        }

        @Override
        void draw(Graphics2D g2d) {
            double sway = Math.sin((frameCount / 30.0) + x / 10.0) * 2;
            g2d.setColor(new Color(34, 139, 34));
            Path2D path = new Path2D.Double();
            path.moveTo(x, y);
            path.quadTo(x + sway, y - 10, x, y - 20);
            g2d.setStroke(new BasicStroke(1));
            g2d.draw(path);
        }
    }

    /**
     * Main method to test the ViridianTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Extraordinary Viridian Theme");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ViridianTheme viridianTheme = new ViridianTheme();
            frame.add(viridianTheme);
            frame.setSize(new Dimension(800, 600)); // Initial size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
