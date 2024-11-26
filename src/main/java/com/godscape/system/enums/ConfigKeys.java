package com.godscape.system.enums;

import lombok.Getter;

@Getter
public enum ConfigKeys {

    // General Configuration
    SAVE_DIRECTORY("saveDirectory", ""),
    CREATE_MISSING_FILES("createMissingFiles", ""),

    // OSRS Configuration
    OSRS_SAVE_DIRECTORY("osrsSaveDirectory", "osrs"),
    OSRS_DATABASE_FILENAME("osrsDatabaseFilename", "osrs", "database.db"),
    OSRS_PROFILES_JSON("osrsProfilesJson", "osrs", "profiles.json"),
    OSRS_PROFILES_XML("osrsProfilesXml", "osrs", "profiles.xml"),
    OSRS_LOG_FILE_DIRECTORY("osrsLogFileDirectory", "osrs", "logs"),
    OSRS_PRELOAD_PROFILES("osrsPreloadProfiles", "osrs"),  // Missing variable added

    // RS3 Configuration
    RS3_SAVE_DIRECTORY("rs3SaveDirectory", "rs3"),
    RS3_DATABASE_FILENAME("rs3DatabaseFilename", "rs3", "database.db"),
    RS3_PROFILES_JSON("rs3ProfilesJson", "rs3", "profiles.json"),
    RS3_PROFILES_XML("rs3ProfilesXml", "rs3", "profiles.xml"),
    RS3_LOG_FILE_DIRECTORY("rs3LogFileDirectory", "rs3", "logs"),
    RS3_PRELOAD_PROFILES("rs3PreloadProfiles", "rs3"),  // Missing variable added

    // Persistence Settings
    ENABLE_JSON("enableJson", ""),
    ENABLE_XML("enableXml", ""),
    BACKUP_PROFILES("backupProfiles", ""),
    BACKUP_CHARACTERS("backupCharacters", ""),

    // Logging Configuration
    LOGGING_LEVEL("loggingLevel", ""),
    LOG_TO_FILE("logToFile", ""),

    // Additional Keys
    PRELOAD_PROFILES("preloadProfiles", ""),
    LAST_UPDATED_TIMESTAMP("lastUpdatedTimestamp", "");

    private final String key;
    private final String[] subpath;

    ConfigKeys(String key, String... subpath) {
        this.key = key;
        this.subpath = subpath;
    }
}
