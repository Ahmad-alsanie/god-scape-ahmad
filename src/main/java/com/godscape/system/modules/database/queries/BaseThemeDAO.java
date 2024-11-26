package com.godscape.system.modules.database.queries;

import com.godscape.system.modules.database.DatabaseConnectionManager;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.enums.Themes;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.builders.FileSystemBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseThemeDAO {

    private static final String TABLE_NAME = "themes";
    private static BaseThemeDAO instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final FileSystemBuilder fileSystemBuilder;

    private BaseThemeDAO() {
        this.fileSystemBuilder = DependencyFactory.getInstance().getInjection(FileSystemBuilder.class);
        initializeDatabase();
    }

    public static synchronized BaseThemeDAO getInstance() {
        if (instance == null) {
            instance = new BaseThemeDAO();
        }
        return instance;
    }

    public void initializeDatabase() {
        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            ThemeSchema.initializeSchema(connection);
            Logger.info("BaseThemeDAO: Theme table schema initialized successfully.");

            if (isThemesTableEmpty(connection)) {
                populateInitialThemes(connection);
            } else {
                Logger.info("BaseThemeDAO: Themes table already populated.");
            }
        } catch (SQLException e) {
            Logger.error("BaseThemeDAO: Error initializing themes database - {}", e.getMessage());
        }
    }

    private boolean isThemesTableEmpty(Connection connection) {
        String countSQL = "SELECT COUNT(*) AS count FROM " + TABLE_NAME;
        try (PreparedStatement stmt = connection.prepareStatement(countSQL);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() && rs.getInt("count") == 0;
        } catch (SQLException e) {
            Logger.error("BaseThemeDAO: Error checking if themes table is empty - {}", e.getMessage());
        }
        return false;
    }

    private void populateInitialThemes(Connection connection) {
        Logger.info("BaseThemeDAO: Populating initial themes as the '{}' table is empty.", TABLE_NAME);
        String insertSQL = "INSERT INTO " + TABLE_NAME + " (" +
                "theme_name," +
                "intensity," +
                "smooth_corners," +
                "smooth_buttons," +
                "animations," +
                "transparency," +
                "highlights," +
                "shadows," +
                "is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
            insertTheme(insertStmt, Themes.LIGHT, "Medium", true, true, true, true, true, true, true);
            insertTheme(insertStmt, Themes.CELESTIAL, "Low", true, true, true, true, true, true, false);
            insertStmt.executeBatch();
            connection.commit();
            Logger.info("BaseThemeDAO: Initial themes inserted.");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                Logger.error("BaseThemeDAO: Error rolling back transaction - {}", ex.getMessage());
            }
            Logger.error("BaseThemeDAO: Error inserting initial themes - {}", e.getMessage());
        }
    }

    private void insertTheme(PreparedStatement stmt, Themes theme, String intensity,
                             boolean smoothCorners, boolean smoothButtons, boolean animations,
                             boolean transparency, boolean highlights, boolean shadows, boolean isActive) throws SQLException {
        stmt.setString(1, theme.name());
        stmt.setString(2, intensity);
        stmt.setBoolean(3, smoothCorners);
        stmt.setBoolean(4, smoothButtons);
        stmt.setBoolean(5, animations);
        stmt.setBoolean(6, transparency);
        stmt.setBoolean(7, highlights);
        stmt.setBoolean(8, shadows);
        stmt.setBoolean(9, isActive);
        stmt.addBatch();
    }

    public boolean setActiveTheme(String themeName) {
        String deactivateSQL = "UPDATE " + TABLE_NAME + " SET is_active = 0";
        String activateSQL = "UPDATE " + TABLE_NAME + " SET is_active = 1 WHERE theme_name = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement deactivateStmt = connection.prepareStatement(deactivateSQL);
                 PreparedStatement activateStmt = connection.prepareStatement(activateSQL)) {

                deactivateStmt.executeUpdate();
                activateStmt.setString(1, themeName);
                int rows = activateStmt.executeUpdate();

                if (rows > 0) {
                    connection.commit();
                    Logger.info("BaseThemeDAO: Theme '{}' set as active.", themeName);
                    return true;
                } else {
                    connection.rollback();
                    Logger.warn("BaseThemeDAO: Theme '{}' not found.", themeName);
                    return false;
                }

            } catch (SQLException e) {
                connection.rollback();
                Logger.error("BaseThemeDAO: Error activating theme '{}' - {}", themeName, e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            Logger.error("BaseThemeDAO: Error setting active theme - {}", e.getMessage());
            return false;
        }
    }

    public List<ThemeSchema> loadAllThemes() {
        List<ThemeSchema> themes = new ArrayList<>();
        String fetchSQL = "SELECT * FROM " + TABLE_NAME;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(fetchSQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                themes.add(mapToThemeSchema(rs));
            }

            Logger.info("BaseThemeDAO: Loaded {} themes.", themes.size());

        } catch (SQLException e) {
            Logger.error("BaseThemeDAO: Error loading themes - {}", e.getMessage());
        }

        return themes;
    }

    public ThemeSchema fetchActiveTheme() {
        String fetchSQL = "SELECT * FROM " + TABLE_NAME + " WHERE is_active = 1 LIMIT 1";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(fetchSQL);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                ThemeSchema activeTheme = mapToThemeSchema(rs);
                Logger.info("BaseThemeDAO: Fetched active theme '{}'.", activeTheme.getThemeName());
                return activeTheme;
            } else {
                Logger.warn("BaseThemeDAO: No active theme found.");
                return null;
            }

        } catch (SQLException e) {
            Logger.error("BaseThemeDAO: Error fetching active theme - {}", e.getMessage());
            return null;
        }
    }

    public boolean updateTheme(ThemeSchema themeSchema) {
        String updateSQL = "UPDATE " + TABLE_NAME + " " +
                "SET intensity = ?," +
                "smooth_corners = ?," +
                "smooth_buttons = ?," +
                "animations = ?," +
                "transparency = ?," +
                "highlights = ?," +
                "shadows = ?," +
                "is_active = ?" +
                "WHERE theme_name = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(updateSQL)) {

            stmt.setString(1, themeSchema.getIntensity());
            stmt.setBoolean(2, themeSchema.isSmoothCorners());
            stmt.setBoolean(3, themeSchema.isSmoothButtons());
            stmt.setBoolean(4, themeSchema.isAnimations());
            stmt.setBoolean(5, themeSchema.isTransparency());
            stmt.setBoolean(6, themeSchema.isHighlights());
            stmt.setBoolean(7, themeSchema.isShadows());
            stmt.setBoolean(8, themeSchema.isActive());
            stmt.setString(9, themeSchema.getThemeName());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                Logger.info("BaseThemeDAO: Updated theme '{}'.", themeSchema.getThemeName());
                return true;
            } else {
                Logger.warn("BaseThemeDAO: No theme found to update with name '{}'.", themeSchema.getThemeName());
                return false;
            }

        } catch (SQLException e) {
            Logger.error("BaseThemeDAO: Error updating theme '{}' - {}", themeSchema.getThemeName(), e.getMessage());
            return false;
        }
    }

    private ThemeSchema mapToThemeSchema(ResultSet rs) throws SQLException {
        ThemeSchema theme = new ThemeSchema();
        theme.setThemeName(rs.getString("theme_name"));
        theme.setIntensity(rs.getString("intensity"));
        theme.setSmoothCorners(rs.getBoolean("smooth_corners"));
        theme.setSmoothButtons(rs.getBoolean("smooth_buttons"));
        theme.setAnimations(rs.getBoolean("animations"));
        theme.setTransparency(rs.getBoolean("transparency"));
        theme.setHighlights(rs.getBoolean("highlights"));
        theme.setShadows(rs.getBoolean("shadows"));
        theme.setActive(rs.getBoolean("is_active"));
        return theme;
    }
}
