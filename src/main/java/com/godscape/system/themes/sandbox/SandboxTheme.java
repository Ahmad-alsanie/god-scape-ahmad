// File: com/godscape/system/themes/sandbox/SandboxTheme.java
package com.godscape.system.themes.sandbox;

import com.godscape.system.themes.ThemeAbstraction;
import com.godscape.system.themes.sandbox.scenes.SandboxBackground;

import javax.swing.*;
import java.awt.*;

/**
 * SandboxTheme is an empty theme used for testing individual components.
 */
public class SandboxTheme extends ThemeAbstraction {
    private SandboxBackground background;

    /**
     * Default constructor which uses the full screen size.
     */
    public SandboxTheme() {
        super();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        background = new SandboxBackground(screenSize.width, screenSize.height);
        this.add(background);  // Add background as a component to SandboxTheme
    }

    @Override
    public void initializeObjects() {
        // No additional objects to initialize in this sandbox theme
    }

    @Override
    protected void drawBackground(Graphics2D g2d) {
        // Delegate background drawing to SandboxBackground
        background.repaint();
    }

    /**
     * Main method to test the SandboxTheme independently.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sandbox Theme - Testing Components");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize SandboxTheme which will use the full screen size
        SandboxTheme sandboxTheme = new SandboxTheme();
        frame.add(sandboxTheme);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize to full screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
