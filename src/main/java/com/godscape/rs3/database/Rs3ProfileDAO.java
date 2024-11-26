package com.godscape.rs3.database;

import com.godscape.rs3.enums.core.Rs3Schemas;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.modules.database.DatabaseConnectionManager;
import com.godscape.system.modules.database.queries.BaseProfileDAO;
import com.godscape.system.utility.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class Rs3ProfileDAO extends BaseProfileDAO<Rs3ProfileSchema, Rs3Schemas> {

    public Rs3ProfileDAO() {
        super(() -> Rs3Schemas.RS3_PROFILE_SCHEMA, Rs3ProfileSchema::getColumnDefinitions);
    }

    public void initializeSchemaInDatabase() {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            initializeSchema(connection);
            Logger.info("Rs3ProfileDAO: RS3 profile schema initialized successfully.");
        } catch (SQLException e) {
            Logger.error("Rs3ProfileDAO: Failed to initialize RS3 profile schema - {}", e.getMessage());
        }
    }

    public void updateRs3Profiles(List<Rs3ProfileSchema> profiles) {
        updateProfilesInDatabase(profiles);
    }

    public List<Rs3ProfileSchema> loadAllRs3Profiles() {
        return loadAllProfilesFromDatabase(Rs3ProfileSchema.class);
    }

    public Rs3ProfileSchema getRs3ProfileById(UUID profileId) {
        return getProfileById(profileId, Rs3ProfileSchema.class);
    }

    public void deleteRs3Profile(UUID profileId) {
        deleteProfileFromDatabase(profileId);
    }

    public boolean rs3ProfileExists(UUID profileId) {
        return profileExists(profileId);
    }
}
