package com.godscape.system.config;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.ConfigKeys;
import com.godscape.system.utility.Logger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class GodscapeConfig {

    private final Map<ConfigKeys, String> hardcodedConfig;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public GodscapeConfig() {
        this.hardcodedConfig = initializeConfig();
        Logger.info("GodscapeConfig: Hardcoded configurations loaded.");
    }

    /**
     * Initializes configurations directly in the code.
     *
     * @return A map containing hard-coded configurations.
     */
    private Map<ConfigKeys, String> initializeConfig() {
        Map<ConfigKeys, String> configMap = new HashMap<>();

        // General Settings
        configMap.put(ConfigKeys.SAVE_DIRECTORY, "C:\\godscape\\");
        configMap.put(ConfigKeys.CREATE_MISSING_FILES, "true");

        // OSRS Settings
        configMap.put(ConfigKeys.OSRS_SAVE_DIRECTORY, configMap.get(ConfigKeys.SAVE_DIRECTORY) + "osrs\\");
        configMap.put(ConfigKeys.OSRS_DATABASE_FILENAME, configMap.get(ConfigKeys.OSRS_SAVE_DIRECTORY) + "osrs_database.db");
        configMap.put(ConfigKeys.OSRS_PROFILES_JSON, configMap.get(ConfigKeys.OSRS_SAVE_DIRECTORY) + "osrs_profiles.json");
        configMap.put(ConfigKeys.OSRS_PROFILES_XML, configMap.get(ConfigKeys.OSRS_SAVE_DIRECTORY) + "osrs_profiles.xml");
        configMap.put(ConfigKeys.OSRS_LOG_FILE_DIRECTORY, configMap.get(ConfigKeys.OSRS_SAVE_DIRECTORY) + "logs\\");
        configMap.put(ConfigKeys.OSRS_PRELOAD_PROFILES, "true");

        // RS3 Settings
        configMap.put(ConfigKeys.RS3_SAVE_DIRECTORY, configMap.get(ConfigKeys.SAVE_DIRECTORY) + "rs3\\");
        configMap.put(ConfigKeys.RS3_DATABASE_FILENAME, configMap.get(ConfigKeys.RS3_SAVE_DIRECTORY) + "rs3_database.db");
        configMap.put(ConfigKeys.RS3_PROFILES_JSON, configMap.get(ConfigKeys.RS3_SAVE_DIRECTORY) + "rs3_profiles.json");
        configMap.put(ConfigKeys.RS3_PROFILES_XML, configMap.get(ConfigKeys.RS3_SAVE_DIRECTORY) + "rs3_profiles.xml");
        configMap.put(ConfigKeys.RS3_LOG_FILE_DIRECTORY, configMap.get(ConfigKeys.RS3_SAVE_DIRECTORY) + "logs\\");
        configMap.put(ConfigKeys.RS3_PRELOAD_PROFILES, "true");

        // Persistence Settings
        configMap.put(ConfigKeys.ENABLE_JSON, "true");
        configMap.put(ConfigKeys.ENABLE_XML, "true");
        configMap.put(ConfigKeys.BACKUP_PROFILES, "true");
        configMap.put(ConfigKeys.BACKUP_CHARACTERS, "true");

        // Logging Configuration
        configMap.put(ConfigKeys.LOGGING_LEVEL, "DEBUG");
        configMap.put(ConfigKeys.LOG_TO_FILE, "true");

        // New Keys
        configMap.put(ConfigKeys.PRELOAD_PROFILES, "true");

        // Timestamp for last updated
        configMap.put(ConfigKeys.LAST_UPDATED_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

        return configMap;
    }

    public String getString(ConfigKeys key) {
        String value = hardcodedConfig.get(key);
        if (value == null) {
            Logger.warn("GodscapeConfig: Configuration key '{}' not found.", key.getKey());
            return "";
        }
        return value;
    }

    public int getInt(ConfigKeys key, int defaultValue) {
        String value = hardcodedConfig.get(key);
        if (value == null) {
            Logger.warn("GodscapeConfig: Configuration key '{}' not found. Using default value: {}", key.getKey(), defaultValue);
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Logger.warn("GodscapeConfig: Invalid integer for key '{}': {}. Using default value: {}", key.getKey(), value, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBoolean(ConfigKeys key, boolean defaultValue) {
        String value = hardcodedConfig.get(key);
        if (value == null) {
            Logger.warn("GodscapeConfig: Configuration key '{}' not found. Using default value: {}", key.getKey(), defaultValue);
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public String getFormattedLastUpdatedTimestamp() {
        String timestamp = hardcodedConfig.get(ConfigKeys.LAST_UPDATED_TIMESTAMP);
        if (timestamp == null) {
            Logger.warn("GodscapeConfig: LAST_UPDATED_TIMESTAMP not found.");
            return "";
        }
        long epochMilli = Long.parseLong(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
        return dateTime.format(TIMESTAMP_FORMATTER);
    }

    // Convenience methods for commonly accessed configurations
    public String getOsrsProfilesJsonPath() {
        return getString(ConfigKeys.OSRS_PROFILES_JSON);
    }

    public String getOsrsProfilesXmlPath() {
        return getString(ConfigKeys.OSRS_PROFILES_XML);
    }

    public String getRs3ProfilesJsonPath() {
        return getString(ConfigKeys.RS3_PROFILES_JSON);
    }

    public String getRs3ProfilesXmlPath() {
        return getString(ConfigKeys.RS3_PROFILES_XML);
    }
}
