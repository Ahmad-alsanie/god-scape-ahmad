package com.godscape.system.themes.springtide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

public class SpringtideTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<SpringObject> springObjects;
    private Random rand = new Random();
    private int frameCount = 0;

    public SpringtideTheme() {
        springObjects = new ArrayList<>();
        initSpringObjects();
        timer = new Timer(30, this);
        timer.start();

        // Add a component listener to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Clear and reinitialize elements on resize
                springObjects.clear();
                initSpringObjects();
            }
        });
    }

    private void initSpringObjects() {
        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) return; // Avoid initializing elements if dimensions are not set

        // Static objects
        springObjects.add(new Tree(100, height - 200));
        springObjects.add(new Tree(width - 200, height - 180));
        springObjects.add(new FlowerPatch(200, height - 100));
        springObjects.add(new FlowerPatch(width - 300, height - 70));

        // Dynamic elements
        for (int i = 0; i < 10; i++)
            springObjects.add(new Butterfly(rand.nextInt(width), rand.nextInt(height / 2), 1 + rand.nextDouble() * 2));
        for (int i = 0; i < 5; i++)
            springObjects.add(new Bird(rand.nextInt(width), rand.nextInt(height / 3), 2 + rand.nextDouble()));
        for (int i = 0; i < 10; i++)
            springObjects.add(new EasterEgg(rand.nextInt(width), rand.nextInt(height), 0.5 + rand.nextDouble()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g2d);
        for (SpringObject obj : springObjects) {
            obj.move();
            obj.draw(g2d);
        }
    }

    private void drawBackground(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();

        // Draw gradient sky
        GradientPaint sky = new GradientPaint(0, 0, new Color(135, 206, 250), 0, height, new Color(173, 216, 230));
        g2d.setPaint(sky);
        g2d.fillRect(0, 0, width, height);

        // Draw sun
        g2d.setColor(new Color(255, 223, 0, 180));
        g2d.fillOval(width - 150, 50, 100, 100);

        // Draw grass
        g2d.setColor(new Color(60, 179, 113));
        g2d.fillRect(0, height - 100, width, 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all spring-themed objects
    abstract class SpringObject {
        double x, y, speed;

        abstract void move();

        abstract void draw(Graphics2D g2d);
    }

    // Class representing a tree
    class Tree extends SpringObject {
        Tree(double x, double y) {
            this.x = x;
            this.y = y;
        }

        void move() {
            // Static object; no movement
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(139, 69, 19));
            g2d.fillRect((int) x, (int) y, 20, 60);
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillOval((int) x - 30, (int) y - 30, 80, 80);
        }
    }

    // Class representing a flower patch
    class FlowerPatch extends SpringObject {
        Color[] colors = {Color.PINK, Color.YELLOW, Color.MAGENTA, Color.ORANGE};

        FlowerPatch(double x, double y) {
            this.x = x;
            this.y = y;
        }

        void move() {
            // Static object; no movement
        }

        void draw(Graphics2D g2d) {
            for (int i = 0; i < 5; i++) {
                g2d.setColor(colors[i % colors.length]);
                int size = 10 + rand.nextInt(5);
                g2d.fillOval((int) x + i * 15, (int) y - size, size, size);
            }
        }
    }

    // Class representing a butterfly
    class Butterfly extends SpringObject {
        Color color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());

        Butterfly(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move() {
            x += speed;
            y += Math.sin(x / 50) * 1.5;
            if (x > getWidth() + 50) x = -50;
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(color);
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.fillOval(0, 0, 20, 10);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(10, 5, -10, 0);
            g2d.setTransform(old);
        }
    }

    // Class representing a bird
    class Bird extends SpringObject {
        Bird(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move() {
            x += speed;
            y += Math.sin(x / 60) * 1.0;
            if (x > getWidth() + 50) x = -50;
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 102));
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.fillOval(0, 0, 30, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(15, 7, -10, 0);
            g2d.setTransform(old);
        }
    }

    // Class representing an Easter egg
    class EasterEgg extends SpringObject {
        EasterEgg(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move() {
            y -= speed;
            if (y < -50) {
                y = getHeight() + rand.nextInt(100);
                x = rand.nextInt(getWidth());
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
            g2d.fillOval((int) x, (int) y, 15, 20);
        }
    }

    // Main method to test the panel independently
    public static void main(String[] args) {
        JFrame frame = new JFrame("Springtide Theme - Easter Celebration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringtideTheme springtideTheme = new SpringtideTheme();
        frame.add(springtideTheme);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
