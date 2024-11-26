package com.godscape.system.factories;

import com.godscape.system.enums.Utilities;
import com.godscape.system.utility.Logger; // Import your custom logger

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UtilityFactory {

    // Singleton instance of UtilityFactory
    private static volatile UtilityFactory instance;

    // Map to store the utility instances
    private final Map<Utilities, Object> utilityMap;

    // Private constructor with initialization log
    private UtilityFactory() {
        this.utilityMap = new ConcurrentHashMap<>();
        Logger.info("UtilityFactory: Initialization complete."); // Log initialization
    }

    // Retrieve the singleton instance of UtilityFactory
    public static UtilityFactory getInstance() {
        if (instance == null) {
            synchronized (UtilityFactory.class) {
                if (instance == null) {
                    Logger.info("UtilityFactory: Initializing..."); // Log before initialization
                    instance = new UtilityFactory();
                }
            }
        }
        return instance;
    }

    // Get or create the utility instance based on the Utilities enum
    @SuppressWarnings("unchecked")
    public <T> T getUtility(Utilities utilityEnum) {
        return (T) utilityMap.computeIfAbsent(utilityEnum, key -> {
            Logger.info("UtilityFactory: Creating utility for {}", key.name()); // Log utility creation
            switch (key) {
                case LOGGER:
                    return new Logger();
                // Add other utilities and their singleton creation logic here
                default:
                    Logger.error("Unsupported utility: {}", key.name());
                    throw new IllegalArgumentException("Unsupported utility: " + key);
            }
        });
    }
}
