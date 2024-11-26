package com.godscape.system.cache;

import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.schemas.GlobalSettingsSchema;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.utility.Logger;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.sun.xml.internal.ws.api.model.wsdl.editable.EditableWSDLPart;

/**
 * Singleton class responsible for caching global settings using Hazelcast.
 */
public class GlobalSettingsCache {

    private static volatile GlobalSettingsCache instance;

    private final HazelcastInstance hazelcastInstance;
    private final IMap<String, GlobalSettingsSchema> globalSettingsMap;

    // Define a constant key for global settings
    private static final String GLOBAL_SETTINGS_KEY = "globalSettings";

    // Private constructor for Singleton pattern
    private GlobalSettingsCache() {
        this.hazelcastInstance = DependencyFactory.getInstance().getInjection(HazelcastConfig.class).getHazelcastInstance();
        this.globalSettingsMap = hazelcastInstance.getMap("GlobalSettingsMap");
        Logger.info("GodscapeCache: Initialized GlobalSettingsMap in Hazelcast.");
    }

    /**
     * Retrieves the singleton instance of GodscapeCache.
     *
     * @return The singleton instance.
     */
    public static GlobalSettingsCache getInstance() {
        if (instance == null) {
            synchronized (GlobalSettingsCache.class) {
                if (instance == null) {
                    instance = new GlobalSettingsCache();
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves the GlobalSettingsSchema from the cache.
     *
     * @return The GlobalSettingsSchema object, or null if not present.
     */
    public GlobalSettingsSchema getGlobalSettings() {
        return globalSettingsMap.get(GLOBAL_SETTINGS_KEY);
    }

    /**
     * Puts the GlobalSettingsSchema into the cache.
     *
     * @param settings The GlobalSettingsSchema to cache.
     */
    public void putGlobalSettings(GlobalSettingsSchema settings) {
        if (settings == null) {
            Logger.warn("GodscapeCache: Attempted to cache a null GlobalSettingsSchema.");
            return;
        }
        globalSettingsMap.put(GLOBAL_SETTINGS_KEY, settings);
        Logger.info("GodscapeCache: Cached GlobalSettingsSchema.");
    }

    /**
     * Updates the GlobalSettingsSchema in the cache.
     *
     * @param settings The updated GlobalSettingsSchema.
     */
    public void updateGlobalSettings(GlobalSettingsSchema settings) {
        putGlobalSettings(settings);
    }

    /**
     * Removes the GlobalSettingsSchema from the cache.
     */
    public void removeGlobalSettings() {
        globalSettingsMap.delete(GLOBAL_SETTINGS_KEY);
        Logger.info("GodscapeCache: Removed GlobalSettingsSchema from cache.");
    }

    /**
     * Clears all entries from the GlobalSettingsMap.
     */
    public void clearCache() {
        globalSettingsMap.clear();
        Logger.info("GodscapeCache: Cleared GlobalSettingsMap cache.");
    }
}
