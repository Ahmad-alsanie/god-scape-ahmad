package com.godscape.rs3.frames.panels;

import javax.swing.*;
import java.awt.*;

public class Rs3HeaderPanel extends JPanel {
    public Rs3HeaderPanel() {
        setLayout(new FlowLayout()); // Set layout for the header panel
        JLabel headerLabel = new JLabel("RS3 Header Panel");
        add(headerLabel); // Add a label to the header panel
        // Additional components can be added here
    }
}
