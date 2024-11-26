package com.godscape.system.utility;

import com.godscape.system.factories.DependencyFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.godscape.system.enums.Utilities;
import com.hazelcast.core.Hazelcast;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CacheExpunge {

    private static volatile CacheExpunge instance;  // Volatile to ensure visibility across threads
    private final HazelcastInstance hazelcastInstance;
    private final ExecutorService cacheClearingExecutor;  // Executor for async cache clearing

    // Private constructor for Singleton pattern with lazy initialization
    public CacheExpunge(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.cacheClearingExecutor = Executors.newSingleThreadExecutor();  // Single-threaded for cache clearing
        Logger.info("CacheExpunge: Hazelcast instance initialized for cache management.");
    }

    // Singleton pattern with double-checked locking for performance and thread safety
    public static CacheExpunge getInstance() {
        if (instance == null) {
            synchronized (CacheExpunge.class) {
                if (instance == null) {
                    HazelcastInstance hazelcastInstance = DependencyFactory.getInstance().getInjection(Hazelcast.class).getHazelcastInstanceByName("godscape-hazelcast-instance");
                    if (hazelcastInstance == null) {
                        Logger.error("CacheExpunge: Hazelcast instance 'godscape-hazelcast-instance' not found.");
                        throw new IllegalStateException("Hazelcast instance is unavailable.");
                    }
                    instance = new CacheExpunge(hazelcastInstance);
                }
            }
        }
        return instance;
    }

    // Asynchronously clears the cache for a specific module
    public void clearModuleCacheAsync(Utilities moduleKey) {
        cacheClearingExecutor.submit(() -> clearModuleCache(moduleKey));  // Submit to the executor for non-blocking operation
    }

    // Clear the cache for a specific module
    public void clearModuleCache(Utilities moduleKey) {
        try {
            IMap<String, Object> moduleCache = hazelcastInstance.getMap(moduleKey.name());
            if (moduleCache != null) {
                int sizeBefore = moduleCache.size();
                moduleCache.clear();
                int sizeAfter = moduleCache.size();  // Sanity check after clearing
                Logger.info("CacheExpunge: Cleared cache for module '{}'. Size before: {}, Size after: {}", moduleKey, sizeBefore, sizeAfter);
            } else {
                Logger.warn("CacheExpunge: No cache found for module '{}'.", moduleKey);
            }
        } catch (Exception e) {
            Logger.error("CacheExpunge: Error while clearing cache for module '{}'. Error: {}", moduleKey, e.getMessage());
        }
    }

    // Clear all caches asynchronously for performance
    public void clearAllCachesAsync() {
        cacheClearingExecutor.submit(this::clearAllCaches);  // Submit all cache clearing to executor
    }

    // Clear all caches in the system in batches
    public void clearAllCaches() {
        Logger.info("CacheExpunge: Clearing all module caches...");
        for (Utilities utility : Utilities.values()) {
            clearModuleCache(utility);
        }
        Logger.info("CacheExpunge: All module caches cleared.");
    }

    // Gracefully shutdown the executor to avoid memory leaks
    public void shutdown() {
        try {
            cacheClearingExecutor.shutdown();
            if (!cacheClearingExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                cacheClearingExecutor.shutdownNow();
                if (!cacheClearingExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    Logger.error("CacheExpunge: Failed to shutdown cache clearing executor.");
                }
            }
        } catch (InterruptedException e) {
            cacheClearingExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Allow DependencyFactory to invoke cache clearing for a specific module
    public static void clearCacheForModuleInFactory(Utilities moduleKey) {
        CacheExpunge cacheExpunge = CacheExpunge.getInstance();
        cacheExpunge.clearModuleCacheAsync(moduleKey);
    }

    // Optional: Add periodic logging or statistics collection to monitor cache usage and clearing
    public void logCacheStatistics() {
        for (Utilities utility : Utilities.values()) {
            IMap<String, Object> moduleCache = hazelcastInstance.getMap(utility.name());
            if (moduleCache != null) {
                Logger.info("CacheExpunge: Cache statistics for module '{}': Size = {}", utility, moduleCache.size());
            }
        }
    }
}
