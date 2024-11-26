package com.godscape.system.templates;

import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmptyPanel5 extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(10, 10, 10); // Very Dark Gray
    private static final Color NEON_COLOR = new Color(57, 255, 20); // Neon Green
    private static final Font TITLE_FONT = new Font("Orbitron", Font.BOLD, 32);
    private static final Font MESSAGE_FONT = new Font("Orbitron", Font.PLAIN, 18);
    private static final String EMPTY_MESSAGE = "This area is currently experiencing high latency. 🚀";

    // Member variables for components
    private JLabel headingLabel;
    private JLabel messageLabel;
    private JButton glowButton;

    public EmptyPanel5() {
        setBackground(BACKGROUND_COLOR);
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
            Logger.error("EmptyPanel5: headingLabel is null.");
        }

        if (messageLabel != null) {
            messageLabel.setText(EMPTY_MESSAGE);
        } else {
            Logger.error("EmptyPanel5: messageLabel is null.");
        }

        // Refresh the UI
        revalidate();
        repaint();
    }

    /**
     * Creates the header section with the title and neon icon.
     */
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
        headerPanel.setOpaque(false);

        // Neon Icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/neon_icon.png")); // Ensure the icon exists
            JLabel iconLabel = new JLabel(icon);
            headerPanel.add(iconLabel);
        } catch (Exception e) {
            Logger.error("EmptyPanel5: Failed to load icon '/icons/neon_icon.png': {}", e);
            // Add a text placeholder instead
            JLabel placeholder = new JLabel("🔮");
            placeholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
            headerPanel.add(placeholder);
        }

        // Title Label
        headingLabel = new JLabel("Neon Zone");
        headingLabel.setFont(TITLE_FONT);
        headingLabel.setForeground(NEON_COLOR);
        headerPanel.add(headingLabel);

        return headerPanel;
    }

    /**
     * Creates the center message section.
     */
    private JPanel createCenterMessage() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        messageLabel = new JLabel(EMPTY_MESSAGE);
        messageLabel.setFont(MESSAGE_FONT);
        messageLabel.setForeground(NEON_COLOR);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(messageLabel);

        return centerPanel;
    }

    /**
     * Creates the footer section with an interactive glowing button.
     */
    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
        footerPanel.setOpaque(false);

        // Glowing Button
        glowButton = new JButton("Activate");
        glowButton.setFocusPainted(false);
        glowButton.setBackground(BACKGROUND_COLOR);
        glowButton.setForeground(NEON_COLOR);
        glowButton.setFont(new Font("Orbitron", Font.BOLD, 16));
        glowButton.setBorder(BorderFactory.createLineBorder(NEON_COLOR, 2));
        glowButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        glowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                glowButton.setBorder(BorderFactory.createLineBorder(NEON_COLOR.brighter(), 3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                glowButton.setBorder(BorderFactory.createLineBorder(NEON_COLOR, 2));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(EmptyPanel5.this, "Activation in progress...");
                // Implement actual activation logic here
            }
        });

        footerPanel.add(glowButton);

        return footerPanel;
    }

    /**
     * Custom painting for the background with futuristic patterns.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Optional: Add futuristic patterns or glowing effects
        Graphics2D g2d = (Graphics2D) g.create();

        // Example: Draw diagonal neon lines
        g2d.setColor(NEON_COLOR);
        for (int i = 0; i < getWidth(); i += 100) {
            g2d.drawLine(i, 0, i + 50, getHeight());
        }

        g2d.dispose();
    }
}
