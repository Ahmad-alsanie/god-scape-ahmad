package com.godscape.osrs.controllers;

import com.godscape.osrs.managers.OsrsProfilesManager;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.UUID;

/**
 * Facade controller class for managing OSRS profiles.
 * This class acts as a facade for the ProfilesManager, handling profile interactions.
 */
@Singleton
public class OsrsProfilesController {

    private final OsrsProfilesManager osrsProfilesManager;

    public OsrsProfilesController() {
        this.osrsProfilesManager = DependencyFactory.getInstance().getInjection(OsrsProfilesManager.class);
        Logger.info("OsrsProfileController initialized.");
    }

    /**
     * Adds a profile to the system, ensuring it doesn't already exist.
     *
     * @param profile The profile to add.
     * @return true if added, false if it already exists.
     */
    public boolean addProfile(OsrsProfileSchema profile) {
        if (profile == null) {
            Logger.warn("Cannot add a null profile.");
            return false;
        }

        if (osrsProfilesManager.profileExists(profile.getProfileId())) {
            Logger.warn("Profile with ID '{}' already exists. Use update instead.", profile.getProfileId());
            return false;
        }

        osrsProfilesManager.addProfile(profile);
        Logger.info("Profile with ID '{}' added.", profile.getProfileId());
        return true;
    }

    /**
     * Updates a profile in the system if there is any change.
     *
     * @param profile The profile to update.
     * @return true if updated, false if it doesn't exist or is unchanged.
     */
    public boolean updateProfile(OsrsProfileSchema profile) {
        if (profile == null) {
            Logger.warn("Cannot update a null profile.");
            return false;
        }

        if (!osrsProfilesManager.profileExists(profile.getProfileId())) {
            Logger.warn("Profile with ID '{}' does not exist. Use add instead.", profile.getProfileId());
            return false;
        }

        if (!osrsProfilesManager.hasChanges(profile)) {
            Logger.info("Profile with ID '{}' is unchanged, no update needed.", profile.getProfileId());
            return false;  // No need to update if the profile is unchanged
        }

        osrsProfilesManager.updateProfile(profile);
        Logger.info("Profile with ID '{}' updated.", profile.getProfileId());
        return true;
    }

    /**
     * Deletes a profile from the system.
     *
     * @param profileId The UUID of the profile to delete.
     * @return true if deleted, false if not found.
     */
    public boolean deleteProfile(UUID profileId) {
        if (profileId == null) {
            Logger.warn("Cannot delete a profile with a null profileId.");
            return false;
        }

        if (!osrsProfilesManager.profileExists(profileId)) {
            Logger.warn("Profile with ID '{}' does not exist. Cannot delete.", profileId);
            return false;
        }

        osrsProfilesManager.removeProfile(profileId);
        Logger.info("Profile with ID '{}' deleted.", profileId);
        return true;
    }

    /**
     * Retrieves all profiles from the manager.
     *
     * @return A collection of all profiles.
     */
    public Collection<OsrsProfileSchema> getAllProfiles() {
        Logger.info("Retrieving all profiles from manager.");
        return osrsProfilesManager.getAllProfiles();
    }

    /**
     * Retrieves a profile by UUID from the manager.
     *
     * @param profileId The UUID of the profile to retrieve.
     * @return The profile or null if not found.
     */
    public OsrsProfileSchema getProfile(UUID profileId) {
        Logger.info("Retrieving profile with ID '{}'", profileId);
        return osrsProfilesManager.getProfile(profileId);
    }

    /**
     * Clears all profiles from the manager.
     */
    public void clearProfiles() {
        osrsProfilesManager.clearProfiles();
        Logger.info("OsrsProfileController: All profiles cleared from manager.");
    }
}
