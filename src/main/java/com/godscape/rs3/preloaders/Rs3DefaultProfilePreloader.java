package com.godscape.rs3.preloaders;

import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.utility.Logger;
import com.godscape.system.interfaces.mark.Preloadable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles the creation of a default RS3 profile.
 */
public class Rs3DefaultProfilePreloader implements Preloadable {

    /**
     * Public no-argument constructor.
     */
    public Rs3DefaultProfilePreloader() {
        Logger.info("Rs3DefaultProfilePreloader: Initialized.");
    }

    /**
     * Preloads the default RS3 profile by creating it.
     */
    @Override
    public void preload() {
        createDefaultProfile();
    }

    /**
     * Creates the default RS3 profile.
     *
     * @return A new Rs3ProfileSchema representing the default profile.
     */
    public Rs3ProfileSchema createDefaultProfile() {
        Logger.info("Rs3DefaultProfilePreloader: Creating default profile.");

        String profileName = "Default";
        UUID profileId = UUID.randomUUID();

        Map<String, Object> defaultSettings = new HashMap<>();
        defaultSettings.put("membership", true);
        defaultSettings.put("mode", "Standard");

        return new Rs3ProfileSchema(
                profileId,
                profileName,
                defaultSettings,
                "Default RS3 profile",
                System.currentTimeMillis()
        );
    }
}
