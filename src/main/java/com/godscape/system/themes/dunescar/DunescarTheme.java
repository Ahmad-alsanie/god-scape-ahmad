package com.godscape.system.themes.dunescar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * DunescarTheme.java
 *
 * This class creates a dynamic and immersive desert landscape featuring rolling sand dunes,
 * drifting sand particles, a radiant sun, heat haze effects, moving camels, sandstorms,
 * flying birds, animated mirages, and more.
 */
public class DunescarTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<DesertObject> desertObjects;
    private Random rand = new Random();
    private int frameCount = 0;

    public DunescarTheme() {
        desertObjects = new ArrayList<>();
        initDesertObjects();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initDesertObjects(); // Reinitialize desert objects based on new size
            }
        });
    }

    /**
     * Initializes desert objects based on the current panel size.
     */
    private void initDesertObjects() {
        desertObjects.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Add static objects
        desertObjects.add(new Oasis(width * 0.5, height * 0.7, 0, width, height));
        desertObjects.add(new Rocks(width * 0.25, height - 50, 0, width, height));
        desertObjects.add(new Rocks(width * 0.75, height - 60, 0, width, height));

        // Add cacti
        for (int i = 0; i < 5; i++) {
            double xPos = 100 + i * (width / 6.0);
            desertObjects.add(new Cactus(xPos, height - 80, width, height));
        }

        // Add sand dunes
        for (int i = 0; i < 3; i++) {
            double xPos = rand.nextInt(width);
            desertObjects.add(new SandDunes(xPos, height - 100 - i * 50, width, height));
        }

        // Add sand particles
        for (int i = 0; i < 200; i++) { // Increased number for denser sand effect
            desertObjects.add(new SandParticle(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble(), width, height));
        }

        // Add clouds
        for (int i = 0; i < 5; i++) {
            desertObjects.add(new Cloud(rand.nextInt(width), rand.nextInt(height / 3), 0.2 + rand.nextDouble() * 0.5, width, height));
        }

        // Add mirages
        for (int i = 0; i < 3; i++) {
            double xPos = rand.nextInt(width);
            double yPos = height - 50 - rand.nextInt(100);
            desertObjects.add(new Mirage(xPos, yPos, width, height));
        }

        // Add moving camels
        for (int i = 0; i < 3; i++) {
            double xPos = rand.nextInt(width);
            double yPos = height - 70 - rand.nextInt(30);
            double speed = 1 + rand.nextDouble() * 1.5;
            desertObjects.add(new Camel(xPos, yPos, speed, width, height));
        }

        // Add sandstorms
        desertObjects.add(new SandStorm(width, height, width, height));

        // Add flying birds
        for (int i = 0; i < 5; i++) {
            double xPos = rand.nextInt(width);
            double yPos = rand.nextInt(height / 3);
            double speed = 2 + rand.nextDouble() * 2;
            desertObjects.add(new Bird(xPos, yPos, speed, width, height));
        }

        // Add sun
        desertObjects.add(new Sun(width - 100, 50, 0.5, width, height));
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

        drawDesertBackground(g2d, width, height);

        // Draw all desert objects
        for (DesertObject obj : desertObjects) {
            obj.move();
            obj.draw(g2d, frameCount);
        }

        g2d.dispose();
    }

    /**
     * Draws the desert background with gradient sky, sun, and heat haze effects.
     */
    private void drawDesertBackground(Graphics2D g2d, int width, int height) {
        // Draw gradient background for the sky
        GradientPaint gp = new GradientPaint(0, 0, new Color(255, 204, 153),
                0, height, new Color(255, 140, 0));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Draw heat haze (wavy lines)
        g2d.setColor(new Color(255, 255, 255, 30));
        for (int i = 0; i < 50; i++) {
            int y = (frameCount + i * 5) % height;
            g2d.drawLine(0, y, width, y + (int) (Math.sin(frameCount / 10.0 + i) * 5));
        }

        // Draw ground (sand)
        g2d.setColor(new Color(210, 180, 140));
        g2d.fillRect(0, height - 50, width, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all desert objects
    abstract class DesertObject {
        double x, y, speed;
        int panelWidth, panelHeight;

        DesertObject(double x, double y, double speed, int width, int height) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.panelWidth = width;
            this.panelHeight = height;
        }

        abstract void move();
        abstract void draw(Graphics2D g2d, int frameCount);
    }

    // Oasis class
    class Oasis extends DesertObject {
        Oasis(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            // Static object; no movement
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(0, 100, 255, 150));
            g2d.fillOval((int) x - 50, (int) y - 20, 100, 40);
            g2d.setColor(new Color(0, 150, 50));
            g2d.drawArc((int) x - 55, (int) y - 30, 110, 60, 0, 180);
        }
    }

    // SandDunes class
    class SandDunes extends DesertObject {
        SandDunes(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            // Static object
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(194, 178, 128));
            g2d.fillArc((int) x, (int) y, 200, 100, 0, 180);
        }
    }

    // Cactus class
    class Cactus extends DesertObject {
        Cactus(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            // Static object
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRect((int) x, (int) y - 40, 10, 40);
            g2d.fillRect((int) x - 5, (int) y - 25, 5, 15);
            g2d.fillRect((int) x + 10, (int) y - 25, 5, 15);
        }
    }

    // Rocks class
    class Rocks extends DesertObject {
        Rocks(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            // Static object
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(Color.GRAY);
            g2d.fillOval((int) x, (int) y, 30, 20);
            g2d.fillOval((int) x + 20, (int) y + 10, 20, 15);
        }
    }

    // SandParticle class
    class SandParticle extends DesertObject {
        SandParticle(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth) {
                x = -rand.nextInt(panelWidth);
                y = rand.nextInt(panelHeight);
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(255, 228, 181, 50));
            int size = 2 + rand.nextInt(3);
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    // Cloud class with slow drifting animation
    class Cloud extends DesertObject {
        Cloud(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth + 100) {
                x = -100;
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.fillOval((int) x, (int) y, 100, 40);
            g2d.fillOval((int) x + 30, (int) y - 20, 60, 30);
            g2d.fillOval((int) x + 60, (int) y + 10, 80, 40);
        }
    }

    // Mirage class with shimmering effect
    class Mirage extends DesertObject {
        double shimmerOffset;

        Mirage(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
            shimmerOffset = rand.nextDouble() * Math.PI * 2;
        }

        @Override
        void move() {
            // Mirage effect simulates shimmering
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(255, 204, 153, 100));
            double shimmer = Math.sin((frameCount / 10.0) + shimmerOffset) * 5;
            g2d.fillRect((int) (x - shimmer), (int) y, 80, 10);
        }
    }

    // Camel class representing moving camels
    class Camel extends DesertObject {
        Camel(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth + 50) {
                x = -100;
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            // Draw camel body
            g2d.setColor(new Color(160, 82, 45)); // Brown color
            g2d.fillOval((int) x, (int) y - 10, 40, 20); // Body
            g2d.fillOval((int) x + 30, (int) y - 20, 30, 20); // Hump

            // Draw legs
            g2d.setColor(new Color(139, 69, 19));
            g2d.fillRect((int) x + 5, (int) y + 10, 5, 10);
            g2d.fillRect((int) x + 15, (int) y + 10, 5, 10);
            g2d.fillRect((int) x + 25, (int) y + 10, 5, 10);
            g2d.fillRect((int) x + 35, (int) y + 10, 5, 10);

            // Draw head
            g2d.fillOval((int) x + 50, (int) y - 25, 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.fillOval((int) x + 55, (int) y - 23, 3, 3); // Eye
        }
    }

    // SandStorm class representing a sandstorm effect
    class SandStorm extends DesertObject {
        private ArrayList<SandParticle> stormParticles;

        SandStorm(int width, int height, int panelWidth, int panelHeight) {
            super(width, height, 0.5, panelWidth, panelHeight);
            stormParticles = new ArrayList<>();
            // Initialize storm particles
            for (int i = 0; i < 300; i++) { // Increased number for dense sandstorm
                stormParticles.add(new SandParticle(rand.nextInt(panelWidth), rand.nextInt(panelHeight), 1 + rand.nextDouble(), panelWidth, panelHeight));
            }
        }

        @Override
        void move() {
            for (SandParticle particle : stormParticles) {
                particle.move();
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            // Draw semi-transparent overlay to simulate sandstorm
            g2d.setColor(new Color(210, 180, 140, 100));
            for (SandParticle particle : stormParticles) {
                g2d.fillOval((int) particle.x, (int) particle.y, 2, 2);
            }
        }
    }

    // Bird class representing flying birds
    class Bird extends DesertObject {
        private int wingFlap;

        Bird(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
            this.wingFlap = 0;
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth + 50) {
                x = -50;
                y = rand.nextInt(panelHeight / 3);
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            wingFlap = (wingFlap + 1) % 20;
            g2d.setColor(Color.BLACK);
            int flap = wingFlap < 10 ? 10 : -10;

            // Draw simple bird shape with flapping wings
            g2d.drawLine((int) x, (int) y, (int) x + 10, (int) y - flap);
            g2d.drawLine((int) x, (int) y, (int) x + 10, (int) y + flap);
        }
    }

    // Sun class representing the setting sun
    class Sun extends DesertObject {
        Sun(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x -= speed;
            y += speed * 0.5; // Move downward as it sets
            if (x < -50) {
                x = panelWidth + 50;
                y = 50;
            }
        }

        @Override
        void draw(Graphics2D g2d, int frameCount) {
            g2d.setColor(new Color(255, 255, 100, 200));
            int sunSize = 80;
            g2d.fillOval((int) x, (int) y, sunSize, sunSize);

            // Draw sun rays
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < 12; i++) {
                double angle = Math.toRadians(i * 30);
                int startX = (int) (x + sunSize / 2 + Math.cos(angle) * sunSize / 2);
                int startY = (int) (y + sunSize / 2 + Math.sin(angle) * sunSize / 2);
                int endX = (int) (x + sunSize / 2 + Math.cos(angle) * (sunSize / 2 + 20));
                int endY = (int) (y + sunSize / 2 + Math.sin(angle) * (sunSize / 2 + 20));
                g2d.drawLine(startX, startY, endX, endY);
            }
        }
    }

    /**
     * Main method to test the DunescarTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Dunescar Desert Theme");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            DunescarTheme dunescarTheme = new DunescarTheme();
            frame.add(dunescarTheme);
            frame.setSize(new Dimension(1000, 800)); // Increased initial size for better visibility
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
