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
public class OsrsPremadeProfilesPreloader implements Preloadable {

    private final OsrsProfileCache osrsProfilesCache;
    private final OsrsDefaultProfilePreloader defaultProfilePreloader;

    private OsrsPremadeProfilesPreloader() {
        this.osrsProfilesCache = DependencyFactory.getInstance().getInjection(OsrsProfileCache.class);
        this.defaultProfilePreloader = DependencyFactory.getInstance().getInjection(OsrsDefaultProfilePreloader.class);
        Logger.info("OsrsPremadeProfilesPreloader: Initialized.");
    }

    @Override
    public void preload() {
        Logger.info("OsrsPremadeProfilesPreloader: Preloading premade profiles.");
        loadPremadeProfiles();
    }

    public void loadPremadeProfiles() {
        Logger.info("OsrsPremadeProfilesPreloader: Loading premade profiles.");
        Map<String, Object> baseSettings = defaultProfilePreloader.getDefaultSettingsMap();

        // Example premade profile setup
        String[] premadeProfileNames = {"Warrior", "Mage", "Skiller"};
        for (String profileName : premadeProfileNames) {
            UUID profileId = UUID.nameUUIDFromBytes(profileName.getBytes());

            if (osrsProfilesCache.getProfile(profileId) == null) {
                Map<String, Object> settings = new HashMap<>(baseSettings);

                OsrsProfileSchema profile = new OsrsProfileSchema(profileId, profileName, settings, System.currentTimeMillis());
                osrsProfilesCache.addProfile(profile);
                Logger.info("OsrsPremadeProfilesPreloader: Added premade profile '{}'", profileName);
            }
        }
    }
}
