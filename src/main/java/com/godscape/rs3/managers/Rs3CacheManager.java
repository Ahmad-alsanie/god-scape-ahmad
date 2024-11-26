package com.godscape.rs3.managers;

import com.godscape.rs3.controllers.Rs3ProfilesController;
import com.godscape.rs3.controllers.Rs3CharacterController;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class Rs3CacheManager {

    private final Rs3ProfilesController profilesController;
    private final Rs3CharacterController characterController;
    private UUID activeProfileId;  // Store the active profile ID

    private Rs3CacheManager() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.profilesController = dependencyFactory.getInjection(Rs3ProfilesController.class);
        this.characterController = dependencyFactory.getInjection(Rs3CharacterController.class);
        Logger.info("Rs3CacheManager: Initialized with profile and character controllers.");
    }

    public void setActiveProfileId(UUID profileId) {
        this.activeProfileId = profileId;
        Logger.info("Rs3CacheManager: Active profile ID set to {}", profileId);
    }

    public Map<UUID, Rs3ProfileSchema> getCachedProfiles() {
        Collection<Rs3ProfileSchema> profiles = profilesController.getAllProfiles();
        return profiles.stream().collect(Collectors.toMap(Rs3ProfileSchema::getProfileId, Function.identity()));
    }

    public void updateProfileInCache(Rs3ProfileSchema profile) {
        profilesController.updateProfile(profile);
        Logger.info("Rs3CacheManager: Updated profile '{}' in cache.", profile.getProfileName());
    }

    public void addProfileToCache(Rs3ProfileSchema profile) {
        if (profilesController.addProfile(profile)) {
            Logger.info("Rs3CacheManager: Profile '{}' added to cache.", profile.getProfileName());
        } else {
            Logger.warn("Rs3CacheManager: Profile '{}' already exists; not added to cache.", profile.getProfileName());
        }
    }

    public void addProfilesToCache(Collection<Rs3ProfileSchema> profiles) {
        profiles.forEach(this::addProfileToCache);
        Logger.info("Rs3CacheManager: Added {} profiles to cache.", profiles.size());
    }

    public Rs3ProfileSchema getProfile(UUID profileId) {
        return profilesController.getProfile(profileId);
    }

    public void removeProfile(UUID profileId) {
        if (profilesController.deleteProfile(profileId)) {
            Logger.info("Rs3CacheManager: Removed profile with ID '{}' from cache.", profileId);
        } else {
            Logger.warn("Rs3CacheManager: Profile with ID '{}' does not exist; cannot remove.", profileId);
        }
    }

    public void clearCache() {
        profilesController.clearProfiles();
        Logger.info("Rs3CacheManager: Cleared all profiles from cache.");
    }

    public void loadCharacter() {
        characterController.loadCharacter();
        Logger.info("Rs3CacheManager: Loaded character through character controller.");
    }

    public void saveCharacter() {
        characterController.saveCharacter();
        Logger.info("Rs3CacheManager: Saved character through character controller.");
    }

    public void shutdownCacheManager() {
        Logger.info("Rs3CacheManager: Shutting down cache manager.");
        // Additional shutdown logic, if needed
    }
}
