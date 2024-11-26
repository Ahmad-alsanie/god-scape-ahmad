package com.godscape.system.themes.heartswake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * HeartswakeTheme represents a romantic Valentine's Day scene with animated hearts, petals, and a dreamy atmosphere.
 */
public class HeartswakeTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<ValentineElement> valentineElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public HeartswakeTheme() {
        valentineElements = new ArrayList<>();
        timer = new Timer(30, this); // ~33 FPS for smooth animations
        timer.start();

        // Add component listener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initValentineElements(); // Reinitialize elements on resize
            }
        });

        initValentineElements(); // Initial element creation
    }

    private void initValentineElements() {
        valentineElements.clear(); // Clear previous elements

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            return; // Avoid initializing elements if dimensions are not set
        }

        // Add floating hearts
        for (int i = 0; i < 10; i++) {
            valentineElements.add(new FloatingHeart(rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble() * 2));
        }

        // Add petals
        for (int i = 0; i < 50; i++) {
            valentineElements.add(new Petal(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }

        // Add sparkles for a magical effect
        for (int i = 0; i < 30; i++) {
            valentineElements.add(new Sparkle(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        drawRomanticBackground(g2d, width, height);
        for (ValentineElement element : valentineElements) {
            element.move(width, height);
            element.draw(g2d);
        }
    }

    private void drawRomanticBackground(Graphics2D g2d, int width, int height) {
        // Gradient background to represent a dreamy pink atmosphere
        GradientPaint gp = new GradientPaint(0, 0, new Color(255, 182, 193), 0, height, new Color(255, 105, 180));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Add a subtle bokeh effect
        g2d.setColor(new Color(255, 255, 255, 50));
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            int size = rand.nextInt(30) + 10;
            g2d.fillOval(x, y, size, size);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all Valentine elements
    abstract class ValentineElement {
        double x, y, speed;

        abstract void move(int canvasWidth, int canvasHeight);

        abstract void draw(Graphics2D g2d);
    }

    // FloatingHeart class representing animated hearts
    class FloatingHeart extends ValentineElement {
        Color color;
        int size;

        FloatingHeart(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
            this.size = 20 + rand.nextInt(30);
        }

        void move(int canvasWidth, int canvasHeight) {
            y -= speed;
            if (y < 0) {
                y = canvasHeight;
                x = rand.nextInt(canvasWidth);
                color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                size = 20 + rand.nextInt(30);
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(color);
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.scale(size / 20.0, size / 20.0);
            g2d.fill(createHeartShape());
            g2d.setTransform(old);
        }

        private Shape createHeartShape() {
            Path2D heart = new Path2D.Double();
            heart.moveTo(0, -10);
            heart.curveTo(10, -20, 20, 0, 0, 15);
            heart.curveTo(-20, 0, -10, -20, 0, -10);
            heart.closePath();
            return heart;
        }
    }

    // Petal class representing falling petals
    class Petal extends ValentineElement {
        double angle;

        Petal(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.angle = rand.nextDouble() * 360;
        }

        void move(int canvasWidth, int canvasHeight) {
            y += speed;
            angle += 1;
            if (y > canvasHeight) {
                y = 0;
                x = rand.nextInt(canvasWidth);
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 105, 180, 150));
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(angle));
            g2d.fillOval(-5, -2, 10, 4);
            g2d.setTransform(old);
        }
    }

    // Sparkle class representing a twinkling effect
    class Sparkle extends ValentineElement {
        double opacity;

        Sparkle(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.opacity = 0.3 + rand.nextDouble() * 0.7;
        }

        void move(int canvasWidth, int canvasHeight) {
            y += speed;
            if (y > canvasHeight) {
                y = 0;
                x = rand.nextInt(canvasWidth);
                opacity = 0.3 + rand.nextDouble() * 0.7;
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, (int) (opacity * 255)));
            g2d.fillOval((int) x, (int) y, 5, 5);
        }
    }

    // Main method to test the Heartswake theme panel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Heartswake Theme - Valentine's Day Celebration");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            HeartswakeTheme heartswakeTheme = new HeartswakeTheme();
            frame.add(heartswakeTheme);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
