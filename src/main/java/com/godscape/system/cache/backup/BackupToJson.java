package com.godscape.system.cache.backup;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.utility.OsrsBackupToJson;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.rs3.utility.Rs3BackupToJson;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BackupToJson {

    private final GameVersion gameVersion;
    private final OsrsBackupToJson osrsBackupToJson;
    private final Rs3BackupToJson rs3BackupToJson;

    public BackupToJson() {
        Logger.info("BackupToJson: Initializing BackupToJson facade...");

        // Retrieve GameVersion from PlatformFactory
        this.gameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();

        if (gameVersion == null) {
            throw new IllegalStateException("GameVersion not available from PlatformFactory.");
        }

        this.osrsBackupToJson = new OsrsBackupToJson();
        this.rs3BackupToJson = new Rs3BackupToJson();
        Logger.info("BackupToJson: Facade initialized for game version {}.", gameVersion);
    }

    /**
     * Saves profiles to a JSON file based on the detected game version (OSRS or RS3).
     *
     * @param profiles        The collection of profiles to save.
     * @param targetDirectory The directory where the JSON file will be saved.
     */
    public void saveProfiles(Collection<?> profiles, String targetDirectory) {
        if (profiles == null || profiles.isEmpty()) {
            Logger.warn("BackupToJson: No profiles to save.");
            return;
        }

        switch (gameVersion) {
            case OSRS:
                osrsBackupToJson.saveProfiles((Collection<OsrsProfileSchema>) profiles, targetDirectory);
                break;
            case RS3:
                rs3BackupToJson.saveProfiles((Collection<Rs3ProfileSchema>) profiles, targetDirectory);
                break;
            default:
                Logger.error("BackupToJson: Unsupported game version - {}", gameVersion);
        }
    }

    /**
     * Loads profiles from a JSON file based on the detected game version (OSRS or RS3).
     *
     * @param targetDirectory The directory where the JSON file is located.
     * @return The list of profiles loaded.
     */
    public List<?> loadProfiles(String targetDirectory) {
        switch (gameVersion) {
            case OSRS:
                return osrsBackupToJson.loadProfiles(targetDirectory);
            case RS3:
                return rs3BackupToJson.loadProfiles(targetDirectory);
            default:
                Logger.error("BackupToJson: Unsupported game version - {}", gameVersion);
                return Collections.emptyList();  // For Java 8 compatibility
        }
    }
}
