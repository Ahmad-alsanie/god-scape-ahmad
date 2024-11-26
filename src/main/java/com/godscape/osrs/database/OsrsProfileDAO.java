package com.godscape.osrs.database;

import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.interfaces.mark.Schemable;
import com.godscape.system.modules.database.DatabaseConnectionManager;
import com.godscape.system.modules.database.queries.BaseProfileDAO;
import com.godscape.system.utility.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class OsrsProfileDAO extends BaseProfileDAO<OsrsProfileSchema, OsrsSchemas> {

    public OsrsProfileDAO() {
        super(() -> OsrsSchemas.OSRS_PROFILE_SCHEMA, OsrsProfileSchema::getColumnDefinitions);
    }

    public void initializeSchemaInDatabase() {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            initializeSchema(connection);
            Logger.info("OsrsProfileDAO: OSRS profile schema initialized successfully.");
        } catch (SQLException e) {
            Logger.error("OsrsProfileDAO: Failed to initialize OSRS profile schema - {}", e.getMessage());
        }
    }

    public void updateOsrsProfiles(List<OsrsProfileSchema> profiles) {
        updateProfilesInDatabase(profiles);
    }

    public List<OsrsProfileSchema> loadAllOsrsProfiles() {
        return loadAllProfilesFromDatabase(OsrsProfileSchema.class);
    }

    public OsrsProfileSchema getOsrsProfileById(UUID profileId) {
        return getProfileById(profileId, OsrsProfileSchema.class);
    }

    public void deleteOsrsProfile(UUID profileId) {
        deleteProfileFromDatabase(profileId);
    }

    public boolean osrsProfileExists(UUID profileId) {
        return profileExists(profileId);
    }
}
