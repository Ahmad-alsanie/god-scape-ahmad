// File: com/godscape/system/themes/sandbox/scenes/SandboxBackground.java
package com.godscape.system.themes.sandbox.scenes;

import com.godscape.system.themes.sandbox.objects.Backdrop;

import javax.swing.*;
import java.awt.*;

public class SandboxBackground extends JPanel {
    private Backdrop backdrop;

    public SandboxBackground(int width, int height) {
        this.backdrop = new Backdrop(width, height); // Pass width and height here
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        backdrop.draw(g2d);  // Use Backdrop to draw the Halloween scene
    }

    /**
     * Main method for testing the SandboxBackground independently.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sandbox Background - Halloween Shadowfest Scene");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        SandboxBackground background = new SandboxBackground(width, height);
        frame.add(background);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        frame.setVisible(true);
    }
}
