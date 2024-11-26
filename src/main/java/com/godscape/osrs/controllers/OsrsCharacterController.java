package com.godscape.osrs.controllers;

import com.godscape.osrs.schemas.OsrsCharacterSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsCharacterController {

    private final Map<UUID, OsrsCharacterSchema> characterCache = new HashMap<>();

    public OsrsCharacterController() {
        Logger.info("OsrsCharacterController: Initialization complete.");
    }

    public OsrsCharacterSchema getCharacter(UUID characterId) {
        if (characterId == null) {
            Logger.warn("OsrsCharacterController: Character ID is null.");
            return null;
        }
        return characterCache.get(characterId);
    }

    public boolean addCharacter(OsrsCharacterSchema character) {
        if (character == null || character.getCharacterId() == null) {
            Logger.warn("OsrsCharacterController: Cannot add null character or character with null ID.");
            return false;
        }
        if (characterCache.containsKey(character.getCharacterId())) {
            Logger.warn("OsrsCharacterController: Character with ID '{}' already exists.", character.getCharacterId());
            return false;
        }
        characterCache.put(character.getCharacterId(), character);
        Logger.info("OsrsCharacterController: Added character '{}' with ID '{}'.", character.getCharacterName(), character.getCharacterId());
        return true;
    }

    public boolean updateCharacter(OsrsCharacterSchema character) {
        if (character == null || character.getCharacterId() == null) {
            Logger.warn("OsrsCharacterController: Cannot update null character or character with null ID.");
            return false;
        }
        if (!characterCache.containsKey(character.getCharacterId())) {
            Logger.warn("OsrsCharacterController: Character with ID '{}' not found.", character.getCharacterId());
            return false;
        }
        characterCache.put(character.getCharacterId(), character);
        Logger.info("OsrsCharacterController: Updated character '{}' with ID '{}'.", character.getCharacterName(), character.getCharacterId());
        return true;
    }

    public boolean deleteCharacter(UUID characterId) {
        if (characterId == null) {
            Logger.warn("OsrsCharacterController: Cannot delete character with null ID.");
            return false;
        }
        if (characterCache.remove(characterId) != null) {
            Logger.info("OsrsCharacterController: Deleted character with ID '{}'.", characterId);
            return true;
        } else {
            Logger.warn("OsrsCharacterController: Character with ID '{}' not found.", characterId);
            return false;
        }
    }

    public void clearCharacters() {
        characterCache.clear();
        Logger.info("OsrsCharacterController: Cleared all characters from cache.");
    }

    public Collection<OsrsCharacterSchema> getAllCharacters() {
        return characterCache.values();
    }

    public void loadCharacter() {
        Logger.info("OsrsCharacterController: Loading character...");
        // Logic for loading character from persistent storage (e.g., file or database) can be added here.
    }

    public void saveCharacter() {
        Logger.info("OsrsCharacterController: Saving character...");
        // Logic for saving character to persistent storage (e.g., file or database) can be added here.
    }
}
