package com.godscape.system.templates;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class EmptyPanel extends JPanel {

    public EmptyPanel() {
        setOpaque(false); // Make the panel transparent
        setLayout(null); // No layout manager
        setPreferredSize(new Dimension(1200, 800)); // Optional size settings
        setMinimumSize(new Dimension(600, 400));
        setBorder(new EmptyBorder(0, 0, 0, 0)); // Remove any borders
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Do not draw anything, leaving the panel fully transparent
    }
}
