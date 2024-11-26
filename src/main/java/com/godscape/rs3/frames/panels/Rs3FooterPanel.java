package com.godscape.rs3.frames.panels;

import javax.swing.*;
import java.awt.*;

public class Rs3FooterPanel extends JPanel {
    public Rs3FooterPanel() {
        setLayout(new FlowLayout()); // Set layout for the footer panel
        JLabel footerLabel = new JLabel("RS3 Footer Panel");
        add(footerLabel); // Add a label to the footer panel
        // Additional components can be added here
    }
}
