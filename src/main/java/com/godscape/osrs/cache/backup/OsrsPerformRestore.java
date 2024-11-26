package com.godscape.osrs.cache.backup;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.cache.backup.PerformRestore;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Facade class responsible for restoring OSRS profiles from backup files.
 */
public class OsrsPerformRestore {

    private static volatile OsrsPerformRestore instance;
    private final PerformRestore performRestore;

    private OsrsPerformRestore() {
        this.performRestore = PerformRestore.getInstance();
        Logger.info("OsrsPreformRestore: Initialized for OSRS profile restore operations.");
    }

    /**
     * Retrieves the singleton instance of OsrsPreformRestore.
     *
     * @return The singleton instance.
     */
    public static OsrsPerformRestore getInstance() {
        if (instance == null) {
            synchronized (OsrsPerformRestore.class) {
                if (instance == null) {
                    instance = new OsrsPerformRestore();
                }
            }
        }
        return instance;
    }

    /**
     * Restores OSRS profiles from a specified backup file in the given format.
     *
     * @param targetDirectory The directory where the backup file is located.
     * @param format          The format of the backup file: "json" or "xml".
     * @return A collection of restored OSRS profiles.
     */
    public Collection<OsrsProfileSchema> restoreProfiles(String targetDirectory, String format) {
        Logger.info("OsrsPreformRestore: Initiating restore for OSRS profiles in {} format.", format.toUpperCase());

        List<?> profiles = performRestore.restoreProfiles(targetDirectory, format);
        if (profiles.isEmpty()) {
            Logger.warn("OsrsPreformRestore: No profiles found in {} format at {}.", format.toUpperCase(), targetDirectory);
            return Collections.emptyList();
        }

        Logger.info("OsrsPreformRestore: Successfully restored {} profiles from {} format.", profiles.size(), format.toUpperCase());
        return castToOsrsProfiles(profiles);
    }

    /**
     * Casts a list of profiles to the OsrsProfileSchema type.
     *
     * @param profiles List of profiles to cast.
     * @return Collection of OsrsProfileSchema profiles.
     */
    @SuppressWarnings("unchecked")
    private Collection<OsrsProfileSchema> castToOsrsProfiles(List<?> profiles) {
        try {
            return (Collection<OsrsProfileSchema>) profiles;
        } catch (ClassCastException e) {
            Logger.error("OsrsPreformRestore: Failed to cast profiles to OsrsProfileSchema.", e);
            return Collections.emptyList();
        }
    }
}
