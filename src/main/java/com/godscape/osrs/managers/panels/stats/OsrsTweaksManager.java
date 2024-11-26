package com.godscape.osrs.managers.panels.stats;

import com.godscape.osrs.frames.panels.stats.OsrsStatsLevelingTweaksPanel;
import com.godscape.osrs.preloaders.OsrsDefaultProfilePreloader;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;

import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsTweaksManager {

    private final OsrsStatsLevelingTweaksPanel tweaksPanel;
    private final OsrsCacheController cacheController;

    public OsrsTweaksManager(OsrsStatsLevelingTweaksPanel tweaksPanel, OsrsCacheController cacheController) {
        this.tweaksPanel = tweaksPanel;
        this.cacheController = cacheController;
    }

    /**
     * Updates the tweaks settings in the UI panel based on the specified profile ID.
     *
     * @param profileId The UUID of the profile to retrieve tweaks for.
     */
    public void updateTweaks(UUID profileId) {
        if (profileId == null) {
            Logger.warn("OsrsTweaksManager: Profile ID is null. Resetting tweaks settings.");
            tweaksPanel.updateTweaks(null);
            return;
        }

        Logger.info("OsrsTweaksManager: Updating tweaks for profile with ID: {}", profileId);

        OsrsProfileSchema profile = cacheController.getProfile(profileId);
        if (profile == null) {
            Logger.warn("OsrsTweaksManager: Profile not found with ID: {}", profileId);
            tweaksPanel.updateTweaks(null);
            return;
        }

        // Retrieve tweaks settings and update the panel
        Map<String, Object> tweaksSettings = profile.getSetting("statSettings", "tweaks", null);
        tweaksPanel.updateTweaks(tweaksSettings);
    }

    /**
     * Saves the current tweaks from the UI panel to the specified profile.
     *
     * @param profileId The UUID of the profile to save tweaks to.
     */
    public void saveTweaks(UUID profileId) {
        if (profileId == null) {
            Logger.warn("OsrsTweaksManager: Profile ID is null. Cannot save tweaks.");
            return;
        }

        OsrsProfileSchema profile = cacheController.getProfile(profileId);
        if (profile == null) {
            Logger.warn("OsrsTweaksManager: Profile not found with ID: {}", profileId);
            return;
        }

        // Retrieve the updated tweaks from the UI and save to profile
        Map<String, Object> updatedTweaks = tweaksPanel.getCurrentTweaks();
        profile.setSetting("statSettings", "tweaks", updatedTweaks);
        cacheController.updateProfile(profile);

        Logger.info("OsrsTweaksManager: Tweaks saved for profile '{}'.", profile.getProfileName());
    }
}
