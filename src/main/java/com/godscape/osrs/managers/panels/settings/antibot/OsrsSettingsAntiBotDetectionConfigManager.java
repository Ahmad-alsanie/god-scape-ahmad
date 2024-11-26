package com.godscape.osrs.managers.panels.settings.antibot;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.utility.Logger;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.osrs.controllers.OsrsSettingsController;

import java.util.UUID;

public class OsrsSettingsAntiBotDetectionConfigManager {

    private final OsrsSettingsController settingsController;

    public OsrsSettingsAntiBotDetectionConfigManager() {
        this.settingsController = DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);
        Logger.info("OsrsSettingsAntiBotDetectionConfigManager initialized.");
    }

    /**
     * Retrieves the current active profile ID, handling potential casting issues.
     *
     * @return The UUID of the current profile, or null if none is set or invalid type.
     */
    public UUID getCurrentProfileId() {
        Object profileIdObj = settingsController.getSettingsMap().get("activeProfileId");
        if (profileIdObj instanceof UUID) {
            return (UUID) profileIdObj;
        } else {
            Logger.warn("OsrsSettingsAntiBotDetectionConfigManager: Active profile ID is not of type UUID.");
            return null;
        }
    }
}
