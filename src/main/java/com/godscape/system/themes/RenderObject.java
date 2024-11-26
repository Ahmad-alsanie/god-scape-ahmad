// File: com/godscape/system/themes/RenderObject.java
package com.godscape.system.themes;

import java.awt.Dimension;
import java.util.Random;

public abstract class RenderObject {
    protected float x, y; // Position of the object (head)
    protected float dx, dy; // Direction and speed of the object
    protected Dimension canvasSize; // Canvas dimensions to be passed for enforcing behaviors
    protected boolean isVisible = false; // Tracks visibility status
    protected float length = 0; // Length of the object, such as a tail or trail
    protected float baseSpeed; // Base speed for the object

    private static final int OFF_SCREEN_BUFFER = 100; // Increased buffer to ensure off-screen spawn
    private static final int RESET_DISTANCE = 3000; // Increased distance beyond which the object is reset

    protected static final Random rand = new Random(); // Shared Random instance

    // New State Flags
    protected boolean isInitializing = true; // Flag to indicate if the object is still initializing

    // Constructor: Enforces starting off-screen and initializes direction
    public RenderObject(Dimension canvasSize, float length, float baseSpeed) {
        this.canvasSize = canvasSize;
        this.length = length;
        this.baseSpeed = baseSpeed;

        // Initialize the object to start off-screen
        initializeOffScreenPosition();

        // Set random trajectory to move towards the screen
        initializeTrajectory();
    }

    // Initialize the starting position to be off-screen
    private void initializeOffScreenPosition() {
        int side = rand.nextInt(4); // Changed to 4 to include all four sides

        switch (side) {
            case 0: // Start to the left of the canvas
                x = -OFF_SCREEN_BUFFER;
                y = rand.nextInt(canvasSize.height + OFF_SCREEN_BUFFER * 2) - OFF_SCREEN_BUFFER;
                break;
            case 1: // Start above the canvas
                x = rand.nextInt(canvasSize.width + OFF_SCREEN_BUFFER * 2) - OFF_SCREEN_BUFFER;
                y = -OFF_SCREEN_BUFFER;
                break;
            case 2: // Start to the right of the canvas
                x = canvasSize.width + OFF_SCREEN_BUFFER;
                y = rand.nextInt(canvasSize.height + OFF_SCREEN_BUFFER * 2) - OFF_SCREEN_BUFFER;
                break;
            case 3: // Start below the canvas
                x = rand.nextInt(canvasSize.width + OFF_SCREEN_BUFFER * 2) - OFF_SCREEN_BUFFER;
                y = canvasSize.height + OFF_SCREEN_BUFFER;
                break;
            default:
                // Fallback in case of unexpected side value
                x = -OFF_SCREEN_BUFFER;
                y = -OFF_SCREEN_BUFFER;
                break;
        }

        // Ensure that the starting position is completely outside the canvas boundaries
        if ((x > 0 && x < canvasSize.width) && (y > 0 && y < canvasSize.height)) {
            // Recursively reposition if the object is inside the canvas
            initializeOffScreenPosition();
        }
    }

    private void initializeTrajectory() {
        // Target point at a random area within a margin from the center
        float targetX = canvasSize.width / 2.0f + rand.nextInt(canvasSize.width / 4) - canvasSize.width / 8;
        float targetY = canvasSize.height / 2.0f + rand.nextInt(canvasSize.height / 4) - canvasSize.height / 8;

        // Calculate dx and dy to move towards the target
        float distanceX = targetX - x;
        float distanceY = targetY - y;
        float magnitude = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);
        float speed = baseSpeed + rand.nextFloat() * 2; // Random speed between baseSpeed and baseSpeed + 2

        // Normalize to get direction and then multiply by speed
        dx = (distanceX / magnitude) * speed;
        dy = (distanceY / magnitude) * speed;

        // Ensure the trajectory requires a reasonable distance of visibility
        if (isCornerCutting()) {
            // Adjust the target to aim for a more central area to avoid cutting corners
            dx *= 0.8f;
            dy *= 0.8f;
        }
    }

    private boolean isCornerCutting() {
        // Adjust for the off-screen buffer to prevent quick corner cuts
        return (x < -OFF_SCREEN_BUFFER && dx > 0 && y < -OFF_SCREEN_BUFFER && dy > 0) || // Top-left corner
                (x > canvasSize.width + OFF_SCREEN_BUFFER && dx < 0 && y < -OFF_SCREEN_BUFFER && dy > 0) || // Top-right corner
                (x < -OFF_SCREEN_BUFFER && dx > 0 && y > canvasSize.height + OFF_SCREEN_BUFFER && dy < 0) || // Bottom-left corner
                (x > canvasSize.width + OFF_SCREEN_BUFFER && dx < 0 && y > canvasSize.height + OFF_SCREEN_BUFFER && dy < 0); // Bottom-right corner
    }

    // Update method to move the object in its trajectory
    public void move() {
        x += dx;
        y += dy;

        // Calculate tail position based on length
        float tailX = x - dx * length;
        float tailY = y - dy * length;

        // Update visibility based on head and tail positions
        if (!isInitializing) {
            if ((x >= 0 && x <= canvasSize.width && y >= 0 && y <= canvasSize.height) ||
                    (tailX >= 0 && tailX <= canvasSize.width && tailY >= 0 && tailY <= canvasSize.height)) {
                isVisible = true;
            } else {
                isVisible = false;
            }
        } else {
            // During initialization, keep the object active regardless of visibility
            if (isOnCanvas(x, y) || isOnCanvas(tailX, tailY)) {
                isInitializing = false; // Initialization complete
                isVisible = true;
            }
        }

        // Reset if the object travels too far from the canvas
        if (Math.abs(x - canvasSize.width / 2.0f) > RESET_DISTANCE ||
                Math.abs(y - canvasSize.height / 2.0f) > RESET_DISTANCE) {
            initializeOffScreenPosition();
            initializeTrajectory();
            isVisible = false; // Reset visibility
            isInitializing = true; // Set back to initializing state
            onReset(); // Notify subclasses of the reset
        }
    }

    // Protected reset callback for subclasses
    protected void onReset() {
        // Default implementation does nothing
    }

    // Abstract draw method that must be implemented by derived classes
    public abstract void draw(java.awt.Graphics2D g2d);

    // Check if the object is currently active and on-screen
    public boolean isActive() {
        return isVisible || isInitializing;
    }

    // Get the base speed of the object
    public float getBaseSpeed() {
        return baseSpeed;
    }

    // Getter and Setter for x
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    // Getter and Setter for y
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    // Utility method to check if a point is on the canvas
    protected boolean isOnCanvas(float x, float y) {
        return x >= 0 && x <= canvasSize.width && y >= 0 && y <= canvasSize.height;
    }
}
