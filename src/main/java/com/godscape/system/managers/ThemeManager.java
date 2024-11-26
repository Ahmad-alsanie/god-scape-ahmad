package com.godscape.system.managers;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.enums.Schemas;
import com.godscape.system.enums.Themes;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * ThemeManager is responsible for handling theme settings and creating theme panels.
 */
public class ThemeManager {
    private static volatile ThemeManager instance;
    private final Map<Themes, JPanel> themeCache;

    private ThemeManager() {
        this.themeCache = new EnumMap<>(Themes.class);
        SwingUtilities.invokeLater(this::initializeThemes); // Asynchronously initialize themes
    }

    /**
     * Returns the singleton instance of ThemeManager.
     *
     * @return The singleton instance.
     */
    public static ThemeManager getInstance() {
        if (instance == null) {
            synchronized (ThemeManager.class) {
                if (instance == null) {
                    instance = new ThemeManager();
                }
            }
        }
        return instance;
    }

    /**
     * Provides access to the theme cache.
     *
     * @return The map containing all cached theme panels.
     */
    public Map<Themes, JPanel> getThemeCache() {
        return themeCache;
    }

    /**
     * Loads theme settings from the specified profile.
     *
     * @param profile The profile from which to load settings.
     */
    public void loadSettings(OsrsProfileSchema profile) {
        Logger.info("Loading theme settings from profile: {}", profile.getProfileName());
        Themes activeTheme = profile.getSetting(Schemas.THEME_SCHEMA.name(), "activeTheme", Themes.LIGHT);
        SwingUtilities.invokeLater(() -> {
            Themes.setCurrentTheme(activeTheme);
            Logger.info("Applied theme '{}' from profile settings.", activeTheme.name());
        });
    }

    /**
     * Saves the currently active theme to the specified profile.
     *
     * @param profile The profile into which to save settings.
     */
    public void saveSettings(OsrsProfileSchema profile) {
        Logger.info("Saving theme settings to profile: {}", profile.getProfileName());
        Themes activeTheme = Themes.getCurrentTheme();
        profile.setSetting(Schemas.THEME_SCHEMA.name(), "activeTheme", activeTheme);
        Logger.info("Saved current theme '{}' to profile settings.", activeTheme.name());
    }

    /**
     * Initializes the theme cache by loading all themes from the Themes enum asynchronously.
     */
    public void initializeThemes() {
        for (Themes theme : Themes.values()) {
            JPanel themePanel = createThemePanel(theme);
            if (themePanel != null) {
                themeCache.put(theme, themePanel);
                Logger.info("Initialized theme panel for '{}'.", theme.name());
            } else {
                Logger.warn("Failed to initialize panel for theme '{}'.", theme.name());
            }
        }
        if (themeCache.isEmpty()) {
            Logger.error("ThemeManager: No themes loaded. Using fallback theme.");
            themeCache.put(Themes.LIGHT, createFallbackTheme());
        }
    }

    private JPanel createThemePanel(Themes theme) {
        try {
            return theme.createPanel();
        } catch (Exception e) {
            Logger.error("Exception while creating panel for theme '{}': {}", theme.name(), e.getMessage());
            return null;
        }
    }

    public JPanel getActiveThemePanel() {
        return themeCache.getOrDefault(Themes.getCurrentTheme(), createFallbackTheme());
    }

    private JPanel createFallbackTheme() {
        Logger.info("Using fallback theme.");
        return Themes.LIGHT.createPanel();
    }
}
