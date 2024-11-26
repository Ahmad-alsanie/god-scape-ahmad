package com.godscape.system.schemas;

import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.enums.Themes;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.SerializableFactory;
import com.godscape.system.utility.Logger;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import lombok.Data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the schema for themes in the database.
 */
@Data
public class ThemeSchema implements IdentifiedDataSerializable {

    private int themeId;
    private String themeName;
    private boolean isActive;
    private String intensity;
    private boolean smoothCorners;
    private boolean smoothButtons;
    private boolean animations;
    private boolean transparency;
    private boolean highlights;
    private boolean shadows;

    private static final HazelcastInstance hazelcastInstance = DependencyFactory.getInstance().getInjection(HazelcastConfig.class).getHazelcastInstance();
    private static final IMap<String, ThemeSchema> themeCache = hazelcastInstance != null ? hazelcastInstance.getMap("themeCache") : null;

    public ThemeSchema() {}

    public ThemeSchema(String themeName) {
        this.themeId = generateUniqueThemeId();
        this.themeName = themeName;
        this.isActive = false;
        this.resetToDefaults();
    }

    private static int generateUniqueThemeId() {
        return themeCache != null ? themeCache.size() + 1 : 1;
    }

    public static ThemeSchema createNewTheme(String themeName) {
        if (themeCache == null) {
            throw new IllegalStateException("Hazelcast instance or themeCache is not initialized.");
        }
        ThemeSchema newTheme = new ThemeSchema(themeName);
        String key = generateMapKey(newTheme);
        themeCache.put(key, newTheme);
        return newTheme;
    }

    private static String generateMapKey(ThemeSchema theme) {
        return theme.getThemeId() + "-" + theme.getThemeName();
    }

    public static ThemeSchema getActiveTheme() {
        if (themeCache == null) {
            throw new IllegalStateException("Hazelcast instance or themeCache is not initialized.");
        }
        for (Map.Entry<String, ThemeSchema> entry : themeCache.entrySet()) {
            if (entry.getValue().isActive()) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void activate() {
        this.isActive = true;
        for (Map.Entry<String, ThemeSchema> entry : themeCache.entrySet()) {
            ThemeSchema otherTheme = entry.getValue();
            if (otherTheme.getThemeId() != this.themeId) {
                otherTheme.setActive(false);
                themeCache.put(entry.getKey(), otherTheme);
            }
        }
        String key = generateMapKey(this);
        themeCache.put(key, this);
    }

    public void resetToDefaults() {
        this.intensity = "Disabled";
        this.smoothCorners = false;
        this.smoothButtons = false;
        this.animations = false;
        this.transparency = false;
        this.highlights = false;
        this.shadows = false;
        this.isActive = false;
    }

    public Themes getSelectedTheme() {
        return Themes.valueOf(this.themeName.toUpperCase());
    }

    public void loadFromSchema(ThemeSchema schema) {
        if (schema != null) {
            this.themeId = schema.getThemeId();
            this.themeName = schema.getThemeName();
            this.isActive = schema.isActive();
            this.intensity = schema.getIntensity();
            this.smoothCorners = schema.isSmoothCorners();
            this.smoothButtons = schema.isSmoothButtons();
            this.animations = schema.isAnimations();
            this.transparency = schema.isTransparency();
            this.highlights = schema.isHighlights();
            this.shadows = schema.isShadows();
        }
    }

    public void setSelectedTheme(Themes theme) {
        if (theme != null) {
            this.themeName = theme.name();
        }
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    /**
     * Provides the column definitions for the Theme table.
     */
    public static Map<String, String> getColumnDefinitions() {
        Map<String, String> columns = new HashMap<>();
        columns.put("theme_id", "INTEGER PRIMARY KEY");
        columns.put("theme_name", "TEXT");
        columns.put("is_active", "INTEGER");
        columns.put("intensity", "TEXT");
        columns.put("smooth_corners", "INTEGER");
        columns.put("smooth_buttons", "INTEGER");
        columns.put("animations", "INTEGER");
        columns.put("transparency", "INTEGER");
        columns.put("highlights", "INTEGER");
        columns.put("shadows", "INTEGER");
        return columns;
    }

    /**
     * Initializes the Theme schema in the database.
     *
     * @param connection The database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static void initializeSchema(Connection connection) throws SQLException {
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS themes (");

        Map<String, String> columns = getColumnDefinitions();
        columns.forEach((name, type) -> createTableSQL.append(name).append(" ").append(type).append(", "));

        createTableSQL.setLength(createTableSQL.length() - 2);  // Remove trailing comma and space
        createTableSQL.append(");");

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL.toString());
            Logger.info("ThemeSchema: Initialized themes table schema successfully.");
        }
    }

    @Override
    public int getFactoryId() {
        return SerializableFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return SerializableFactory.THEME_SCHEMA_ID;
    }

    @Override
    public void writeData(com.hazelcast.nio.ObjectDataOutput out) throws IOException {
        out.writeInt(themeId);
        out.writeString(themeName != null ? themeName : "");
        out.writeBoolean(isActive);
        out.writeString(intensity != null ? intensity : "");
        out.writeBoolean(smoothCorners);
        out.writeBoolean(smoothButtons);
        out.writeBoolean(animations);
        out.writeBoolean(transparency);
        out.writeBoolean(highlights);
        out.writeBoolean(shadows);
    }

    @Override
    public void readData(com.hazelcast.nio.ObjectDataInput in) throws IOException {
        this.themeId = in.readInt();
        this.themeName = in.readString();
        this.isActive = in.readBoolean();
        this.intensity = in.readString();
        this.smoothCorners = in.readBoolean();
        this.smoothButtons = in.readBoolean();
        this.animations = in.readBoolean();
        this.transparency = in.readBoolean();
        this.highlights = in.readBoolean();
        this.shadows = in.readBoolean();
    }
}
