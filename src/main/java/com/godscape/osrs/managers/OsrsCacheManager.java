package com.godscape.osrs.managers;

import com.godscape.osrs.controllers.OsrsProfilesController;
import com.godscape.osrs.controllers.OsrsCharacterController;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.schemas.OsrsCharacterSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class OsrsCacheManager {

    private final OsrsProfilesController profilesController;
    private final OsrsCharacterController characterController;

    public OsrsCacheManager() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.profilesController = dependencyFactory.getInjection(OsrsProfilesController.class);
        this.characterController = dependencyFactory.getInjection(OsrsCharacterController.class);
        Logger.info("OsrsCacheManager: Initialized with profile and character controllers.");
    }

    // Profile-related methods
    public Map<UUID, OsrsProfileSchema> getCachedProfiles() {
        Collection<OsrsProfileSchema> profiles = profilesController.getAllProfiles();
        if (profiles == null || profiles.isEmpty()) {
            Logger.warn("OsrsCacheManager: No profiles found in cache.");
            return Collections.emptyMap();
        }
        return profiles.stream().collect(Collectors.toMap(OsrsProfileSchema::getProfileId, Function.identity()));
    }

    public OsrsProfileSchema getProfile(UUID profileId) {
        if (profileId == null) {
            Logger.warn("OsrsCacheManager: Profile ID cannot be null.");
            return null;
        }

        OsrsProfileSchema profile = profilesController.getProfile(profileId);

        if (profile != null) {
            Logger.info("OsrsCacheManager: Successfully retrieved profile '{}' with name '{}'.", profileId, profile.getProfileName());
        } else {
            Logger.warn("OsrsCacheManager: Profile with ID '{}' not found in cache.", profileId);
        }

        return profile;
    }

    public boolean updateProfileInCache(OsrsProfileSchema profile) {
        if (profile == null || profile.getProfileId() == null) {
            Logger.warn("OsrsCacheManager: Cannot update null profile or profile with null ID.");
            return false;
        }

        boolean updated = profilesController.updateProfile(profile);
        Logger.info(updated ? "OsrsCacheManager: Updated profile in cache." : "OsrsCacheManager: Profile update failed.");
        return updated;
    }

    public boolean addProfileToCache(OsrsProfileSchema profile) {
        if (profile == null || profile.getProfileId() == null) {
            Logger.warn("OsrsCacheManager: Cannot add null profile or profile with null ID.");
            return false;
        }

        boolean added = profilesController.addProfile(profile);
        Logger.info(added ? "OsrsCacheManager: Profile added to cache." : "OsrsCacheManager: Failed to add profile to cache.");
        return added;
    }

    public void addProfilesToCache(Collection<OsrsProfileSchema> profiles) {
        if (profiles == null || profiles.isEmpty()) {
            Logger.warn("OsrsCacheManager: No profiles provided for bulk addition.");
            return;
        }

        profiles.forEach(this::addProfileToCache);
        Logger.info("OsrsCacheManager: Added {} profiles to cache.", profiles.size());
    }

    public boolean removeProfile(UUID profileId) {
        if (profileId == null) {
            Logger.warn("OsrsCacheManager: Cannot remove profile with null ID.");
            return false;
        }

        boolean removed = profilesController.deleteProfile(profileId);
        Logger.info(removed ? "OsrsCacheManager: Profile removed from cache." : "OsrsCacheManager: Failed to remove profile.");
        return removed;
    }

    // Character-related methods
    public OsrsCharacterSchema getCharacter(UUID characterId) {
        if (characterId == null) {
            Logger.warn("OsrsCacheManager: Character ID cannot be null.");
            return null;
        }

        OsrsCharacterSchema character = characterController.getCharacter(characterId);

        if (character != null) {
            Logger.info("OsrsCacheManager: Successfully retrieved character '{}' with ID '{}'.", character.getCharacterName(), characterId);
        } else {
            Logger.warn("OsrsCacheManager: Character with ID '{}' not found in cache.", characterId);
        }

        return character;
    }

    public boolean updateCharacterInCache(OsrsCharacterSchema character) {
        if (character == null || character.getCharacterId() == null) {
            Logger.warn("OsrsCacheManager: Cannot update null character or character with null ID.");
            return false;
        }

        boolean updated = characterController.updateCharacter(character);
        Logger.info(updated ? "OsrsCacheManager: Updated character in cache." : "OsrsCacheManager: Character update failed.");
        return updated;
    }

    public boolean addCharacterToCache(OsrsCharacterSchema character) {
        if (character == null || character.getCharacterId() == null) {
            Logger.warn("OsrsCacheManager: Cannot add null character or character with null ID.");
            return false;
        }

        boolean added = characterController.addCharacter(character);
        Logger.info(added ? "OsrsCacheManager: Character added to cache." : "OsrsCacheManager: Failed to add character to cache.");
        return added;
    }

    public boolean removeCharacter(UUID characterId) {
        if (characterId == null) {
            Logger.warn("OsrsCacheManager: Cannot remove character with null ID.");
            return false;
        }

        boolean removed = characterController.deleteCharacter(characterId);
        Logger.info(removed ? "OsrsCacheManager: Character removed from cache." : "OsrsCacheManager: Character removal failed.");
        return removed;
    }

    public void clearCache() {
        profilesController.clearProfiles();
        characterController.clearCharacters();
        Logger.info("OsrsCacheManager: Cleared all profiles and characters from cache.");
    }

    public void shutdownCacheManager() {
        Logger.info("OsrsCacheManager: Shutting down cache manager.");
    }
}
