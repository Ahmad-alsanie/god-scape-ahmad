package com.godscape.system.templates;

import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A sophisticated and transparent JPanel that displays a "Coming Soon" message.
 */
public class ComingSoon extends JPanel {
    private Timer timer;
    private int yPosition = 700; // Initial position for the scrolling text
    private JLabel headingLabel;
    private String panelPath; // To store the panel path title

    /**
     * Constructs the ComingSoon panel with styled text and transparency.
     */
    public ComingSoon() {
        setPreferredSize(new Dimension(800, 600));
        setOpaque(false); // Make the panel transparent
        setDoubleBuffered(true); // Enable double buffering
        initializeComponents();
        initializeScrollTimer();
    }

    /**
     * Initializes the heading label and other components.
     */
    private void initializeComponents() {
        headingLabel = new JLabel("Under Construction");
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(headingLabel, BorderLayout.NORTH);
    }

    /**
     * Initializes the timer to scroll the text upwards.
     */
    private void initializeScrollTimer() {
        timer = new Timer(100, new ActionListener() { // Increase timer interval to 100ms
            @Override
            public void actionPerformed(ActionEvent e) {
                yPosition -= 2; // Speed of scrolling
                if (yPosition < -600) {
                    yPosition = 700; // Reset position after scrolling off-screen
                }
                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for smoother text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set transparency
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

        // Draw the 3D flat-style effect text
        draw3DScrollingText(g2d);

        g2d.dispose();
    }

    /**
     * Draws the scrolling text content on the panel with a 3D perspective effect.
     */
    private void draw3DScrollingText(Graphics2D g2d) {
        String scrollingText = "Coming Soon\n" + panelPath;

        // Split the text into lines
        String[] lines = scrollingText.split("\n");

        int lineHeight = g2d.getFontMetrics().getHeight();
        int y = yPosition;
        int panelHeight = getHeight();

        // Apply a 3D perspective transformation to each line
        for (String line : lines) {
            // Calculate the scale factor based on the position on the screen
            double scaleFactor = 1.0 - ((double) (y - panelHeight / 4) / panelHeight);
            scaleFactor = Math.max(scaleFactor, 0.5); // Prevent the scale from becoming too small

            // Set font size based on the scale factor
            Font originalFont = g2d.getFont();
            g2d.setFont(originalFont.deriveFont((float) (24 * scaleFactor)));

            // Set transparency based on the scale factor for a fading effect
            float alpha = (float) Math.min(1.0, scaleFactor);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Calculate the x position to keep the text centered
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(line);
            int x = (getWidth() - textWidth) / 2;

            // Draw the text
            g2d.drawString(line, x, y);

            // Restore the original font and update y position for the next line
            g2d.setFont(originalFont);
            y += lineHeight * scaleFactor;
        }
    }

    /**
     * Sets the panel path (title).
     */
    public void setPanelPath(String panelPath) {
        this.panelPath = panelPath;
        if (headingLabel != null) {
            headingLabel.setText(panelPath);
        } else {
            Logger.error("headingLabel is null.");
        }
        revalidate();
        repaint();
    }

    /**
     * Main method for standalone testing of the ComingSoon panel.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Create a JFrame to showcase the ComingSoon panel
        JFrame frame = new JFrame("Godscape: Under Construction");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Add an instance of ComingSoon to the frame
        ComingSoon panel = new ComingSoon();
        panel.setPanelPath("Godscape Features");
        frame.add(panel);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Make the frame visible
        frame.setVisible(true);
    }
}
