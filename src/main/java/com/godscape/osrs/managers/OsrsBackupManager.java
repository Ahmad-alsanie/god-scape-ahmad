package com.godscape.osrs.managers;

import com.godscape.osrs.cache.backup.OsrsPerformBackup;
import com.godscape.osrs.cache.backup.OsrsPerformRestore;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.interfaces.mark.Saveable;
import com.godscape.system.managers.BaseBackupManager;
import com.godscape.system.utility.Logger;

import java.util.Collection;

/**
 * OsrsBackupManager coordinates backup and restore operations for OSRS profiles.
 */
public class OsrsBackupManager extends BaseBackupManager<OsrsProfileSchema> {

    private static volatile OsrsBackupManager instance;
    private final OsrsPerformBackup performBackup;
    private final OsrsPerformRestore performRestore;

    private OsrsBackupManager() {
        super();
        this.performBackup = OsrsPerformBackup.getInstance();
        this.performRestore = OsrsPerformRestore.getInstance();
        Logger.info("OsrsBackupManager: Initialized and ready to manage backup and restore operations.");
    }

    public static OsrsBackupManager getInstance() {
        if (instance == null) {
            synchronized (OsrsBackupManager.class) {
                if (instance == null) {
                    instance = new OsrsBackupManager();
                }
            }
        }
        return instance;
    }

    public void backupProfiles(Collection<OsrsProfileSchema> profiles, String targetDirectory, String format) {
        Logger.info("OsrsBackupManager: Starting backup of {} profiles.", profiles.size());
        super.backupProfiles(profiles, targetDirectory, format);
        Logger.info("OsrsBackupManager: Backup process completed successfully.");
    }
}
