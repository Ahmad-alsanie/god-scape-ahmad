package com.godscape.rs3.controllers;

import com.godscape.rs3.cache.backup.Rs3PerformBackup;
import com.godscape.rs3.cache.backup.Rs3PerformRestore;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.UUID;

/**
 * Rs3BackupController manages RS3-specific backup and restore operations by delegating to Rs3PerformBackup and Rs3PerformRestore.
 */
public class Rs3BackupController {

    private static volatile Rs3BackupController instance;
    private final Rs3PerformBackup performBackup;
    private final Rs3PerformRestore performRestore;

    private Rs3BackupController() {
        this.performBackup = DependencyFactory.getInstance().getInjection(Rs3PerformBackup.class);
        this.performRestore = DependencyFactory.getInstance().getInjection(Rs3PerformRestore.class);
        Logger.info("Rs3BackupController: Initialized for RS3 backup and restore operations.");
    }

    public static Rs3BackupController getInstance() {
        if (instance == null) {
            synchronized (Rs3BackupController.class) {
                if (instance == null) {
                    instance = new Rs3BackupController();
                }
            }
        }
        return instance;
    }

    public void backupProfiles(Collection<Rs3ProfileSchema> profiles, String targetDirectory, String format) {
        Logger.info("Rs3BackupController: Initiating backup for RS3 profiles.");
        performBackup.syncProfilesToFiles(profiles, format);
    }

    public Collection<Rs3ProfileSchema> restoreProfiles(String targetDirectory, String format) {
        Logger.info("Rs3BackupController: Initiating restore for RS3 profiles.");
        return performRestore.restoreProfiles(targetDirectory, format);
    }

    public void backupSingleProfile(Rs3ProfileSchema profile, String targetDirectory, String format) {
        Logger.info("Rs3BackupController: Backing up single RS3 profile '{}'.", profile.getProfileName());
        performBackup.backupSingleProfile(profile, format);
    }

    public void shutdown() {
        Logger.info("Rs3BackupController: Shutting down RS3 backup and restore operations.");
    }
}
