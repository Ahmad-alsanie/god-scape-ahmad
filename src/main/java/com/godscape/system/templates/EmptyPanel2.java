package com.godscape.system.templates;

import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmptyPanel2 extends JPanel {
    private static final Color BACKGROUND_COLOR = new Color(34, 34, 34); // Dark Gray
    private static final Color ACCENT_COLOR = new Color(0, 204, 204); // Cyan
    private static final Font TITLE_FONT = new Font("Tahoma", Font.BOLD, 30);
    private static final Font MESSAGE_FONT = new Font("Tahoma", Font.PLAIN, 18);
    private static final String EMPTY_MESSAGE = "This section is currently unavailable. Please check back later! 🌙";

    // Member variables for components
    private JLabel headingLabel;
    private JLabel messageLabel;
    private JButton retryButton;

    public EmptyPanel2() {
        Logger.info("EmptyPanel2: Initializing...");

        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());

        // Create and add components
        add(createHeader(), BorderLayout.NORTH);
        add(createCenterMessage(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        Logger.info("EmptyPanel2: Initialized successfully.");
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
            Logger.error("EmptyPanel2: headingLabel is null.");
        }

        if (messageLabel != null) {
            messageLabel.setText(EMPTY_MESSAGE);
        } else {
            Logger.error("EmptyPanel2: messageLabel is null.");
        }

        // Refresh the UI
        revalidate();
        repaint();
    }

    /**
     * Creates the header section with the title and animated icon.
     */
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
        headerPanel.setOpaque(true); // Set to true temporarily for testing

        // Animated Icon (e.g., a spinning cog)
        try {
            ImageIcon spinnerIcon = new ImageIcon(getClass().getResource("/icons/spinner.gif")); // Ensure spinner.gif exists
            if (spinnerIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                throw new Exception("Image not found or failed to load.");
            }
            JLabel iconLabel = new JLabel(spinnerIcon);
            headerPanel.add(iconLabel);
        } catch (Exception e) {
            Logger.error("EmptyPanel2: Failed to load spinner.gif. Using fallback.");
            JLabel fallbackIconLabel = new JLabel("🔄"); // Fallback placeholder icon
            headerPanel.add(fallbackIconLabel);
        }

        // Title Label
        headingLabel = new JLabel("Loading...");
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
        centerPanel.setOpaque(true); // Set to true temporarily for testing

        messageLabel = new JLabel(EMPTY_MESSAGE);
        messageLabel.setFont(MESSAGE_FONT);
        messageLabel.setForeground(Color.LIGHT_GRAY);
        centerPanel.add(messageLabel);

        return centerPanel;
    }

    /**
     * Creates the footer section with an interactive button.
     */
    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        footerPanel.setOpaque(true); // Set to true temporarily for testing

        // Retry Button
        retryButton = new JButton("Retry");
        retryButton.setFocusPainted(false);
        retryButton.setBackground(ACCENT_COLOR);
        retryButton.setForeground(Color.WHITE);
        retryButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        retryButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 2));
        retryButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        retryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                retryButton.setBackground(new Color(0, 179, 179));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                retryButton.setBackground(ACCENT_COLOR);
            }
        });

        // Action Listener
        retryButton.addActionListener(e -> {
            Logger.info("EmptyPanel2: Retry button clicked.");
            JOptionPane.showMessageDialog(this, "Retrying to load the panel...");
            // Optionally, implement actual retry logic here
        });

        footerPanel.add(retryButton);

        return footerPanel;
    }

    /**
     * Custom painting for the background with subtle patterns.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Optional: Add subtle patterns or gradients if desired
        Graphics2D g2d = (Graphics2D) g.create();
        // For example, adding a diagonal gradient
        GradientPaint gp = new GradientPaint(
                0, 0, BACKGROUND_COLOR,
                getWidth(), getHeight(), new Color(30, 30, 30)
        );
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }
}
