package com.godscape.system.preloaders;

import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.enums.ConfigKeys;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.interfaces.mark.Preloadable;
import com.godscape.system.schemas.GlobalSettingsSchema;
import com.godscape.system.templates.GlobalSettingFileTemplate;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.builders.FileSystemBuilder;
import com.godscape.system.utility.builders.PathBuilder;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GlobalSettingsPreloader implements Preloadable {

    private static volatile GlobalSettingsPreloader instance;

    private final GlobalSettingsSchema settingsSchema;
    private final PathBuilder pathBuilder;
    private final GodscapeConfig config;
    private final FileSystemBuilder fileSystemBuilder;
    private static final String CONFIG_FILE = "godscape.properties";
    private static final Map<String, String> defaultSettings = new HashMap<>();

    static {
        defaultSettings.put("createMissingFiles", "true");
        defaultSettings.put("preloadProfiles", "true");
        defaultSettings.put("autoSyncProfiles", "true");
        defaultSettings.put("loadProfilesOnStart", "true");
        defaultSettings.put("enableJson", "true");
        defaultSettings.put("enableXml", "true");
        defaultSettings.put("backupProfiles", "true");
        defaultSettings.put("backupCharacters", "true");
        defaultSettings.put("loggingLevel", "INFO");
        defaultSettings.put("logToFile", "true");
    }

    private GlobalSettingsPreloader() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.settingsSchema = dependencyFactory.getInjection(GlobalSettingsSchema.class);
        this.pathBuilder = dependencyFactory.getInjection(PathBuilder.class);
        this.config = dependencyFactory.getInjection(GodscapeConfig.class);
        this.fileSystemBuilder = dependencyFactory.getInjection(FileSystemBuilder.class);
        preloadSettings();
    }

    public static GlobalSettingsPreloader getInstance() {
        if (instance == null) {
            synchronized (GlobalSettingsPreloader.class) {
                if (instance == null) {
                    instance = new GlobalSettingsPreloader();
                }
            }
        }
        return instance;
    }

    @Override
    public void preload() {
        preloadSettings();
    }

    private void preloadSettings() {
        loadSettingsFromFile();
        createConfigFileIfMissing();
        Logger.info("Global settings loaded and configuration file check complete.");
    }

    private void loadSettingsFromFile() {
        Path configFile = pathBuilder.buildPath(ConfigKeys.SAVE_DIRECTORY).resolve(CONFIG_FILE);
        Logger.debug("Attempting to load configuration from: {}", configFile);

        if (configFile.toFile().exists()) {
            try (InputStream input = new FileInputStream(configFile.toFile())) {
                Properties prop = new Properties();
                prop.load(input);
                applyLoadedSettings(prop);
                Logger.info("Settings successfully loaded from configuration file.");
            } catch (IOException ex) {
                Logger.error("Error loading settings from configuration file: {}", ex.getMessage());
                applyDefaultSettings();
            }
        } else {
            Logger.info("Configuration file not found. Applying default settings.");
            applyDefaultSettings();
        }
    }

    private void applyLoadedSettings(Properties prop) {
        settingsSchema.setSaveDirectory(config.getString(ConfigKeys.SAVE_DIRECTORY));
        settingsSchema.setCreateMissingFiles(Boolean.parseBoolean(prop.getProperty("createMissingFiles", defaultSettings.get("createMissingFiles"))));

        settingsSchema.setEnableJson(Boolean.parseBoolean(prop.getProperty("enableJson", defaultSettings.get("enableJson"))));
        settingsSchema.setEnableXml(Boolean.parseBoolean(prop.getProperty("enableXml", defaultSettings.get("enableXml"))));
        settingsSchema.setBackupProfiles(Boolean.parseBoolean(prop.getProperty("backupProfiles", defaultSettings.get("backupProfiles"))));
        settingsSchema.setBackupCharacters(Boolean.parseBoolean(prop.getProperty("backupCharacters", defaultSettings.get("backupCharacters"))));

        settingsSchema.setLoggingLevel(prop.getProperty("loggingLevel", defaultSettings.get("loggingLevel")));
        settingsSchema.setLogToFile(Boolean.parseBoolean(prop.getProperty("logToFile", defaultSettings.get("logToFile"))));

        settingsSchema.setPreloadProfiles(Boolean.parseBoolean(prop.getProperty("preloadProfiles", defaultSettings.get("preloadProfiles"))));
        settingsSchema.setAutoSyncProfiles(Boolean.parseBoolean(prop.getProperty("autoSyncProfiles", defaultSettings.get("autoSyncProfiles"))));
        settingsSchema.setLoadProfilesOnStart(Boolean.parseBoolean(prop.getProperty("loadProfilesOnStart", defaultSettings.get("loadProfilesOnStart"))));

        setGameSpecificPaths(prop);
        settingsSchema.setLastUpdated(System.currentTimeMillis());
        Logger.debug("Settings loaded and applied.");
    }

    private void setGameSpecificPaths(Properties prop) {
        GameVersion gameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();
        if (gameVersion == GameVersion.OSRS) {
            settingsSchema.setDatabaseFilename(prop.getProperty("osrsDatabaseFilename", config.getString(ConfigKeys.OSRS_DATABASE_FILENAME)));
            settingsSchema.setProfilesJsonPath(prop.getProperty("osrsProfilesJson", config.getString(ConfigKeys.OSRS_PROFILES_JSON)));
            settingsSchema.setProfilesXmlPath(prop.getProperty("osrsProfilesXml", config.getString(ConfigKeys.OSRS_PROFILES_XML)));
            settingsSchema.setLogFileDirectory(prop.getProperty("osrsLogFileDirectory", config.getString(ConfigKeys.OSRS_LOG_FILE_DIRECTORY)));
        } else if (gameVersion == GameVersion.RS3) {
            settingsSchema.setDatabaseFilename(prop.getProperty("rs3DatabaseFilename", config.getString(ConfigKeys.RS3_DATABASE_FILENAME)));
            settingsSchema.setProfilesJsonPath(prop.getProperty("rs3ProfilesJson", config.getString(ConfigKeys.RS3_PROFILES_JSON)));
            settingsSchema.setProfilesXmlPath(prop.getProperty("rs3ProfilesXml", config.getString(ConfigKeys.RS3_PROFILES_XML)));
            settingsSchema.setLogFileDirectory(prop.getProperty("rs3LogFileDirectory", config.getString(ConfigKeys.RS3_LOG_FILE_DIRECTORY)));
        }
    }

    private void applyDefaultSettings() {
        settingsSchema.setSaveDirectory(config.getString(ConfigKeys.SAVE_DIRECTORY));
        settingsSchema.setCreateMissingFiles(Boolean.parseBoolean(defaultSettings.get("createMissingFiles")));

        settingsSchema.setEnableJson(Boolean.parseBoolean(defaultSettings.get("enableJson")));
        settingsSchema.setEnableXml(Boolean.parseBoolean(defaultSettings.get("enableXml")));
        settingsSchema.setBackupProfiles(Boolean.parseBoolean(defaultSettings.get("backupProfiles")));
        settingsSchema.setBackupCharacters(Boolean.parseBoolean(defaultSettings.get("backupCharacters")));

        settingsSchema.setLoggingLevel(defaultSettings.get("loggingLevel"));
        settingsSchema.setLogToFile(Boolean.parseBoolean(defaultSettings.get("logToFile")));

        settingsSchema.setPreloadProfiles(Boolean.parseBoolean(defaultSettings.get("preloadProfiles")));
        settingsSchema.setAutoSyncProfiles(Boolean.parseBoolean(defaultSettings.get("autoSyncProfiles")));
        settingsSchema.setLoadProfilesOnStart(Boolean.parseBoolean(defaultSettings.get("loadProfilesOnStart")));

        setGameSpecificPaths(new Properties());
        settingsSchema.setLastUpdated(System.currentTimeMillis());
        Logger.debug("Default settings applied successfully.");
    }

    private void createConfigFileIfMissing() {
        GlobalSettingFileTemplate template = new GlobalSettingFileTemplate(settingsSchema, CONFIG_FILE);
        if (settingsSchema.isCreateMissingFiles()) {
            fileSystemBuilder.createFileIfMissing(template.getConfigFilePath().toString());
        }
        template.createConfigFile();
        Logger.info("Configuration file creation check complete.");
    }

    public GlobalSettingsSchema getSettingsSchema() {
        return settingsSchema;
    }
}
