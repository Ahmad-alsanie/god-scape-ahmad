package com.godscape.osrs.frames;

import com.godscape.osrs.controllers.OsrsObservationController;
import com.godscape.osrs.frames.panels.OsrsFooterPanel;
import com.godscape.osrs.frames.panels.OsrsHeaderPanel;
import com.godscape.osrs.frames.panels.OsrsMainPanel;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.controllers.SettingsController;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.ThemeFactory;
import com.godscape.system.managers.PlatformManager;
import com.godscape.system.modules.DatabaseModule;
import com.godscape.system.utility.Logger;
import com.godscape.system.enums.Themes;

import javax.swing.*;
import java.awt.*;

@Singleton
public class OsrsMainFrame extends JFrame {

    private static volatile OsrsMainFrame instance;
    private final OsrsMainPanel osrsMainPanel;
    private OsrsHeaderPanel osrsHeaderPanel;
    private OsrsFooterPanel osrsFooterPanel;
    private JPanel themePanel;
    private final SettingsController settingsController;
    private final DatabaseModule databaseModule;
    private final OsrsObservationController osrsObservationController;
    private volatile boolean isRefreshing = false;

    private OsrsMainFrame() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.settingsController = dependencyFactory.getInjection(SettingsController.class);
        this.databaseModule = dependencyFactory.getInjection(DatabaseModule.class);
        this.osrsObservationController = dependencyFactory.getInjection(OsrsObservationController.class);
        this.osrsMainPanel = dependencyFactory.getInjection(OsrsMainPanel.class);
        initializeUI();
    }

    public static OsrsMainFrame getInstance() {
        if (instance == null || !instance.isDisplayable()) {
            synchronized (OsrsMainFrame.class) {
                if (instance == null || !instance.isDisplayable()) {
                    instance = new OsrsMainFrame();
                    Logger.info("OsrsMainFrame: New instance created.");
                } else {
                    SwingUtilities.invokeLater(instance::toFront);
                }
            }
        }
        return instance;
    }

    private void initializeUI() {
        SwingUtilities.invokeLater(() -> {
            try {
                setTitle("GodScape - OSRS Edition");
                setSize(1000, 750);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setResizable(false);
                setLayout(new BorderLayout());
                setLocationRelativeTo(null);
                if (!initializeDatabaseConnection()) {
                    JOptionPane.showMessageDialog(this, "Database connection failed. Limited functionality available.", "Database Error", JOptionPane.WARNING_MESSAGE);
                }

                osrsObservationController.registerThemeListener(this::refreshTheme);
                applyTheme();
                setupPanels();
                setVisible(true);
                Logger.info("OsrsMainFrame: UI initialized successfully.");
            } catch (Exception e) {
                handleInitializationError(e);
            }
        });
    }

    private boolean initializeDatabaseConnection() {
        if (databaseModule != null && databaseModule.testConnection()) {
            Logger.info("OsrsMainFrame: Database connection established.");
            return true;
        } else {
            Logger.error("OsrsMainFrame: Database connection failed.");
            return false;
        }
    }

    private void applyTheme() {
        try {
            databaseModule.setActiveTheme("Default");
            DependencyFactory.getInstance().getInjection(ThemeFactory.class).reloadActiveThemeFromDatabase();
            Themes currentTheme = Themes.getCurrentTheme();
            themePanel = currentTheme.createPanel();
            themePanel.setLayout(new BorderLayout());
            setContentPane(themePanel);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            Logger.error("OsrsMainFrame: Failed to load theme, applying fallback.", e);
            applyFallbackTheme();
        }
    }

    private void applyFallbackTheme() {
        themePanel = new JPanel(new BorderLayout());
        themePanel.setBackground(Color.LIGHT_GRAY);
        setContentPane(themePanel);
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void setupPanels() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        osrsHeaderPanel = dependencyFactory.getInjection(OsrsHeaderPanel.class);
        osrsHeaderPanel.setOpaque(false);
        osrsHeaderPanel.setPreferredSize(new Dimension(getWidth(), 80));
        themePanel.add(osrsHeaderPanel, BorderLayout.NORTH);

        osrsMainPanel.setOpaque(false);
        themePanel.add(osrsMainPanel, BorderLayout.CENTER);

        osrsFooterPanel = new OsrsFooterPanel();
        osrsFooterPanel.setOpaque(false);
        osrsFooterPanel.setPreferredSize(new Dimension(getWidth(), 80));
        themePanel.add(osrsFooterPanel, BorderLayout.SOUTH);
    }

    private void handleInitializationError(Exception e) {
        Logger.error("OsrsMainFrame: Initialization error: {}", e.getMessage(), e);
        JOptionPane.showMessageDialog(this, "Failed to initialize UI. Check logs.", "Initialization Error", JOptionPane.ERROR_MESSAGE);
        dispose();
    }

    @Override
    public void dispose() {
        osrsObservationController.unregisterThemeListener(this::refreshTheme);
        SwingUtilities.invokeLater(() -> DependencyFactory.getInstance().getInjection(PlatformManager.class).shutdownPlatform());
        super.dispose();
        instance = null;
    }

    public static void closeUI() {
        if (instance != null && instance.isDisplayable()) {
            SwingUtilities.invokeLater(() -> {
                instance.dispose();
                instance = null;
            });
        }
    }

    public void refreshTheme(Themes newTheme) {
        if (isRefreshing) return;
        isRefreshing = true;
        SwingUtilities.invokeLater(() -> {
            try {
                getContentPane().removeAll();
                applyTheme();
                setupPanels();
                revalidate();
                repaint();
            } finally {
                isRefreshing = false;
            }
        });
    }
}
