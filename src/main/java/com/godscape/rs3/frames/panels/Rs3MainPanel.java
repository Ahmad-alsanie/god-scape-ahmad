package com.godscape.rs3.frames.panels;

import javax.swing.*;
import java.awt.*;

/**
 * Placeholder panel for RS3 Main Interface.
 */
public class Rs3MainPanel extends JPanel {

    /**
     * Constructor for Rs3MainPanel.
     * Initializes the panel with a simple label.
     */
    public Rs3MainPanel() {
        // Set a layout manager (optional, default is FlowLayout)
        setLayout(new BorderLayout());

        // Create a placeholder label
        JLabel placeholderLabel = new JLabel("RS3 Main Panel Placeholder", SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Add the label to the center of the panel
        add(placeholderLabel, BorderLayout.CENTER);

        // Optional: Set a background color for visibility
        setBackground(Color.LIGHT_GRAY);
    }
}
