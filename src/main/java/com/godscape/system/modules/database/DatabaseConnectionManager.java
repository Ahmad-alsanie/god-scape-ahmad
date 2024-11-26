package com.godscape.system.modules.database;

import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.enums.ConfigKeys;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Path;

public class DatabaseConnectionManager {
    private static String DB_URL;

    static {
        initializeDatabaseUrl();
    }

    /**
     * Initializes the database URL based on the global settings and current game version.
     */
    private static void initializeDatabaseUrl() {
        if (DB_URL == null) {
            GameVersion currentGameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();
            if (currentGameVersion == null) {
                Logger.error("DatabaseConnectionManager: Game version not detected. Cannot initialize the database URL.");
                return;
            }

            DependencyFactory dependencyFactory = DependencyFactory.getInstance();
            GodscapeConfig config = dependencyFactory.getInjection(GodscapeConfig.class);

            String databaseFilename;
            if (currentGameVersion == GameVersion.OSRS) {
                databaseFilename = config.getString(ConfigKeys.OSRS_DATABASE_FILENAME);
            } else if (currentGameVersion == GameVersion.RS3) {
                databaseFilename = config.getString(ConfigKeys.RS3_DATABASE_FILENAME);
            } else {
                Logger.error("DatabaseConnectionManager: Unsupported game version.");
                return;
            }

            DB_URL = "jdbc:sqlite:" + databaseFilename;

            try {
                Class.forName("org.sqlite.JDBC");
                Logger.info("DatabaseConnectionManager: SQLite JDBC Driver loaded successfully.");
                Logger.info("DatabaseConnectionManager: Database URL set to {}", DB_URL);
            } catch (ClassNotFoundException e) {
                Logger.error("DatabaseConnectionManager: SQLite JDBC Driver not found.", e);
            }
        }
    }

    /**
     * Obtains a connection to the database, initializing the URL if needed.
     *
     * @return A Connection to the SQLite database
     * @throws SQLException if the connection cannot be obtained
     */
    public static Connection getConnection() throws SQLException {
        if (DB_URL == null) {
            Logger.warn("DatabaseConnectionManager: DB_URL is not initialized. Attempting to initialize now.");
            initializeDatabaseUrl();
            if (DB_URL == null) {
                throw new SQLException("DatabaseConnectionManager: DB_URL is not set. Cannot obtain a database connection.");
            }
        }
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Closes the provided database connection, logging any errors encountered.
     *
     * @param connection The Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                Logger.info("DatabaseConnectionManager: Connection closed successfully.");
            } catch (SQLException e) {
                Logger.error("DatabaseConnectionManager: Error closing connection - {}", e.getMessage(), e);
            }
        }
    }
}
