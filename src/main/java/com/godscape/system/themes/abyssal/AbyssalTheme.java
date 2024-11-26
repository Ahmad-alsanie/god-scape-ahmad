package com.godscape.system.themes.abyssal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * AbyssalTheme.java
 *
 * This class creates a dynamic and immersive dark oceanic scene featuring ominous floating entities,
 * mysterious shadows, ghostly apparitions, and other unsettling elements. The scene auto-adapts
 * to the canvas size and includes animations to enhance the eerie atmosphere.
 */
public class AbyssalTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<AbyssalElement> abyssalElements;
    private Random rand = new Random();
    private int frameCount = 0;

    public AbyssalTheme() {
        abyssalElements = new ArrayList<>();
        initAbyssalElements();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initAbyssalElements(); // Reinitialize elements based on new size
            }
        });
    }

    /**
     * Initializes abyssal elements based on the current panel size.
     */
    private void initAbyssalElements() {
        abyssalElements.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Add ghostly apparitions
        for (int i = 0; i < 5; i++) {
            abyssalElements.add(new GhostlyApparition(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }

        // Add floating shadows
        for (int i = 0; i < 7; i++) {
            abyssalElements.add(new FloatingShadow(rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble()));
        }

        // Add ominous eyes
        for (int i = 0; i < 10; i++) {
            abyssalElements.add(new OminousEyes(rand.nextInt(width), rand.nextInt(height / 2), rand.nextInt(360)));
        }

        // Add creeping mist
        abyssalElements.add(new CreepingMist(width, height));

        // Add dark tentacles
        for (int i = 0; i < 4; i++) {
            abyssalElements.add(new DarkTentacle(rand.nextInt(width), height + rand.nextInt(100), 0.5 + rand.nextDouble(), width, height));
        }

        // Add flickering lights
        for (int i = 0; i < 6; i++) {
            abyssalElements.add(new FlickeringLight(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
        }

        // Add background vortex
        abyssalElements.add(new BackgroundVortex(width / 2, height / 2));
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

        drawAbyssalBackground(g2d, width, height);

        // Draw all abyssal elements
        for (AbyssalElement element : abyssalElements) {
            element.move();
            element.draw(g2d, frameCount);
        }

        g2d.dispose();
    }

    /**
     * Draws the abyssal background with gradient darkness and subtle textures.
     */
    private void drawAbyssalBackground(Graphics2D g2d, int width, int height) {
        // Draw gradient background for the abyss
        GradientPaint gradient = new GradientPaint(0, 0, new Color(10, 10, 20),
                0, height, new Color(0, 0, 0));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

        // Add subtle textures or patterns if desired
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all abyssal elements
    abstract class AbyssalElement {
        double x, y, speed;

        abstract void move();

        abstract void draw(Graphics2D g2d, int frameCount);
    }

    // GhostlyApparition class
    class GhostlyApparition extends AbyssalElement {
        private double alphaOffset;

        GhostlyApparition(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.alphaOffset = rand.nextDouble() * Math.PI * 2;
        }

        @Override
        void move() {
            y -= speed;
            if (y < -100) {
                y = getHeight() + 100;
                x = rand.nextInt(getWidth());
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            float alpha = (float) (0.2 + 0.2 * Math.sin((frameCount / 10.0) + alphaOffset));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(new Color(150, 150, 200));
            g2d.fillOval((int) x - 30, (int) y - 50, 60, 100);
            // Draw subtle facial features
            g2d.setColor(new Color(100, 100, 150));
            g2d.fillOval((int) x - 10, (int) y - 20, 10, 10); // Left eye
            g2d.fillOval((int) x + 10, (int) y - 20, 10, 10); // Right eye
            // Reset composite
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    // FloatingShadow class
    class FloatingShadow extends AbyssalElement {
        FloatingShadow(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed * (rand.nextBoolean() ? 1 : -1);
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(x / 50.0) * 2;
            if (x > getWidth() + 100 || x < -100) {
                speed = -speed;
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(20, 20, 30, 150));
            g2d.fillOval((int) x - 50, (int) y - 50, 100, 100);
        }
    }

    // OminousEyes class
    class OminousEyes extends AbyssalElement {
        private int angle;

        OminousEyes(double x, double y, int angle) {
            this.x = x;
            this.y = y;
            this.speed = 0;
            this.angle = angle;
        }

        @Override
        void move() {
            // Eyes can flicker or move slightly if desired
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(255, 0, 0, 200));
            // Left eye
            g2d.fillOval((int) x - 15, (int) y, 10, 5);
            // Right eye
            g2d.fillOval((int) x + 5, (int) y, 10, 5);
        }
    }

    // CreepingMist class
    class CreepingMist extends AbyssalElement {
        private ArrayList<MistParticle> mistParticles;

        CreepingMist(int width, int height) {
            this.x = 0;
            this.y = 0;
            this.speed = 0;
            mistParticles = new ArrayList<>();
            // Initialize mist particles
            for (int i = 0; i < 150; i++) {
                mistParticles.add(new MistParticle(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
            }
        }

        @Override
        void move() {
            for (MistParticle particle : mistParticles) {
                particle.move();
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            for (MistParticle particle : mistParticles) {
                particle.draw(g2d);
            }
        }

        // Inner class for individual mist particles
        class MistParticle {
            double x, y, speed;

            MistParticle(double x, double y, double speed) {
                this.x = x;
                this.y = y;
                this.speed = speed;
            }

            void move() {
                x += speed / 2;
                y += speed / 4;
                if (x > getWidth()) {
                    x = -50;
                }
                if (y > getHeight()) {
                    y = -50;
                }
            }

            void draw(Graphics2D g2d) {
                g2d.setColor(new Color(50, 50, 70, 50));
                g2d.fillOval((int) x, (int) y, 50, 30);
            }
        }
    }

    // DarkTentacle class
    class DarkTentacle extends AbyssalElement {
        private double swayOffset;
        private int panelWidth, panelHeight;

        DarkTentacle(double x, double y, double speed, int width, int height) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.swayOffset = rand.nextDouble() * Math.PI * 2;
            this.panelWidth = width;
            this.panelHeight = height;
        }

        @Override
        void move() {
            y -= speed;
            if (y < -200) {
                y = panelHeight + 100 + rand.nextInt(200);
                x = rand.nextInt(panelWidth);
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(10, 10, 20, 200));
            Path2D path = new Path2D.Double();
            path.moveTo(x, y);
            double sway = Math.sin((frameCount / 20.0) + swayOffset) * 20;
            path.curveTo(x + sway, y - 50, x - sway, y - 100, x, y - 150);
            g2d.setStroke(new BasicStroke(10));
            g2d.draw(path);
        }
    }

    // FlickeringLight class
    class FlickeringLight extends AbyssalElement {
        private double flickerRate;

        FlickeringLight(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.flickerRate = rand.nextDouble() * 0.1 + 0.05;
        }

        @Override
        void move() {
            y += Math.sin((frameCount / 10.0)) * speed;
            x += Math.cos((frameCount / 10.0)) * speed;
            if (x > getWidth() + 50 || x < -50 || y > getHeight() + 50 || y < -50) {
                x = rand.nextInt(getWidth());
                y = rand.nextInt(getHeight());
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            float brightness = (float) (0.5 + 0.5 * Math.sin(frameCount * flickerRate));
            g2d.setColor(new Color(200, 200, 50, (int) (brightness * 255)));
            g2d.fillOval((int) x - 10, (int) y - 10, 20, 20);
        }
    }

    // BackgroundVortex class
    class BackgroundVortex extends AbyssalElement {
        BackgroundVortex(double x, double y) {
            this.x = x;
            this.y = y;
            this.speed = 0;
        }

        @Override
        void move() {
            // Static vortex; could rotate if desired
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(frameCount % 360));
            g2d.setColor(new Color(20, 20, 30, 100));
            for (int i = 0; i < 5; i++) {
                g2d.drawOval(-50 - i * 20, -50 - i * 20, 100 + i * 40, 100 + i * 40);
            }
            g2d.setTransform(old);
        }
    }

    /**
     * Main method to test the AbyssalTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Abyssal Theme - Dark Oceanic Abyss");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            AbyssalTheme abyssalTheme = new AbyssalTheme();
            frame.add(abyssalTheme);
            frame.setSize(new Dimension(1000, 800)); // Initial size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
