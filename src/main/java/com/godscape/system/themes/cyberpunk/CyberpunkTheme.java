package com.godscape.system.themes.cyberpunk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * CyberpunkTheme represents an animated cyberpunk city scene with neon lights, flying cars, and rain.
 */
public class CyberpunkTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<CyberpunkElement> cyberpunkElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public CyberpunkTheme() {
        cyberpunkElements = new ArrayList<>();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add component listener to handle window resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initCyberpunkElements(); // Reinitialize elements on resize
            }
        });

        initCyberpunkElements(); // Initial element creation
    }

    private void initCyberpunkElements() {
        cyberpunkElements.clear(); // Clear previous elements

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            return; // Avoid initializing elements if dimensions are not set
        }

        // Add neon lights on buildings
        for (int i = 0; i < 10; i++) {
            cyberpunkElements.add(new NeonLight(rand.nextInt(width), rand.nextInt(height - 200), rand.nextInt(3) + 1));
        }

        // Add flying cars
        for (int i = 0; i < 5; i++) {
            cyberpunkElements.add(new FlyingCar(rand.nextInt(width), rand.nextInt(height / 2), 2 + rand.nextDouble()));
        }

        // Add rain drops
        for (int i = 0; i < 100; i++) {
            cyberpunkElements.add(new RainDrop(rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble() * 2));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        drawCyberpunkBackground(g2d, width, height);
        for (CyberpunkElement element : cyberpunkElements) {
            element.move(width, height);
            element.draw(g2d);
        }
    }

    private void drawCyberpunkBackground(Graphics2D g2d, int width, int height) {
        // Gradient background to represent a neon-lit night sky
        GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40), 0, height, new Color(0, 0, 0));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Draw city skyline silhouette
        g2d.setColor(new Color(30, 30, 30));
        for (int i = 0; i < width; i += 80) {
            int buildingHeight = rand.nextInt(height / 3) + height / 3;
            g2d.fillRect(i, height - buildingHeight, 60, buildingHeight);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all Cyberpunk elements
    abstract class CyberpunkElement {
        double x, y, speed;

        abstract void move(int canvasWidth, int canvasHeight);

        abstract void draw(Graphics2D g2d);
    }

    // NeonLight class representing flickering neon lights on buildings
    class NeonLight extends CyberpunkElement {
        Color[] colors = {Color.MAGENTA, Color.CYAN, Color.YELLOW};
        int size;

        NeonLight(double x, double y, int size) {
            this.x = x;
            this.y = y;
            this.size = size * 10;
        }

        void move(int canvasWidth, int canvasHeight) {
            // Static neon light; no movement
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(colors[rand.nextInt(colors.length)]);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f + rand.nextFloat() * 0.3f));
            g2d.fillRect((int) x, (int) y, size, size / 2);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    // FlyingCar class representing cars flying in the night sky
    class FlyingCar extends CyberpunkElement {
        FlyingCar(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move(int canvasWidth, int canvasHeight) {
            x += speed;
            if (x > canvasWidth) {
                x = -50;
                y = rand.nextInt(canvasHeight / 2);
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(0, 255, 255));
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.fillRoundRect(0, 0, 50, 20, 10, 10);
            g2d.setTransform(old);

            // Draw neon trail
            g2d.setColor(new Color(0, 255, 255, 80));
            for (int i = 0; i < 5; i++) {
                g2d.drawLine((int) x - i * 10, (int) y + 10, (int) x, (int) y + 10);
            }
        }
    }

    // RainDrop class representing falling rain
    class RainDrop extends CyberpunkElement {
        RainDrop(double x, double y, double speed) {
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
            g2d.setColor(new Color(173, 216, 230, 150));
            g2d.drawLine((int) x, (int) y, (int) x, (int) y + 10);
        }
    }

    // Main method to test the Cyberpunk theme panel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cyberpunk Theme - Futuristic City");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            CyberpunkTheme cyberpunkTheme = new CyberpunkTheme();
            frame.add(cyberpunkTheme);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
