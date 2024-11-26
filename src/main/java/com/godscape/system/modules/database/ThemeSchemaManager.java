package com.godscape.system.modules.database;

import com.godscape.system.utility.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ThemeSchemaManager extends BaseSchemaManager {

    private static final String TABLE_NAME = "themes";

    public ThemeSchemaManager() {
        ensureThemeTableExists();
    }

    /**
     * Ensures the themes table exists in the database. If not, creates it.
     */
    public void ensureThemeTableExists() {
        if (!tableExists(TABLE_NAME)) {
            createTable();
        }
    }


    @Override
    protected boolean createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + "theme_name TEXT PRIMARY KEY, "
                + "intensity TEXT, "
                + "smooth_corners BOOLEAN, "
                + "smooth_buttons BOOLEAN, "
                + "animations BOOLEAN, "
                + "transparency BOOLEAN, "
                + "highlights BOOLEAN, "
                + "shadows BOOLEAN, "
                + "is_active BOOLEAN"
                + ");";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createTableSQL);
            Logger.info("ThemeSchemaManager: '{}' table created or already exists.", TABLE_NAME);
            return true;

        } catch (SQLException e) {
            Logger.error("ThemeSchemaManager: Error creating '{}' table - {}", TABLE_NAME, e.getMessage());
            return false;
        }
    }
}





