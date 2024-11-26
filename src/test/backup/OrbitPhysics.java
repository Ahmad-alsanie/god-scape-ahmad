package com.godscape.system.themes.celestial.engine;

import java.time.ZonedDateTime;

/**
 * OrbitPhysics handles the calculations for celestial bodies orbiting around a central point (e.g., the Sun).
 */
public class OrbitPhysics {

    /**
     * Calculates the current angular position of a celestial body in its orbit.
     *
     * @param orbitalPeriodInDays The orbital period of the body in Earth days.
     * @param currentTime         The current time.
     * @return The angular position in radians.
     */
    public static double calculateOrbitalAngle(double orbitalPeriodInDays, ZonedDateTime currentTime) {
        // Convert current time to the number of days since an epoch (e.g., 1970-01-01)
        double daysSinceEpoch = currentTime.toEpochSecond() / 86400.0;

        // Calculate the fraction of the orbital period that has passed
        double fractionOfPeriod = (daysSinceEpoch % orbitalPeriodInDays) / orbitalPeriodInDays;

        // Convert this fraction to an angle in radians (0 to 2*PI)
        return 2 * Math.PI * fractionOfPeriod;
    }

    /**
     * Calculates the x and y coordinates of a celestial body in its orbit around a central point.
     *
     * @param centerX            The x-coordinate of the orbit's center (e.g., the Sun).
     * @param centerY            The y-coordinate of the orbit's center.
     * @param orbitRadius        The radius of the orbit in pixels.
     * @param orbitalAngle       The current angular position in radians.
     * @return An array of two doubles representing the x and y coordinates of the orbiting body.
     */
    public static double[] calculateOrbitPosition(double centerX, double centerY, double orbitRadius, double orbitalAngle) {
        // Calculate x and y based on the angle and radius
        double x = centerX + orbitRadius * Math.cos(orbitalAngle);
        double y = centerY + orbitRadius * Math.sin(orbitalAngle);

        // Return the coordinates as an array
        return new double[]{x, y};
    }

    /**
     * Calculates the orbital speed based on Kepler's third law for simplified circular orbits.
     *
     * @param semiMajorAxis The semi-major axis of the orbit in astronomical units (AU).
     * @return The orbital speed in degrees per day.
     */
    public static double calculateOrbitalSpeed(double semiMajorAxis) {
        // Kepler's third law for orbital speed (assuming mass of central body is dominant)
        return 360.0 / (Math.sqrt(Math.pow(semiMajorAxis, 3)));
    }
}
