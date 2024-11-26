package com.godscape.osrs.frames.panels;

import com.godscape.osrs.managers.panels.OsrsFooterManager;
import com.godscape.system.enums.BotState;
import com.godscape.system.factories.DependencyFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * OsrsFooterPanel represents the bottom panel of the UI, which includes start/resume, pause, and stop buttons for controlling the bot.
 */
public class OsrsFooterPanel extends JPanel {

    private final OsrsFooterManager footerManager;
    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;

    public OsrsFooterPanel() {
        this.footerManager = DependencyFactory.getInstance().getInjection(OsrsFooterManager.class);

        if (footerManager == null) {
            throw new IllegalStateException("OsrsFooterManager could not be initialized in OsrsFooterPanel.");
        }

        setOpaque(false); // Make the footer panel transparent to allow the theme background to show.
        initializeFooterPanel();
        updateButtonStates();
    }

    private void initializeFooterPanel() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Add 10px padding to all sides

        Dimension buttonSize = new Dimension(120, 50);  // Set the button size

        startButton = new JButton("Start");
        startButton.setPreferredSize(buttonSize);

        pauseButton = new JButton("Pause");
        pauseButton.setPreferredSize(buttonSize);

        stopButton = new JButton("Stop");
        stopButton.setPreferredSize(buttonSize);

        add(startButton);
        add(pauseButton);
        add(stopButton);

        startButton.addActionListener(e -> {
            footerManager.startScript();
            updateButtonStates();
        });

        pauseButton.addActionListener(e -> {
            footerManager.pauseScript();
            updateButtonStates();
        });

        stopButton.addActionListener(e -> {
            footerManager.stopScript();
            updateButtonStates();
        });
    }

    private void updateButtonStates() {
        BotState botState = footerManager.getBotState();

        switch (botState) {
            case RUNNING:
                startButton.setText("Running");
                startButton.setEnabled(false);
                pauseButton.setText("Pause");
                pauseButton.setEnabled(true);
                stopButton.setText("Stop");
                stopButton.setEnabled(true);
                break;
            case PAUSED:
                startButton.setText("Resume");
                startButton.setEnabled(true);
                pauseButton.setText("Paused");
                pauseButton.setEnabled(false);
                stopButton.setText("Stop");
                stopButton.setEnabled(true);
                break;
            case STOPPED:
                startButton.setText("Start");
                startButton.setEnabled(true);
                pauseButton.setText("Standby");
                pauseButton.setEnabled(false);
                stopButton.setText("Standby");
                stopButton.setEnabled(false);
                break;
        }
    }
}
