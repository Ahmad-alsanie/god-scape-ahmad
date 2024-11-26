package com.godscape.system.managers;

import com.godscape.system.cache.backup.PerformBackup;
import com.godscape.system.cache.backup.PerformRestore;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.interfaces.mark.Saveable;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.List;

public abstract class BaseBackupManager<T extends Saveable> {

    private final PerformBackup performBackup;
    private final PerformRestore performRestore;

    protected BaseBackupManager() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.performBackup = dependencyFactory.getInjection(PerformBackup.class);
        this.performRestore = PerformRestore.getInstance(); // Using singleton instance
        Logger.info("BaseBackupManager: Initialized with dependencies.");
    }

    public void backupProfiles(Collection<T> profiles, String targetDirectory, String format) {
        GameVersion gameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();
        Logger.info("BaseBackupManager: Initiating {} backup for {} version.", format.toUpperCase(), gameVersion);

        if (profiles == null || profiles.isEmpty()) {
            Logger.warn("BaseBackupManager: No profiles to back up.");
            return;
        }

        performBackup.backupProfiles(profiles, targetDirectory, format);
    }

    public List<T> restoreProfiles(String targetDirectory, String format) {
        GameVersion gameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();
        Logger.info("BaseBackupManager: Starting {} restore for {} version.", format.toUpperCase(), gameVersion);

        return performRestore.restoreProfiles(targetDirectory, format); // This now works with the generic method
    }
}
