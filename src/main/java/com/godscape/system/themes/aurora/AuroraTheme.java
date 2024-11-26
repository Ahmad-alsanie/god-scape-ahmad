package com.godscape.system.themes.aurora;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

/**
 * AuroraTheme represents an animated night sky with flowing auroras and glowing stars.
 */
public class AuroraTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<AuroraElement> auroraElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public AuroraTheme() {
        auroraElements = new ArrayList<>();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add component listener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initAuroraElements(); // Reinitialize elements on resize
            }
        });

        initAuroraElements(); // Initial element creation
    }

    private void initAuroraElements() {
        auroraElements.clear(); // Clear previous elements

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            return; // Avoid initializing elements if dimensions are not set
        }

        // Add aurora waves
        for (int i = 0; i < 5; i++) {
            auroraElements.add(new AuroraWave(rand.nextInt(width), rand.nextInt(200), 1.0 + rand.nextDouble()));
        }

        // Add drifting stars
        for (int i = 0; i < 50; i++) {
            auroraElements.add(new Star(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        drawAuroraBackground(g2d, width, height);
        for (AuroraElement element : auroraElements) {
            element.move(width, height);
            element.draw(g2d);
        }
    }

    private void drawAuroraBackground(Graphics2D g2d, int width, int height) {
        // Gradient background to represent a starry night sky
        GradientPaint gp = new GradientPaint(0, 0, new Color(15, 15, 45), 0, height, new Color(5, 5, 25));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all Aurora elements
    abstract class AuroraElement {
        double x, y, speed;

        abstract void move(int canvasWidth, int canvasHeight);

        abstract void draw(Graphics2D g2d);
    }

    // AuroraWave class representing flowing aurora lights
    class AuroraWave extends AuroraElement {
        Color[] colors = {new Color(100, 255, 200, 100), new Color(200, 150, 255, 80), new Color(50, 200, 255, 120)};
        double waveOffset = rand.nextDouble() * Math.PI * 2;

        AuroraWave(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move(int canvasWidth, int canvasHeight) {
            x += Math.sin(waveOffset + frameCount / 100.0) * 2;
            if (x > canvasWidth + 100) x = -100;
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(colors[rand.nextInt(colors.length)]);
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.sin(frameCount / 50.0) * 0.2);
            g2d.fillRoundRect(0, 0, 200, 30, 15, 15);
            g2d.setTransform(old);
        }
    }

    // Star class for drifting stars
    class Star extends AuroraElement {
        Star(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move(int canvasWidth, int canvasHeight) {
            y += speed;
            if (y > canvasHeight) {
                y = -10;
                x = rand.nextInt(canvasWidth);
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, 200));
            int size = 2 + rand.nextInt(3);
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    // Main method to test the Aurora theme panel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aurora Theme - Northern Lights");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            AuroraTheme auroraTheme = new AuroraTheme();
            frame.add(auroraTheme);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
