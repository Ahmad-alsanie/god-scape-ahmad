package com.godscape.system.managers;

import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.schemas.GlobalSettingsSchema;
import java.util.Properties;

public class GlobalSettingsManager {

    public static GlobalSettingsManager getInjection() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        return dependencyFactory.getInjection(GlobalSettingsManager.class);
    }

    public void applySettingsFromProperties(Properties props) {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        GameVersion gameVersion = dependencyFactory.getInjection(GameVersion.class);
        GlobalSettingsSchema schema = GlobalSettingsSchema.getInstance();

        if (gameVersion == GameVersion.OSRS) {
            schema.setDatabaseFilename(props.getProperty("osrsDatabaseFilename", ""));
            schema.setProfilesXmlPath(props.getProperty("osrsProfilesXmlPath", ""));
            schema.setProfilesJsonPath(props.getProperty("osrsProfilesJsonPath", ""));
            schema.setLogFileDirectory(props.getProperty("osrsLogFileDirectory", ""));
            schema.setPreloadProfiles(Boolean.parseBoolean(props.getProperty("osrsPreloadProfiles", "false")));
        } else if (gameVersion == GameVersion.RS3) {
            schema.setDatabaseFilename(props.getProperty("rs3DatabaseFilename", ""));
            schema.setProfilesXmlPath(props.getProperty("rs3ProfilesXmlPath", ""));
            schema.setProfilesJsonPath(props.getProperty("rs3ProfilesJsonPath", ""));
            schema.setLogFileDirectory(props.getProperty("rs3LogFileDirectory", ""));
            schema.setPreloadProfiles(Boolean.parseBoolean(props.getProperty("rs3PreloadProfiles", "false")));
        }
    }

    public boolean getPreloadProfilesForCurrentGame() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        GameVersion gameVersion = dependencyFactory.getInjection(GameVersion.class);
        GlobalSettingsSchema schema = GlobalSettingsSchema.getInstance();
        return gameVersion == GameVersion.OSRS ? schema.isPreloadProfiles() : schema.isPreloadProfiles();
    }
}
