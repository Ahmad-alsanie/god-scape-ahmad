package com.godscape.system.templates;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract base class for Empty Panels with common functionalities for setting titles and messages.
 */
public abstract class BaseEmptyPanel extends JPanel {

    public BaseEmptyPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        add(createHeader(), BorderLayout.NORTH);
        add(createCenterMessage(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    /**
     * Abstract method to get the title for the panel.
     * @return title as String
     */
    protected abstract String getTitle();

    /**
     * Abstract method to get the title font.
     * @return Font for the title
     */
    protected abstract Font getTitleFont();

    /**
     * Abstract method to get the title color.
     * @return Color of the title
     */
    protected abstract Color getTitleColor();

    /**
     * Abstract method to get the empty message.
     * @return Message string
     */
    protected abstract String getEmptyMessage();

    /**
     * Abstract method to get the message font.
     * @return Font for the message
     */
    protected abstract Font getMessageFont();

    /**
     * Abstract method to get the message color.
     * @return Color of the message
     */
    protected abstract Color getMessageColor();

    /**
     * Abstract method to get the icon.
     * @return Icon as ImageIcon
     */
    protected abstract ImageIcon getIcon();

    /**
     * Create the header section.
     * @return JPanel for the header
     */
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(getIcon());
        JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(getTitleFont());
        titleLabel.setForeground(getTitleColor());

        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);

        return headerPanel;
    }

    /**
     * Create the center message section.
     * @return JPanel for the message
     */
    private JPanel createCenterMessage() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JLabel messageLabel = new JLabel(getEmptyMessage());
        messageLabel.setFont(getMessageFont());
        messageLabel.setForeground(getMessageColor());
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        centerPanel.add(messageLabel, BorderLayout.CENTER);

        return centerPanel;
    }

    /**
     * Create the footer section. Can be overridden by subclasses.
     * @return JPanel for the footer
     */
    protected JPanel createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        footerPanel.setOpaque(false);

        JLabel footerLabel = new JLabel("Footer section - can be customized.");
        footerLabel.setForeground(Color.LIGHT_GRAY);
        footerPanel.add(footerLabel);

        return footerPanel;
    }
}
