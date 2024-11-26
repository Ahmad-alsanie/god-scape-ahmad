package com.godscape.system.managers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Configs;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.SettingsFactory;
import com.godscape.system.schemas.GlobalSettingsSchema;
import com.godscape.system.utility.Logger;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.PlatformFactory;

@Singleton
public class ConfigManager {

    private static volatile ConfigManager instance;
    private final SettingsFactory settingsFactory;
    private final GlobalSettingsSchema globalSettings;
    private final PlatformFactory platformFactory;

    // Private constructor to enforce singleton
    private ConfigManager() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.settingsFactory = dependencyFactory.getInjection(SettingsFactory.class);
        this.globalSettings = dependencyFactory.getInjection(GlobalSettingsSchema.class);
        this.platformFactory = dependencyFactory.getInjection(PlatformFactory.class);
        Logger.info("ConfigManager: Initialized with ConfigFactory and GlobalSettingsSchema.");
    }

    /**
     * Retrieves a configuration instance based on the requested Configs enum.
     *
     * @param config The configuration type requested.
     * @return The configuration instance as an Object (to be cast as needed).
     */
    public Object getConfig(Configs config) {
        Logger.info("ConfigManager: Retrieving configuration for {}", config.name());
        return settingsFactory.getConfig(config);
    }

    /**
     * Gets the JSON path for profiles based on the current game version.
     *
     * @return The JSON path for OSRS or RS3 profiles.
     */
    public String getProfilesJsonPath() {
        GameVersion gameVersion = platformFactory.getCurrentGameVersion();
        switch (gameVersion) {
            case OSRS:
                return globalSettings.getOsrsProfilesJsonPath();
            case RS3:
                return globalSettings.getRs3ProfilesJsonPath();
            default:
                Logger.warn("ConfigManager: Unrecognized game version. Returning null for profiles JSON path.");
                return null;
        }
    }

    /**
     * Gets the XML path for profiles based on the current game version.
     *
     * @return The XML path for OSRS or RS3 profiles.
     */
    public String getProfilesXmlPath() {
        GameVersion gameVersion = platformFactory.getCurrentGameVersion();
        switch (gameVersion) {
            case OSRS:
                return globalSettings.getOsrsProfilesXmlPath();
            case RS3:
                return globalSettings.getRs3ProfilesXmlPath();
            default:
                Logger.warn("ConfigManager: Unrecognized game version. Returning null for profiles XML path.");
                return null;
        }
    }

    /**
     * Retrieves the database filename based on the current game version.
     *
     * @return The database filename for OSRS or RS3.
     */
    public String getDatabaseFilename() {
        GameVersion gameVersion = platformFactory.getCurrentGameVersion();
        switch (gameVersion) {
            case OSRS:
                return globalSettings.getOsrsDatabaseFilename();
            case RS3:
                return globalSettings.getRs3DatabaseFilename();
            default:
                Logger.warn("ConfigManager: Unrecognized game version. Returning null for database filename.");
                return null;
        }
    }
}