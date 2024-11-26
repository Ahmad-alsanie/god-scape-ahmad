package com.godscape.system.modules.database;

import com.godscape.system.utility.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class TriggerManager {

    private static final String PROFILE_TABLE = "profiles";
    private static final String CRITICAL_FIELD = "profileName"; // Configurable critical field

    /**
     * Ensures the necessary triggers are created in the database.
     */
    public static void ensureTriggers() {
        if (!doesTriggerExist("prevent_empty_profiles_insert")) {
            createPreventEmptyFieldTrigger("insert", PROFILE_TABLE, CRITICAL_FIELD);
        }
        if (!doesTriggerExist("prevent_empty_profiles_update")) {
            createPreventEmptyFieldTrigger("update", PROFILE_TABLE, CRITICAL_FIELD);
        }
    }

    /**
     * Creates a trigger to prevent empty critical fields during specified operations.
     *
     * @param operation The operation to trigger on ("insert" or "update").
     * @param tableName The name of the table.
     * @param fieldName The critical field to check.
     */
    private static boolean createPreventEmptyFieldTrigger(String operation, String tableName, String fieldName) {
        String triggerName = String.format("prevent_empty_%s_%s", tableName, operation);
        String triggerSQL = String.format(
                "CREATE TRIGGER IF NOT EXISTS %s BEFORE %s ON %s " +
                        "FOR EACH ROW " +
                        "WHEN NEW.%s IS NULL OR NEW.%s = '' " +
                        "BEGIN " +
                        "   SELECT RAISE(ABORT, 'Cannot %s empty %s'); " +
                        "END;",
                triggerName, operation.toUpperCase(), tableName, fieldName, fieldName, operation, fieldName
        );
        return executeTrigger(triggerSQL, triggerName);
    }

    /**
     * Executes the provided SQL statement to create or update a trigger.
     *
     * @param triggerSQL The SQL statement to execute.
     * @param triggerName The name of the trigger.
     * @return true if the trigger executed successfully, false otherwise.
     */
    private static boolean executeTrigger(String triggerSQL, String triggerName) {
        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(triggerSQL);
            Logger.info("TriggerManager: Trigger '{}' executed successfully.", triggerName);
            return true;

        } catch (SQLException e) {
            Logger.error("TriggerManager: Error executing trigger '{}': {}", triggerName, e.getMessage());
            return false;
        }
    }

    /**
     * Checks if a specified trigger exists in the database.
     *
     * @param triggerName The name of the trigger to check.
     * @return true if the trigger exists, false otherwise.
     */
    public static boolean doesTriggerExist(String triggerName) {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet rs = metaData.getTables(null, null, triggerName, new String[]{"TRIGGER"})) {
                boolean exists = rs.next();
                Logger.debug("TriggerManager: Checked existence for trigger '{}': {}", triggerName, exists);
                return exists;
            }
        } catch (SQLException e) {
            Logger.error("TriggerManager: Error checking if trigger '{}' exists - {}", triggerName, e.getMessage());
            return false;
        }
    }
}
