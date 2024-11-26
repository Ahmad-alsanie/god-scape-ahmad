package com.godscape.system.themes.shadowfest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * ShadowfestTheme.java
 *
 * This class creates a dynamic and immersive haunted cemetery scene with animated elements such as ghosts,
 * bats, spirits, lanterns, skeletons, eerie fog, unearthly auras, and mysterious glows.
 */
public class ShadowfestTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<SpookyElement> spookyElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public ShadowfestTheme() {
        spookyElements = new ArrayList<>();
        initSpookyElements();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initSpookyElements(); // Reinitialize elements based on new size
            }
        });
    }

    /**
     * Initializes spooky elements based on the current panel size.
     */
    private void initSpookyElements() {
        spookyElements.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Add ghosts
        for (int i = 0; i < 5; i++) {
            spookyElements.add(new Ghost(rand.nextInt(width), rand.nextInt(height / 2), 1 + rand.nextDouble()));
        }

        // Add bats
        for (int i = 0; i < 10; i++) {
            spookyElements.add(new Bat(rand.nextInt(width), rand.nextInt(height / 3), 2 + rand.nextDouble()));
        }

        // Add spirits
        for (int i = 0; i < 7; i++) {
            spookyElements.add(new Spirit(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }

        // Add lanterns
        for (int i = 0; i < 4; i++) {
            double xPos = 100 + i * (width / 5.0);
            spookyElements.add(new Lantern(xPos, height - 150));
        }

        // Add skeletons
        for (int i = 0; i < 3; i++) {
            spookyElements.add(new Skeleton(rand.nextInt(width), height - 100, 1 + rand.nextDouble()));
        }

        // Add eerie fog
        spookyElements.add(new EerieFog(width, height));

        // Add unearthly auras
        for (int i = 0; i < 3; i++) {
            spookyElements.add(new UnearthlyAura(rand.nextInt(width), rand.nextInt(height)));
        }

        // Add mysterious glows
        for (int i = 0; i < 5; i++) {
            spookyElements.add(new MysteriousGlow(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }

        // Add haunted house
        spookyElements.add(new HauntedHouse(width / 2 - 100, height - 300));
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

        // Draw all spooky elements
        for (SpookyElement element : spookyElements) {
            element.move();
            element.draw(g2d, frameCount);
        }

        g2d.dispose();
    }

    /**
     * Draws the haunted cemetery background with a dark sky, moon, and distant trees.
     */
    private void drawBackground(Graphics2D g2d, int width, int height) {
        // Draw gradient background for the night sky
        GradientPaint gp = new GradientPaint(0, 0, new Color(10, 10, 30),
                0, height, new Color(0, 0, 0));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Draw moon
        g2d.setColor(new Color(200, 200, 220, 200));
        int moonSize = 80;
        g2d.fillOval(width - moonSize - 50, 50, moonSize, moonSize);

        // Draw distant trees silhouette
        g2d.setColor(new Color(0, 0, 0, 150));
        for (int i = 0; i < width; i += 50) {
            int treeHeight = 100 + rand.nextInt(50);
            g2d.fillRect(i, height - treeHeight, 20, treeHeight);
        }

        // Draw ground
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRect(0, height - 50, width, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all spooky elements
    abstract class SpookyElement {
        double x, y, speed;

        abstract void move();

        abstract void draw(Graphics2D g2d, int frameCount);
    }

    // Ghost class
    class Ghost extends SpookyElement {
        private double alphaOffset;

        Ghost(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.alphaOffset = rand.nextDouble() * Math.PI * 2;
        }

        @Override
        void move() {
            y += Math.sin((frameCount / 20.0) + alphaOffset) * 0.5;
            x += speed;
            if (x > getWidth() + 50) {
                x = -50;
                y = rand.nextInt(getHeight() / 2);
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            float alpha = (float) (0.5 + 0.5 * Math.sin((frameCount / 20.0) + alphaOffset));
            alpha = Math.min(1.0f, Math.max(0.2f, alpha));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(new Color(200, 200, 255));
            g2d.fillOval((int) x, (int) y, 40, 50);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    // Bat class
    class Bat extends SpookyElement {
        private int wingFlap;

        Bat(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.wingFlap = 0;
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(frameCount / 10.0) * 2;
            if (x > getWidth() + 50) {
                x = -50;
                y = rand.nextInt(getHeight() / 3);
            }
            wingFlap = (wingFlap + 1) % 20;
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(Color.BLACK);
            int flap = wingFlap < 10 ? 10 : -10;
            Polygon batShape = new Polygon();
            batShape.addPoint((int) x, (int) y);
            batShape.addPoint((int) x + 20, (int) y + flap);
            batShape.addPoint((int) x + 40, (int) y);
            batShape.addPoint((int) x + 20, (int) y - flap);
            g2d.fill(batShape);
        }
    }

    // Spirit class
    class Spirit extends SpookyElement {
        private double alphaOffset;

        Spirit(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.alphaOffset = rand.nextDouble() * Math.PI * 2;
        }

        @Override
        void move() {
            y -= speed;
            if (y < -50) {
                y = getHeight() + 50;
                x = rand.nextInt(getWidth());
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            float alpha = (float) (0.3 + 0.3 * Math.sin((frameCount / 10.0) + alphaOffset));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(new Color(150, 150, 255));
            g2d.fillOval((int) x, (int) y, 30, 30);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    // Lantern class
    class Lantern extends SpookyElement {
        private double flickerRate;

        Lantern(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
            this.flickerRate = rand.nextDouble() * 0.1 + 0.05;
        }

        @Override
        void move() {
            // Static lantern; no movement
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            float brightness = (float) (0.7 + 0.3 * Math.sin(frameCount * flickerRate));
            g2d.setColor(new Color(255, 200, 50));
            g2d.fillRect((int) x - 5, (int) y - 20, 10, 20); // Lantern body
            g2d.setColor(new Color(255, 255, 200, (int) (brightness * 255)));
            g2d.fillOval((int) x - 10, (int) y - 30, 20, 20); // Lantern glow
        }
    }

    // Skeleton class
    class Skeleton extends SpookyElement {
        Skeleton(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed * (rand.nextBoolean() ? 1 : -1);
        }

        @Override
        void move() {
            x += speed;
            if (x > getWidth() + 50 || x < -50) {
                speed = -speed; // Reverse direction
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(220, 220, 220));
            // Draw head
            g2d.fillOval((int) x - 10, (int) y - 40, 20, 20);
            // Draw body
            g2d.drawLine((int) x, (int) y - 20, (int) x, (int) y);
            // Draw arms
            g2d.drawLine((int) x, (int) y - 15, (int) x - 10, (int) y - 5);
            g2d.drawLine((int) x, (int) y - 15, (int) x + 10, (int) y - 5);
            // Draw legs
            g2d.drawLine((int) x, (int) y, (int) x - 5, (int) y + 15);
            g2d.drawLine((int) x, (int) y, (int) x + 5, (int) y + 15);
        }
    }

    // EerieFog class
    class EerieFog extends SpookyElement {
        private ArrayList<FogParticle> fogParticles;

        EerieFog(int width, int height) {
            this.x = 0;
            this.y = 0;
            this.speed = 0;
            fogParticles = new ArrayList<>();
            // Initialize fog particles
            for (int i = 0; i < 200; i++) {
                fogParticles.add(new FogParticle(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
            }
        }

        @Override
        void move() {
            for (FogParticle particle : fogParticles) {
                particle.move();
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            for (FogParticle particle : fogParticles) {
                particle.draw(g2d);
            }
        }

        // Inner class for individual fog particles
        class FogParticle {
            double x, y, speed;

            FogParticle(double x, double y, double speed) {
                this.x = x;
                this.y = y;
                this.speed = speed;
            }

            void move() {
                x += speed / 2;
                y -= speed / 4;
                if (x > getWidth()) {
                    x = -50;
                }
                if (y < -50) {
                    y = getHeight() + 50;
                }
            }

            void draw(Graphics2D g2d) {
                g2d.setColor(new Color(100, 100, 100, 50));
                g2d.fillOval((int) x, (int) y, 50, 30);
            }
        }
    }

    // UnearthlyAura class
    class UnearthlyAura extends SpookyElement {
        private double pulseRate;

        UnearthlyAura(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
            this.pulseRate = rand.nextDouble() * 0.1 + 0.05;
        }

        @Override
        void move() {
            // Static aura; no movement
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            float alpha = (float) (0.3 + 0.3 * Math.sin(frameCount * pulseRate));
            g2d.setColor(new Color(100, 0, 150, (int) (alpha * 255)));
            g2d.fillOval((int) x - 30, (int) y - 30, 60, 60);
        }
    }

    // MysteriousGlow class
    class MysteriousGlow extends SpookyElement {
        private double glowRate;

        MysteriousGlow(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.glowRate = rand.nextDouble() * 0.1 + 0.05;
        }

        @Override
        void move() {
            y -= speed;
            if (y < -50) {
                y = getHeight() + 50;
                x = rand.nextInt(getWidth());
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            float alpha = (float) (0.3 + 0.3 * Math.sin(frameCount * glowRate));
            g2d.setColor(new Color(50, 255, 50, (int) (alpha * 255)));
            g2d.fillOval((int) x - 10, (int) y - 10, 20, 20);
        }
    }

    // HauntedHouse class
    class HauntedHouse extends SpookyElement {
        HauntedHouse(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
        }

        @Override
        void move() {
            // Static haunted house; no movement
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            // Draw the main house structure
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRect((int) x, (int) y, 200, 200);

            // Draw roof
            Polygon roof = new Polygon();
            roof.addPoint((int) x, (int) y);
            roof.addPoint((int) x + 100, (int) y - 80);
            roof.addPoint((int) x + 200, (int) y);
            g2d.fillPolygon(roof);

            // Draw windows with flickering lights
            float brightness = (float) (0.5 + 0.5 * Math.sin(frameCount * 0.1));
            g2d.setColor(new Color(255, 255, 0, (int) (brightness * 255)));
            g2d.fillRect((int) x + 50, (int) y + 50, 30, 40);
            g2d.fillRect((int) x + 120, (int) y + 50, 30, 40);

            // Draw door
            g2d.setColor(new Color(30, 30, 30));
            g2d.fillRect((int) x + 85, (int) y + 150, 30, 50);
        }
    }

    /**
     * Main method to test the ShadowfestTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Shadowfest Theme - Haunted Cemetery");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ShadowfestTheme shadowfestTheme = new ShadowfestTheme();
            frame.add(shadowfestTheme);
            frame.setSize(new Dimension(1000, 800)); // Initial size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
