package com.godscape.rs3.cache.backup;

import com.godscape.rs3.enums.core.Rs3Schemas;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.cache.backup.PerformBackup;
import com.godscape.system.enums.Utilities;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Singleton class responsible for backing up RS3 profiles by serializing profiles to JSON and XML files.
 */
public class Rs3PerformBackup {

    private static volatile Rs3PerformBackup instance;
    private final PerformBackup performBackup;
    private final String cacheName;

    private Rs3PerformBackup() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.performBackup = dependencyFactory.getInjection(Utilities.PERFORM_BACKUP);
        this.cacheName = Rs3Schemas.RS3_PROFILE_SCHEMA.toString();
        Logger.info("Rs3PerformBackup: Initialized for cache '{}'.", cacheName);
    }

    public static Rs3PerformBackup getInstance() {
        if (instance == null) {
            synchronized (Rs3PerformBackup.class) {
                if (instance == null) {
                    instance = new Rs3PerformBackup();
                }
            }
        }
        return instance;
    }

    public Map<UUID, Rs3ProfileSchema> getProfileMap(Collection<Rs3ProfileSchema> profiles) {
        return profiles.stream()
                .collect(Collectors.toMap(Rs3ProfileSchema::getProfileId, Function.identity()));
    }

    /**
     * Backs up a single profile to the specified format.
     *
     * @param profile The RS3 profile to back up.
     * @param format  The backup format (e.g., JSON or XML).
     */
    public void backupSingleProfile(Rs3ProfileSchema profile, String format) {
        if (profile != null) {
            Logger.info("Backing up profile '{}' in cache '{}'.", profile.getProfileName(), cacheName);
            performBackup.backupProfiles(Collections.singleton(profile), "path/to/rs3", format);
        } else {
            Logger.warn("Profile is null, cannot perform backup in cache '{}'.", cacheName);
        }
    }

    /**
     * Synchronizes profiles by writing them to files in the specified format.
     *
     * @param profiles The collection of profiles to sync.
     * @param format   The backup format (e.g., JSON or XML).
     */
    public void syncProfilesToFiles(Collection<Rs3ProfileSchema> profiles, String format) {
        if (!profiles.isEmpty()) {
            Logger.info("Starting {} backup of RS3 profiles for cache '{}'.", format.toUpperCase(), cacheName);
            performBackup.backupProfiles(profiles, "path/to/rs3", format);
        } else {
            Logger.info("No profiles to backup in cache '{}'.", cacheName);
        }
    }
}
