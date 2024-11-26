package com.godscape.system.factories;

import javax.swing.*;

import com.godscape.osrs.frames.OsrsMainFrame;
import com.godscape.system.utility.Logger;

/**
 * Factory class responsible for managing frames.
 */
public class FrameFactory {
    private static volatile FrameFactory instance;

    public FrameFactory() {
        Logger.info("FrameFactory: Initialization complete.");
    }

    public static FrameFactory getInstance() {
        if (instance == null) {
            synchronized (FrameFactory.class) {
                if (instance == null) {
                    Logger.info("FrameFactory: Initializing FrameFactory...");
                    instance = new FrameFactory();
                }
            }
        }
        return instance;
    }

    /**
     * Opens the platform-specific main frame. Uses the OsrsMainFrame Singleton.
     * Ensures that the UI remains accessible even if initializations fail.
     */
    public void getFrame() {
        Logger.info("Opening platform-specific UI...");
        SwingUtilities.invokeLater(() -> {
            try {
                DependencyFactory dependencyFactory = DependencyFactory.getInstance();
                OsrsMainFrame mainFrame = dependencyFactory.getInjection(OsrsMainFrame.class);
                if (!mainFrame.isVisible()) {
                    mainFrame.setVisible(true);
                    Logger.info("OsrsMainFrame is now visible.");
                } else {
                    Logger.info("OsrsMainFrame is already open. Bringing it to the front.");
                    mainFrame.toFront();
                    mainFrame.requestFocus();
                }
            } catch (Exception e) {
                Logger.error("FrameFactory: Failed to initialize OsrsMainFrame - {}", e.getMessage(), e);
                JOptionPane.showMessageDialog(null, "Failed to open main frame. Check logs for details.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Disposes of the current main frame.
     */
    public void disposeFrame() {
        SwingUtilities.invokeLater(OsrsMainFrame::closeUI);
    }
}
