package com.godscape.system.themes.neon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

/**
 * NeonTheme represents an animated futuristic cityscape with neon lights, signs, and drifting holograms.
 */
public class NeonTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<NeonElement> neonElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public NeonTheme() {
        neonElements = new ArrayList<>();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add component listener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initNeonElements(); // Reinitialize elements on resize
            }
        });

        initNeonElements(); // Initial element creation
    }

    private void initNeonElements() {
        neonElements.clear(); // Clear previous elements

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            return; // Avoid initializing elements if dimensions are not set
        }

        // Add neon signs
        for (int i = 0; i < 10; i++)
            neonElements.add(new NeonSign(rand.nextInt(width), rand.nextInt(height - 100), 0.5 + rand.nextDouble()));

        // Add drifting holograms
        for (int i = 0; i < 30; i++)
            neonElements.add(new Hologram(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        drawCityBackground(g2d, width, height);
        for (NeonElement element : neonElements) {
            element.move(width, height);
            element.draw(g2d);
        }
    }

    private void drawCityBackground(Graphics2D g2d, int width, int height) {
        // Gradient background to represent the night sky with a neon glow
        GradientPaint gp = new GradientPaint(0, 0, new Color(10, 10, 30), 0, height, new Color(40, 10, 60));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Draw silhouettes of buildings
        g2d.setColor(new Color(30, 30, 30));
        for (int i = 0; i < 15; i++) {
            int buildingWidth = 50 + rand.nextInt(100);
            int buildingHeight = 200 + rand.nextInt(300);
            int x = rand.nextInt(width);
            int y = height - buildingHeight;
            g2d.fillRect(x, y, buildingWidth, buildingHeight);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all Neon City elements
    abstract class NeonElement {
        double x, y, speed;

        abstract void move(int canvasWidth, int canvasHeight);

        abstract void draw(Graphics2D g2d);
    }

    // NeonSign class representing glowing signs
    class NeonSign extends NeonElement {
        Color[] colors = {Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.PINK};

        NeonSign(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move(int canvasWidth, int canvasHeight) {
            x += speed;
            if (x > canvasWidth + 50) x = -50;
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(colors[rand.nextInt(colors.length)]);
            int signWidth = 80 + rand.nextInt(40);
            int signHeight = 20 + rand.nextInt(10);
            g2d.fillRoundRect((int) x, (int) y, signWidth, signHeight, 10, 10);

            g2d.setColor(Color.BLACK);
            g2d.drawString("NEON", (int) x + 10, (int) y + 15);
        }
    }

    // Hologram class for floating holographic projections
    class Hologram extends NeonElement {
        Hologram(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move(int canvasWidth, int canvasHeight) {
            y += speed;
            x += Math.sin(y / 40) * 0.2;
            if (y > canvasHeight) {
                y = -50;
                x = rand.nextInt(canvasWidth);
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(0, 255, 255, 100));
            int size = 30 + rand.nextInt(20);
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    // Main method to test the Neon theme panel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Neon Theme - Futuristic Cityscape");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            NeonTheme neonTheme = new NeonTheme();
            frame.add(neonTheme);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
