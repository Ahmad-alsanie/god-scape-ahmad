package com.godscape.system.themes.celestial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * CelestialTheme class creates a dynamic and animated celestial scene,
 * incorporating various astronomical phenomena and objects.
 */
public class CelestialTheme extends JPanel implements ActionListener {
    private Timer timer;
    private ArrayList<CelestialObject> celestialObjects;
    private Random rand = new Random();
    private int width = 1200, height = 800;
    private int frameCount = 0;

    public CelestialTheme() {
        setPreferredSize(new Dimension(width, height));
        celestialObjects = new ArrayList<>();
        initCelestialObjects();
        timer = new Timer(30, this); // ~33 FPS
        timer.start();

        // Ensure objects are initialized based on actual size
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                width = getWidth();
                height = getHeight();
                celestialObjects.clear();
                initCelestialObjects();
            }
        });
    }

    private void initCelestialObjects() {
        // Initialize Stars
        for (int i = 0; i < 300; i++) {
            celestialObjects.add(new Star(rand.nextInt(width), rand.nextInt(height / 2), rand.nextDouble() * 2));
        }

        // Initialize Meteor Showers
        for (int i = 0; i < 5; i++) {
            celestialObjects.add(new MeteorShower(-rand.nextInt(width), rand.nextInt(height / 2), 4 + rand.nextDouble() * 2));
        }

        // Initialize Comets
        for (int i = 0; i < 3; i++) {
            celestialObjects.add(new Comet(-rand.nextInt(width), rand.nextInt(height / 2), 2 + rand.nextDouble() * 1.5));
        }

        // Initialize Black Holes
        celestialObjects.add(new BlackHole(width / 2, height / 2, 80));

        // Initialize Auroras
        for (int i = 0; i < 2; i++) {
            celestialObjects.add(new Aurora(rand.nextInt(width), rand.nextInt(height / 2), 1 + rand.nextDouble()));
        }

        // Initialize Nebulae
        for (int i = 0; i < 3; i++) {
            celestialObjects.add(new Nebula(rand.nextInt(width), rand.nextInt(height / 3), 1 + rand.nextDouble() * 0.5));
        }

        // Initialize Supernovae
        for (int i = 0; i < 2; i++) {
            celestialObjects.add(new Supernova(rand.nextInt(width), rand.nextInt(height / 2), 0.5 + rand.nextDouble() * 0.5));
        }

        // Initialize Eclipses
        celestialObjects.add(new Eclipse(-100, height / 4, 1 + rand.nextDouble()));

        // Initialize Asteroids and Dwarf Planets
        for (int i = 0; i < 15; i++) {
            celestialObjects.add(new Asteroid(rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble()));
        }
        for (int i = 0; i < 5; i++) {
            celestialObjects.add(new DwarfPlanet(rand.nextInt(width), rand.nextInt(height), 1 + rand.nextDouble()));
        }

        // Initialize Additional Celestial Objects from Your List
        // Quasars, Pulsars, Wormholes, etc.
        for (int i = 0; i < 3; i++) {
            celestialObjects.add(new Quasar(rand.nextInt(width), rand.nextInt(height / 2), 1 + rand.nextDouble()));
        }
        for (int i = 0; i < 3; i++) {
            celestialObjects.add(new Pulsar(rand.nextInt(width), rand.nextInt(height / 2), 2 + rand.nextDouble()));
        }
        celestialObjects.add(new Wormhole(rand.nextInt(width), rand.nextInt(height / 2), 0.5 + rand.nextDouble()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        frameCount++;
        Graphics2D g2d = (Graphics2D) g;
        drawBackground(g2d);
        for (CelestialObject obj : celestialObjects) {
            obj.move(width, height);
            obj.draw(g2d);
        }
    }

    private void drawBackground(Graphics2D g2d) {
        // Draw gradient background to represent deep space
        GradientPaint gp = new GradientPaint(0, 0, new Color(10, 10, 30), 0, height, new Color(0, 0, 0));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        // Draw stars shimmer
        g2d.setColor(new Color(255, 255, 255, 20));
        for (int i = 0; i < 200; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height / 2);
            int size = 1 + rand.nextInt(3);
            g2d.fillOval(x, y, size, size);
        }

        // Draw cosmic rays or caustic light patterns
        g2d.setColor(new Color(255, 255, 255, 10));
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(width);
            int y = (frameCount * 2 + rand.nextInt(height)) % height;
            g2d.fillOval(x, y, 2, 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // Abstract class for all celestial objects
    abstract class CelestialObject {
        double x, y, speed;

        abstract void move(int canvasWidth, int canvasHeight);

        abstract void draw(Graphics2D g2d);
    }

    // Star class representing a twinkling star
    class Star extends CelestialObject {
        double twinkleFactor;
        float alpha = 1.0f;
        boolean fading = true;

        Star(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.twinkleFactor = rand.nextDouble() * 0.005;
        }

        void move(int canvasWidth, int canvasHeight) {
            // Twinkle effect
            if (fading) {
                alpha -= twinkleFactor;
                if (alpha <= 0.2f) fading = false;
            } else {
                alpha += twinkleFactor;
                if (alpha >= 1.0f) fading = true;
            }
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(255, 255, 255, (int) (alpha * 255)));
            g2d.fillOval((int) x, (int) y, 2, 2);
        }
    }

    // MeteorShower class representing a stream of meteors
    class MeteorShower extends CelestialObject {
        double angle = Math.toRadians(45);
        double velocityX, velocityY;
        boolean active = true;

        MeteorShower(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.velocityX = speed * Math.cos(angle);
            this.velocityY = speed * Math.sin(angle);
        }

        void move(int canvasWidth, int canvasHeight) {
            if (active) {
                x += velocityX;
                y += velocityY;
                if (x > canvasWidth || y > canvasHeight) {
                    active = false;
                }
            } else {
                // Reset meteor position for continuous meteor shower
                x = -rand.nextInt(canvasWidth / 2);
                y = rand.nextInt(canvasHeight / 2);
                active = true;
            }
        }

        void draw(Graphics2D g2d) {
            if (active) {
                GradientPaint meteorGradient = new GradientPaint(
                        (float) x, (float) y, Color.YELLOW,
                        (float) (x - 10), (float) (y - 10), new Color(255, 255, 255, 0));
                g2d.setPaint(meteorGradient);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine((int) x, (int) y, (int) (x - 10), (int) (y - 10));
            }
        }
    }

    // Comet class representing a moving comet with a glowing tail
    class Comet extends CelestialObject {
        double tailLength;
        Color tailColor;

        Comet(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.tailLength = 20 + rand.nextDouble() * 30;
            this.tailColor = new Color(255, 215, 0, 150); // Gold color with transparency
        }

        void move(int canvasWidth, int canvasHeight) {
            x += speed;
            y += Math.sin(x / 50) * 0.5;
            if (x > canvasWidth + 100) {
                x = -100;
                y = rand.nextInt(canvasHeight / 2);
            }
        }

        void draw(Graphics2D g2d) {
            // Draw comet head
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int) x, (int) y, 5, 5);

            // Draw comet tail
            GradientPaint tailGradient = new GradientPaint(
                    (float) x, (float) y, tailColor,
                    (float) (x - tailLength), (float) (y - tailLength / 2), new Color(255, 215, 0, 0));
            g2d.setPaint(tailGradient);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine((int) x, (int) y, (int) (x - tailLength), (int) (y - tailLength / 2));
        }
    }

    // BlackHole class representing a gravitational center with an accretion disk
    class BlackHole extends CelestialObject {
        double radius;
        double accretionAngle = 0;

        BlackHole(double x, double y, double radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.speed = 0; // Static
        }

        void move(int canvasWidth, int canvasHeight) {
            // Accretion disk rotation
            accretionAngle += 0.02;
        }

        void draw(Graphics2D g2d) {
            // Draw event horizon
            g2d.setColor(new Color(0, 0, 0, 200));
            g2d.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));

            // Draw accretion disk
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(accretionAngle);
            GradientPaint accretionGradient = new GradientPaint(
                    0, 0, new Color(255, 140, 0, 150),
                    (float) radius * 2, 0, new Color(255, 69, 0, 100));
            g2d.setPaint(accretionGradient);
            g2d.fillOval((int) (-radius), (int) (-radius / 4), (int) (2 * radius), (int) (radius / 2));
            g2d.setTransform(old);
        }
    }

    // Aurora class representing shimmering aurora-like effects
    class Aurora extends CelestialObject {
        double amplitude;
        double frequency;
        float alpha = 0.5f;

        Aurora(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.amplitude = 20 + rand.nextDouble() * 30;
            this.frequency = 0.05 + rand.nextDouble() * 0.05;
        }

        void move(int canvasWidth, int canvasHeight) {
            x += speed;
            if (x > canvasWidth + 100) x = -100;
        }

        void draw(Graphics2D g2d) {
            GradientPaint auroraGradient = new GradientPaint(
                    (float) x, (float) y, new Color(0, 255, 127, (int) (alpha * 255)),
                    (float) (x + 100), (float) (y - amplitude * Math.sin(frameCount * frequency)), new Color(0, 255, 127, 0));
            g2d.setPaint(auroraGradient);
            g2d.setStroke(new BasicStroke(4));
            g2d.drawLine((int) x, (int) y, (int) (x + 100), (int) (y - amplitude * Math.sin(frameCount * frequency)));
        }
    }

    // Nebula class representing pulsating nebula clouds
    class Nebula extends CelestialObject {
        double pulsateRadius;
        double pulsateSpeed;

        Nebula(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.pulsateRadius = 50 + rand.nextDouble() * 50;
            this.pulsateSpeed = 0.05 + rand.nextDouble() * 0.05;
        }

        void move(int canvasWidth, int canvasHeight) {
            // Pulsate effect
            pulsateRadius += pulsateSpeed;
            if (pulsateRadius > 60 || pulsateRadius < 40) {
                pulsateSpeed = -pulsateSpeed;
            }
        }

        void draw(Graphics2D g2d) {
            RadialGradientPaint nebulaGradient = new RadialGradientPaint(
                    (float) x, (float) y, (float) pulsateRadius,
                    new float[]{0f, 1f},
                    new Color[]{new Color(138, 43, 226, 200), new Color(25, 25, 112, 0)});
            g2d.setPaint(nebulaGradient);
            g2d.fill(new Ellipse2D.Double(x - pulsateRadius, y - pulsateRadius, pulsateRadius * 2, pulsateRadius * 2));
        }
    }

    // Supernova class representing explosive star deaths
    class Supernova extends CelestialObject {
        boolean exploded = false;
        int explosionFrame = 0;

        Supernova(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move(int canvasWidth, int canvasHeight) {
            if (!exploded && rand.nextDouble() < 0.001) { // Random explosion trigger
                exploded = true;
            }
            if (exploded) {
                explosionFrame++;
                if (explosionFrame > 100) { // Reset after explosion
                    exploded = false;
                    explosionFrame = 0;
                    x = rand.nextInt(canvasWidth);
                    y = rand.nextInt(canvasHeight / 2);
                }
            }
        }

        void draw(Graphics2D g2d) {
            if (!exploded) {
                // Draw the star before explosion
                g2d.setColor(new Color(255, 215, 0));
                g2d.fillOval((int) x, (int) y, 5, 5);
            } else {
                // Draw explosion
                float alpha = Math.max(0, 1.0f - (float) explosionFrame / 100);
                g2d.setColor(new Color(255, 69, 0, (int) (alpha * 255)));
                float size = explosionFrame * 2;
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new Ellipse2D.Double(x - size / 2, y - size / 2, size, size));
            }
        }
    }

    // Eclipse class representing solar or lunar eclipses
    class Eclipse extends CelestialObject {
        double phase = 0; // 0 to 1 representing the eclipse phase
        double phaseSpeed;

        Eclipse(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.phaseSpeed = 0.01 + rand.nextDouble() * 0.01;
        }

        void move(int canvasWidth, int canvasHeight) {
            phase += phaseSpeed;
            if (phase > 1.0) phase = 0;
        }

        void draw(Graphics2D g2d) {
            // Draw the Sun
            g2d.setColor(Color.YELLOW);
            g2d.fillOval((int) x, (int) y, 50, 50);

            // Draw the Moon passing in front
            g2d.setColor(new Color(50, 50, 50, 150));
            int moonX = (int) (x + 50 * phase);
            g2d.fillOval(moonX, (int) y, 50, 50);
        }
    }

    // Asteroid class representing rocky bodies moving through space
    class Asteroid extends CelestialObject {
        double direction;
        double rotation;
        double rotationSpeed;
        Shape shape;

        Asteroid(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.direction = rand.nextDouble() * 360;
            this.rotation = rand.nextDouble() * 360;
            this.rotationSpeed = rand.nextDouble() * 2 - 1;
            createShape();
        }

        /**
         * Creates a random irregular asteroid shape.
         */
        private void createShape() {
            int points = 8 + rand.nextInt(5);
            double radius = 10 + rand.nextDouble() * 10;
            Path2D.Double path = new Path2D.Double();
            for (int i = 0; i < points; i++) {
                double angle = 2 * Math.PI / points * i;
                double r = radius + rand.nextDouble() * 5;
                double px = r * Math.cos(angle);
                double py = r * Math.sin(angle);
                if (i == 0) {
                    path.moveTo(px, py);
                } else {
                    path.lineTo(px, py);
                }
            }
            path.closePath();
            shape = path;
        }

        void move(int canvasWidth, int canvasHeight) {
            double rad = Math.toRadians(direction);
            x += speed * Math.cos(rad);
            y += speed * Math.sin(rad);
            rotation += rotationSpeed;
            if (x > canvasWidth + 50) x = -50;
            if (x < -50) x = canvasWidth + 50;
            if (y > canvasHeight + 50) y = -50;
            if (y < -50) y = canvasHeight + 50;
        }

        void draw(Graphics2D g2d) {
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));
            g2d.setColor(new Color(105, 105, 105));
            g2d.fill(shape);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(1));
            g2d.draw(shape);
            g2d.setTransform(old);
        }
    }

    // DwarfPlanet class representing smaller planetary bodies
    class DwarfPlanet extends CelestialObject {
        double orbitRadius;
        double orbitSpeed;
        double angle = rand.nextDouble() * 360;
        Color color;

        DwarfPlanet(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.orbitRadius = 100 + rand.nextDouble() * 50;
            this.orbitSpeed = 1 + rand.nextDouble() * 1;
            this.color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        }

        void move(int canvasWidth, int canvasHeight) {
            angle += orbitSpeed;
            if (angle > 360) angle -= 360;
        }

        void draw(Graphics2D g2d) {
            double rad = Math.toRadians(angle);
            double planetX = x + orbitRadius * Math.cos(rad);
            double planetY = y + orbitRadius * Math.sin(rad);
            g2d.setColor(color);
            g2d.fillOval((int) planetX, (int) planetY, 10, 10);
        }
    }

    // Quasar class representing extremely luminous active galactic nuclei
    class Quasar extends CelestialObject {
        double luminosity;

        Quasar(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.luminosity = 100 + rand.nextDouble() * 100;
        }

        void move(int canvasWidth, int canvasHeight) {
            x += speed;
            if (x > canvasWidth + 100) x = -100;
        }

        void draw(Graphics2D g2d) {
            // Draw the core
            g2d.setColor(new Color(255, 255, 0, 200));
            g2d.fillOval((int) x, (int) y, 20, 20);

            // Draw jets
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(new Color(255, 215, 0, 150));
            g2d.drawLine((int) x + 10, (int) y + 10, (int) x + 100, (int) y - 50);
            g2d.drawLine((int) x + 10, (int) y + 10, (int) x + 100, (int) y + 70);
        }
    }

    // Pulsar class representing rapidly spinning neutron stars emitting beams of radiation
    class Pulsar extends CelestialObject {
        double beamAngle = 0;
        double beamSpeed;

        Pulsar(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.beamSpeed = 5 + rand.nextDouble() * 5;
        }

        void move(int canvasWidth, int canvasHeight) {
            beamAngle += beamSpeed;
            if (beamAngle > 360) beamAngle -= 360;
        }

        void draw(Graphics2D g2d) {
            // Draw the pulsar
            g2d.setColor(new Color(173, 216, 230));
            g2d.fillOval((int) x, (int) y, 10, 10);

            // Draw the beam
            g2d.setColor(new Color(135, 206, 250, 150));
            g2d.setStroke(new BasicStroke(2));
            double rad = Math.toRadians(beamAngle);
            double endX = x + 100 * Math.cos(rad);
            double endY = y + 100 * Math.sin(rad);
            g2d.drawLine((int) x + 5, (int) y + 5, (int) endX, (int) endY);
        }
    }

    // Wormhole class representing theoretical passages through space-time
    class Wormhole extends CelestialObject {
        double tunnelRadius = 30;
        double tunnelRotation = 0;

        Wormhole(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        void move(int canvasWidth, int canvasHeight) {
            // Wormhole is static in this example
        }

        void draw(Graphics2D g2d) {
            // Draw the wormhole tunnel
            AffineTransform old = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(tunnelRotation);
            tunnelRotation += 0.05;

            RadialGradientPaint tunnelGradient = new RadialGradientPaint(
                    0, 0, (float) tunnelRadius,
                    new float[]{0f, 1f},
                    new Color[]{new Color(0, 191, 255, 150), new Color(0, 0, 128, 0)});
            g2d.setPaint(tunnelGradient);
            g2d.fill(new Ellipse2D.Double(-tunnelRadius, -tunnelRadius, tunnelRadius * 2, tunnelRadius * 2));

            g2d.setTransform(old);
        }
    }

    // Method to return the JPanel
    public JPanel getPanel() {
        return this;
    }

    // Main method to test the panel independently
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Celestial Theme - Astronomical Phenomena");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            CelestialTheme celestialTheme = new CelestialTheme();
            frame.add(celestialTheme);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
