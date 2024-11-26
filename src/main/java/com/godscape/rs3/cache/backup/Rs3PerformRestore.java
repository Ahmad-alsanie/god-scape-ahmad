package com.godscape.rs3.cache.backup;

import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.cache.backup.PerformRestore;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Singleton class responsible for restoring RS3 profiles from backup files.
 */
public class Rs3PerformRestore {

    private static volatile Rs3PerformRestore instance;
    private final PerformRestore performRestore;

    private Rs3PerformRestore() {
        this.performRestore = PerformRestore.getInstance();
        Logger.info("Rs3PerformRestore: Initialized for RS3 profile restore operations.");
    }

    /**
     * Retrieves the singleton instance of Rs3PerformRestore.
     *
     * @return The singleton instance.
     */
    public static Rs3PerformRestore getInstance() {
        if (instance == null) {
            synchronized (Rs3PerformRestore.class) {
                if (instance == null) {
                    instance = new Rs3PerformRestore();
                }
            }
        }
        return instance;
    }

    /**
     * Restores RS3 profiles from a specified backup file in the given format.
     *
     * @param targetDirectory The directory where the backup file is located.
     * @param format          The format of the file: "json" or "xml".
     * @return A collection of restored RS3 profiles.
     */
    public Collection<Rs3ProfileSchema> restoreProfiles(String targetDirectory, String format) {
        Logger.info("Rs3PerformRestore: Initiating restore for RS3 profiles in {} format.", format.toUpperCase());

        List<?> restoredProfiles = performRestore.restoreProfiles(targetDirectory, format);

        if (restoredProfiles.isEmpty()) {
            Logger.warn("Rs3PerformRestore: No profiles found in {} format at {}.", format.toUpperCase(), targetDirectory);
            return Collections.emptyList();
        }

        Collection<Rs3ProfileSchema> rs3Profiles = castToRs3Profiles(restoredProfiles);
        Logger.info("Rs3PerformRestore: Successfully restored {} RS3 profiles.", rs3Profiles.size());

        return rs3Profiles;
    }

    /**
     * Casts a list of profiles to the Rs3ProfileSchema type.
     *
     * @param profiles List of profiles to cast.
     * @return Collection of Rs3ProfileSchema profiles.
     */
    @SuppressWarnings("unchecked")
    private Collection<Rs3ProfileSchema> castToRs3Profiles(List<?> profiles) {
        try {
            return (Collection<Rs3ProfileSchema>) profiles;
        } catch (ClassCastException e) {
            Logger.error("Rs3PerformRestore: Failed to cast profiles to Rs3ProfileSchema.", e);
            return Collections.emptyList();
        }
    }
}
