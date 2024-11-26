package com.godscape.rs3.controllers;

import com.godscape.rs3.cache.Rs3ProfileCache;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import com.godscape.system.annotations.Singleton;

import java.util.Collection;
import java.util.UUID;

/**
 * Rs3ProfileController is a facade for interacting with Rs3ProfileCache, handling CRUD operations for RS3 profiles.
 */
@Singleton
public class Rs3ProfileController {

    private Rs3ProfileCache profileCache;

    /**
     * Public no-argument constructor.
     * Dependency initialization is deferred to lazy initialization using getInjection.
     */
    public Rs3ProfileController() {
        Logger.info("Rs3ProfileController: Initialized.");
    }

    /**
     * Lazy initialization for profileCache dependency using DependencyFactory.
     *
     * @return The Rs3ProfileCache instance.
     */
    private Rs3ProfileCache getProfileCache() {
        if (profileCache == null) {
            profileCache = DependencyFactory.getInstance().getInjection(Rs3ProfileCache.class);
            if (profileCache == null) {
                Logger.error("Rs3ProfileController: Failed to inject Rs3ProfileCache.");
                // Handle error appropriately, possibly throw an exception or terminate
            }
        }
        return profileCache;
    }

    /**
     * Adds a profile to the cache.
     *
     * @param profile The RS3 profile to add.
     * @return true if added, false if it already exists.
     */
    public boolean addProfile(Rs3ProfileSchema profile) {
        Rs3ProfileCache cache = getProfileCache();
        if (profile == null || cache.getProfile(profile.getProfileId()) != null) {
            Logger.warn("Cannot add profile; profile is null or already exists.");
            return false;
        }
        cache.addProfile(profile);
        Logger.info("Profile '{}' added to cache.", profile.getProfileName());
        return true;
    }

    /**
     * Updates an existing profile in the cache.
     *
     * @param profile The RS3 profile to update.
     * @return true if updated, false if it doesn't exist.
     */
    public boolean updateProfile(Rs3ProfileSchema profile) {
        Rs3ProfileCache cache = getProfileCache();
        if (profile == null || cache.getProfile(profile.getProfileId()) == null) {
            Logger.warn("Cannot update profile; profile is null or does not exist.");
            return false;
        }
        cache.updateProfile(profile);
        Logger.info("Profile '{}' updated in cache.", profile.getProfileName());
        return true;
    }

    /**
     * Deletes a profile from the cache by UUID.
     *
     * @param profileId The UUID of the profile to delete.
     * @return true if deleted, false if it doesn't exist.
     */
    public boolean deleteProfile(UUID profileId) {
        Rs3ProfileCache cache = getProfileCache();
        if (profileId == null || cache.getProfile(profileId) == null) {
            Logger.warn("Cannot delete profile; profile ID is null or does not exist.");
            return false;
        }
        cache.removeProfile(profileId);
        Logger.info("Profile with ID '{}' deleted from cache.", profileId);
        return true;
    }

    /**
     * Retrieves all profiles from the cache.
     *
     * @return A collection of RS3 profiles in the cache.
     */
    public Collection<Rs3ProfileSchema> getAllProfiles() {
        Logger.info("Retrieving all RS3 profiles from cache.");
        return getProfileCache().getAllProfiles();
    }

    /**
     * Retrieves a profile from the cache by UUID.
     *
     * @param profileId The UUID of the profile to retrieve.
     * @return The profile, or null if it doesn't exist.
     */
    public Rs3ProfileSchema getProfile(UUID profileId) {
        Logger.info("Retrieving profile with ID '{}'.", profileId);
        return getProfileCache().getProfile(profileId);
    }

    /**
     * Clears all profiles from the cache.
     */
    public void clearCache() {
        getProfileCache().clearProfileCache();
        Logger.info("All profiles cleared from cache.");
    }
}
