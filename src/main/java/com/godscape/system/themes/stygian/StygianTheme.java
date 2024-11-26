package com.godscape.system.themes.stygian;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * StygianTheme.java
 *
 * This class creates a dynamic and immersive deep-sea scene featuring bioluminescent creatures,
 * underwater vents, mysterious shadows, glowing plants, and other deep-sea phenomena.
 * The scene auto-adapts to the canvas size.
 */
public class StygianTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<DeepSeaElement> deepSeaElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public StygianTheme() {
        deepSeaElements = new ArrayList<>();
        initDeepSeaElements();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initDeepSeaElements(); // Reinitialize elements based on new size
            }
        });
    }

    /**
     * Initializes deep-sea elements based on the current panel size.
     */
    private void initDeepSeaElements() {
        deepSeaElements.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Add glowing plants
        for (int i = 0; i < 20; i++) {
            double xPos = rand.nextInt(width);
            double yPos = height - 50 - rand.nextInt(100);
            deepSeaElements.add(new GlowingPlant(xPos, yPos));
        }

        // Add bioluminescent creatures
        for (int i = 0; i < 10; i++) {
            deepSeaElements.add(new BioluminescentCreature(rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble()));
        }

        // Add mysterious shadows
        for (int i = 0; i < 5; i++) {
            deepSeaElements.add(new MysteriousShadow(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }

        // Add underwater vents
        for (int i = 0; i < 3; i++) {
            double xPos = rand.nextInt(width);
            double yPos = height - 50;
            deepSeaElements.add(new UnderwaterVent(xPos, yPos));
        }

        // Add deep-sea fish
        for (int i = 0; i < 15; i++) {
            deepSeaElements.add(new DeepSeaFish(-rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble() * 2));
        }

        // Add floating particles
        for (int i = 0; i < 100; i++) {
            deepSeaElements.add(new FloatingParticle(rand.nextInt(width), rand.nextInt(height), 0.2 + rand.nextDouble() * 0.5));
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

        drawDeepSeaBackground(g2d, width, height);

        // Draw all deep-sea elements
        for (DeepSeaElement element : deepSeaElements) {
            element.move();
            element.draw(g2d, frameCount);
        }

        g2d.dispose();
    }

    /**
     * Draws the deep-sea background with gradient water and faint light rays.
     */
    private void drawDeepSeaBackground(Graphics2D g2d, int width, int height) {
        // Draw gradient background for deep sea
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 30),
                0, height, new Color(0, 0, 0));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Draw faint light rays
        g2d.setColor(new Color(50, 50, 100, 50));
        for (int i = 0; i < 5; i++) {
            int rayWidth = width / 5;
            int x = i * rayWidth;
            Polygon ray = new Polygon();
            ray.addPoint(x, 0);
            ray.addPoint(x + rayWidth / 2, height);
            ray.addPoint(x + rayWidth, 0);
            g2d.fill(ray);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all deep-sea elements
    abstract class DeepSeaElement {
        double x, y, speed;

        abstract void move();

        abstract void draw(Graphics2D g2d, int frameCount);
    }

    // GlowingPlant class
    class GlowingPlant extends DeepSeaElement {
        private double swayOffset;

        GlowingPlant(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
            this.swayOffset = rand.nextDouble() * Math.PI * 2;
        }

        @Override
        void move() {
            // Swaying motion
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(0, 255, 100, 150));
            Path2D path = new Path2D.Double();
            path.moveTo(x, y);
            double sway = Math.sin((frameCount / 20.0) + swayOffset) * 10;
            path.curveTo(x + sway, y - 30, x - sway, y - 60, x, y - 90);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(path);
            // Glow effect
            g2d.setColor(new Color(0, 255, 100, 80));
            g2d.fillOval((int) x - 10, (int) y - 100, 20, 20);
        }
    }

    // BioluminescentCreature class
    class BioluminescentCreature extends DeepSeaElement {
        private Color color;
        private double glowRate;

        BioluminescentCreature(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            this.glowRate = rand.nextDouble() * 0.1 + 0.05;
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(x / 50.0) * 2;
            if (x > getWidth() + 50) {
                x = -50;
                y = rand.nextInt(getHeight());
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            float alpha = (float) (0.5 + 0.5 * Math.sin(frameCount * glowRate));
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)));
            g2d.fillOval((int) x, (int) y, 30, 15);
            // Antennae
            g2d.drawLine((int) x + 15, (int) y, (int) x + 15, (int) y - 10);
        }
    }

    // MysteriousShadow class
    class MysteriousShadow extends DeepSeaElement {
        MysteriousShadow(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed * (rand.nextBoolean() ? 1 : -1);
        }

        @Override
        void move() {
            x += speed;
            if (x > getWidth() + 100 || x < -100) {
                speed = -speed;
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(0, 0, 0, 100));
            int[] xPoints = {(int) x, (int) x + 40, (int) x + 80};
            int[] yPoints = {(int) y, (int) y - 20, (int) y};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }

    // UnderwaterVent class
    class UnderwaterVent extends DeepSeaElement {
        UnderwaterVent(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
        }

        @Override
        void move() {
            // Static vent
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            // Draw vent
            g2d.setColor(new Color(80, 80, 80));
            g2d.fillRect((int) x - 20, (int) y - 40, 40, 40);
            // Draw smoke
            g2d.setColor(new Color(50, 50, 50, 100));
            for (int i = 0; i < 5; i++) {
                int smokeSize = 20 + i * 10;
                int alpha = 100 - i * 20;
                g2d.setColor(new Color(50, 50, 50, alpha));
                g2d.fillOval((int) x - smokeSize / 2, (int) y - 40 - i * 20, smokeSize, smokeSize);
            }
        }
    }

    // DeepSeaFish class
    // DeepSeaFish class
    class DeepSeaFish extends DeepSeaElement {
        private boolean movingRight;

        DeepSeaFish(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.movingRight = speed > 0; // Determines the direction of the fish
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(x / 80.0) * 1.5; // Simulate fish swimming in a wavy pattern

            // Wrap around the screen if the fish goes out of bounds
            if (x > getWidth() + 100) {
                x = -100;
                y = rand.nextInt(getHeight());
            } else if (x < -100) {
                x = getWidth() + 100;
                y = rand.nextInt(getHeight());
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(100, 100, 150));
            AffineTransform old = g2d.getTransform();

            // Flip the fish if it's moving left
            if (!movingRight) {
                g2d.scale(-1, 1);
                g2d.translate(-x - 50, y);
            } else {
                g2d.translate(x, y);
            }

            // Draw the body
            g2d.fillOval(0, 0, 50, 20);

            // Draw the tail with slight oscillation for a swimming effect
            int tailOffset = (int) (Math.sin(frameCount / 5.0) * 5);
            Polygon tail = new Polygon();
            tail.addPoint(50, 10);
            tail.addPoint(60 + tailOffset, 0);
            tail.addPoint(60 + tailOffset, 20);
            g2d.fill(tail);

            // Draw the eye
            g2d.setColor(new Color(255, 255, 255, 150));
            g2d.fillOval(10, 5, 5, 5);

            g2d.setTransform(old); // Restore original transformation
        }
    }

    // FloatingParticle class
    class FloatingParticle extends DeepSeaElement {
        private double driftOffset;

        FloatingParticle(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.driftOffset = rand.nextDouble() * Math.PI * 2;
        }

        @Override
        void move() {
            y += speed;
            x += Math.sin((frameCount / 20.0) + driftOffset) * 0.5;
            if (y > getHeight()) {
                y = -10;
                x = rand.nextInt(getWidth());
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(255, 255, 255, 50));
            int size = 2 + rand.nextInt(3);
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    /**
     * Main method to test the StygianTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Stygian Theme - Deep Sea Exploration");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            StygianTheme stygianTheme = new StygianTheme();
            frame.add(stygianTheme);
            frame.setSize(new Dimension(1000, 800)); // Initial size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
