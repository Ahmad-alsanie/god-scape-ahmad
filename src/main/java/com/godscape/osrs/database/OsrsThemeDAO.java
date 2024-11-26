package com.godscape.osrs.database;

import com.godscape.system.enums.Themes;
import com.godscape.system.modules.database.queries.BaseThemeDAO;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.utility.Logger;

import java.util.List;

/**
 * Data Access Object for OSRS-specific Themes.
 */
public class OsrsThemeDAO {

    private final BaseThemeDAO baseThemeDAO;

    public OsrsThemeDAO() {
        this.baseThemeDAO = BaseThemeDAO.getInstance();
    }

    /**
     * Activates an OSRS-specific theme by its enum value.
     *
     * @param theme The OSRS theme to activate.
     * @return True if successful, else false.
     */
    public boolean activateOsrsTheme(Themes theme) {
        if (theme == null) {
            Logger.warn("OsrsThemeDAO: Provided theme is null.");
            return false;
        }
        return baseThemeDAO.setActiveTheme(theme.name() + "_OSRS"); // Example: Append to differentiate
    }

    /**
     * Retrieves the currently active OSRS theme.
     *
     * @return The active ThemeSchema object, or null if none is active.
     */
    public ThemeSchema getActiveOsrsTheme() {
        return baseThemeDAO.fetchActiveTheme();
    }

    /**
     * Retrieves all available OSRS themes.
     *
     * @return A list of ThemeSchema objects.
     */
    public List<ThemeSchema> getAllOsrsThemes() {
        return baseThemeDAO.loadAllThemes();
    }

    // Add more OSRS-specific methods if needed
}
