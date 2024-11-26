package com.godscape.system.cache.backup;

import com.godscape.system.enums.Factories;
import com.godscape.system.enums.Utilities;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;

public class PerformBackup {

    private final BackupToJson backupToJson;
    private final BackupToXml backupToXml;
    private final GameVersion gameVersion;

    private static volatile PerformBackup instance;

    private PerformBackup() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();

        // Retrieve GameVersion through dependency injection
        PlatformFactory platformFactory = dependencyFactory.getInjection(Factories.PLATFORM_FACTORY);
        this.gameVersion = dependencyFactory.getInjection(GameVersion.class);

        // Retrieve backup utilities through dependency injection
        this.backupToJson = dependencyFactory.getInjection(Utilities.BACKUP_TO_JSON);
        this.backupToXml = dependencyFactory.getInjection(Utilities.BACKUP_TO_XML);

        Logger.info("PerformBackup: Initialized with GameVersion '{}'.", gameVersion);
    }

    /**
     * Retrieves the singleton instance of PerformBackup.
     *
     * @return The singleton instance.
     */
    public static PerformBackup getInstance() {
        if (instance == null) {
            synchronized (PerformBackup.class) {
                if (instance == null) {
                    instance = new PerformBackup();
                }
            }
        }
        return instance;
    }

    /**
     * Backs up profiles to a JSON or XML file based on the format specified.
     *
     * @param profiles        The collection of profiles to back up.
     * @param targetDirectory The directory where the file will be saved.
     * @param format          The format of the file: "json" or "xml".
     */
    public void backupProfiles(Collection<?> profiles, String targetDirectory, String format) {
        Logger.info("PerformBackup: Starting backup for {} format and {} game version.", format.toUpperCase(), gameVersion);

        switch (format.toLowerCase()) {
            case "json":
                backupToJson.saveProfiles(profiles, targetDirectory);
                break;
            case "xml":
                backupToXml.saveProfiles(profiles, targetDirectory);
                break;
            default:
                Logger.error("PerformBackup: Unsupported format '{}'. Only 'json' and 'xml' are supported.", format);
        }
    }
}
