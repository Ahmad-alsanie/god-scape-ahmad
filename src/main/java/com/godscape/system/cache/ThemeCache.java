package com.godscape.system.cache;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.enums.Themes;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.modules.DatabaseModule;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.utility.Logger;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class ThemeCache {

    private final IMap<String, ThemeSchema> themeCacheMap;
    private final DatabaseModule databaseModule;

    public ThemeCache(HazelcastConfig hazelcastConfig, DatabaseModule databaseModule) {
        HazelcastInstance hazelcastInstance = hazelcastConfig.getHazelcastInstance();
        themeCacheMap = hazelcastInstance.getMap("themeCache");
        this.databaseModule = databaseModule;
        Logger.info("ThemeCache: Initialized and ready to cache themes.");
    }

    /**
     * Initializes and caches all themes from the Themes enum, adding missing themes to both the cache and database.
     */
    public void initializeCache() {
        Logger.info("ThemeCache: Starting cache initialization.");
        List<ThemeSchema> existingThemes = databaseModule.getAllThemes();
        Map<String, ThemeSchema> existingThemesMap = existingThemes.stream()
                .collect(Collectors.toMap(ThemeSchema::getThemeName, theme -> theme));

        themeCacheMap.clear(); // Clear existing cache

        for (Themes theme : Themes.values()) {
            String themeName = theme.name();
            ThemeSchema themeSchema = existingThemesMap.getOrDefault(themeName, createDefaultThemeSchema(theme));
            themeCacheMap.put(themeName, themeSchema);
            Logger.info("ThemeCache: Cached theme '{}'.", themeName);
            if (!existingThemesMap.containsKey(themeName)) {
                databaseModule.saveThemeConfig(themeSchema); // Persist newly added theme to the database
                Logger.info("ThemeCache: Added missing theme '{}' to database.", themeName);
            }
        }
        Logger.info("ThemeCache: Total themes cached: {}", themeCacheMap.size());
    }

    /**
     * Adds or updates a theme in the cache and database.
     */
    public void updateThemeInCache(Themes themeEnum, ThemeSchema themeSchema) {
        if (themeEnum == null || themeSchema == null) {
            Logger.warn("ThemeCache: Cannot update theme. Invalid themeEnum or themeSchema.");
            return;
        }
        themeCacheMap.put(themeEnum.name(), themeSchema);
        databaseModule.saveThemeConfig(themeSchema);
        Logger.info("ThemeCache: Updated theme '{}' in cache and database.", themeEnum.name());
    }

    /**
     * Creates a ThemeSchema with default values for a given theme.
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
     * Sets a theme as active and updates the cache and database accordingly.
     */
    public void setActiveTheme(Themes activeTheme) {
        if (activeTheme == null) {
            Logger.warn("ThemeCache: Cannot set active theme. Theme is null.");
            return;
        }

        for (Map.Entry<String, ThemeSchema> entry : themeCacheMap.entrySet()) {
            ThemeSchema theme = entry.getValue();
            boolean isActive = entry.getKey().equals(activeTheme.name());
            theme.setActive(isActive);
            themeCacheMap.put(entry.getKey(), theme);
            if (isActive) {
                databaseModule.setActiveTheme(activeTheme.name());
            }
            Logger.info("ThemeCache: Theme '{}' set as {}active in cache and database.", theme.getThemeName(), isActive ? "" : "in");
        }
    }

    /**
     * Retrieves the currently active theme from the cache.
     */
    public ThemeSchema getActiveTheme() {
        for (ThemeSchema theme : themeCacheMap.values()) {
            if (theme.isActive()) {
                Logger.info("ThemeCache: Active theme retrieved: '{}'.", theme.getThemeName());
                return theme;
            }
        }
        Logger.warn("ThemeCache: No active theme found in the cache.");
        return null;
    }

    /**
     * Clears all theme cache.
     */
    public void clearCache() {
        themeCacheMap.clear();
        Logger.info("ThemeCache: All theme cache cleared.");
    }

    /**
     * Retrieves a specific theme by name from the cache.
     */
    public ThemeSchema getThemeByName(String themeName) {
        ThemeSchema theme = themeCacheMap.get(themeName);
        if (theme != null) {
            Logger.info("ThemeCache: Theme '{}' retrieved from cache.", themeName);
        } else {
            Logger.warn("ThemeCache: Theme '{}' not found in cache.", themeName);
        }
        return theme;
    }

    /**
     * Retrieves all themes from the cache.
     */
    public Collection<ThemeSchema> getAllThemes() {
        return themeCacheMap.values();
    }
}
