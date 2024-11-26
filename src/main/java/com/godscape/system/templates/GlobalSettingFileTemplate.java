package com.godscape.system.templates;

import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.enums.ConfigKeys;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.schemas.GlobalSettingsSchema;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.Separate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GlobalSettingFileTemplate {

    private final GlobalSettingsSchema settingsSchema;
    private final GodscapeConfig config;
    private final String configFileName;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public GlobalSettingFileTemplate(GlobalSettingsSchema settingsSchema, String configFileName) {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.config = dependencyFactory.getInjection(GodscapeConfig.class);
        this.settingsSchema = settingsSchema;
        this.configFileName = configFileName;
    }

    /**
     * Returns the absolute path of the configuration file.
     * @return Path to the configuration file.
     */
    public Path getConfigFilePath() {
        return Paths.get(settingsSchema.getSaveDirectory(), configFileName).toAbsolutePath().normalize();
    }

    /**
     * Checks if the configuration file already exists.
     * @return true if the configuration file exists, false otherwise.
     */
    private boolean configFileExists() {
        return Files.exists(getConfigFilePath());
    }

    /**
     * Creates the configuration file if it does not exist.
     */
    public void createConfigFile() {
        Path configFile = getConfigFilePath();

        // Ensure the directory exists
        try {
            Files.createDirectories(configFile.getParent());
        } catch (IOException e) {
            Logger.error("Failed to create directories for config file: {}", e.getMessage());
            return;
        }

        if (configFileExists()) {
            Logger.info("Configuration file already exists at: {}", configFile);
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
            writeConfigFileContents(writer);
            Logger.info("Configuration file created at: {}", configFile);
        } catch (IOException e) {
            Logger.error("Failed to create config file '{}': {}", configFile, e.getMessage());
        }
    }

    /**
     * Writes the default configuration content to the specified writer.
     * @param writer BufferedWriter instance.
     */
    private void writeConfigFileContents(BufferedWriter writer) throws IOException {
        String separator = Separate.repeatChar('#', 50);

        writer.write(separator);
        writer.newLine();
        writer.write("# Godscape Configuration File");
        writer.newLine();
        writer.write("# Created on: " + LocalDateTime.now().format(DATE_FORMATTER));
        writer.newLine();
        writer.write(separator);
        writer.newLine();
        writer.write("# WARNING: Editing this configuration directly may break functionality.");
        writer.newLine();
        writer.write(separator);
        writer.newLine();

        // General Settings
        writer.write("[General]");
        writer.newLine();
        writer.write("saveDirectory=" + settingsSchema.getSaveDirectory());
        writer.newLine();
        writer.write("createMissingFiles=" + config.getBoolean(ConfigKeys.CREATE_MISSING_FILES, false));
        writer.newLine();
        writer.newLine();

        // OSRS Settings
        writer.write("[Old School RuneScape]");
        writer.newLine();
        writer.write("osrsDatabaseFilename=" + config.getString(ConfigKeys.OSRS_DATABASE_FILENAME));
        writer.newLine();
        writer.write("osrsProfilesXml=" + config.getOsrsProfilesXmlPath());
        writer.newLine();
        writer.write("osrsPreloadProfiles=" + config.getBoolean(ConfigKeys.OSRS_PRELOAD_PROFILES, false));
        writer.newLine();
        writer.write("osrsProfilesJson=" + config.getOsrsProfilesJsonPath());
        writer.newLine();
        writer.write("osrsLogFileDirectory=" + config.getString(ConfigKeys.OSRS_LOG_FILE_DIRECTORY));
        writer.newLine();
        writer.newLine();

        // RS3 Settings
        writer.write("[RuneScape 3]");
        writer.newLine();
        writer.write("rs3DatabaseFilename=" + config.getString(ConfigKeys.RS3_DATABASE_FILENAME));
        writer.newLine();
        writer.write("rs3ProfilesXml=" + config.getRs3ProfilesXmlPath());
        writer.newLine();
        writer.write("rs3PreloadProfiles=" + config.getBoolean(ConfigKeys.RS3_PRELOAD_PROFILES, false));
        writer.newLine();
        writer.write("rs3ProfilesJson=" + config.getRs3ProfilesJsonPath());
        writer.newLine();
        writer.write("rs3LogFileDirectory=" + config.getString(ConfigKeys.RS3_LOG_FILE_DIRECTORY));
        writer.newLine();
        writer.newLine();

        // Persistence Settings
        writer.write("[Persistence]");
        writer.newLine();
        writer.write("enableJson=" + config.getBoolean(ConfigKeys.ENABLE_JSON, false));
        writer.newLine();
        writer.write("enableXml=" + config.getBoolean(ConfigKeys.ENABLE_XML, false));
        writer.newLine();
        writer.write("backupProfiles=" + config.getBoolean(ConfigKeys.BACKUP_PROFILES, false));
        writer.newLine();
        writer.write("backupCharacters=" + config.getBoolean(ConfigKeys.BACKUP_CHARACTERS, false));
        writer.newLine();
        writer.newLine();

        // Logging Settings
        writer.write("[Logging]");
        writer.newLine();
        writer.write("loggingLevel=" + config.getString(ConfigKeys.LOGGING_LEVEL));
        writer.newLine();
        writer.write("logToFile=" + config.getBoolean(ConfigKeys.LOG_TO_FILE, false));
        writer.newLine();
        writer.newLine();
    }
}
