package com.godscape.system.themes.oceanic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * OceanicTheme.java
 *
 * This class creates a dynamic and immersive ocean scene featuring various sea objects like
 * fish, sharks, whales, corals, and more. The scene adjusts automatically to the canvas size.
 */
public class OceanicTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<SeaObject> seaObjects;
    private Random rand = new Random();
    private int frameCount = 0;

    public OceanicTheme() {
        seaObjects = new ArrayList<>();
        initSeaObjects();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initSeaObjects(); // Reinitialize sea objects based on new size
            }
        });
    }

    /**
     * Initializes sea objects based on the current panel size.
     */
    private void initSeaObjects() {
        seaObjects.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Static objects
        seaObjects.add(new Shipwreck(width * 3 / 8.0, height * 2 / 3.0, 0, width, height));
        seaObjects.add(new CoralReef(width / 8.0, height - 100, 0, width, height));
        seaObjects.add(new CoralReef(width * 0.75, height - 50, 0, width, height));
        seaObjects.add(new Rocks(width / 4.0, height - 50, 0, width, height));
        seaObjects.add(new Rocks(width * 0.625, height - 60, 0, width, height));

        // Sea plants
        for (int i = 0; i < 15; i++) {
            double xPos = 50 + i * (width / 20.0);
            seaObjects.add(new SeaPlant(xPos, height - 50, width, height));
        }

        // Sea creatures
        for (int i = 0; i < 10; i++) {
            seaObjects.add(new Fish(-rand.nextInt(width), 100 + i * 50, 1 + rand.nextDouble() * 2, width, height));
        }
        for (int i = 0; i < 5; i++) {
            seaObjects.add(new Shark(-rand.nextInt(width * 2), 200 + i * 80, 2 + rand.nextDouble(), width, height));
        }
        for (int i = 0; i < 3; i++) {
            seaObjects.add(new Dolphin(-rand.nextInt(width * 3), 150 + i * 100, 3 + rand.nextDouble(), width, height));
        }
        for (int i = 0; i < 2; i++) {
            seaObjects.add(new Whale(-rand.nextInt(width * 4), 100 + i * 150, 1 + rand.nextDouble(), width, height));
        }
        for (int i = 0; i < 5; i++) {
            seaObjects.add(new Octopus(rand.nextInt(width), height + rand.nextInt(400), 0.5 + rand.nextDouble(), width, height));
        }
        for (int i = 0; i < 7; i++) {
            seaObjects.add(new Crab(rand.nextInt(width), height - 30 - rand.nextInt(20), 0.5 + rand.nextDouble(), width, height));
        }
        for (int i = 0; i < 10; i++) {
            seaObjects.add(new Starfish(rand.nextInt(width), height - 20 - rand.nextInt(30), width, height));
        }
        for (int i = 0; i < 5; i++) {
            seaObjects.add(new Seahorse(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble(), width, height));
        }
        for (int i = 0; i < 50; i++) {
            seaObjects.add(new Bubble(rand.nextInt(width), height + rand.nextInt(600), 0.5 + rand.nextDouble(), width, height));
        }
        for (int i = 0; i < 100; i++) {
            seaObjects.add(new SmallFish(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble(), width, height));
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
        for (SeaObject obj : seaObjects) {
            obj.move();
            obj.draw(g2d);
        }

        g2d.dispose();
    }

    /**
     * Draws the ocean background with gradient sky, sun rays, caustic light patterns, and sea floor.
     */
    private void drawBackground(Graphics2D g2d, int width, int height) {
        // Draw gradient background for the sky and water
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 30, 60),
                0, height, new Color(0, 0, 139));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Draw sun rays
        g2d.setColor(new Color(255, 255, 200, 30));
        for (int i = 0; i < 5; i++) {
            int rayWidth = width / 5;
            int x = i * rayWidth;
            Polygon ray = new Polygon();
            ray.addPoint(x, 0);
            ray.addPoint(x + rayWidth / 2, height);
            ray.addPoint(x + rayWidth, 0);
            g2d.fill(ray);
        }

        // Draw caustic light patterns
        g2d.setColor(new Color(255, 255, 255, 20));
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(width);
            int y = (frameCount * 2 + rand.nextInt(height)) % height;
            g2d.fillOval(x, y, 2, 2);
        }

        // Draw sea floor (sand)
        g2d.setColor(new Color(194, 178, 128));
        g2d.fillRect(0, height - 50, width, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all sea objects
    abstract class SeaObject {
        double x, y, speed;
        int panelWidth, panelHeight;

        SeaObject(double x, double y, double speed, int width, int height) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.panelWidth = width;
            this.panelHeight = height;
        }

        abstract void move();

        abstract void draw(Graphics2D g2d);
    }

    // Static objects like coral reefs
    class CoralReef extends SeaObject {
        Color[] colors = {Color.MAGENTA, Color.ORANGE, Color.YELLOW};

        CoralReef(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            // Static object; no movement
        }

        @Override
        void draw(Graphics2D g2d) {
            for (int i = 0; i < 5; i++) {
                g2d.setColor(colors[i % colors.length]);
                int size = 20 + rand.nextInt(10);
                g2d.fillOval((int) x + i * 15, (int) y - size, size, size);
            }
        }
    }

    // Rocks on the sea floor
    class Rocks extends SeaObject {
        Rocks(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            // Static object
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillOval((int) x, (int) y, 50, 30);
            g2d.fillOval((int) x + 30, (int) y + 10, 40, 25);
        }
    }

    // Sea plants that sway
    class SeaPlant extends SeaObject {
        int plantHeight = 50;
        double swayOffset = rand.nextDouble() * Math.PI * 2;

        SeaPlant(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            // Swaying motion handled in draw method
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(0, 100, 0));
            Path2D path = new Path2D.Double();
            path.moveTo(x, y);
            double sway = Math.sin((frameCount / 20.0) + swayOffset) * 10;
            path.quadTo(x + sway, y - plantHeight / 2.0, x, y - plantHeight);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(path);
        }
    }

    // Fish class with improved movement
    class Fish extends SeaObject {
        Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());

        Fish(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(x / 50.0) * 0.5;
            if (x > panelWidth + 100) x = -100;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(color);
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.fillOval(0, 0, 30, 15);
            Polygon tail = new Polygon();
            tail.addPoint(0, 7);
            tail.addPoint(-10, 0);
            tail.addPoint(-10, 15);
            g2d.fill(tail);
            g2d.setTransform(old);
        }
    }

    // Shark class
    class Shark extends SeaObject {
        Shark(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(x / 80.0) * 1.5;
            if (x > panelWidth + 200) x = -200;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.GRAY);
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.fillOval(0, 0, 60, 20);
            Polygon fin = new Polygon();
            fin.addPoint(30, -10);
            fin.addPoint(40, 0);
            fin.addPoint(20, 0);
            g2d.fill(fin);
            Polygon tail = new Polygon();
            tail.addPoint(60, 10);
            tail.addPoint(80, 0);
            tail.addPoint(80, 20);
            g2d.fill(tail);
            g2d.setTransform(old);
        }
    }

    // Whale class
    class Whale extends SeaObject {
        Whale(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(x / 100.0) * 1;
            if (x > panelWidth + 300) x = -300;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(70, 130, 180));
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.fillOval(0, 0, 100, 30);
            Polygon tail = new Polygon();
            tail.addPoint(100, 15);
            tail.addPoint(120, 0);
            tail.addPoint(120, 30);
            g2d.fill(tail);
            g2d.setTransform(old);
        }
    }

    // Dolphin class
    class Dolphin extends SeaObject {
        Dolphin(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin(x / 60.0) * 2;
            if (x > panelWidth + 200) x = -200;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.LIGHT_GRAY);
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.fillOval(0, 0, 50, 15);
            Polygon fin = new Polygon();
            fin.addPoint(25, -5);
            fin.addPoint(35, 5);
            fin.addPoint(15, 5);
            g2d.fill(fin);
            Polygon tail = new Polygon();
            tail.addPoint(50, 7);
            tail.addPoint(60, 0);
            tail.addPoint(60, 15);
            g2d.fill(tail);
            g2d.setTransform(old);
        }
    }

    // Octopus class
    class Octopus extends SeaObject {
        Octopus(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            y -= speed;
            if (y < -100) y = panelHeight + rand.nextInt(400);
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(128, 0, 128, 150));
            g2d.fillOval((int) x, (int) y, 30, 30);
            for (int i = 0; i < 8; i++) {
                g2d.drawLine((int) x + 15, (int) y + 30, (int) x + i * 4, (int) y + 50 + rand.nextInt(10));
            }
        }
    }

    // Crab class
    class Crab extends SeaObject {
        Crab(double x, double y, double speed, int width, int height) {
            super(x, y, speed * (rand.nextBoolean() ? 1 : -1), width, height);
        }

        @Override
        void move() {
            x += speed;
            if (x > panelWidth || x < 0) speed = -speed;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.RED);
            g2d.fillOval((int) x, (int) y, 20, 10);
            g2d.drawLine((int) x + 5, (int) y, (int) x - 5, (int) y - 5);
            g2d.drawLine((int) x + 15, (int) y, (int) x + 25, (int) y - 5);
        }
    }

    // Starfish class
    class Starfish extends SeaObject {
        Starfish(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            // Static on the sea floor
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.ORANGE);
            Polygon star = new Polygon();
            for (int i = 0; i < 5; i++) {
                star.addPoint((int) (x + 10 * Math.cos(i * 2 * Math.PI / 5)),
                        (int) (y + 10 * Math.sin(i * 2 * Math.PI / 5)));
            }
            g2d.fill(star);
        }
    }

    // Seahorse class
    class Seahorse extends SeaObject {
        Seahorse(double x, double y, double speed, int width, int height) {
            super(x, y, speed * (rand.nextBoolean() ? 1 : -1), width, height);
        }

        @Override
        void move() {
            y += speed;
            if (y > panelHeight || y < 0) speed = -speed;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 215, 0));
            g2d.fillOval((int) x, (int) y, 10, 20);
            g2d.drawArc((int) x - 5, (int) y + 10, 20, 20, 180, 180);
        }
    }

    // Bubble class with floating particles
    class Bubble extends SeaObject {
        Bubble(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            y -= speed;
            if (y < -50) {
                y = panelHeight + rand.nextInt(600);
                x = rand.nextInt(panelWidth);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(173, 216, 230, 80));
            int size = 5 + rand.nextInt(5);
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    // Small fish that move in a school
    class SmallFish extends SeaObject {
        SmallFish(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            x += speed;
            y += Math.sin((x + y) / 50.0) * 0.5;
            if (x > panelWidth + 50) x = -50;
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(Color.YELLOW);
            g2d.fillOval((int) x, (int) y, 10, 5);
        }
    }

    // Shipwreck class
    class Shipwreck extends SeaObject {
        Shipwreck(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            // Static object
        }

        @Override
        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(139, 69, 19));
            Path2D ship = new Path2D.Double();
            ship.moveTo(x, y);
            ship.lineTo(x + 100, y - 50);
            ship.lineTo(x + 120, y - 30);
            ship.lineTo(x + 20, y + 20);
            ship.closePath();
            g2d.fill(ship);
        }
    }

    /**
     * Main method to test the OceanicTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Incredible Oceanic Theme");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            OceanicTheme oceanicTheme = new OceanicTheme();
            frame.add(oceanicTheme);
            frame.setSize(new Dimension(800, 600)); // Initial size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
