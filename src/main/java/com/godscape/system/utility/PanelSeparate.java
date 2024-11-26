package com.godscape.system.utility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelSeparate extends JPanel {

    public PanelSeparate(String title) {
        setLayout(new BorderLayout());
        setOpaque(false); // Make the separator transparent, if needed

        // Create a label for the title with bold, white font
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 15)); // Set font to bold
        titleLabel.setForeground(Color.WHITE); // Set text color to white
        titleLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Create a JSeparator to go underneath the title
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.LIGHT_GRAY);
        separator.setPreferredSize(new Dimension(1, 1));

        // Add title label and separator to the panel
        add(titleLabel, BorderLayout.NORTH);
        add(separator, BorderLayout.CENTER);
    }

    // Overloaded constructor for a simple separator without a title
    public PanelSeparate() {
        this("");
    }
}
