package com.godscape.system.templates;

import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmptyPanel3 extends JPanel {
    private static final Color[] GRADIENT_COLORS = {new Color(255, 87, 34), new Color(255, 152, 0)}; // Orange Gradient
    private static final Font TITLE_FONT = new Font("Verdana", Font.BOLD, 26);
    private static final Font MESSAGE_FONT = new Font("Verdana", Font.PLAIN, 16);
    private static final String EMPTY_MESSAGE = "Whoops! Nothing here yet. Stay tuned! 🎨";

    // Member variables for components
    private JLabel headingLabel;
    private JLabel messageLabel;
    private JLabel interactiveLabel;

    public EmptyPanel3() {
        setOpaque(false); // To allow custom painting of background
        setLayout(new BorderLayout());

        // Create and add components
        add(createHeader(), BorderLayout.NORTH);
        add(createCenterMessage(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    /**
     * Updates the panel to display the current active tab path.
     *
     * @param panelPath The full path of the current active tab.
     */
    public void setPanelPath(String panelPath) {
        if (headingLabel != null) {
            headingLabel.setText(panelPath);
        } else {
            Logger.error("EmptyPanel3: headingLabel is null.");
        }

        if (messageLabel != null) {
            messageLabel.setText(EMPTY_MESSAGE);
        } else {
            Logger.error("EmptyPanel3: messageLabel is null.");
        }

        // Refresh the UI
        revalidate();
        repaint();
    }

    /**
     * Creates the header section with the title and icon with shadow.
     */
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
        headerPanel.setOpaque(false);

        // Icon with Shadow
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/paintbrush.png")); // Ensure the icon exists
            JLabel iconLabel = new JLabel(icon);
            // Add shadow effect
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            headerPanel.add(iconLabel);
        } catch (Exception e) {
            Logger.error("EmptyPanel3: Failed to load icon '/icons/paintbrush.png': {}", e);
            // Add a text placeholder instead
            JLabel placeholder = new JLabel("🎨");
            placeholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
            headerPanel.add(placeholder);
        }

        // Title Label
        headingLabel = new JLabel("Explore");
        headingLabel.setFont(TITLE_FONT);
        headingLabel.setForeground(Color.WHITE);
        headerPanel.add(headingLabel);

        return headerPanel;
    }

    /**
     * Creates the center message section with an interactive label.
     */
    private JPanel createCenterMessage() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        messageLabel = new JLabel(EMPTY_MESSAGE);
        messageLabel.setFont(MESSAGE_FONT);
        messageLabel.setForeground(Color.WHITE);
        centerPanel.add(messageLabel);

        // Interactive Label
        interactiveLabel = new JLabel("Click here to learn more!");
        interactiveLabel.setFont(new Font("Verdana", Font.ITALIC, 14));
        interactiveLabel.setForeground(new Color(255, 255, 255, 180));
        interactiveLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        interactiveLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                interactiveLabel.setForeground(new Color(255, 255, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                interactiveLabel.setForeground(new Color(255, 255, 255, 180));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(EmptyPanel3.this, "More information will be available soon!");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        centerPanel.add(interactiveLabel, gbc);

        return centerPanel;
    }

    /**
     * Creates the footer section with decorative elements.
     */
    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        footerPanel.setOpaque(false);

        // Decorative Button (non-functional)
        JButton decorativeButton = new JButton("Decor");
        decorativeButton.setFocusPainted(false);
        decorativeButton.setBackground(new Color(255, 255, 255, 100));
        decorativeButton.setForeground(GRADIENT_COLORS[0]);
        decorativeButton.setFont(new Font("Verdana", Font.BOLD, 12));
        decorativeButton.setBorder(BorderFactory.createLineBorder(GRADIENT_COLORS[0], 1));
        decorativeButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        footerPanel.add(decorativeButton);

        return footerPanel;
    }

    /**
     * Custom painting for the background with vibrant gradient.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw vibrant gradient background
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();

        GradientPaint gp = new GradientPaint(
                0, 0, GRADIENT_COLORS[0],
                width, height, GRADIENT_COLORS[1]
        );
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);

        g2d.dispose();
    }
}
