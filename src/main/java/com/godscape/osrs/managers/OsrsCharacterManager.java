package com.godscape.osrs.managers;

import com.godscape.osrs.cache.OsrsCharacterCache;
import com.godscape.osrs.schemas.OsrsCharacterSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OsrsCharacterManager {

    private final OsrsCharacterCache characterCache;
    private final Queue<Runnable> characterChangeQueue = new ConcurrentLinkedQueue<>();
    private boolean isCharacterDetected = false;
    private UUID currentCharacterId;

    public OsrsCharacterManager() {
        this.characterCache = DependencyFactory.getInstance().getInjection(OsrsCharacterCache.class);
        Logger.info("OsrsCharacterManager: Initialized with character cache.");
    }

    /**
     * Sets the current active character by ID and processes queued changes if valid.
     *
     * @param characterId The UUID of the active character.
     */
    public void setActiveCharacter(UUID characterId) {
        if (characterId == null) {
            Logger.warn("OsrsCharacterManager: Cannot set active character with null ID.");
            return;
        }

        OsrsCharacterSchema character = characterCache.getCharacter(characterId);
        if (character != null) {
            this.currentCharacterId = characterId;
            isCharacterDetected = true;
            Logger.info("OsrsCharacterManager: Active character set to '{}' with ID '{}'.", character.getCharacterName(), characterId);
            processQueuedChanges();
        } else {
            Logger.warn("OsrsCharacterManager: Character with ID '{}' not found in cache. Changes will remain queued.", characterId);
            isCharacterDetected = false;
        }
    }

    /**
     * Validates whether an active character is detected.
     */
    private boolean isActiveCharacterDetected() {
        if (!isCharacterDetected || currentCharacterId == null) {
            Logger.warn("OsrsCharacterManager: No active character detected. Changes will be queued.");
            return false;
        }
        return true;
    }

    /**
     * Adds a new character to the cache or queues the operation if no character is detected.
     *
     * @param character The character to add.
     */
    public void addCharacter(OsrsCharacterSchema character) {
        if (!isActiveCharacterDetected()) {
            queueChange(() -> addCharacterToCache(character));
            Logger.info("OsrsCharacterManager: Queued add operation for character '{}'.", character.getCharacterName());
            return;
        }
        addCharacterToCache(character);
    }

    private void addCharacterToCache(OsrsCharacterSchema character) {
        if (validateCharacter(character)) {
            characterCache.addCharacter(character);
            Logger.info("OsrsCharacterManager: Added character '{}' to cache.", character.getCharacterName());
        } else {
            Logger.warn("OsrsCharacterManager: Failed to add character: Invalid character data.");
        }
    }

    /**
     * Updates an existing character in the cache or queues the operation if no character is detected.
     *
     * @param character The character to update.
     */
    public void updateCharacter(OsrsCharacterSchema character) {
        if (!isActiveCharacterDetected()) {
            queueChange(() -> updateCharacterInCache(character));
            Logger.info("OsrsCharacterManager: Queued update operation for character '{}'.", character.getCharacterName());
            return;
        }

        if (currentCharacterId.equals(character.getCharacterId())) {
            updateCharacterInCache(character);
        } else {
            Logger.warn("OsrsCharacterManager: Active character mismatch. Queuing update for '{}'.", character.getCharacterName());
            queueChange(() -> updateCharacterInCache(character));
        }
    }

    private void updateCharacterInCache(OsrsCharacterSchema character) {
        if (validateCharacter(character)) {
            characterCache.updateCharacter(character);
            Logger.info("OsrsCharacterManager: Updated character '{}' in cache.", character.getCharacterName());
        } else {
            Logger.warn("OsrsCharacterManager: Failed to update character: Invalid character data.");
        }
    }

    /**
     * Removes a character from the cache or queues the operation if no character is detected.
     *
     * @param characterId The UUID of the character to remove.
     */
    public void removeCharacter(UUID characterId) {
        if (!isActiveCharacterDetected()) {
            queueChange(() -> removeCharacterFromCache(characterId));
            Logger.info("OsrsCharacterManager: Queued remove operation for character ID '{}'.", characterId);
            return;
        }
        removeCharacterFromCache(characterId);
    }

    private void removeCharacterFromCache(UUID characterId) {
        if (characterId != null) {
            characterCache.removeCharacter(characterId);
            Logger.info("OsrsCharacterManager: Removed character with ID '{}' from cache.", characterId);
        } else {
            Logger.warn("OsrsCharacterManager: Failed to remove character: Invalid UUID.");
        }
    }

    /**
     * Processes queued changes when a character is detected.
     */
    private void processQueuedChanges() {
        Logger.info("OsrsCharacterManager: Processing queued changes.");
        while (!characterChangeQueue.isEmpty()) {
            try {
                characterChangeQueue.poll().run();
            } catch (Exception e) {
                Logger.error("OsrsCharacterManager: Error processing queued change. {}", e.getMessage());
            }
        }
    }

    /**
     * Queues a character-related change for later execution.
     *
     * @param change A runnable representing the change.
     */
    private void queueChange(Runnable change) {
        characterChangeQueue.offer(change);
    }

    /**
     * Validates if the character contains necessary data.
     * This ensures that character data is not null and meets basic criteria.
     *
     * @param character The character to validate.
     * @return True if the character is valid, false otherwise.
     */
    private boolean validateCharacter(OsrsCharacterSchema character) {
        if (character == null) {
            Logger.warn("OsrsCharacterManager: Character validation failed: Character is null.");
            return false;
        }
        if (character.getCharacterId() == null) {
            Logger.warn("OsrsCharacterManager: Character validation failed: Character ID is null.");
            return false;
        }
        if (character.getCharacterName() == null || character.getCharacterName().isEmpty()) {
            Logger.warn("OsrsCharacterManager: Character validation failed: Character name is empty.");
            return false;
        }
        return true;
    }
}
