package com.godscape.osrs.cache;

import com.godscape.osrs.schemas.OsrsCharacterSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.observers.ProfileChangeListener;
import com.godscape.system.utility.Logger;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.godscape.osrs.enums.core.OsrsSchemas;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsCharacterCache {

    private IMap<UUID, OsrsCharacterSchema> characterCache;
    private HazelcastInstance hazelcastInstance;

    // Private constructor for dependency injection
    public OsrsCharacterCache() {
        Logger.info("OsrsCharacterCache: Initialized.");
    }

    // Lazily initializes the character cache with dependency injection
    private IMap<UUID, OsrsCharacterSchema> getCharacterCache() {
        if (characterCache == null) {
            Logger.info("Initializing Hazelcast instance and character cache.");
            hazelcastInstance = DependencyFactory.getInstance().getInjection(HazelcastConfig.class).getHazelcastInstance();

            // Use the cache name from the OsrsSchemas enum for characters
            String cacheName = OsrsSchemas.OSRS_CHARACTER_SCHEMA.toString();

            characterCache = hazelcastInstance.getMap(cacheName);
            initializeCacheListeners(DependencyFactory.getInstance().getInjection(ProfileChangeListener.class));
            // Apply the TTL and eviction policy when initializing the cache
            configureCache(cacheName);
        }
        return characterCache;
    }

    // Configures TTL and eviction policy for the cache
    private void configureCache(String cacheName) {
        // Similar cache configuration for character cache
        hazelcastInstance.getConfig().getMapConfig(cacheName)
                .setTimeToLiveSeconds(3600)  // 1 hour TTL
                .setEvictionConfig(new com.hazelcast.config.EvictionConfig()
                        .setEvictionPolicy(com.hazelcast.config.EvictionPolicy.LRU)
                        .setSize(1000));  // Max 1000 entries in the cache
    }

    // Initializes cache listeners for character changes
    private void initializeCacheListeners(ProfileChangeListener profileChangeListener) {
        if (profileChangeListener != null) {
            getCharacterCache().addEntryListener(profileChangeListener, true);
            Logger.info("Character change listener added to cache.");
        }
    }

    // Ensures the character has a unique UUID (if missing)
    private void ensureCharacterId(OsrsCharacterSchema character) {
        if (character.getCharacterId() == null) {
            character.setCharacterId(UUID.randomUUID());
            Logger.info("Assigned new UUID to character '{}'.", character.getCharacterName());
        }
    }

    // Adds a character to the cache (ensure UUID)
    public void addCharacter(OsrsCharacterSchema character) {
        ensureCharacterId(character);
        getCharacterCache().put(character.getCharacterId(), character);
        Logger.info("Character added to cache with ID: '{}'", character.getCharacterId());
    }

    // Updates a character in the cache
    public void updateCharacter(OsrsCharacterSchema character) {
        ensureCharacterId(character);
        getCharacterCache().put(character.getCharacterId(), character);
        Logger.info("Character updated in cache with ID: '{}'", character.getCharacterId());
    }

    // Removes a character from the cache by its UUID
    public void removeCharacter(UUID characterId) {
        if (characterId == null) {
            Logger.warn("Cannot remove character: UUID is null.");
            return;
        }
        getCharacterCache().remove(characterId);
        Logger.info("Character removed from cache with ID: '{}'", characterId);
    }

    // Retrieves a character from the cache by UUID
    public OsrsCharacterSchema getCharacter(UUID characterId) {
        OsrsCharacterSchema character = getCharacterCache().get(characterId);
        if (character == null) {
            Logger.warn("Character with ID '{}' not found in cache.", characterId);
        }
        return character;
    }

    // Retrieves all characters from the cache
    public Collection<OsrsCharacterSchema> getAllCharacters() {
        Collection<OsrsCharacterSchema> characters = getCharacterCache().values();
        if (characters.isEmpty()) {
            Logger.warn("No characters found in cache.");
        }
        return characters;
    }

    // Adds multiple characters to the cache
    public void addCharacters(Collection<OsrsCharacterSchema> characters) {
        characters.forEach(this::addCharacter);
        Logger.info("Bulk characters added to cache.");
    }

    // Clears all characters from the cache
    public void clearCharacterCache() {
        getCharacterCache().clear();
        Logger.info("OsrsCharacterCache: All characters cleared from cache.");
    }
}
