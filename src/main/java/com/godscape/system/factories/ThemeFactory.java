package com.godscape.system.factories;

import com.godscape.system.enums.Themes;
import com.godscape.system.managers.ThemeManager;
import com.godscape.system.modules.DatabaseModule;
import com.godscape.system.observers.ThemeChangeObservation;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;

public class ThemeFactory {

    private static volatile ThemeFactory instance;
    private static final ThemeManager themeManager = DependencyFactory.getInstance().getInjection(ThemeManager.class);
    private static final DatabaseModule databaseModule = DependencyFactory.getInstance().getInjection(DatabaseModule.class);
    private static volatile ThemeChangeObservation themeChangeObservation;

    private ThemeFactory() {
        try {
            themeManager.initializeThemes();
            setActiveThemeFromDatabase();
        } catch (Exception e) {
            Logger.error("ThemeFactory: Error initializing themes - {}", e.getMessage(), e);
            Themes.setCurrentTheme(Themes.LIGHT);
            Logger.warn("ThemeFactory: Default theme '{}' applied due to initialization failure.", Themes.LIGHT.name());
        }
    }

    public static ThemeFactory getInstance() {
        if (instance == null) {
            synchronized (ThemeFactory.class) {
                if (instance == null) {
                    instance = new ThemeFactory();
                }
            }
        }
        return instance;
    }

    private static void setActiveThemeFromDatabase() {
        try {
            ThemeSchema activeTheme = databaseModule.getAllThemes().stream()
                    .filter(ThemeSchema::isActive)
                    .findFirst()
                    .orElse(null);

            if (activeTheme != null) {
                Themes.setCurrentTheme(activeTheme.getSelectedTheme());
                Logger.info("ThemeFactory: Active theme '{}' set from database.", activeTheme.getSelectedTheme().name());
            } else {
                setDefaultTheme();
            }
        } catch (Exception e) {
            Logger.error("ThemeFactory: Failed to set active theme from database - {}", e.getMessage());
            setDefaultTheme();
        }
    }

    private static void setDefaultTheme() {
        Themes.setCurrentTheme(Themes.LIGHT);
        Logger.info("ThemeFactory: Using default theme '{}'.", Themes.LIGHT.name());
    }

    public void reloadActiveThemeFromDatabase() {
        setActiveThemeFromDatabase();
        Logger.info("ThemeFactory: Active theme reloaded from database.");
    }

    public void setCurrentTheme(Themes theme, boolean notifyListener) {
        if (theme == null || !themeManager.getThemeCache().containsKey(theme)) {
            Logger.warn("ThemeFactory: Theme '{}' not found in cache. Applying fallback.", theme);
            setDefaultTheme();
            return;
        }
        if (theme.equals(Themes.getCurrentTheme())) {
            Logger.info("ThemeFactory: Current theme '{}' is already set. No change needed.", theme);
            return;
        }
        Themes.setCurrentTheme(theme);
        Logger.info("ThemeFactory: Current theme set to '{}'.", theme);
        if (notifyListener && themeChangeObservation != null) {
            themeChangeObservation.onThemeChange(theme);
        }
    }

    public Themes getCurrentTheme() {
        return Themes.getCurrentTheme();
    }

    public JPanel getTheme() {
        try {
            JPanel themedPanel = themeManager.getActiveThemePanel();
            Logger.debug("ThemeFactory: Returning theme panel for '{}'.", Themes.getCurrentTheme());
            return themedPanel;
        } catch (Exception e) {
            Logger.error("ThemeFactory: Error retrieving theme panel - {}", e.getMessage());
            return createFallbackPanel();
        }
    }

    public void setThemeChangeListener(ThemeChangeObservation listener) {
        themeChangeObservation = listener;
        Logger.info("ThemeFactory: Theme change listener set.");
    }

    public void clearThemeCache() {
        themeManager.getThemeCache().clear();
        Logger.info("ThemeFactory: Theme cache cleared.");
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        themeManager.initializeThemes();
    }

    public JPanel getThemePanel(Themes theme) {
        if (themeManager.getThemeCache().containsKey(theme)) {
            Logger.debug("ThemeFactory: Returning theme panel for '{}'.", theme.name());
            return themeManager.getThemeCache().get(theme);
        } else {
            Logger.warn("ThemeFactory: Theme '{}' not found in cache. Using fallback.", theme.name());
            return createFallbackPanel();
        }
    }

    private JPanel createFallbackPanel() {
        JPanel fallbackPanel = new JPanel();
        fallbackPanel.setBackground(Color.GRAY);
        fallbackPanel.add(new JLabel("Fallback Theme Panel"));
        Logger.info("ThemeFactory: Returning fallback theme panel.");
        return fallbackPanel;
    }
}
