package com.godscape.rs3.controllers;

import com.godscape.rs3.managers.Rs3ProfilesManager;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.UUID;

/**
 * Facade controller for managing RS3 profiles.
 */
@Singleton
public class Rs3ProfilesController {

    private final Rs3ProfilesManager profilesManager;

    public Rs3ProfilesController() {
        this.profilesManager = DependencyFactory.getInstance().getInjection(Rs3ProfilesManager.class);
        Logger.info("Rs3ProfilesController initialized.");
    }

    public boolean addProfile(Rs3ProfileSchema profile) {
        if (profile == null) {
            Logger.warn("Cannot add a null profile.");
            return false;
        }

        if (profilesManager.profileExists(profile.getProfileId())) {
            Logger.warn("Profile with ID '{}' already exists. Use update instead.", profile.getProfileId());
            return false;
        }

        profilesManager.addProfile(profile);
        Logger.info("Profile with ID '{}' added.", profile.getProfileId());
        return true;
    }

    public boolean updateProfile(Rs3ProfileSchema profile) {
        if (profile == null) {
            Logger.warn("Cannot update a null profile.");
            return false;
        }

        if (!profilesManager.profileExists(profile.getProfileId())) {
            Logger.warn("Profile with ID '{}' does not exist. Use add instead.", profile.getProfileId());
            return false;
        }

        profilesManager.updateProfile(profile);
        Logger.info("Profile with ID '{}' updated.", profile.getProfileId());
        return true;
    }

    public boolean deleteProfile(UUID profileId) {
        if (profileId == null) {
            Logger.warn("Cannot delete a profile with a null profileId.");
            return false;
        }

        if (!profilesManager.profileExists(profileId)) {
            Logger.warn("Profile with ID '{}' does not exist. Cannot delete.", profileId);
            return false;
        }

        profilesManager.removeProfile(profileId);
        Logger.info("Profile with ID '{}' deleted.", profileId);
        return true;
    }

    public Collection<Rs3ProfileSchema> getAllProfiles() {
        Logger.info("Retrieving all profiles from manager.");
        return profilesManager.getAllProfiles();
    }

    public Rs3ProfileSchema getProfile(UUID profileId) {
        Logger.info("Retrieving profile with ID '{}'", profileId);
        return profilesManager.getProfile(profileId);
    }

    public void clearProfiles() {
        profilesManager.clearProfiles();
        Logger.info("All profiles cleared from manager.");
    }
}
