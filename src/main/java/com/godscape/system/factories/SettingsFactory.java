package com.godscape.system.factories;

import com.godscape.system.enums.Configs;
import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.config.SQLConfig;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.utility.Logger;

public class SettingsFactory {

    private static volatile SettingsFactory instance;

    // Private constructor to enforce singleton
    private SettingsFactory() {
        Logger.info("ConfigFactory: Initialized.");
    }

    public static SettingsFactory getInstance() {
        if (instance == null) {
            synchronized (SettingsFactory.class) {
                if (instance == null) {
                    instance = new SettingsFactory();
                }
            }
        }
        return instance;
    }

    /**
     * Retrieves a configuration instance based on the requested Configs enum.
     *
     * @param config The configuration type requested.
     * @return The configuration instance as an Object (to be cast as needed).
     */
    public Object getConfig(Configs config) {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        switch (config) {
            case HAZELCAST_CONFIG:
                return dependencyFactory.getInjection(HazelcastConfig.class);
            case SQL_CONFIG:
                return dependencyFactory.getInjection(SQLConfig.class);
            case GODSCAPE_CONFIG:
                return dependencyFactory.getInjection(GodscapeConfig.class);
            default:
                Logger.warn("ConfigFactory: Unknown configuration requested for {}", config.name());
                return null;
        }
    }
}
