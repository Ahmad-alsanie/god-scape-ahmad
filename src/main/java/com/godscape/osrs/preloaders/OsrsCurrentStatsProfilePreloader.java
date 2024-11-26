package com.godscape.osrs.preloaders;

import com.godscape.osrs.cache.OsrsProfileCache;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Preloadable;
import com.godscape.system.utility.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsCurrentStatsProfilePreloader implements Preloadable {

    private final OsrsProfileCache osrsProfilesCache;
    private final OsrsDefaultProfilePreloader defaultProfilePreloader;

    private OsrsCurrentStatsProfilePreloader() {
        this.osrsProfilesCache = DependencyFactory.getInstance().getInjection(OsrsProfileCache.class);
        this.defaultProfilePreloader = DependencyFactory.getInstance().getInjection(OsrsDefaultProfilePreloader.class);
        Logger.info("OsrsCurrentStatsProfilePreloader: Initialized.");
    }

    @Override
    public void preload() {
        Logger.info("OsrsCurrentStatsProfilePreloader: Preloading current stats profile.");
        loadCurrentStatsProfile();
    }

    public void loadCurrentStatsProfile() {
        Logger.info("OsrsCurrentStatsProfilePreloader: Loading current stats profile.");

        String profileName = "Current Stats";
        UUID profileId = UUID.nameUUIDFromBytes(profileName.getBytes());

        if (osrsProfilesCache.getProfile(profileId) == null) {
            Map<String, Object> settings = new HashMap<>(defaultProfilePreloader.getDefaultSettingsMap());
            settings.put("is_current", true);

            OsrsProfileSchema currentProfile = new OsrsProfileSchema(profileId, profileName, settings, System.currentTimeMillis());
            osrsProfilesCache.addProfile(currentProfile);
            Logger.info("OsrsCurrentStatsProfilePreloader: Added current stats profile.");
        }
    }
}
