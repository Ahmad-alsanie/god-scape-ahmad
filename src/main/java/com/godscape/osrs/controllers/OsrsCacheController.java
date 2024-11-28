package com.godscape.osrs.controllers;

import com.godscape.osrs.managers.OsrsCacheManager;
import com.godscape.osrs.schemas.OsrsCharacterSchema;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsCacheController {

    private final OsrsCacheManager cacheManager;

    public OsrsCacheController() {
        this.cacheManager = DependencyFactory.getInstance().getInjection(OsrsCacheManager.class);
        Logger.info("OsrsCacheController initialized.");
    }

    // Profile Management Methods

    public Collection<OsrsProfileSchema> getAllProfiles() {
        Logger.info("OsrsCacheController: Retrieving all profiles from cache.");
        return cacheManager.getCachedProfiles().values();
    }

    public OsrsProfileSchema getProfile(UUID profileId) {
        Logger.info("OsrsCacheController: Retrieving profile with ID '{}'.", profileId);
        return cacheManager.getProfile(profileId);
    }

    public boolean addProfile(OsrsProfileSchema profile) {
        boolean added = cacheManager.addProfileToCache(profile);
        Logger.info(added ? "OsrsCacheController: Added profile '{}' to cache." :
                "OsrsCacheController: Profile '{}' already exists; not added.", profile.getProfileId());
        return added;
    }

    public boolean updateProfile(OsrsProfileSchema profile) {
        boolean updated = cacheManager.updateProfileInCache(profile);
        if (updated) {
            Logger.info("OsrsCacheController: Updated profile '{}' in cache.", profile.getProfileId());
            notifyProfileUpdate(profile); // Notify listeners about profile update
        } else {
            Logger.error("OsrsCacheController: Profile '{}' update failed.", profile.getProfileId());
        }
        return updated;
    }

    public boolean deleteProfile(UUID profileId) {
        boolean deleted = cacheManager.removeProfile(profileId);
        Logger.info(deleted ? "OsrsCacheController: Deleted profile '{}' from cache." :
                "OsrsCacheController: Profile '{}' deletion failed.", profileId);
        return deleted;
    }

    public void saveToProfileCache(UUID profileId, String componentId, Object value) {
        OsrsProfileSchema profile = cacheManager.getProfile(profileId);
        if (profile == null) {
            Logger.error("OsrsCacheController: Profile with ID '{}' not found.", profileId);
            return;
        }

        Map<String, Object> settingsMap = profile.getSettingsMap();
        if (settingsMap == null) {
            settingsMap = new HashMap<>();
            profile.setSettingsMap(settingsMap);
        }

        settingsMap.put(componentId.toLowerCase(), value);
        boolean updated = cacheManager.updateProfileInCache(profile);
        if (updated) {
            notifyProfileUpdate(profile); // Notify listeners about the setting update
            Logger.info("OsrsCacheController: Setting '{}' saved to profile cache.", componentId);
        } else {
            Logger.error("OsrsCacheController: Failed to save setting '{}' to profile cache.", componentId);
        }
    }

    public Object loadFromProfileCache(UUID profileId, String componentId) {
        OsrsProfileSchema profile = cacheManager.getProfile(profileId);
        if (profile == null) {
            Logger.error("OsrsCacheController: Profile with ID '{}' not found.", profileId);
            return null;
        }

        Map<String, Object> settingsMap = profile.getSettingsMap();
        if (settingsMap != null) {
            return settingsMap.get(componentId.toLowerCase());
        }

        Logger.warn("OsrsCacheController: No settings found for profile '{}'.", profileId);
        return null;
    }

    // Character Management Methods

    public OsrsCharacterSchema getCharacter(UUID characterId) {
        if (characterId == null) {
            Logger.warn("OsrsCacheController: Character ID cannot be null.");
            return null;
        }

        return cacheManager.getCharacter(characterId);
    }

    public boolean addCharacter(OsrsCharacterSchema character) {
        boolean added = cacheManager.addCharacterToCache(character);
        Logger.info(added ? "OsrsCacheController: Added character '{}' to cache." :
                "OsrsCacheController: Character '{}' already exists; not added.", character.getCharacterId());
        return added;
    }

    public boolean updateCharacter(OsrsCharacterSchema character) {
        boolean updated = cacheManager.updateCharacterInCache(character);
        Logger.info(updated ? "OsrsCacheController: Updated character '{}' in cache." :
                "OsrsCacheController: Character '{}' update failed.", character.getCharacterId());
        return updated;
    }

    public boolean deleteCharacter(UUID characterId) {
        boolean deleted = cacheManager.removeCharacter(characterId);
        Logger.info(deleted ? "OsrsCacheController: Deleted character '{}' from cache." :
                "OsrsCacheController: Character '{}' deletion failed.", characterId);
        return deleted;
    }

    public void saveToCharacterCache(UUID characterId, String componentId, Object value) {
        OsrsCharacterSchema character = cacheManager.getCharacter(characterId);
        if (character == null) {
            Logger.error("OsrsCacheController: Character with ID '{}' not found.", characterId);
            return;
        }

        Map<String, Object> settingsMap = character.getSettingsMap();
        if (settingsMap == null) {
            settingsMap = new HashMap<>();
            character.setSettingsMap(settingsMap);
        }

        settingsMap.put(componentId.toLowerCase(), value);
        boolean updated = cacheManager.updateCharacterInCache(character);
        Logger.info(updated ? "OsrsCacheController: Setting '{}' saved to character cache." :
                "OsrsCacheController: Failed to save setting '{}' to character cache.", componentId);
    }

    public Object loadFromCharacterCache(UUID characterId, String componentId) {
        OsrsCharacterSchema character = cacheManager.getCharacter(characterId);
        if (character == null) {
            Logger.error("OsrsCacheController: Character with ID '{}' not found.", characterId);
            return null;
        }

        Map<String, Object> settingsMap = character.getSettingsMap();
        if (settingsMap != null) {
            return settingsMap.get(componentId.toLowerCase());
        }

        Logger.warn("OsrsCacheController: No settings found for character '{}'.", characterId);
        return null;
    }

    // General Settings Map Retrieval

    public Map<String, Object> getSettingsMap(UUID entityId, boolean isProfile) {
        if (entityId == null) {
            Logger.error("OsrsCacheController: Entity ID cannot be null.");
            return new HashMap<>();
        }

        if (isProfile) {
            OsrsProfileSchema profile = cacheManager.getProfile(entityId);
            if (profile != null && profile.getSettingsMap() != null) {
                return profile.getSettingsMap();
            }
        } else {
            OsrsCharacterSchema character = cacheManager.getCharacter(entityId);
            if (character != null && character.getSettingsMap() != null) {
                return character.getSettingsMap();
            }
        }

        Logger.warn("OsrsCacheController: No settings map found for entity ID '{}'.", entityId);
        return new HashMap<>();
    }

    // Cache Maintenance Methods

    public void clearCache() {
        cacheManager.clearCache();
        Logger.info("OsrsCacheController: All profiles and characters cleared from cache.");
    }

    public void shutdownCache() {
        clearCache();
        Logger.info("OsrsCacheController: Cache shutdown complete.");
    }

    // Private Methods for Notifying Profile Updates
    private void notifyProfileUpdate(OsrsProfileSchema profile) {
        // This method is used to notify listeners that a profile has been updated.
        // You need to implement this logic to trigger appropriate updates in other components (e.g., UI listeners).
        Logger.info("OsrsCacheController: Notifying listeners about profile update for '{}'", profile.getProfileId());
        // Add code here to notify listeners
    }
}
