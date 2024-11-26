package com.godscape.rs3.database;

import com.godscape.system.enums.Themes;
import com.godscape.system.modules.database.queries.BaseThemeDAO;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.utility.Logger;

import java.util.List;

/**
 * Data Access Object for RS3-specific Themes.
 */
public class Rs3ThemeDAO {

    private final BaseThemeDAO baseThemeDAO;

    public Rs3ThemeDAO() {
        this.baseThemeDAO = BaseThemeDAO.getInstance();
    }

    /**
     * Activates an RS3-specific theme by its enum value.
     *
     * @param theme The RS3 theme to activate.
     * @return True if successful, else false.
     */
    public boolean activateRs3Theme(Themes theme) {
        if (theme == null) {
            Logger.warn("Rs3ThemeDAO: Provided theme is null.");
            return false;
        }
        return baseThemeDAO.setActiveTheme(theme.name() + "_RS3"); // Append to differentiate
    }

    /**
     * Retrieves the currently active RS3 theme.
     *
     * @return The active ThemeSchema object, or null if none is active.
     */
    public ThemeSchema getActiveRs3Theme() {
        return baseThemeDAO.fetchActiveTheme();
    }

    /**
     * Retrieves all available RS3 themes.
     *
     * @return A list of ThemeSchema objects.
     */
    public List<ThemeSchema> getAllRs3Themes() {
        return baseThemeDAO.loadAllThemes();
    }

    // Add more RS3-specific methods if needed
}
