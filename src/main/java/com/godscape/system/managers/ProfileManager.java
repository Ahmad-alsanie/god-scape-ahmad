package com.godscape.system.managers;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Data
@Singleton
public class ProfileManager {

    private static volatile ProfileManager instance;

    private final CacheManager cacheManager;
    private final GameVersion currentGameVersion;

    private ProfileManager() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.cacheManager = dependencyFactory.getInjection(CacheManager.class);
        this.currentGameVersion = dependencyFactory.getInjection(GameVersion.class);
        Logger.info("ProfileManager initialized for game version: {}", currentGameVersion);
    }

    public static ProfileManager getInstance() {
        if (instance == null) {
            synchronized (ProfileManager.class) {
                if (instance == null) {
                    Logger.info("ProfileManager: Creating Singleton instance...");
                    instance = new ProfileManager();
                }
            }
        }
        return instance;
    }

    public void addOrUpdateProfile(Object profile) {
        if (profile == null) {
            Logger.error("ProfileManager: Cannot add or update a null profile.");
            return;
        }

        UUID profileId = extractProfileId(profile);

        if (profileId == null) {
            Logger.error("ProfileManager: Profile must have a valid 'profileId'.");
            return;
        }

        cacheManager.updateProfileInCache(profile);
        Logger.info("ProfileManager: Profile with ID '{}' added/updated in cache.", profileId);
    }

    public void removeProfile(UUID profileId) {
        if (profileId == null) {
            Logger.error("ProfileManager: 'profileId' cannot be null.");
            return;
        }

        cacheManager.removeProfile(currentGameVersion, profileId);
        Logger.info("ProfileManager: Profile with ID '{}' removed from cache for {}.", profileId, currentGameVersion);
    }

    public Object getProfileFromCache(UUID profileId) {
        if (profileId == null) {
            Logger.error("ProfileManager: 'profileId' cannot be null.");
            return null;
        }

        Object profile = cacheManager.getProfile(currentGameVersion, profileId);
        if (profile != null) {
            Logger.info("ProfileManager: Profile with ID '{}' retrieved from cache.", profileId);
        } else {
            Logger.warn("ProfileManager: Profile with ID '{}' not found in cache.", profileId);
        }

        return profile;
    }

    public Collection<?> getAllProfiles() {
        Map<UUID, ?> profilesMap = cacheManager.getCachedProfiles(currentGameVersion);
        if (profilesMap == null || profilesMap.isEmpty()) {
            Logger.warn("ProfileManager: No profiles found in cache.");
            return Collections.emptyList();
        }

        Logger.info("ProfileManager: Retrieved {} profiles from cache.", profilesMap.size());
        return profilesMap.values();
    }

    private UUID extractProfileId(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            return ((OsrsProfileSchema) profile).getProfileId();
        } else if (profile instanceof Rs3ProfileSchema) {
            return ((Rs3ProfileSchema) profile).getProfileId();
        } else {
            Logger.error("ProfileManager: Unsupported profile type: {}", profile.getClass().getName());
            return null;
        }
    }
}
