package com.godscape.rs3.cache;

import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.enums.Observations;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.observers.ProfileChangeListener;
import com.godscape.system.utility.Logger;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class Rs3ProfileCache {
    private static volatile Rs3ProfileCache instance;
    private final IMap<UUID, Rs3ProfileSchema> profileCache;

    private Rs3ProfileCache() {
        HazelcastInstance hazelcastInstance = DependencyFactory.getInstance().getInjection(HazelcastConfig.class).getHazelcastInstance();
        this.profileCache = hazelcastInstance.getMap("rs3ProfileCache");
        initializeCacheListeners();
    }

    public static Rs3ProfileCache getInstance() {
        if (instance == null) {
            synchronized (Rs3ProfileCache.class) {
                if (instance == null) {
                    instance = new Rs3ProfileCache();
                }
            }
        }
        return instance;
    }

    private void initializeCacheListeners() {
        ProfileChangeListener profileChangeListener = DependencyFactory.getInstance().getInjection(Observations.PROFILE_CHANGE_LISTENER);
        profileCache.addEntryListener(profileChangeListener, true);
    }

    private UUID getProfileId(Rs3ProfileSchema profile) {
        return profile.getProfileId();
    }

    private void ensureProfileId(Rs3ProfileSchema profile) {
        if (profile.getProfileId() == null) {
            profile.setProfileId(UUID.randomUUID());
            Logger.info("Rs3ProfileCache: Assigned new UUID to profile '{}'.", profile.getProfileName());
        }
    }

    private String getProfileName(Rs3ProfileSchema profile) {
        return profile.getProfileName();
    }

    private void normalizeSettings(Rs3ProfileSchema profile) {
        if (profile.getSettingsMap() == null) {
            Logger.warn("Rs3ProfileCache: 'settingsMap' is null in profile '{}'. Skipping normalization.", profile.getProfileName());
            return;
        }
        normalizeMap(profile.getSettingsMap());
    }

    private void normalizeMap(Map<String, Object> map) {
        map.forEach((key, value) -> {
            if (value instanceof Number) {
                map.put(key, ((Number) value).intValue());
            } else if (value instanceof String) {
                try {
                    map.put(key, Integer.parseInt((String) value));
                } catch (NumberFormatException ignored) {}
            } else if (value instanceof Map) {
                normalizeMap((Map<String, Object>) value);
            }
        });
    }

    public void addProfile(Rs3ProfileSchema profile) {
        ensureProfileId(profile);
        normalizeSettings(profile);
        profileCache.put(getProfileId(profile), profile);
        Logger.info("Profile added to cache with ID: '{}'", getProfileId(profile));
    }

    public void updateProfile(Rs3ProfileSchema profile) {
        ensureProfileId(profile);
        normalizeSettings(profile);
        profileCache.put(getProfileId(profile), profile);
        Logger.info("Profile updated in cache with ID: '{}'", getProfileId(profile));
    }

    public void updateProfileInCache(Rs3ProfileSchema profile) {
        if (profile != null) {
            ensureProfileId(profile);
            normalizeSettings(profile);
            profileCache.put(getProfileId(profile), profile);
            Logger.info("Profile updated in cache with ID: '{}'", getProfileId(profile));
        } else {
            Logger.warn("Profile is null, cannot update in cache.");
        }
    }

    public void removeProfile(UUID profileId) {
        if (profileId == null) return;
        profileCache.remove(profileId);
        Logger.info("Profile removed from cache with ID: '{}'", profileId);
    }

    public void removeProfileFromCache(UUID profileId) {
        if (profileId != null) {
            profileCache.remove(profileId);
            Logger.info("Profile removed from cache with ID: '{}'", profileId);
        } else {
            Logger.warn("Profile ID is null, cannot remove from cache.");
        }
    }

    public Rs3ProfileSchema getProfile(UUID profileId) {
        return profileCache.get(profileId);
    }

    /**
     * Retrieves all profiles from the cache.
     *
     * @return A collection of all Rs3ProfileSchema profiles from the cache.
     */
    public Collection<Rs3ProfileSchema> getAllProfiles() {
        return profileCache.values();
    }

    /**
     * Clears all profiles from the cache.
     */
    public void clearProfileCache() {
        profileCache.clear();
        Logger.info("Cleared cache 'rs3ProfileCache'.");
    }

    public void shutdownCache() {
        HazelcastInstance hazelcastInstance = DependencyFactory.getInstance().getInjection(HazelcastConfig.class).getHazelcastInstance();
        hazelcastInstance.shutdown();
        Logger.info("Cache 'rs3ProfileCache' shutdown completed.");
    }
}
