// RestoreFromXml.java
package com.godscape.system.cache.backup;

import com.godscape.osrs.utility.OsrsRestoreFromXml;
import com.godscape.rs3.utility.Rs3RestoreFromXml;
import com.godscape.system.enums.Factories;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;
import javafx.application.Platform;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class RestoreFromXml {

    private final GameVersion gameVersion;
    private final OsrsRestoreFromXml osrsRestoreFromXml;
    private final Rs3RestoreFromXml rs3RestoreFromXml;

    public RestoreFromXml() {
        Logger.info("RestoreFromXml: Initializing RestoreFromXml facade...");
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.gameVersion = dependencyFactory.getInjection(PlatformFactory.class).getCurrentGameVersion();
        this.osrsRestoreFromXml = new OsrsRestoreFromXml();
        this.rs3RestoreFromXml = new Rs3RestoreFromXml();
        Logger.info("RestoreFromXml: Facade initialized for game version {}.", gameVersion);
    }

    public List<?> loadProfiles(String targetDirectory) {
        String fileName = gameVersion == GameVersion.OSRS ? "osrs_profiles.xml" : "rs3_profiles.xml";
        String filePath = Paths.get(targetDirectory, fileName).toString();

        if (gameVersion == GameVersion.OSRS) {
            return osrsRestoreFromXml.loadProfiles(filePath);
        } else if (gameVersion == GameVersion.RS3) {
            return rs3RestoreFromXml.loadProfiles(filePath);
        } else {
            Logger.error("RestoreFromXml: Unsupported game version - {}", gameVersion);
            return Collections.emptyList();
        }
    }
}
