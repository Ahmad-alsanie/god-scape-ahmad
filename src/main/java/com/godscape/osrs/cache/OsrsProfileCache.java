package com.godscape.osrs.cache;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.observers.ProfileChangeListener;
import com.godscape.system.utility.Logger;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.godscape.osrs.enums.core.OsrsSchemas;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsProfileCache {

    private IMap<UUID, OsrsProfileSchema> profileCache;
    private HazelcastInstance hazelcastInstance;

    public OsrsProfileCache() {
        Logger.info("OsrsProfileCache: Initialized.");
    }

    /**
     * Retrieves the Hazelcast IMap for profiles, initializing it if necessary.
     *
     * @return The IMap instance for profile caching.
     */
    private IMap<UUID, OsrsProfileSchema> getProfileCache() {
        if (profileCache == null) {
            Logger.info("Initializing Hazelcast instance and profile cache.");
            hazelcastInstance = DependencyFactory.getInstance().getInjection(HazelcastConfig.class).getHazelcastInstance();
            String cacheName = OsrsSchemas.OSRS_PROFILE_SCHEMA.getCacheName();
            profileCache = hazelcastInstance.getMap(cacheName);
            initializeCacheListeners(DependencyFactory.getInstance().getInjection(ProfileChangeListener.class));
            configureCache(cacheName);
        }
        return profileCache;
    }

    /**
     * Configures the Hazelcast cache settings.
     *
     * @param cacheName The name of the cache to configure.
     */
    private void configureCache(String cacheName) {
        hazelcastInstance.getConfig().getMapConfig(cacheName)
                .setTimeToLiveSeconds(3600) // 1 hour TTL
                .setEvictionConfig(new com.hazelcast.config.EvictionConfig()
                        .setEvictionPolicy(com.hazelcast.config.EvictionPolicy.LRU)
                        .setSize(1000)); // Max 1000 entries
    }

    /**
     * Initializes cache listeners for profile changes.
     *
     * @param profileChangeListener The listener to add.
     */
    private void initializeCacheListeners(ProfileChangeListener profileChangeListener) {
        if (profileChangeListener != null) {
            getProfileCache().addEntryListener(profileChangeListener, true);
            Logger.info("Profile change listener added to cache.");
        } else {
            Logger.warn("ProfileChangeListener is null. No listeners added to cache.");
        }
    }

    /**
     * Ensures that the profile has a unique UUID. If not, assigns a new one.
     *
     * @param profile The profile to check and update.
     */
    private void ensureProfileId(OsrsProfileSchema profile) {
        if (profile.getProfileId() == null) {
            profile.setProfileId(UUID.randomUUID());
            Logger.info("Assigned new UUID to profile '{}'.", profile.getProfileName());
        }
    }

    /**
     * Adds a new profile to the cache.
     *
     * @param profile The profile to add.
     * @return True if added successfully, false if a profile with the same name exists.
     */
    public boolean addProfile(OsrsProfileSchema profile) {
        if (profile == null) {
            Logger.warn("Cannot add a null profile.");
            return false;
        }

        OsrsProfileSchema existingProfile = getProfileByName(profile.getProfileName());
        if (existingProfile != null) {
            Logger.warn("Profile with name '{}' already exists with ID: '{}'. Not adding a new profile.", profile.getProfileName(), existingProfile.getProfileId());
            return false;
        }

        ensureProfileId(profile);

        // Directly use the flat settingsMap without normalization
        // Ensure settingsMap is initialized and flat
        if (profile.getSettingsMap() == null) {
            profile.setSettingsMap(new HashMap<>());
        }

        // No flattening or unflattening since settingsMap is already flat
        if (getProfileCache().putIfAbsent(profile.getProfileId(), profile) == null) {
            Logger.info("Profile added to cache with ID: '{}' and name: '{}'", profile.getProfileId(), profile.getProfileName());
            return true;
        } else {
            Logger.warn("Profile with ID: '{}' and name: '{}' already exists in cache.", profile.getProfileId(), profile.getProfileName());
            return false;
        }
    }

    /**
     * Retrieves a profile by its name.
     *
     * @param profileName The name of the profile.
     * @return The profile if found, null otherwise.
     */
    public OsrsProfileSchema getProfileByName(String profileName) {
        if (profileName == null || profileName.trim().isEmpty()) {
            Logger.warn("Cannot retrieve profile: profileName is null or empty.");
            return null;
        }

        return getProfileCache().values().stream()
                .filter(profile -> profileName.equalsIgnoreCase(profile.getProfileName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing profile in the cache.
     *
     * @param profile The profile to update.
     * @return True if updated successfully, false otherwise.
     */
    public boolean updateProfile(OsrsProfileSchema profile) {
        if (profile == null) {
            Logger.warn("Cannot update a null profile.");
            return false;
        }

        ensureProfileId(profile);

        // Directly use the flat settingsMap without normalization
        if (profile.getSettingsMap() == null) {
            profile.setSettingsMap(new HashMap<>());
        }

        getProfileCache().put(profile.getProfileId(), profile);
        Logger.info("Profile updated in cache with ID: '{}' and name: '{}'", profile.getProfileId(), profile.getProfileName());
        return true;
    }

    /**
     * Removes a profile from the cache by its UUID.
     *
     * @param profileId The UUID of the profile to remove.
     * @return True if removed successfully, false otherwise.
     */
    public boolean removeProfile(UUID profileId) {
        if (profileId == null) {
            Logger.warn("Cannot remove profile: UUID is null.");
            return false;
        }
        OsrsProfileSchema removedProfile = getProfileCache().remove(profileId);
        if (removedProfile != null) {
            Logger.info("Profile removed from cache with ID: '{}'", profileId);
            return true;
        } else {
            Logger.warn("Profile with ID '{}' not found in cache. Cannot remove.", profileId);
            return false;
        }
    }

    /**
     * Retrieves a profile by its UUID.
     *
     * @param profileId The UUID of the profile.
     * @return The profile if found, null otherwise.
     */
    public OsrsProfileSchema getProfile(UUID profileId) {
        if (profileId == null) {
            Logger.warn("Cannot retrieve profile: UUID is null.");
            return null;
        }

        OsrsProfileSchema profile = getProfileCache().get(profileId);
        if (profile == null) {
            Logger.warn("Profile with ID '{}' not found in cache.", profileId);
        } else {
            Logger.info("Profile retrieved from cache with ID: '{}' and name: '{}'", profile.getProfileId(), profile.getProfileName());
        }
        return profile;
    }

    /**
     * Retrieves all profiles from the cache.
     *
     * @return A collection of all profiles.
     */
    public Collection<OsrsProfileSchema> getAllProfiles() {
        Collection<OsrsProfileSchema> profiles = getProfileCache().values();
        if (profiles.isEmpty()) {
            Logger.warn("No profiles found in cache.");
        } else {
            Logger.info("Retrieved all profiles from cache: {} profiles found.", profiles.size());
        }
        return profiles;
    }

    /**
     * Adds multiple profiles to the cache.
     *
     * @param profiles The collection of profiles to add.
     */
    public void addProfiles(Collection<OsrsProfileSchema> profiles) {
        if (profiles == null || profiles.isEmpty()) {
            Logger.warn("No profiles provided to add.");
            return;
        }
        profiles.forEach(this::addProfile);
        Logger.info("Bulk profiles added to cache.");
    }

    /**
     * Clears all profiles from the cache.
     */
    public void clearProfileCache() {
        getProfileCache().clear();
        Logger.info("OsrsProfileCache: All profiles cleared from cache.");
    }
}
