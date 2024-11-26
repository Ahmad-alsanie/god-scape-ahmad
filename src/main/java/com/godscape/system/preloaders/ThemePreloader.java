package com.godscape.system.preloaders;

import com.godscape.system.enums.Themes;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.modules.DatabaseModule;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.utility.Logger;
import com.godscape.system.schemas.GlobalSettingsSchema;
import com.godscape.system.config.HazelcastConfig;
import com.hazelcast.map.IMap;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ThemesPreloader is responsible for initializing the themes database,
 * creating necessary tables, and inserting all themes defined in the Themes enum
 * if they are missing. It also manages the active theme to ensure only one theme
 * is active at any given time.
 */
public class ThemePreloader {

    private static final DatabaseModule databaseModule = DependencyFactory.getInstance().getInjection(DatabaseModule.class);
    private static volatile ThemePreloader instance;

    private ThemePreloader() {
        // Private constructor to enforce singleton
    }

    public static ThemePreloader getInstance() {
        if (instance == null) {
            synchronized (ThemePreloader.class) {
                if (instance == null) {
                    instance = new ThemePreloader();
                }
            }
        }
        return instance;
    }

    /**
     * Initializes the database for theme settings using the provided global settings.
     *
     * @param settingsSchema The global settings schema containing paths and configurations.
     */
    public synchronized void initializeDatabase(GlobalSettingsSchema settingsSchema) {
        if (settingsSchema == null) {
            Logger.error("ThemesPreloader: GlobalSettingsSchema is null. Cannot initialize the database.");
            return;
        }

        try {
            DependencyFactory.getInstance().getInjection(DatabaseModule.class).initializeDatabase();
            addMissingThemes();
            ensureSingleActiveTheme();
            refreshHazelcastCache();
            Logger.info("ThemesPreloader: Themes database initialization completed successfully.");
        } catch (Exception e) {
            Logger.error("ThemesPreloader: Error during database initialization - {}", e.getMessage(), e);
        }
    }

    /**
     * Inserts missing themes from the Themes enum into the database.
     */
    private void addMissingThemes() {
        Set<String> existingThemes = databaseModule.getAllThemes().stream()
                .map(ThemeSchema::getThemeName)
                .collect(Collectors.toSet());

        for (Themes theme : Themes.values()) {
            String themeName = theme.name();
            if (!existingThemes.contains(themeName)) {
                ThemeSchema themeSchema = createDefaultThemeSchema(theme);
                databaseModule.saveThemeConfig(themeSchema); // Persist each theme in the database
                Logger.info("ThemesPreloader: Inserted missing theme '{}'. Active: {}", themeName, themeSchema.isActive());
            }
        }
    }

    /**
     * Creates a ThemeSchema with default values for each theme in the enum.
     *
     * @param theme The theme enum value.
     * @return A ThemeSchema instance with default settings.
     */
    private ThemeSchema createDefaultThemeSchema(Themes theme) {
        ThemeSchema themeSchema = new ThemeSchema();
        themeSchema.setThemeName(theme.name());
        themeSchema.setIntensity("Medium");
        themeSchema.setSmoothCorners(true);
        themeSchema.setSmoothButtons(true);
        themeSchema.setAnimations(true);
        themeSchema.setTransparency(true);
        themeSchema.setHighlights(true);
        themeSchema.setShadows(true);
        themeSchema.setActive(theme == Themes.LIGHT); // Set "LIGHT" as the default active theme
        return themeSchema;
    }

    /**
     * Ensures only one theme is active in the database. Activates the default theme if none are active.
     */
    private void ensureSingleActiveTheme() {
        List<ThemeSchema> activeThemes = databaseModule.getAllThemes().stream()
                .filter(ThemeSchema::isActive)
                .collect(Collectors.toList());

        if (activeThemes.size() > 1) {
            Logger.warn("ThemesPreloader: Multiple active themes detected. Deactivating extras.");
            activeThemes.stream().skip(1).forEach(theme -> {
                theme.setActive(false);
                databaseModule.setActiveTheme(theme.getThemeName());
            });
        } else if (activeThemes.isEmpty()) {
            Logger.info("ThemesPreloader: No active theme found. Activating default theme 'LIGHT'.");
            databaseModule.setActiveTheme("LIGHT");
        } else {
            Logger.info("ThemesPreloader: Exactly one active theme found. No changes needed.");
        }
    }

    /**
     * Refreshes the Hazelcast cache with the current themes from the database.
     */
    private void refreshHazelcastCache() {
        IMap<String, ThemeSchema> themeCache = DependencyFactory.getInstance().getInjection(HazelcastConfig.class).getHazelcastInstance().getMap("themeCache");
        themeCache.clear();

        for (ThemeSchema theme : databaseModule.getAllThemes()) {
            themeCache.put(theme.getThemeName(), theme);
        }

        Logger.info("ThemesPreloader: Hazelcast themeCache refreshed with current themes.");
    }
}
