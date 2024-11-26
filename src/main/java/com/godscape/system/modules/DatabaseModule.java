package com.godscape.system.modules;

import com.godscape.osrs.database.OsrsProfileDAO;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.database.Rs3ProfileDAO;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.modules.database.DatabaseConnectionManager;
import com.godscape.system.modules.database.queries.BaseThemeDAO;
import com.godscape.system.modules.database.TriggerManager;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.utility.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Singleton
public class DatabaseModule {

    private static volatile DatabaseModule instance;
    private final OsrsProfileDAO osrsProfileDAO;
    private final Rs3ProfileDAO rs3ProfileDAO;
    private final BaseThemeDAO baseThemeDAO;

    public DatabaseModule() {
        this.osrsProfileDAO = new OsrsProfileDAO();
        this.rs3ProfileDAO = new Rs3ProfileDAO();
        this.baseThemeDAO = BaseThemeDAO.getInstance();
    }

    public void initializeDatabase() {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            Logger.info("DatabaseModule: Initializing database schema and triggers.");

            osrsProfileDAO.initializeSchemaInDatabase();
            rs3ProfileDAO.initializeSchemaInDatabase();
            baseThemeDAO.initializeDatabase();
            TriggerManager.ensureTriggers();

            Logger.info("DatabaseModule: Database schema and triggers initialized successfully.");
        } catch (SQLException e) {
            Logger.error("DatabaseModule: Failed to initialize database - {}", e.getMessage());
        }
    }

    public void updateOsrsProfiles(List<OsrsProfileSchema> profiles) {
        if (profiles == null || profiles.isEmpty()) {
            Logger.warn("DatabaseModule: No OSRS profiles provided to update.");
            return;
        }
        osrsProfileDAO.updateOsrsProfiles(profiles);
        Logger.info("DatabaseModule: Updated {} OSRS profiles in the database.", profiles.size());
    }

    public List<OsrsProfileSchema> getAllOsrsProfiles() {
        return osrsProfileDAO.loadAllOsrsProfiles();
    }

    public OsrsProfileSchema getOsrsProfileById(UUID profileId) {
        return osrsProfileDAO.getProfileById(profileId, OsrsProfileSchema.class);
    }

    public void deleteOsrsProfile(UUID profileId) {
        osrsProfileDAO.deleteOsrsProfile(profileId);
        Logger.info("DatabaseModule: Deleted OSRS profile with ID '{}'.", profileId);
    }

    public boolean osrsProfileExists(UUID profileId) {
        return osrsProfileDAO.osrsProfileExists(profileId);
    }

    public void updateRs3Profiles(List<Rs3ProfileSchema> profiles) {
        if (profiles == null || profiles.isEmpty()) {
            Logger.warn("DatabaseModule: No RS3 profiles provided to update.");
            return;
        }
        rs3ProfileDAO.updateRs3Profiles(profiles);
        Logger.info("DatabaseModule: Updated {} RS3 profiles in the database.", profiles.size());
    }

    public List<Rs3ProfileSchema> getAllRs3Profiles() {
        return rs3ProfileDAO.loadAllRs3Profiles();
    }

    public Rs3ProfileSchema getRs3ProfileById(UUID profileId) {
        return rs3ProfileDAO.getProfileById(profileId, Rs3ProfileSchema.class);
    }

    public void deleteRs3Profile(UUID profileId) {
        rs3ProfileDAO.deleteRs3Profile(profileId);
        Logger.info("DatabaseModule: Deleted RS3 profile with ID '{}'.", profileId);
    }

    public boolean rs3ProfileExists(UUID profileId) {
        return rs3ProfileDAO.rs3ProfileExists(profileId);
    }

    public List<ThemeSchema> getAllThemes() {
        return baseThemeDAO.loadAllThemes();
    }

    public boolean setActiveTheme(String themeName) {
        return baseThemeDAO.setActiveTheme(themeName);
    }

    public boolean saveThemeConfig(ThemeSchema themeSchema) {
        if (themeSchema == null || themeSchema.getThemeName() == null) {
            Logger.error("DatabaseModule: Cannot save theme configuration. ThemeSchema or theme name is null.");
            return false;
        }
        try {
            boolean saved = baseThemeDAO.updateTheme(themeSchema);
            if (saved) {
                Logger.info("DatabaseModule: Theme configuration for '{}' saved to the database.", themeSchema.getThemeName());
            } else {
                Logger.error("DatabaseModule: Failed to save theme configuration for '{}'.", themeSchema.getThemeName());
            }
            return saved;
        } catch (Exception e) {
            Logger.error("DatabaseModule: Exception while saving theme configuration for '{}'.", themeSchema.getThemeName(), e);
            return false;
        }
    }

    public boolean dropTable(String tableName) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement stmt = connection.createStatement()) {
            String sql = "DROP TABLE IF EXISTS " + tableName;
            stmt.executeUpdate(sql);
            Logger.info("DatabaseModule: Dropped table '{}'", tableName);
            return true;
        } catch (SQLException e) {
            Logger.error("DatabaseModule: Failed to drop table '{}' - {}", tableName, e.getMessage());
            return false;
        }
    }

    public boolean testConnection() {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            Logger.info("DatabaseModule: Database connection test successful.");
            return true;
        } catch (SQLException e) {
            Logger.error("DatabaseModule: Database connection test failed - {}", e.getMessage());
            return false;
        }
    }
}
