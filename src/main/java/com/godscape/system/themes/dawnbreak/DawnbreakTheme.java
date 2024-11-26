package com.godscape.system.themes.dawnbreak;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * DawnbreakTheme represents a New Year's celebration scene with fireworks, sparkling lights, and festive elements.
 */
public class DawnbreakTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<NewYearElement> newYearElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public DawnbreakTheme() {
        newYearElements = new ArrayList<>();
        timer = new Timer(30, this); // ~33 FPS for smooth animations
        timer.start();

        // Add component listener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initNewYearElements(); // Reinitialize elements on resize
            }
        });

        initNewYearElements(); // Initial element creation
    }

    private void initNewYearElements() {
        newYearElements.clear(); // Clear previous elements

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            return; // Avoid initializing elements if dimensions are not set
        }

        // Add fireworks
        for (int i = 0; i < 5; i++) {
            newYearElements.add(new Firework(rand.nextInt(width), rand.nextInt(height / 2), 1 + rand.nextDouble() * 2));
        }

        // Add sparkling lights
        for (int i = 0; i < 100; i++) {
            newYearElements.add(new Sparkle(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }

        // Add festive confetti
        for (int i = 0; i < 50; i++) {
            newYearElements.add(new Confetti(rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        drawCelebrationBackground(g2d, width, height);
        for (NewYearElement element : newYearElements) {
            element.move(width, height);
            element.draw(g2d);
        }
    }

    private void drawCelebrationBackground(Graphics2D g2d, int width, int height) {
        // Gradient background to represent the vibrant sky
        GradientPaint gp = new GradientPaint(0, 0, new Color(10, 10, 40), 0, height, new Color(30, 30, 60));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Add shimmering effect to the sky
        g2d.setColor(new Color(255, 255, 255, 50));
        for (int i = 0; i < 200; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height / 2);
            int size = rand.nextInt(3) + 1;
            g2d.fillOval(x, y, size, size);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all New Year elements
    abstract class NewYearElement {
        double x, y, speed;

        abstract void move(int canvasWidth, int canvasHeight);

        abstract void draw(Graphics2D g2d);
    }

    // Firework class representing colorful fireworks
    class Firework extends NewYearElement {
        Color color;
        int size;

        Firework(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
            this.size = 50 + rand.nextInt(50);
        }

        void move(int canvasWidth, int canvasHeight) {
            y -= speed;
            if (y < 0) {
                y = canvasHeight - rand.nextInt(canvasHeight / 2);
                x = rand.nextInt(canvasWidth);
                color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
                size = 50 + rand.nextInt(50);
            }
        }

        void draw(Graphics2D g2d) {
            // Draw the explosion effect of the firework
            g2d.setColor(color);
            for (int i = 0; i < 8; i++) {
                int xOffset = (int) (Math.cos(i * Math.PI / 4) * size);
                int yOffset = (int) (Math.sin(i * Math.PI / 4) * size);
                g2d.drawLine((int) x, (int) y, (int) x + xOffset, (int) y + yOffset);
            }
        }
    }

    // Sparkle class representing sparkling lights
    class Sparkle extends NewYearElement {
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

    // Confetti class representing falling confetti
    class Confetti extends NewYearElement {
        Color color;
        int width, height;

        Confetti(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
            this.width = 5 + rand.nextInt(10);
            this.height = 5 + rand.nextInt(10);
        }

        void move(int canvasWidth, int canvasHeight) {
            y += speed;
            if (y > canvasHeight) {
                y = 0;
                x = rand.nextInt(canvasWidth);
                color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillRect((int) x, (int) y, width, height);
        }
    }

    // Main method to test the Dawnbreak theme panel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dawnbreak Theme - New Year's Celebration");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            DawnbreakTheme dawnbreakTheme = new DawnbreakTheme();
            frame.add(dawnbreakTheme);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
