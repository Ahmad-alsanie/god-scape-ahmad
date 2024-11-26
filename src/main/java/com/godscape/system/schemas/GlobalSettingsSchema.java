package com.godscape.system.schemas;

import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;
import com.godscape.system.cache.GlobalSettingsCache;
import lombok.Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Data
public class GlobalSettingsSchema {

    // Singleton instance
    private static volatile GlobalSettingsSchema instance;

    // General Settings
    private String saveDirectory;

    // Persistence Settings
    private boolean enableJson;
    private boolean enableXml;
    private boolean backupProfiles;
    private boolean backupCharacters;
    private boolean createMissingFiles;

    // OSRS and RS3 specific fields
    private String osrsProfilesJsonPath;
    private String osrsProfilesXmlPath;
    private String osrsDatabaseFilename;
    private String osrsLogFileDirectory;
    private boolean osrsPreloadProfiles;

    private String rs3ProfilesJsonPath;
    private String rs3ProfilesXmlPath;
    private String rs3DatabaseFilename;
    private String rs3LogFileDirectory;
    private boolean rs3PreloadProfiles;

    // Additional Settings
    private boolean preloadProfiles;
    private boolean autoSyncProfiles;
    private boolean loadProfilesOnStart;

    // Logging Settings
    private String loggingLevel;
    private boolean logToFile;

    private long lastUpdated;

    // Private constructor for Singleton
    private GlobalSettingsSchema() {
        Logger.info("GlobalSettingsSchema: Singleton instance created.");
    }

    /**
     * Retrieves the singleton instance of GlobalSettingsSchema.
     *
     * @return The singleton instance.
     */
    public static GlobalSettingsSchema getInstance() {
        if (instance == null) {
            synchronized (GlobalSettingsSchema.class) {
                if (instance == null) {
                    instance = new GlobalSettingsSchema();
                    Logger.info("GlobalSettingsSchema: Singleton instance initialized.");
                }
            }
        }
        return instance;
    }

    /**
     * Load configuration from the properties file and cache it.
     *
     * @param configPath The path to the configuration file.
     */
    public void loadFromConfigFile(String configPath) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configPath)) {
            props.load(fis);

            // General settings
            this.saveDirectory = props.getProperty("saveDirectory", "");

            // Persistence settings
            this.enableJson = Boolean.parseBoolean(props.getProperty("enableJson", "false"));
            this.enableXml = Boolean.parseBoolean(props.getProperty("enableXml", "false"));
            this.backupProfiles = Boolean.parseBoolean(props.getProperty("backupProfiles", "false"));
            this.backupCharacters = Boolean.parseBoolean(props.getProperty("backupCharacters", "false"));
            this.createMissingFiles = Boolean.parseBoolean(props.getProperty("createMissingFiles", "false"));

            // Logging settings
            this.loggingLevel = props.getProperty("loggingLevel", "INFO");
            this.logToFile = Boolean.parseBoolean(props.getProperty("logToFile", "false"));

            // OSRS settings
            this.osrsDatabaseFilename = props.getProperty("osrsDatabaseFilename", "");
            this.osrsProfilesXmlPath = props.getProperty("osrsProfilesXml", "");
            this.osrsProfilesJsonPath = props.getProperty("osrsProfilesJson", "");
            this.osrsLogFileDirectory = props.getProperty("osrsLogFileDirectory", "");
            this.osrsPreloadProfiles = Boolean.parseBoolean(props.getProperty("osrsPreloadProfiles", "false"));

            // RS3 settings
            this.rs3DatabaseFilename = props.getProperty("rs3DatabaseFilename", "");
            this.rs3ProfilesXmlPath = props.getProperty("rs3ProfilesXml", "");
            this.rs3ProfilesJsonPath = props.getProperty("rs3ProfilesJson", "");
            this.rs3LogFileDirectory = props.getProperty("rs3LogFileDirectory", "");
            this.rs3PreloadProfiles = Boolean.parseBoolean(props.getProperty("rs3PreloadProfiles", "false"));

            this.preloadProfiles = Boolean.parseBoolean(props.getProperty("preloadProfiles", "false"));
            this.autoSyncProfiles = Boolean.parseBoolean(props.getProperty("autoSyncProfiles", "false"));
            this.loadProfilesOnStart = Boolean.parseBoolean(props.getProperty("loadProfilesOnStart", "false"));

            this.lastUpdated = System.currentTimeMillis();

            // Cache the loaded settings
            GlobalSettingsCache.getInstance().putGlobalSettings(this);

            Logger.info("Configuration loaded successfully from {}", configPath);
        } catch (IOException e) {
            Logger.error("Failed to load configuration from '{}': {}", configPath, e.getMessage());
        }
    }

    // Getters for game-version-specific paths and settings based on GameVersion
    private GameVersion getCurrentGameVersion() {
        return DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();
    }

    public String getProfilesJsonPath() {
        return getCurrentGameVersion() == GameVersion.OSRS ? osrsProfilesJsonPath : rs3ProfilesJsonPath;
    }

    public String getProfilesXmlPath() {
        return getCurrentGameVersion() == GameVersion.OSRS ? osrsProfilesXmlPath : rs3ProfilesXmlPath;
    }

    public String getDatabaseFilename() {
        return getCurrentGameVersion() == GameVersion.OSRS ? osrsDatabaseFilename : rs3DatabaseFilename;
    }

    public String getLogFileDirectory() {
        return getCurrentGameVersion() == GameVersion.OSRS ? osrsLogFileDirectory : rs3LogFileDirectory;
    }

    public boolean isPreloadProfiles() {
        return getCurrentGameVersion() == GameVersion.OSRS ? osrsPreloadProfiles : rs3PreloadProfiles;
    }

    /**
     * Log the current profile paths for debugging purposes.
     */
    public void logProfilePaths() {
        Logger.info("JSON Path: {}", getProfilesJsonPath());
        Logger.info("XML Path: {}", getProfilesXmlPath());
        Logger.info("Database Path: {}", getDatabaseFilename());
    }

    // Methods for dynamically setting paths and configurations based on GameVersion
    public void setProfilesJsonPath(String path) {
        if (getCurrentGameVersion() == GameVersion.OSRS) {
            this.osrsProfilesJsonPath = path;
        } else {
            this.rs3ProfilesJsonPath = path;
        }
    }

    public void setProfilesXmlPath(String path) {
        if (getCurrentGameVersion() == GameVersion.OSRS) {
            this.osrsProfilesXmlPath = path;
        } else {
            this.rs3ProfilesXmlPath = path;
        }
    }

    public void setDatabaseFilename(String filename) {
        if (getCurrentGameVersion() == GameVersion.OSRS) {
            this.osrsDatabaseFilename = filename;
        } else {
            this.rs3DatabaseFilename = filename;
        }
    }

    public void setLogFileDirectory(String logDir) {
        if (getCurrentGameVersion() == GameVersion.OSRS) {
            this.osrsLogFileDirectory = logDir;
        } else {
            this.rs3LogFileDirectory = logDir;
        }
    }

    public void setPreloadProfiles(boolean preload) {
        if (getCurrentGameVersion() == GameVersion.OSRS) {
            this.osrsPreloadProfiles = preload;
        } else {
            this.rs3PreloadProfiles = preload;
        }
    }

    /**
     * Loads the GlobalSettingsSchema from the cache if available; otherwise, loads from the config file.
     *
     * @param configPath The path to the configuration file.
     */
    public void loadSettings(String configPath) {
        GlobalSettingsSchema cachedSettings = GlobalSettingsCache.getInstance().getGlobalSettings();
        if (cachedSettings != null) {
            copyFrom(cachedSettings);
            Logger.info("GlobalSettingsSchema: Loaded settings from cache.");
        } else {
            loadFromConfigFile(configPath);
            Logger.info("GlobalSettingsSchema: Loaded settings from config file and cached them.");
        }
    }

    /**
     * Copies settings from another GlobalSettingsSchema instance.
     *
     * @param other The source GlobalSettingsSchema to copy from.
     */
    private void copyFrom(GlobalSettingsSchema other) {
        this.saveDirectory = other.saveDirectory;
        this.enableJson = other.enableJson;
        this.enableXml = other.enableXml;
        this.backupProfiles = other.backupProfiles;
        this.backupCharacters = other.backupCharacters;
        this.createMissingFiles = other.createMissingFiles;
        this.loggingLevel = other.loggingLevel;
        this.logToFile = other.logToFile;
        this.osrsDatabaseFilename = other.osrsDatabaseFilename;
        this.osrsProfilesXmlPath = other.osrsProfilesXmlPath;
        this.osrsProfilesJsonPath = other.osrsProfilesJsonPath;
        this.osrsLogFileDirectory = other.osrsLogFileDirectory;
        this.osrsPreloadProfiles = other.osrsPreloadProfiles;
        this.rs3DatabaseFilename = other.rs3DatabaseFilename;
        this.rs3ProfilesXmlPath = other.rs3ProfilesXmlPath;
        this.rs3ProfilesJsonPath = other.rs3ProfilesJsonPath;
        this.rs3LogFileDirectory = other.rs3LogFileDirectory;
        this.rs3PreloadProfiles = other.rs3PreloadProfiles;
        this.preloadProfiles = other.preloadProfiles;
        this.autoSyncProfiles = other.autoSyncProfiles;
        this.loadProfilesOnStart = other.loadProfilesOnStart;
        this.lastUpdated = other.lastUpdated;
    }
}
