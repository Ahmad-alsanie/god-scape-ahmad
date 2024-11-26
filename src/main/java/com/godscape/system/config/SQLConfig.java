package com.godscape.system.config;

import com.godscape.system.enums.Factories;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.preloaders.GlobalSettingsPreloader;
import com.godscape.system.schemas.GlobalSettingsSchema;
import com.godscape.system.utility.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class SQLConfig {

    private static volatile SQLConfig instance; // Singleton instance
    private static String DATABASE_URL;

    // Private constructor to prevent instantiation
    public SQLConfig() {
        configureDatabase();  // Automatically configure the database on instantiation
    }

    // Public method to access the singleton instance, using DependencyFactory for retrieval
    public static SQLConfig getInstance() {
        if (instance == null) {
            synchronized (SQLConfig.class) {
                if (instance == null) {
                    instance = DependencyFactory.getInstance().getInjection(SQLConfig.class);
                }
            }
        }
        return instance;
    }

    /**
     * Configures the database and creates the admin user.
     */
    public void configureDatabase() {
        // Load settings from GlobalSettingsPreloader
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        GlobalSettingsSchema settingsSchema = dependencyFactory.getInjection(GlobalSettingsSchema.class);

        // Determine current game type using PlatformFactory
        String currentGameType = dependencyFactory.getInjection(Factories.PLATFORM_FACTORY);

        // Construct the path to the database based on the game type
        String baseDirectory = settingsSchema.getSaveDirectory();
        String databaseFilename = settingsSchema.getDatabaseFilename();

        // Append game type directory (osrs, rs3) to the base directory
        Path gameTypeDirectory = Paths.get(baseDirectory, currentGameType);

        // Create the full database URL (e.g., C:/Godscape/osrs/database.db)
        DATABASE_URL = "jdbc:sqlite:" + Paths.get(gameTypeDirectory.toString(), databaseFilename).toString();

        // Log the database path
        Logger.info("SQLConfig: Configuring database for game type '{}' at {}", currentGameType, DATABASE_URL);

        // Ensure the game-specific directory exists
        ensureDirectoryExists(gameTypeDirectory.toString());

        // Ensure the database exists before proceeding
        createDatabaseIfNotExists();

        // Explicitly load SQLite driver
        try {
            Class.forName("org.sqlite.JDBC");
            Logger.info("SQLConfig: SQLite JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            Logger.error("SQLConfig: SQLite JDBC Driver not found - {}", e.getMessage());
            return;  // Return early if the driver isn't loaded
        }

        // Perform database operations (e.g., create admin user, create tables)
        createUsersTable();
        createAdminUser();
        createPreventEmptyInsertTrigger();

        Logger.info("SQLConfig: Database configuration completed.");
    }

    /**
     * Ensures the directory for the specific game type exists, creating it if necessary.
     */
    public void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Logger.info("SQLConfig: Created game-specific directory at {}", directoryPath);
            } else {
                Logger.error("SQLConfig: Failed to create directory at {}", directoryPath);
            }
        }
    }

    /**
     * Creates the SQLite database if it doesn't already exist.
     */
    private void createDatabaseIfNotExists() {
        String filePath = DATABASE_URL.replace("jdbc:sqlite:", "");
        File dbFile = new File(filePath);

        if (!dbFile.exists()) {
            try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
                if (connection != null) {
                    Logger.info("SQLConfig: Database created at {}", filePath);
                }
            } catch (SQLException e) {
                Logger.error("SQLConfig: Error creating database at {} - {}", filePath, e.getMessage());
            }
        }
    }

    /**
     * Creates the users table with enhanced constraints.
     */
    private void createUsersTable() {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL CHECK(username <> ''), " +
                "password TEXT NOT NULL CHECK(password <> ''), " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(createUsersTable);
            Logger.info("SQLConfig: Users table created or already exists.");

        } catch (SQLException e) {
            Logger.error("SQLConfig: Error creating users table - {}", e.getMessage());
        }
    }

    /**
     * Creates a trigger to prevent inserting empty rows into the users table.
     */
    private void createPreventEmptyInsertTrigger() {
        String createTrigger = "CREATE TRIGGER IF NOT EXISTS prevent_empty_insert " +
                "BEFORE INSERT ON users " +
                "FOR EACH ROW " +
                "WHEN NEW.username IS NULL OR NEW.username = '' OR NEW.password IS NULL OR NEW.password = '' " +
                "BEGIN " +
                "   SELECT RAISE(ABORT, 'Cannot insert empty username or password'); " +
                "END;";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             Statement statement = connection.createStatement()) {

            statement.execute(createTrigger);
            Logger.info("SQLConfig: Trigger to prevent empty inserts created.");

        } catch (SQLException e) {
            Logger.error("SQLConfig: Error creating trigger - {}", e.getMessage());
        }
    }

    /**
     * Creates an admin user "Shujin" for database access.
     */
    private void createAdminUser() {
        String username = "Shujin";
        String password = "Shujin";

        String insertAdminUser = "INSERT OR IGNORE INTO users (username, password) VALUES (?, ?);";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertAdminUser)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Logger.info("SQLConfig: Admin user '{}' created.", username);
            } else {
                Logger.info("SQLConfig: Admin user '{}' already exists.", username);
            }

        } catch (SQLException e) {
            Logger.error("SQLConfig: Error creating admin user - {}", e.getMessage());
        }
    }

    /**
     * Verifies the user's credentials.
     */
    public boolean verifyUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            boolean isValid = resultSet.next();

            if (isValid) {
                Logger.info("SQLConfig: User '{}' successfully verified.", username);
            } else {
                Logger.warn("SQLConfig: User '{}' verification failed.", username);
            }

            return isValid;

        } catch (SQLException e) {
            Logger.error("SQLConfig: Error verifying user '{}' - {}", username, e.getMessage());
            return false;
        }
    }
}
