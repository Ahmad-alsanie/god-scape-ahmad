package com.godscape.system.utility;

import com.godscape.osrs.managers.panels.OsrsFooterManager;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.system.factories.PanelFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * A JPanel that lazily initializes its content when shown.
 */
public class LazyTabPanel extends JPanel {
    private final PanelFactory panelFactory;
    private final OsrsPanels panelEnum;
    private final OsrsFooterManager footerManager;
    private boolean initialized = false;

    public LazyTabPanel(PanelFactory panelFactory, OsrsPanels panelEnum, OsrsFooterManager footerManager) {
        this.panelFactory = panelFactory;
        this.panelEnum = panelEnum;
        this.footerManager = footerManager;

        // Set transparent background if necessary
        setOpaque(false);

        // Add a component listener to detect when the panel becomes visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                Logger.info("LazyTabPanel '{}' became visible. Initializing if not already done.", panelEnum.getTabTitle());
                initialize();
            }
        });

        setLayout(new BorderLayout());
    }

    private void initialize() {
        if (!initialized) {
            SwingUtilities.invokeLater(() -> {
                try {
                    Logger.info("Initializing content for '{}'", panelEnum.getTabTitle());

                    JPanel panel = panelFactory.createPanel(panelEnum, footerManager);
                    if (panel != null) {
                        panel.setOpaque(false); // Make the loaded panel transparent if required
                        this.add(panel, BorderLayout.CENTER);
                        Logger.info("Successfully initialized tab: '{}'", panelEnum.getTabTitle());
                    } else {
                        Logger.warn("Panel creation failed for '{}'. Displaying error panel.", panelEnum.getTabTitle());
                        this.add(createErrorPanel(panelEnum.getTabTitle()), BorderLayout.CENTER);
                    }

                    this.revalidate();
                    this.repaint();
                    initialized = true;
                } catch (Exception ex) {
                    Logger.error("Exception during initialization of '{}': {}", panelEnum.getTabTitle(), ex.getMessage());
                    this.removeAll();
                    this.add(createErrorPanel(panelEnum.getTabTitle()), BorderLayout.CENTER);
                    this.revalidate();
                    this.repaint();
                }
            });
        } else {
            Logger.debug("Tab '{}' already initialized. Skipping re-initialization.", panelEnum.getTabTitle());
        }
    }

    /**
     * Creates an error panel that shows a message when a tab fails to load.
     *
     * @param tabTitle The title of the tab that failed to load.
     * @return A panel containing the error message.
     */
    private JPanel createErrorPanel(String tabTitle) {
        JPanel errorPanel = new JPanel();
        errorPanel.setOpaque(true);
        errorPanel.setBackground(new Color(255, 100, 100, 150)); // Semi-transparent red for visibility
        JLabel errorMessage = new JLabel("Failed to load the '" + tabTitle + "' tab.");
        errorMessage.setForeground(Color.WHITE);
        errorPanel.add(errorMessage);
        Logger.error("Created error panel for tab: '{}'", tabTitle);
        return errorPanel;
    }
}
