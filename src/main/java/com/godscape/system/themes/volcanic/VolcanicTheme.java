package com.godscape.system.themes.volcanic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * VolcanicTheme.java
 *
 * This class creates a dynamic and immersive volcanic landscape with animated elements such as volcanoes,
 * meteors resembling the FF7 meteor, lightning, earthquakes, geysers, lava fountains, and other cataclysmic events.
 */
public class VolcanicTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<VolcanicObject> volcanicObjects;
    private ArrayList<VolcanicObject> newObjects; // To store new objects safely
    private ArrayList<VolcanicObject> objectsToRemove; // To collect objects to remove
    private Random rand = new Random();
    private int frameCount = 0;
    private int shakeOffsetX = 0;
    private int shakeOffsetY = 0;

    public VolcanicTheme() {
        volcanicObjects = new ArrayList<>();
        newObjects = new ArrayList<>();
        objectsToRemove = new ArrayList<>();
        initVolcanicObjects();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Add a listener to handle resizing
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initVolcanicObjects(); // Reinitialize volcanic objects based on new size
            }
        });
    }

    /**
     * Initializes volcanic objects based on the current panel size.
     */
    private void initVolcanicObjects() {
        volcanicObjects.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0 || height == 0) {
            // Panel not yet visible; defer initialization
            return;
        }

        // Volcanoes
        volcanicObjects.add(new Volcano(width / 4.0, height - 100, width, height));
        volcanicObjects.add(new Volcano(width * 3 / 4.0, height - 100, width, height));

        // Meteors (resembling FF7 meteor)
        for (int i = 0; i < 5; i++) {
            volcanicObjects.add(new Meteor(rand.nextInt(width), -rand.nextInt(height), 2 + rand.nextDouble() * 3, width, height));
        }

        // Explosions
        volcanicObjects.add(new ExplosionManager(width, height));

        // Ash particles
        for (int i = 0; i < 200; i++) {
            volcanicObjects.add(new AshParticle(rand.nextInt(width), rand.nextInt(height), rand.nextDouble() + 0.5, width, height));
        }

        // Smoke
        volcanicObjects.add(new Smoke(width / 4.0, height - 150, width, height));
        volcanicObjects.add(new Smoke(width * 3 / 4.0, height - 150, width, height));

        // Lightning
        volcanicObjects.add(new Lightning(width, height));

        // Geysers
        volcanicObjects.add(new Geyser(width / 2.0, height - 50, width, height));

        // Lava Fountains
        volcanicObjects.add(new LavaFountain(width / 2.0, height - 80, width, height));

        // Fire Embers
        for (int i = 0; i < 100; i++) {
            volcanicObjects.add(new FireEmber(rand.nextInt(width), rand.nextInt(height), rand.nextDouble() + 0.5, width, height));
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

        // Apply screen shake effect
        g2d.translate(shakeOffsetX, shakeOffsetY);

        drawBackground(g2d, width, height);

        // Iterate over a copy of the list to prevent ConcurrentModificationException
        ArrayList<VolcanicObject> objectsCopy = new ArrayList<>(volcanicObjects);

        for (VolcanicObject obj : objectsCopy) {
            obj.move();
            obj.draw(g2d);
        }

        // Remove expired objects
        volcanicObjects.removeAll(objectsToRemove);
        objectsToRemove.clear();

        // Add any new objects collected during the update
        volcanicObjects.addAll(newObjects);
        newObjects.clear();

        g2d.dispose();

        // Reset shake offsets
        shakeOffsetX = 0;
        shakeOffsetY = 0;
    }

    /**
     * Draws the volcanic background with gradient sky, distant mountains, and fiery ambiance.
     */
    private void drawBackground(Graphics2D g2d, int width, int height) {
        // Draw gradient background for the sky
        GradientPaint gp = new GradientPaint(0, 0, new Color(139, 0, 0), // DarkRed
                0, height, new Color(0, 0, 0)); // Black
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Distant mountains silhouettes
        g2d.setColor(new Color(70, 70, 70)); // Dark Gray
        for (int i = 0; i < 10; i++) {
            int x = i * (width / 10);
            int h = 50 + rand.nextInt(50);
            g2d.fillRect(x, height - h, width / 10, h);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all volcanic objects
    abstract class VolcanicObject {
        double x, y, speed;
        int panelWidth, panelHeight;

        VolcanicObject(double x, double y, double speed, int width, int height) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.panelWidth = width;
            this.panelHeight = height;
        }

        abstract void move();

        abstract void draw(Graphics2D g2d);
    }

    // Volcano class with bigger erupting animation
    class Volcano extends VolcanicObject {
        double eruptionTimer = 0;

        Volcano(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            eruptionTimer += 0.01;
            if (eruptionTimer > 1) {
                // Big eruption
                int numFireballs = 30; // Increase number of fireballs for a bigger eruption
                for (int i = 0; i < numFireballs; i++) {
                    double angle = Math.toRadians(rand.nextInt(120) - 60); // Spread angles between -60 to +60 degrees
                    double speed = 5 + rand.nextDouble() * 3; // Faster speed
                    newObjects.add(new EruptionParticle(x, y - 50, speed, angle, panelWidth, panelHeight));
                }
                eruptionTimer = 0;

                // Trigger screen shake
                shakeScreen();
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            // Draw volcano
            g2d.setColor(new Color(105, 105, 105)); // DimGray
            Polygon volcano = new Polygon();
            volcano.addPoint((int) x, (int) y);
            volcano.addPoint((int) (x - 50), (int) panelHeight);
            volcano.addPoint((int) (x + 50), (int) panelHeight);
            g2d.fill(volcano);

            // Draw lava at the top
            g2d.setColor(new Color(255, 69, 0)); // OrangeRed
            g2d.fillOval((int) x - 15, (int) y - 10, 30, 20);
        }

        private void shakeScreen() {
            shakeOffsetX = rand.nextInt(10) - 5;
            shakeOffsetY = rand.nextInt(10) - 5;
        }
    }

    // EruptionParticle class for volcano eruptions
    class EruptionParticle extends VolcanicObject {
        double angle;
        Color color;
        double gravity = 0.1;
        double alpha = 1.0; // For fading out

        EruptionParticle(double x, double y, double speed, double angle, int width, int height) {
            super(x, y, speed, width, height);
            this.angle = angle;
            color = new Color(255, 140, 0, 150); // Semi-transparent DarkOrange
        }

        @Override
        void move() {
            x += speed * Math.cos(angle);
            y += speed * Math.sin(angle);
            speed *= 0.98; // Slight deceleration
            angle += gravity; // Simulate gravity effect
            alpha -= 0.01; // Fade out
            if (alpha <= 0 || y > panelHeight || x < -50 || x > panelWidth + 50) {
                objectsToRemove.add(this);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            int size = 10;
            RadialGradientPaint rgp = new RadialGradientPaint(
                    new Point2D.Double(x, y),
                    size,
                    new float[]{0f, 1f},
                    new Color[]{new Color(255, 140, 0, (int) (alpha * 255)), new Color(255, 69, 0, 0)}); // Fades to transparent
            g2d.setPaint(rgp);
            g2d.fillOval((int) x - size, (int) y - size, size * 2, size * 2);
        }
    }

    // Meteor class resembling FF7 meteor without side spread in the trail
    class Meteor extends VolcanicObject {
        double angle;
        Color meteorColor = new Color(255, 69, 0); // OrangeRed

        Meteor(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
            angle = Math.atan2(height - y, (width / 2.0) - x);
        }

        @Override
        void move() {
            x += speed * Math.cos(angle);
            y += speed * Math.sin(angle);
            if (y > panelHeight || x > panelWidth || x < -50) {
                x = rand.nextInt(panelWidth);
                y = -50;
                angle = Math.atan2(panelHeight - y, (panelWidth / 2.0) - x);
            }

            // Create trail directly behind the meteor without side spread
            double trailX = x - speed * Math.cos(angle) * 5;
            double trailY = y - speed * Math.sin(angle) * 5;
            newObjects.add(new MeteorTrail(trailX, trailY, panelWidth, panelHeight));
        }

        @Override
        void draw(Graphics2D g2d) {
            // Draw meteor resembling FF7 meteor
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(angle + Math.PI / 2);
            // Draw the core
            g2d.setColor(meteorColor);
            g2d.fillOval(-20, -20, 40, 40);
            // Draw the spiky edges
            g2d.setColor(Color.RED);
            for (int i = 0; i < 8; i++) {
                double theta = i * Math.PI / 4;
                int x1 = (int) (30 * Math.cos(theta));
                int y1 = (int) (30 * Math.sin(theta));
                int x2 = (int) (50 * Math.cos(theta));
                int y2 = (int) (50 * Math.sin(theta));
                g2d.drawLine(0, 0, x2, y2);
            }
            g2d.setTransform(old);
        }
    }

    // MeteorTrail class
    class MeteorTrail extends VolcanicObject {
        double alpha = 1.0;

        MeteorTrail(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
        }

        @Override
        void move() {
            alpha -= 0.05;
            if (alpha <= 0) {
                objectsToRemove.add(this);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            int size = 20;
            RadialGradientPaint rgp = new RadialGradientPaint(
                    new Point2D.Double(x, y),
                    size,
                    new float[]{0f, 1f},
                    new Color[]{new Color(255, 140, 0, (int) (alpha * 255)), new Color(255, 69, 0, 0)}); // Fades to transparent
            g2d.setPaint(rgp);
            g2d.fillOval((int) x - size, (int) y - size, size * 2, size * 2);
        }
    }

    // ExplosionManager class to handle explosions
    class ExplosionManager extends VolcanicObject {
        ArrayList<Explosion> explosions;

        ExplosionManager(int width, int height) {
            super(0, 0, 0, width, height);
            explosions = new ArrayList<>();
        }

        @Override
        void move() {
            // Randomly trigger explosions
            if (rand.nextInt(100) < 2) { // 2% chance each frame
                explosions.add(new Explosion(rand.nextInt(panelWidth), rand.nextInt(panelHeight / 2)));
            }
            // Update explosions
            Iterator<Explosion> it = explosions.iterator();
            while (it.hasNext()) {
                Explosion exp = it.next();
                exp.update();
                if (exp.isFinished()) {
                    it.remove();
                }
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            for (Explosion exp : explosions) {
                exp.draw(g2d);
            }
        }
    }

    // Explosion class
    class Explosion {
        int x, y;
        int radius = 0;
        int maxRadius = 50 + rand.nextInt(50);
        Color color = new Color(255, 215, 0, 150); // Semi-transparent Gold

        public Explosion(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void update() {
            radius += 5;
        }

        public boolean isFinished() {
            return radius > maxRadius;
        }

        public void draw(Graphics2D g2d) {
            RadialGradientPaint rgp = new RadialGradientPaint(
                    new Point2D.Double(x, y),
                    radius,
                    new float[]{0f, 1f},
                    new Color[]{color, new Color(255, 69, 0, 0)}); // Fades to transparent
            g2d.setPaint(rgp);
            g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    // AshParticle class
    class AshParticle extends VolcanicObject {
        Color color = new Color(169, 169, 169, 100); // Semi-transparent DarkGray

        AshParticle(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            y -= speed;
            x += Math.sin(y / 20.0) * 0.5;
            if (y < -10) {
                y = panelHeight;
                x = rand.nextInt(panelWidth);
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            int size = 2 + rand.nextInt(2);
            g2d.setColor(color);
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    // Smoke class
    class Smoke extends VolcanicObject {
        ArrayList<SmokeParticle> smokeParticles;

        Smoke(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
            smokeParticles = new ArrayList<>();
        }

        @Override
        void move() {
            // Generate smoke particles
            if (frameCount % 5 == 0) {
                smokeParticles.add(new SmokeParticle(x + rand.nextInt(30) - 15, y));
            }
            // Update smoke particles
            Iterator<SmokeParticle> it = smokeParticles.iterator();
            while (it.hasNext()) {
                SmokeParticle sp = it.next();
                sp.update();
                if (sp.isFinished()) {
                    it.remove();
                }
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            for (SmokeParticle sp : smokeParticles) {
                sp.draw(g2d);
            }
        }
    }

    // SmokeParticle class
    class SmokeParticle {
        double x, y;
        double alpha = 1.0;
        double size = 10 + rand.nextInt(10);

        public SmokeParticle(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void update() {
            y -= 0.5;
            x += rand.nextDouble() - 0.5;
            alpha -= 0.01;
        }

        public boolean isFinished() {
            return alpha <= 0;
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(new Color(105, 105, 105, (int) (alpha * 255))); // Dynamic transparency
            g2d.fillOval((int) x, (int) y, (int) size, (int) size);
        }
    }

    // Lightning class
    class Lightning extends VolcanicObject {
        ArrayList<LightningBolt> bolts;

        Lightning(int width, int height) {
            super(0, 0, 0, width, height);
            bolts = new ArrayList<>();
        }

        @Override
        void move() {
            // Randomly create lightning bolts
            if (rand.nextInt(200) < 1) { // 0.5% chance each frame
                bolts.add(new LightningBolt(rand.nextInt(panelWidth), 0, panelWidth, panelHeight));
            }

            // Update bolts
            Iterator<LightningBolt> it = bolts.iterator();
            while (it.hasNext()) {
                LightningBolt bolt = it.next();
                bolt.update();
                if (bolt.isFinished()) {
                    it.remove();
                }
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            for (LightningBolt bolt : bolts) {
                bolt.draw(g2d);
            }
        }
    }

    // LightningBolt class
    class LightningBolt {
        int x, y;
        int life = 0;
        int maxLife = 5;
        int panelWidth, panelHeight;
        ArrayList<Line2D> segments;

        public LightningBolt(int x, int y, int panelWidth, int panelHeight) {
            this.x = x;
            this.y = y;
            this.panelWidth = panelWidth;
            this.panelHeight = panelHeight;
            segments = generateBolt(x, y, panelHeight / 2 + rand.nextInt(panelHeight / 2));
        }

        private ArrayList<Line2D> generateBolt(int startX, int startY, int endY) {
            ArrayList<Line2D> lines = new ArrayList<>();
            int x = startX;
            int y = startY;

            while (y < endY) {
                int nextX = x + rand.nextInt(20) - 10;
                int nextY = y + rand.nextInt(20) + 10;
                lines.add(new Line2D.Double(x, y, nextX, nextY));
                x = nextX;
                y = nextY;
            }
            return lines;
        }

        public void update() {
            life++;
        }

        public boolean isFinished() {
            return life > maxLife;
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, 200));
            g2d.setStroke(new BasicStroke(2));
            for (Line2D line : segments) {
                g2d.draw(line);
            }
        }
    }

    // Geyser class
    class Geyser extends VolcanicObject {
        ArrayList<GeyserParticle> particles;

        Geyser(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
            particles = new ArrayList<>();
        }

        @Override
        void move() {
            // Generate geyser particles
            if (frameCount % 10 == 0) {
                particles.add(new GeyserParticle(x + rand.nextInt(20) - 10, y, 2 + rand.nextDouble() * 2));
            }
            // Update particles
            Iterator<GeyserParticle> it = particles.iterator();
            while (it.hasNext()) {
                GeyserParticle gp = it.next();
                gp.update();
                if (gp.isFinished()) {
                    it.remove();
                }
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            for (GeyserParticle gp : particles) {
                gp.draw(g2d);
            }
        }
    }

    // GeyserParticle class
    class GeyserParticle {
        double x, y;
        double speed;
        double alpha = 1.0;

        public GeyserParticle(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = -speed;
        }

        public void update() {
            y += speed;
            alpha -= 0.02;
        }

        public boolean isFinished() {
            return alpha <= 0;
        }

        public void draw(Graphics2D g2d) {
            int size = 5;
            g2d.setColor(new Color(173, 216, 230, (int) (alpha * 255))); // LightBlue with transparency
            g2d.fillOval((int) x - size / 2, (int) y - size / 2, size, size);
        }
    }

    // LavaFountain class
    class LavaFountain extends VolcanicObject {
        ArrayList<LavaParticle> particles;

        LavaFountain(double x, double y, int width, int height) {
            super(x, y, 0, width, height);
            particles = new ArrayList<>();
        }

        @Override
        void move() {
            // Generate lava particles
            if (frameCount % 5 == 0) {
                particles.add(new LavaParticle(x + rand.nextInt(20) - 10, y, 4 + rand.nextDouble() * 2, Math.toRadians(-90 + rand.nextInt(40) - 20)));
            }
            // Update particles
            Iterator<LavaParticle> it = particles.iterator();
            while (it.hasNext()) {
                LavaParticle lp = it.next();
                lp.update();
                if (lp.isFinished()) {
                    it.remove();
                }
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            for (LavaParticle lp : particles) {
                lp.draw(g2d);
            }
        }
    }

    // LavaParticle class
    class LavaParticle {
        double x, y;
        double speed;
        double angle;
        double gravity = 0.2;
        double alpha = 1.0;

        public LavaParticle(double x, double y, double speed, double angle) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.angle = angle;
        }

        public void update() {
            x += speed * Math.cos(angle);
            y += speed * Math.sin(angle);
            speed *= 0.98;
            angle += gravity * 0.05;
            alpha -= 0.02;
        }

        public boolean isFinished() {
            return alpha <= 0;
        }

        public void draw(Graphics2D g2d) {
            int size = 5;
            g2d.setColor(new Color(255, 69, 0, (int) (alpha * 255))); // OrangeRed with transparency
            g2d.fillOval((int) x - size / 2, (int) y - size / 2, size, size);
        }
    }

    // FireEmber class
    class FireEmber extends VolcanicObject {
        double alpha = 1.0;

        FireEmber(double x, double y, double speed, int width, int height) {
            super(x, y, speed, width, height);
        }

        @Override
        void move() {
            y -= speed;
            x += rand.nextDouble() - 0.5;
            alpha -= 0.005;
            if (alpha <= 0 || y < -10) {
                y = panelHeight;
                x = rand.nextInt(panelWidth);
                alpha = 1.0;
            }
        }

        @Override
        void draw(Graphics2D g2d) {
            int size = 3;
            g2d.setColor(new Color(255, 140, 0, (int) (alpha * 255)));
            g2d.fillOval((int) x, (int) y, size, size);
        }
    }

    /**
     * Main method to test the VolcanicTheme panel independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Volcanic Theme");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            VolcanicTheme volcanicTheme = new VolcanicTheme();
            frame.add(volcanicTheme);
            frame.setSize(new Dimension(800, 600));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
