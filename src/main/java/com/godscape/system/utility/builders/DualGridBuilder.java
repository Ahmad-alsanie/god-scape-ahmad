package com.godscape.system.utility.builders;

import javax.swing.*;
import java.awt.*;

public class DualGridBuilder {
    private final JPanel mainPanel;
    private final JPanel leftPanel;
    private final JPanel rightPanel;
    private final GridBagConstraints leftConstraints;
    private final GridBagConstraints rightConstraints;
    private int leftRow = 0;
    private int rightRow = 0;

    public DualGridBuilder() {
        mainPanel = new JPanel(new GridBagLayout());

        // Left panel setup
        leftPanel = new JPanel(new GridBagLayout());
        leftConstraints = new GridBagConstraints();
        leftConstraints.insets = new Insets(5, 5, 5, 5);
        leftConstraints.anchor = GridBagConstraints.NORTHWEST;
        leftConstraints.fill = GridBagConstraints.HORIZONTAL;
        leftConstraints.weightx = 1.0;
        leftConstraints.weighty = 0.0;

        // Right panel setup
        rightPanel = new JPanel(new GridBagLayout());
        rightConstraints = new GridBagConstraints();
        rightConstraints.insets = new Insets(5, 5, 5, 5);
        rightConstraints.anchor = GridBagConstraints.NORTHWEST;
        rightConstraints.fill = GridBagConstraints.BOTH;
        rightConstraints.weightx = 0.5;
        rightConstraints.weighty = 1.0;

        // Add leftPanel to mainPanel with separate constraints
        GridBagConstraints leftMainConstraints = new GridBagConstraints();
        leftMainConstraints.gridx = 0;
        leftMainConstraints.gridy = 0;
        leftMainConstraints.weightx = 0.5; // Set to 50%
        leftMainConstraints.weighty = 1.0;
        leftMainConstraints.fill = GridBagConstraints.BOTH;
        leftMainConstraints.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(leftPanel, leftMainConstraints);

        // Add rightPanel to mainPanel with separate constraints
        GridBagConstraints rightMainConstraints = new GridBagConstraints();
        rightMainConstraints.gridx = 1;
        rightMainConstraints.gridy = 0;
        rightMainConstraints.weightx = 0.5; // Set to 50%
        rightMainConstraints.weighty = 1.0;
        rightMainConstraints.fill = GridBagConstraints.BOTH;
        mainPanel.add(rightPanel, rightMainConstraints);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Method to set JLabel alignment to line end
    private JComponent prepareComponent(JComponent component) {
        if (component instanceof JLabel) {
            ((JLabel) component).setHorizontalAlignment(SwingConstants.RIGHT);
        }
        return component;
    }

    // Method to wrap a component with padding
    private JComponent wrapWithPadding(JComponent component, int padding) {
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        paddedPanel.add(component, BorderLayout.CENTER);
        return paddedPanel;
    }

    // Methods to add components to the left grid
    public void addLeftComponent(JComponent component) {
        GridBagConstraints gbc = (GridBagConstraints) leftConstraints.clone();
        gbc.gridx = 0;
        gbc.gridy = leftRow++;
        leftPanel.add(prepareComponent(component), gbc);
    }

    public void addLeftComponent(JComponent component, GridBagConstraints constraints) {
        GridBagConstraints gbc = (GridBagConstraints) constraints.clone();
        gbc.gridy = leftRow++;
        leftPanel.add(prepareComponent(component), gbc);
    }

    // Methods to add components to the right grid
    public void addRightComponent(JComponent component) {
        GridBagConstraints gbc = (GridBagConstraints) rightConstraints.clone();
        gbc.gridx = 0;
        gbc.gridy = rightRow++;
        rightPanel.add(prepareComponent(component), gbc);
    }

    public void addRightComponent(JComponent component, GridBagConstraints constraints) {
        GridBagConstraints gbc = (GridBagConstraints) constraints.clone();
        gbc.gridy = rightRow++;
        rightPanel.add(prepareComponent(component), gbc);
    }

    // Method to add a padded, fixed-width list to the right grid
    public void addPaddedListToRight(JList<?> list, int padding) {
        JScrollPane scrollPane = new JScrollPane(list);

        // Set the preferred width to the full width of the right panel minus padding
        //int fixedWidth = rightPanel.getPreferredSize().width - (2 * padding);
        int fixedWidth = 300;
        scrollPane.setPreferredSize(new Dimension(fixedWidth, scrollPane.getPreferredSize().height));

        // Add padding around the scrollPane and add it to the right panel
        addRightComponent(wrapWithPadding(scrollPane, padding));
    }

    // Optional methods to reset the row counters if needed
    public void resetLeftRow() {
        leftRow = 0;
    }

    public void resetRightRow() {
        rightRow = 0;
    }
}
