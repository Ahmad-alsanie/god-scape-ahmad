package com.godscape.system.utility.builders;

import javax.swing.*;
import java.awt.*;

public class CustomGridPanel extends JPanel {
    private final GridBagLayout gridBagLayout;

    public CustomGridPanel() {
        gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
    }

    public void addComponent(JComponent component,
                             int gridx, int gridy,
                             int gridwidth, int gridheight,
                             double weightx, double weighty,
                             int fill, Insets insets) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;             // Column position
        gbc.gridy = gridy;             // Row position
        gbc.gridwidth = gridwidth;     // Number of columns to span
        gbc.gridheight = gridheight;   // Number of rows to span
        gbc.weightx = weightx;         // How to distribute space horizontally
        gbc.weighty = weighty;         // How to distribute space vertically
        gbc.fill = fill;               // How the component should fill its display area
        gbc.insets = insets;           // Padding around the component
        gbc.anchor = GridBagConstraints.NORTHWEST; // Positioning within the cell

        add(component, gbc);
    }

    // Convenience method for adding components without specifying all constraints
    public void addComponent(JComponent component, int gridx, int gridy) {
        addComponent(component, gridx, gridy,
                1, 1, 0.0, 0.0,
                GridBagConstraints.NONE,
                new Insets(5, 5, 5, 5));
    }
}
