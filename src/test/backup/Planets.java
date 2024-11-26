package com.godscape.system.themes.celestial;

import java.awt.*;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the solar system's planets, including their positions and rendering.
 * Adds a side-on perspective for a more realistic view of the orbits.
 */
public class Planets {
    private static final int ORBIT_SCALE = 200; // Scaling factor for orbit distances
    private static final float ELLIPSE_FACTOR = 0.5f; // Compression factor for the vertical axis
    private static final float PLANET_SIZE_SCALE = 0.0000003f; // Further reduced scaling factor for realistic planet sizes
    private static final float SUN_SIZE_SCALE = 0.000002f; // Further reduced scaling factor for the Sun's size
    private static final float SUN_DIAMETER = 1392.7f; // Sun's diameter in thousands of kilometers

    private final Map<String, Planet> planets = new HashMap<>();

    public Planets() {
        initializePlanets();
    }

    /**
     * Initializes the planets with approximate sizes and orbital distances.
     */
    private void initializePlanets() {
        // Sizes are in thousands of kilometers, distances are in millions of kilometers
        planets.put("Sun", new Planet(SUN_DIAMETER, 0, true)); // Sun at the center
        planets.put("Mercury", new Planet(4.88f, 57.9f));   // Diameter: 4,880 km, Distance: 57.9 million km
        planets.put("Venus", new Planet(12.1f, 108.2f));    // Diameter: 12,100 km, Distance: 108.2 million km
        planets.put("Earth", new Planet(12.7f, 149.6f));    // Diameter: 12,742 km, Distance: 149.6 million km
        planets.put("Mars", new Planet(6.8f, 227.9f));      // Diameter: 6,779 km, Distance: 227.9 million km
        planets.put("Jupiter", new Planet(139.8f, 778.5f)); // Diameter: 139,820 km, Distance: 778.5 million km
        planets.put("Saturn", new Planet(116.5f, 1434f));   // Diameter: 116,460 km, Distance: 1,434 million km
        planets.put("Uranus", new Planet(50.7f, 2871f));    // Diameter: 50,724 km, Distance: 2,871 million km
        planets.put("Neptune", new Planet(49.2f, 4495f));   // Diameter: 49,244 km, Distance: 4,495 million km
    }

    /**
     * Draws all planets on the given Graphics2D context.
     *
     * @param g2d    The Graphics2D context.
     * @param width  The width of the panel.
     * @param height The height of the panel.
     * @param now    The current time.
     */
    public void draw(Graphics2D g2d, int width, int height, ZonedDateTime now) {
        int sunX = width / 2;
        int sunY = height / 2;

        for (Map.Entry<String, Planet> entry : planets.entrySet()) {
            Planet planet = entry.getValue();

            // Calculate the planet's position based on its orbital period
            double angle = calculateOrbitalAngle(planet, now);
            int distanceFromSun = (int) (planet.distanceFromSun * ORBIT_SCALE / 1000); // Scale distance

            // Apply elliptical effect by reducing the vertical component
            int planetX = sunX + (int) (Math.cos(angle) * distanceFromSun) - (int) (planet.getScaledSize() / 2);
            int planetY = sunY + (int) (Math.sin(angle) * distanceFromSun * ELLIPSE_FACTOR) - (int) (planet.getScaledSize() / 2);

            // Draw the planet
            g2d.setColor(entry.getKey().equals("Sun") ? Color.YELLOW : Color.WHITE);
            g2d.fillOval(planetX, planetY, (int) planet.getScaledSize(), (int) planet.getScaledSize());
        }
    }

    /**
     * Calculates the orbital angle of a planet based on the current time.
     *
     * @param planet The planet whose orbital angle is being calculated.
     * @param now    The current time.
     * @return The calculated angle in radians.
     */
    private double calculateOrbitalAngle(Planet planet, ZonedDateTime now) {
        double orbitalPeriod = planet.getOrbitalPeriodInDays();
        double daysSinceEpoch = now.toEpochSecond() / 86400.0;
        double angle = 2 * Math.PI * (daysSinceEpoch % orbitalPeriod) / orbitalPeriod;
        return angle;
    }

    /**
     * Represents a planet with its size and distance from the Sun.
     */
    private static class Planet {
        float size; // Diameter in thousands of kilometers
        float distanceFromSun; // Distance from the Sun in millions of kilometers
        boolean isSun; // Flag to check if this planet is the Sun

        Planet(float size, float distanceFromSun) {
            this(size, distanceFromSun, false);
        }

        Planet(float size, float distanceFromSun, boolean isSun) {
            this.size = size;
            this.distanceFromSun = distanceFromSun;
            this.isSun = isSun;
        }

        /**
         * Gets the scaled size of the planet for drawing.
         *
         * @return The scaled size in pixels.
         */
        float getScaledSize() {
            return isSun ? size * SUN_SIZE_SCALE : size * PLANET_SIZE_SCALE;
        }

        /**
         * Gets the orbital period of the planet in Earth days.
         *
         * @return The orbital period in days.
         */
        double getOrbitalPeriodInDays() {
            // Simplified orbital periods in Earth days for the planets
            switch ((int) distanceFromSun) {
                case 0:
                    return 0; // Sun doesn't orbit
                case 58:
                    return 88;
                case 108:
                    return 225;
                case 150:
                    return 365;
                case 228:
                    return 687;
                case 779:
                    return 4333;
                case 1434:
                    return 10759;
                case 2871:
                    return 30685;
                case 4495:
                    return 60190;
                default:
                    return 365; // Default to Earth's orbital period
            }
        }
    }
}
