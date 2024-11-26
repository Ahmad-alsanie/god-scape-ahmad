package com.godscape.rs3.managers;

import com.godscape.rs3.cache.Rs3ProfileCache;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Saveable;
import com.godscape.system.managers.BaseBackupManager;
import com.godscape.system.utility.Logger;

import java.util.Collection;

/**
 * Manages RS3 backup operations, handling profile data backups in JSON or XML format.
 */
public class Rs3BackupManager extends BaseBackupManager<Rs3ProfileSchema> {

    private static volatile Rs3BackupManager instance;
    private final Rs3ProfileCache profilesCache;

    private Rs3BackupManager() {
        super();
        this.profilesCache = DependencyFactory.getInstance().getInjection(Rs3ProfileCache.class);
        Logger.info("Rs3BackupManager: Initialized.");
    }

    public static Rs3BackupManager getInstance() {
        if (instance == null) {
            synchronized (Rs3BackupManager.class) {
                if (instance == null) {
                    instance = new Rs3BackupManager();
                }
            }
        }
        return instance;
    }

    /**
     * Backs up all cached RS3 profiles in the specified format (JSON or XML).
     *
     * @param targetDirectory The directory where the backup file will be saved.
     * @param format          The format of the backup file, either "json" or "xml".
     */
    public void backupAllProfiles(String targetDirectory, String format) {
        Collection<Rs3ProfileSchema> profiles = profilesCache.getAllProfiles();
        backupProfiles(profiles, targetDirectory, format);
    }
}
