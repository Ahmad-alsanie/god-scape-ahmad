package com.godscape.system.templates;

import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;

public class EmptyPanel4 extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color ACCENT_COLOR = new Color(100, 149, 237); // Cornflower Blue
    private static final Font TITLE_FONT = new Font("Helvetica Neue", Font.BOLD, 24);
    private static final Font MESSAGE_FONT = new Font("Helvetica Neue", Font.PLAIN, 16);
    private static final String EMPTY_MESSAGE = "Nothing to display here right now. 🌿";

    // Member variables for components
    private JLabel headingLabel;
    private JLabel messageLabel;

    public EmptyPanel4() {
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Create and add components
        add(createHeader(), BorderLayout.NORTH);
        add(createCenterMessage(), BorderLayout.CENTER);
        // No footer for a cleaner look
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
            Logger.error("EmptyPanel4: headingLabel is null.");
        }

        if (messageLabel != null) {
            messageLabel.setText(EMPTY_MESSAGE);
        } else {
            Logger.error("EmptyPanel4: messageLabel is null.");
        }

        // Refresh the UI
        revalidate();
        repaint();
    }

    /**
     * Creates the header section with the title.
     */
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 40));
        headerPanel.setOpaque(false);

        // Title Label
        headingLabel = new JLabel("Welcome");
        headingLabel.setFont(TITLE_FONT);
        headingLabel.setForeground(ACCENT_COLOR);
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
        messageLabel.setForeground(new Color(80, 80, 80));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(messageLabel);

        return centerPanel;
    }

    /**
     * Custom painting for the background with minimalist patterns.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Optional: Add subtle lines or shapes
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(220, 220, 220));
        // Draw horizontal lines
        for (int y = 50; y < getHeight(); y += 100) {
            g2d.drawLine(0, y, getWidth(), y);
        }
        g2d.dispose();
    }
}
