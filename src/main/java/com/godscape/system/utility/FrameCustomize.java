package com.godscape.system.utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrameCustomize {

    /**
     * Sets the background color of the frame.
     *
     * @param frame The JFrame to customize.
     * @param color The background color to set.
     */
    public static void setBackgroundColor(JFrame frame, Color color) {
        frame.getContentPane().setBackground(color);
    }

    /**
     * Sets the size of the frame.
     *
     * @param frame The JFrame to customize.
     * @param width The width of the frame.
     * @param height The height of the frame.
     */
    public static void setSize(JFrame frame, int width, int height) {
        frame.setSize(width, height);
    }

    /**
     * Sets the title of the frame.
     *
     * @param frame The JFrame to customize.
     * @param title The title to set.
     */
    public static void setTitle(JFrame frame, String title) {
        frame.setTitle(title);
    }

    /**
     * Sets the visibility of the frame's title bar.
     *
     * @param frame The JFrame to customize.
     * @param visible Whether the title bar should be visible.
     */
    public static void setTitleBarVisible(JFrame frame, boolean visible) {
        frame.setUndecorated(!visible);
    }

    /**
     * Adds a custom font to all components in the frame.
     *
     * @param frame The JFrame to customize.
     * @param font The font to apply to all components.
     */
    public static void setFont(JFrame frame, Font font) {
        for (Component component : frame.getContentPane().getComponents()) {
            component.setFont(font);
            if (component instanceof JPanel) {
                setFontForPanel((JPanel) component, font);
            }
        }
    }

    /**
     * Sets the font for all components within a panel.
     *
     * @param panel The panel containing components.
     * @param font The font to apply.
     */
    private static void setFontForPanel(JPanel panel, Font font) {
        for (Component component : panel.getComponents()) {
            component.setFont(font);
            if (component instanceof JPanel) {
                setFontForPanel((JPanel) component, font);
            }
        }
    }

    /**
     * Enables or disables window decorations (frame borders and title bar).
     *
     * @param frame The JFrame to customize.
     * @param enabled Whether to enable window decorations.
     */
    public static void setWindowDecorations(JFrame frame, boolean enabled) {
        frame.setDefaultLookAndFeelDecorated(enabled);
    }

    /**
     * Adds a custom title bar with buttons to the frame.
     *
     * @param frame The JFrame to customize.
     */
    public static void addCustomTitleBar(JFrame frame) {
        JPanel titleBar = new JPanel();
        titleBar.setLayout(new BorderLayout());
        titleBar.setBackground(Color.DARK_GRAY);

        JLabel titleLabel = new JLabel(frame.getTitle());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton closeButton = new JButton("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> frame.dispose());

        titleBar.add(titleLabel, BorderLayout.CENTER);
        titleBar.add(closeButton, BorderLayout.EAST);

        frame.getContentPane().add(titleBar, BorderLayout.NORTH);
        frame.setUndecorated(true); // Hide the default title bar
    }

    /**
     * Sets the frame to be full screen or not.
     *
     * @param frame The JFrame to customize.
     * @param fullscreen Whether to set the frame to full screen.
     */
    public static void setFullscreen(JFrame frame, boolean fullscreen) {
        if (fullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
        } else {
            frame.setExtendedState(JFrame.NORMAL);
            frame.setUndecorated(false);
        }
    }

    /**
     * Adds a status bar to the bottom of the frame.
     *
     * @param frame The JFrame to customize.
     * @param text The text to display in the status bar.
     */
    public static void addStatusBar(JFrame frame, String text) {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setPreferredSize(new Dimension(frame.getWidth(), 30));
        statusBar.setBackground(Color.LIGHT_GRAY);

        JLabel statusLabel = new JLabel(text);
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusBar.add(statusLabel, BorderLayout.WEST);

        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    /**
     * Adds a menu bar to the frame with custom menu items.
     *
     * @param frame The JFrame to customize.
     * @param menuItems The array of menu items to add.
     */
    public static void addMenuBar(JFrame frame, String[] menuItems) {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");

        for (String item : menuItems) {
            JMenuItem menuItem = new JMenuItem(item);
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Logger.info("Menu item clicked: " + item);
                }
            });
            menu.add(menuItem);
        }

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
    }
}
