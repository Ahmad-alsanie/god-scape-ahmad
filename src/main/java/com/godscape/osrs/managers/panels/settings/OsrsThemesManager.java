package com.godscape.osrs.managers.panels.settings;

import com.godscape.osrs.frames.panels.settings.OsrsSettingsThemesPanel;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Themes;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.utility.Logger;
import com.godscape.system.factories.ThemeFactory;
import com.godscape.system.cache.ThemeCache;

@Singleton
public class OsrsThemesManager {

    private final ThemeCache themeCache;
    private final ThemeFactory themeFactory;

    public OsrsThemesManager() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.themeCache = dependencyFactory.getInjection(ThemeCache.class);
        this.themeFactory = dependencyFactory.getInjection(ThemeFactory.class);
        Logger.info("OsrsThemesManager: Initialized with required dependencies.");
    }

    public ThemeSchema fetchThemeSchema(Themes theme) {
        try {
            ThemeSchema fetchedTheme = themeCache.getThemeByName(theme.name());
            if (fetchedTheme != null) {
                Logger.info("OsrsThemesManager: Successfully fetched theme schema for '{}'.", theme.name());
            } else {
                Logger.warn("OsrsThemesManager: Theme schema for '{}' not found in cache.", theme.name());
            }
            return fetchedTheme;
        } catch (Exception e) {
            Logger.error("OsrsThemesManager: Exception while fetching theme schema for '{}'.", theme.name(), e);
            return null;
        }
    }

    public boolean updateOptionsForAllThemes(String intensity, boolean smoothCorners, boolean smoothButtons,
                                             boolean animations, boolean transparency, boolean highlights, boolean shadows) {
        try {
            themeCache.clearCache();
            for (ThemeSchema theme : themeCache.getAllThemes()) {
                theme.setIntensity(intensity);
                theme.setSmoothCorners(smoothCorners);
                theme.setSmoothButtons(smoothButtons);
                theme.setAnimations(animations);
                theme.setTransparency(transparency);
                theme.setHighlights(highlights);
                theme.setShadows(shadows);
                themeCache.updateThemeInCache(Themes.valueOf(theme.getThemeName().toUpperCase()), theme);
            }
            Logger.info("OsrsThemesManager: Options updated for all themes successfully in the cache and database.");
            return true;
        } catch (Exception e) {
            Logger.error("OsrsThemesManager: Failed to update options for all themes.", e);
            return false;
        }
    }

    public boolean setActiveTheme(ThemeSchema themeSchema) {
        if (themeSchema == null || themeSchema.getSelectedTheme() == null) {
            Logger.error("OsrsThemesManager: Cannot set active theme. The theme schema or selected theme is null.");
            return false;
        }

        Themes selectedTheme = themeSchema.getSelectedTheme();
        ThemeSchema currentActiveTheme = fetchCurrentThemeConfig();
        if (currentActiveTheme != null && selectedTheme == currentActiveTheme.getSelectedTheme()) {
            Logger.info("OsrsThemesManager: The selected theme '{}' is already active.", selectedTheme.name());
            notifyPanelForThemeUpdate();
            return true;
        }

        try {
            themeCache.setActiveTheme(selectedTheme);
            Logger.info("OsrsThemesManager: Set active theme to '{}' in the cache and database.", selectedTheme.name());

            themeFactory.setCurrentTheme(selectedTheme, true);
            Logger.info("OsrsThemesManager: ThemeFactory updated with the new current theme '{}'.", selectedTheme.name());

            notifyPanelForThemeUpdate();
            return true;
        } catch (Exception e) {
            Logger.error("OsrsThemesManager: Failed to set active theme '{}'.", selectedTheme.name(), e);
            return false;
        }
    }

    public ThemeSchema fetchCurrentThemeConfig() {
        try {
            ThemeSchema themeSchema = themeCache.getActiveTheme();
            if (themeSchema != null) {
                Logger.info("OsrsThemesManager: Retrieved active theme '{}' from cache.", themeSchema.getSelectedTheme().name());
                return themeSchema;
            }
            Logger.warn("OsrsThemesManager: No active theme found in the cache.");
            return null;
        } catch (Exception e) {
            Logger.error("OsrsThemesManager: Failed to fetch current theme configuration.", e);
            return null;
        }
    }

    public boolean saveThemeConfig(ThemeSchema themeSchema) {
        if (themeSchema == null || themeSchema.getSelectedTheme() == null) {
            Logger.error("OsrsThemesManager: Cannot save theme configuration. The theme schema or selected theme is null.");
            return false;
        }

        try {
            themeCache.updateThemeInCache(themeSchema.getSelectedTheme(), themeSchema);
            Logger.info("OsrsThemesManager: Cache and database updated with theme configuration for '{}'.", themeSchema.getSelectedTheme().name());
            return true;
        } catch (Exception e) {
            Logger.error("OsrsThemesManager: Failed to save theme configuration for '{}'.", themeSchema.getSelectedTheme().name(), e);
            return false;
        }
    }

    private void notifyPanelForThemeUpdate() {
        DependencyFactory.getInstance().getInjection(OsrsSettingsThemesPanel.class).updatePanelOpacity();
    }
}
