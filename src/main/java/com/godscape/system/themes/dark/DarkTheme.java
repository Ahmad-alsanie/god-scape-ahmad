package com.godscape.system.themes.dark;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * DarkTheme.java
 *
 * This class represents a sleek, modern, and lightweight dark theme for your application.
 * It defines a consistent color scheme, fonts, and styling for components.
 */
public class DarkTheme extends JPanel {

        public DarkTheme() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10)); // Add 10px padding to all sides
            setBackground(Color.BLACK); // Set the background color to black
        }
}
