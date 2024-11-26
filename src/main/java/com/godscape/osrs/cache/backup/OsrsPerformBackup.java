package com.godscape.osrs.cache.backup;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.cache.backup.PerformBackup;
import com.godscape.system.utility.Logger;

import java.util.Collection;

/**
 * Facade class responsible for managing OSRS-specific profile backup operations.
 */
public class OsrsPerformBackup {

    private static volatile OsrsPerformBackup instance;
    private final PerformBackup performBackup;

    private OsrsPerformBackup() {
        this.performBackup = PerformBackup.getInstance(); // Access PerformBackup for backup operations
        Logger.info("OsrsPerformBackup: Initialized for OSRS profile backup operations.");
    }

    /**
     * Retrieves the singleton instance of OsrsPerformBackup.
     *
     * @return The singleton instance.
     */
    public static OsrsPerformBackup getInstance() {
        if (instance == null) {
            synchronized (OsrsPerformBackup.class) {
                if (instance == null) {
                    instance = new OsrsPerformBackup();
                }
            }
        }
        return instance;
    }

    /**
     * Backs up OSRS profiles to the specified format and directory.
     *
     * @param profiles        The collection of OSRS profiles to back up.
     * @param targetDirectory The directory where the backup file will be saved.
     * @param format          The format of the backup file: "json" or "xml".
     */
    public void backupProfiles(Collection<OsrsProfileSchema> profiles, String targetDirectory, String format) {
        Logger.info("OsrsPerformBackup: Initiating {} backup for OSRS profiles.", format.toUpperCase());
        performBackup.backupProfiles(profiles, targetDirectory, format); // Perform backup using PerformBackup
    }
}
