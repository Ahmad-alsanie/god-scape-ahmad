package com.godscape.system.interfaces.fuse;

import java.util.Collection;
import java.util.UUID;

public interface ProfileFuse<T> {

    /**
     * Updates a profile in the cache.
     *
     * @param profile the profile to update.
     */
    void updateProfileInCache(T profile);

    /**
     * Adds a collection of profiles to the cache.
     *
     * @param profiles the profiles to add.
     */
    void addProfilesToCache(Collection<T> profiles);

    /**
     * Retrieves all profiles from the cache.
     *
     * @return a collection of all profiles in the cache.
     */
    Collection<T> getProfilesFromCache();

    /**
     * Retrieves a profile by its ID from the cache.
     *
     * @param profileId the ID of the profile.
     * @return the profile, or null if not found.
     */
    T getProfileFromCache(UUID profileId);

    /**
     * Checks if a profile with the specified name exists in the cache.
     *
     * @param profileName the name of the profile.
     * @return true if the profile exists, false otherwise.
     */
    boolean containsProfile(String profileName);

    /**
     * Removes a profile from the cache by its ID.
     *
     * @param profileId the ID of the profile to remove.
     */
    void removeProfileFromCache(UUID profileId);

    /**
     * Clears all entries in the cache.
     */
    void clearProfileCache();

    /**
     * Shuts down the cache, performing any necessary cleanup.
     */
    void shutdownCache();

    /**
     * Expunges all profiles from the cache.
     */
    void expunge();
}
