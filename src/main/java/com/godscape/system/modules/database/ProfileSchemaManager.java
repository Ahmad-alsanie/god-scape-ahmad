package com.godscape.system.modules.database;

import com.godscape.system.utility.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
public class ProfileSchemaManager extends BaseSchemaManager {

    private static final String TABLE_NAME = "profiles";

    public ProfileSchemaManager() {
        ensureTableExists();
    }

    private void ensureTableExists() {
        if (!tableExists(TABLE_NAME)) {
            createTable();
        }
    }

    @Override
    protected boolean createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "profile_id TEXT PRIMARY KEY, "
                + "profile_name TEXT NOT NULL, "
                + "membership BOOLEAN, "
                + "mode TEXT, "
                + "playstyle TEXT, "
                + "autoprofiler BOOLEAN, "
                + "profile_notes TEXT, "
                + "last_updated INTEGER"
                + ");";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createTableSQL);
            Logger.info("ProfileSchemaManager: '{}' table created or already exists.", TABLE_NAME);
            return true;

        } catch (SQLException e) {
            Logger.error("ProfileSchemaManager: Error creating '{}' table - {}", TABLE_NAME, e.getMessage());
            return false;
        }
    }
}