package com.godscape.rs3.frames;

import com.godscape.rs3.frames.panels.Rs3HeaderPanel;
import com.godscape.system.controllers.BotController;
import com.godscape.system.enums.Controllers;
import com.godscape.system.enums.Factories;
import com.godscape.rs3.enums.core.Rs3Panels;
import com.godscape.system.factories.ControllerFactory;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PanelFactory;
import com.godscape.system.utility.Logger;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * Main UI class that integrates profile management and various bot settings into a tabbed interface for RS3.
 */
@Getter
public class Rs3MainFrame extends JFrame {

    // Singleton instance
    private static volatile Rs3MainFrame instance;

    // Dependencies
    private final DependencyFactory dependencyFactory;
    private final ControllerFactory controllerFactory;
    private final PanelFactory panelFactory;

    private BotController botController;

    private JTabbedPane tabbedPane;
    private Rs3HeaderPanel headerPanel;
    private JPanel footerPanel;
    private JPanel mainContentPanel;
    private Dimension originalSize;

    /**
     * Private constructor to ensure Singleton pattern.
     * Handles dependency initialization internally via DependencyFactory.
     */
    private Rs3MainFrame() {
        this.dependencyFactory = DependencyFactory.getInstance();
        this.controllerFactory = dependencyFactory.getInjection(ControllerFactory.class);
        this.panelFactory = dependencyFactory.getInjection(PanelFactory.class);

        // Initialize BotController
        this.botController = controllerFactory.getController(Controllers.BOT_CONTROLLER);

        // Initialize the UI components on the EDT
        SwingUtilities.invokeLater(this::initializeUI);
    }

    /**
     * Provides access to the Singleton instance of Rs3MainFrame.
     * Ensures only one instance is created.
     *
     * @return Singleton instance of Rs3MainFrame
     */
    public static Rs3MainFrame getInstance() {
        if (instance == null) {
            synchronized (Rs3MainFrame.class) {
                if (instance == null) {
                    instance = new Rs3MainFrame();
                    Logger.info("Rs3MainFrame instance created.");
                }
            }
        }
        return instance;
    }

    /**
     * Initializes the UI components.
     */
    private void initializeUI() {
        try {
            setTitle("Godscape - RS3 Edition");
            setSize(700, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setResizable(false);
            setLayout(new BorderLayout(2, 2));
            originalSize = getSize();

            initializeProfilePanel();
            initializeFooterPanel();
            initializeMainContentPanel();

            setLocationRelativeTo(null);
            setVisible(true);

            Logger.info("Rs3MainFrame initialized successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing UI: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Failed to initialize UI. Check logs for details.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    /**
     * Initializes the Rs3HeaderPanel and adds it to the NORTH of the layout.
     */
    private void initializeProfilePanel() {
        try {
            if (panelFactory == null) {
                Logger.warn("PanelFactory is null. Cannot initialize Rs3HeaderPanel.");
                return;
            }

            // Use the Panels enum instead of a string for panel creation
            headerPanel = (Rs3HeaderPanel) panelFactory.createPanel(Rs3Panels.RS3_HEADER_PANEL, null);
            if (headerPanel != null) {
                add(headerPanel, BorderLayout.NORTH);
                Logger.info("Rs3HeaderPanel added to Rs3MainFrame at the NORTH.");
            } else {
                Logger.warn("Rs3HeaderPanel is null. Skipping adding to UI.");
            }
        } catch (Exception e) {
            Logger.error("Error initializing Rs3HeaderPanel: {}", e.getMessage(), e);
        }
    }

    /**
     * Initializes the Rs3FooterPanel using the Manager retrieved from the DependencyFactory via enum.
     */
    private void initializeFooterPanel() {
        try {
            if (panelFactory == null) {
                Logger.warn("PanelFactory is null. Cannot initialize Rs3FooterPanel.");
                return;
            }

            // Use the Panels enum instead of a string for panel creation
            footerPanel = panelFactory.createPanel(Rs3Panels.RS3_FOOTER_PANEL, null);
            if (footerPanel != null) {
                add(footerPanel, BorderLayout.SOUTH);
                Logger.info("Rs3FooterPanel added to Rs3MainFrame.");
            } else {
                Logger.warn("Rs3FooterPanel is null. Skipping adding to UI.");
            }
        } catch (Exception e) {
            Logger.error("Error initializing Rs3FooterPanel: {}", e.getMessage(), e);
        }
    }

    /**
     * Initializes the main content panel and adds it to the CENTER of the layout.
     */
    private void initializeMainContentPanel() {
        mainContentPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();
        mainContentPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    /**
     * Sets the visibility of the main UI panel.
     *
     * @param isVisible Whether the main UI should be visible.
     */
    public void setMainUIVisible(boolean isVisible) {
        SwingUtilities.invokeLater(() -> {
            tabbedPane.setVisible(isVisible);
            if (footerPanel != null) {
                footerPanel.setVisible(isVisible);
            }
            revalidate();
            repaint();
        });
    }

    /**
     * Sets the content panel dynamically in the main frame, retaining profile and footer panels.
     *
     * @param panel The panel to set (either JPanel or JTabbedPane).
     */
    public void setContentPanel(Component panel) {
        mainContentPanel.removeAll();
        mainContentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Closes the Rs3MainFrame when the script ends.
     */
    public static void closeUI() {
        if (instance != null) {
            SwingUtilities.invokeLater(() -> {
                instance.dispose();
                Logger.info("Rs3MainFrame UI closed.");
                instance = null;
            });
        }
    }

    /**
     * Ensures only one instance is created and no new instance is created if the window is closed.
     */
    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
