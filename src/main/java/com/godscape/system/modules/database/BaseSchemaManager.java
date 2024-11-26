package com.godscape.system.modules.database;

import com.godscape.system.utility.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseSchemaManager {

    /**
     * Checks if a specified table exists in the database.
     *
     * @param tableName Name of the table to check.
     * @return true if the table exists, false otherwise.
     */
    protected boolean tableExists(String tableName) {
        String checkTableSQL = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(checkTableSQL)) {

            statement.setString(1, tableName);
            return statement.executeQuery().next();

        } catch (SQLException e) {
            Logger.error("{}: Error checking if table '{}' exists - {}", getClass().getSimpleName(), tableName, e.getMessage());
            return false;
        }
    }

    /**
     * Abstract method to create a specific table, implemented by subclasses.
     *
     * @return true if the table was created or already exists; false otherwise
     */
    protected abstract boolean createTable();
}