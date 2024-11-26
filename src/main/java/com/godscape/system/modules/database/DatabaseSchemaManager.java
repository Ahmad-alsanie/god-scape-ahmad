package com.godscape.system.modules.database;

import com.godscape.system.utility.Logger;
import com.godscape.system.annotations.AnnotationHelper;
import com.godscape.osrs.schemas.OsrsProfileSchema;

import java.sql.*;
import java.util.*;

public class DatabaseSchemaManager {

    private static final Set<String> EXCLUDED_FIELDS = new HashSet<>(Arrays.asList("statsettings_skillgoals", "statsettings_levelingtweaks", "panels"));

    /**
     * Initializes or updates the specified table in the database based on the provided field types.
     *
     * @param conn       the database connection
     * @param fieldTypes map of field names to SQL types
     * @throws SQLException if a database access error occurs
     */
    public static void initializeOrUpdateTable(Connection conn, Map<String, String> fieldTypes) throws SQLException {
        String tableName = "profiles";

        try {
            if (!tableExists(conn, tableName)) {
                createTable(conn, tableName, fieldTypes);
            } else {
                alterTable(conn, tableName, fieldTypes);
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            Logger.error("DatabaseSchemaManager: Error during schema initialization or update for table '{}': {}", tableName, e.getMessage());
            throw e;
        }
    }

    /**
     * Checks if the specified table exists in the database.
     *
     * @param conn      the database connection
     * @param tableName the name of the table to check
     * @return true if the table exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet res = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return res.next();
        }
    }

    /**
     * Creates a new table in the database based on the provided field types.
     *
     * @param conn       the database connection
     * @param tableName  the name of the table to create
     * @param fieldTypes map of field names to SQL types
     * @throws SQLException if a database access error occurs
     */
    private static void createTable(Connection conn, String tableName, Map<String, String> fieldTypes) throws SQLException {
        String createTableSQL = generateCreateTableSQL(tableName, fieldTypes);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            Logger.info("DatabaseSchemaManager: Created table '{}' with constraints.", tableName);
        }
    }

    /**
     * Alters an existing table to add missing columns based on the provided field types.
     *
     * @param conn       the database connection
     * @param tableName  the name of the table to alter
     * @param fieldTypes map of field names to SQL types
     * @throws SQLException if a database access error occurs
     */
    private static void alterTable(Connection conn, String tableName, Map<String, String> fieldTypes) throws SQLException {
        List<String> alterTableSQLs = generateAlterTableSQL(conn, tableName, fieldTypes);
        try (Statement stmt = conn.createStatement()) {
            for (String sql : alterTableSQLs) {
                stmt.execute(sql);
                Logger.info("DatabaseSchemaManager: Executed ALTER TABLE statement: {}", sql);
            }
        }
    }

    /**
     * Generates the SQL statement to create a table with the specified fields.
     *
     * @param tableName  the name of the table to create
     * @param fieldTypes map of field names to SQL types
     * @return the SQL statement to create the table
     */
    private static String generateCreateTableSQL(String tableName, Map<String, String> fieldTypes) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

        fieldTypes.forEach((field, type) -> {
            String columnName = AnnotationHelper.getSqlColumnName(OsrsProfileSchema.class, field);
            sql.append(columnName).append(" ").append(type);

            if ("profile_id".equals(columnName)) {
                sql.append(" PRIMARY KEY");
            }
            sql.append(" NOT NULL, ");
        });

        sql.setLength(sql.length() - 2); // Remove trailing comma and space
        sql.append(");");

        return sql.toString();
    }

    /**
     * Generates a list of SQL statements to alter a table by adding missing columns.
     *
     * @param conn       the database connection
     * @param tableName  the name of the table to alter
     * @param fieldTypes map of field names to SQL types
     * @return a list of SQL statements to alter the table
     * @throws SQLException if a database access error occurs
     */
    private static List<String> generateAlterTableSQL(Connection conn, String tableName, Map<String, String> fieldTypes) throws SQLException {
        List<String> sqlStatements = new ArrayList<>();
        Set<String> existingColumns = getExistingColumns(conn, tableName);

        for (Map.Entry<String, String> field : fieldTypes.entrySet()) {
            String columnName = AnnotationHelper.getSqlColumnName(OsrsProfileSchema.class, field.getKey());
            if (!existingColumns.contains(columnName) && !EXCLUDED_FIELDS.contains(columnName)) {
                sqlStatements.add(String.format("ALTER TABLE %s ADD COLUMN %s %s NOT NULL;", tableName, columnName, field.getValue()));
            }
        }
        return sqlStatements;
    }

    /**
     * Retrieves the existing columns in the specified table.
     *
     * @param conn      the database connection
     * @param tableName the name of the table
     * @return a set of column names in the specified table
     * @throws SQLException if a database access error occurs
     */
    private static Set<String> getExistingColumns(Connection conn, String tableName) throws SQLException {
        Set<String> columns = new HashSet<>();
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME"));
            }
        }
        return columns;
    }
}
