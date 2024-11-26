package com.godscape.system.cache.backup;

import com.godscape.osrs.utility.OsrsBackupToXml;
import com.godscape.rs3.utility.Rs3BackupToXml;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;

public class BackupToXml {

    private final GameVersion gameVersion;
    private final OsrsBackupToXml osrsBackupToXml;
    private final Rs3BackupToXml rs3BackupToXml;

    public BackupToXml() {
        Logger.info("BackupToXml: Initializing BackupToXml facade...");

        // Retrieve GameVersion through PlatformFactory
        this.gameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();

        if (gameVersion == null) {
            throw new IllegalStateException("GameVersion not available from PlatformFactory.");
        }

        this.osrsBackupToXml = new OsrsBackupToXml();
        this.rs3BackupToXml = new Rs3BackupToXml();
        Logger.info("BackupToXml: Facade initialized for game version {}.", gameVersion);
    }

    /**
     * Saves profiles to an XML file based on the detected game version (OSRS or RS3).
     *
     * @param profiles        The collection of profiles to save.
     * @param targetDirectory The directory where the XML file will be saved.
     */
    public void saveProfiles(Collection<?> profiles, String targetDirectory) {
        if (profiles == null || profiles.isEmpty()) {
            Logger.warn("BackupToXml: No profiles to save.");
            return;
        }

        switch (gameVersion) {
            case OSRS:
                osrsBackupToXml.saveProfiles(profiles, targetDirectory);
                break;
            case RS3:
                rs3BackupToXml.saveProfiles(profiles, targetDirectory);
                break;
            default:
                Logger.error("BackupToXml: Unsupported game version - {}", gameVersion);
        }
    }
}
