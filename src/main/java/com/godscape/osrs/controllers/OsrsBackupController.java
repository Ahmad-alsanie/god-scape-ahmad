package com.godscape.osrs.controllers;

import com.godscape.osrs.cache.backup.OsrsPerformBackup;
import com.godscape.osrs.cache.backup.OsrsPerformRestore;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.utility.Logger;

import java.util.Collection;

/**
 * OsrsBackupController manages OSRS-specific backup and restore operations by
 * using OsrsPerformBackup and OsrsPreformRestore for profile management.
 */
public class OsrsBackupController {

    private final OsrsPerformBackup performBackup;
    private final OsrsPerformRestore performRestore;

    public OsrsBackupController() {
        this.performBackup = OsrsPerformBackup.getInstance();
        this.performRestore = OsrsPerformRestore.getInstance();
        Logger.info("OsrsBackupController: Initialized for OSRS backup and restore management.");
    }

    /**
     * Backs up OSRS profiles by using OsrsPerformBackup.
     *
     * @param profiles        The collection of OSRS profiles to back up.
     * @param targetDirectory The directory for the backup.
     * @param format          The format of the backup file: "json" or "xml".
     */
    public void backupProfiles(Collection<OsrsProfileSchema> profiles, String targetDirectory, String format) {
        Logger.info("OsrsBackupController: Initiating backup for OSRS profiles.");
        performBackup.backupProfiles(profiles, targetDirectory, format);
    }

    /**
     * Restores OSRS profiles by using OsrsPreformRestore.
     *
     * @param targetDirectory The directory of the backup file.
     * @param format          The format of the backup file: "json" or "xml".
     * @return A collection of restored OSRS profiles.
     */
    public Collection<OsrsProfileSchema> restoreProfiles(String targetDirectory, String format) {
        Logger.info("OsrsBackupController: Initiating restore for OSRS profiles.");
        return performRestore.restoreProfiles(targetDirectory, format);
    }
}
