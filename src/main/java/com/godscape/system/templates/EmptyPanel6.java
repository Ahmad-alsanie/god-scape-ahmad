package com.godscape.system.templates;

import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmptyPanel6 extends BaseEmptyPanel {
    private static final Color TITLE_COLOR = new Color(75, 0, 130); // Indigo
    private static final Font TITLE_FONT = new Font("Garamond", Font.BOLD, 28);
    private static final Font MESSAGE_FONT = new Font("Garamond", Font.ITALIC, 16);
    private static final String EMPTY_MESSAGE = "Art is not what you see, but what you make others see. 🎨";
    private static final ImageIcon ART_ICON = loadIcon("/icons/art_icon.png");
    private static final Color MESSAGE_COLOR = new Color(60, 60, 60);
    private static final Color BUTTON_BG_COLOR = new Color(75, 0, 130, 180); // Semi-transparent Indigo

    // Member variables for components
    private JButton artButton;

    public EmptyPanel6() {
        super();
        // Additional customization if needed
    }

    @Override
    protected String getEmptyMessage() {
        return EMPTY_MESSAGE;
    }

    @Override
    protected ImageIcon getIcon() {
        return ART_ICON;
    }

    @Override
    protected String getTitle() {
        return "Gallery";
    }

    @Override
    protected Font getTitleFont() {
        return TITLE_FONT;
    }

    @Override
    protected Color getTitleColor() {
        return TITLE_COLOR;
    }

    @Override
    protected Font getMessageFont() {
        return MESSAGE_FONT;
    }

    @Override
    protected Color getMessageColor() {
        return MESSAGE_COLOR;
    }

    /**
     * Loads an icon from the specified path.
     *
     * @param path The resource path to the icon.
     * @return ImageIcon object or null if failed to load.
     */
    private static ImageIcon loadIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(EmptyPanel6.class.getResource(path));
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                throw new Exception("Image not found or failed to load.");
            }
            // Resize icon if necessary
            Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            Logger.error("EmptyPanel6: Failed to load icon '{}': {}", path, e);
            return null;
        }
    }

    @Override
    protected JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        footerPanel.setOpaque(false);

        // Art Button
        artButton = new JButton("Appreciate Art");
        artButton.setFocusPainted(false);
        artButton.setBackground(BUTTON_BG_COLOR); // Semi-transparent Indigo
        artButton.setForeground(Color.WHITE);
        artButton.setFont(new Font("Garamond", Font.BOLD, 14));
        artButton.setBorder(BorderFactory.createLineBorder(new Color(75, 0, 130), 2));
        artButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        artButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                artButton.setBackground(new Color(75, 0, 130)); // Opaque Indigo on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                artButton.setBackground(BUTTON_BG_COLOR); // Semi-transparent Indigo
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(EmptyPanel6.this, "Art appreciation session coming soon!");
                // Implement actual art appreciation logic here
            }
        });

        footerPanel.add(artButton);

        return footerPanel;
    }

    /**
     * Custom painting for the background with watercolor splash.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw watercolor splash background
        Graphics2D g2d = (Graphics2D) g.create();
        // Enable anti-aliasing for smoother images
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Load and draw the watercolor image
        try {
            Image watercolor = new ImageIcon(getClass().getResource("/images/watercolor_splash.png")).getImage(); // Ensure the image exists
            if (watercolor == null) {
                throw new Exception("Watercolor image not found.");
            }
            g2d.drawImage(watercolor, 0, 0, getWidth(), getHeight(), this);
        } catch (Exception e) {
            Logger.error("EmptyPanel6: Failed to load watercolor background '/images/watercolor_splash.png': {}", e);
            // Fallback to solid color
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.dispose();
    }
}
