package com.godscape.system.registries;

import com.godscape.system.enums.Configs;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

/**
 * ConfigRegistry synchronizes with the Configs enum to provide a registry of configuration instances
 * using the DependencyRegistry for dependency injection.
 */
public class ConfigRegistry {

    private static volatile ConfigRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes configurations by registering them with the DependencyRegistry.
     */
    private ConfigRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializeConfigs();
        Logger.info("ConfigRegistry: Initialized with configurations from the Configs enum.");
    }

    /**
     * Retrieves the singleton instance of ConfigRegistry.
     *
     * @return the singleton instance
     */
    public static ConfigRegistry getInstance() {
        if (instance == null) {
            synchronized (ConfigRegistry.class) {
                if (instance == null) {
                    instance = new ConfigRegistry();
                    Logger.info("ConfigRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    /**
     * Initializes configurations by registering each config enum with the DependencyRegistry.
     * Ensures that configurations are managed via dependency injection.
     */
    private void initializeConfigs() {
        Logger.info("ConfigRegistry: Initializing configurations...");
        for (Configs config : Configs.values()) {
            try {
                Supplier<?> supplier = config.getSupplier(); // Assumes Configs enum has getSupplier()
                if (supplier != null) {
                    dependencyRegistry.registerEnumMapping(config, supplier, config.getClass());
                    Logger.debug("ConfigRegistry: Registered configuration '{}'", config.name());
                } else {
                    Logger.warn("ConfigRegistry: Supplier for configuration '{}' is null.", config.name());
                }
            } catch (Exception e) {
                Logger.error("ConfigRegistry: Exception while registering configuration '{}': {}", config.name(), e.getMessage());
            }
        }
        Logger.info("ConfigRegistry: Configurations initialization complete.");
    }

    /**
     * Retrieves the configuration instance corresponding to the specified config enum.
     *
     * @param config the config enum
     * @param <T>    the type of the configuration
     * @return the configuration instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(Configs config) {
        return dependencyRegistry.getInjection(config);
    }

    /**
     * Reloads (re-initializes) the specified configuration.
     * This forces the DependencyRegistry to recreate the configuration instance.
     *
     * @param config the config enum to reload
     */
    public void reloadConfig(Configs config) {
        dependencyRegistry.registerEnumMapping(config, config.getSupplier(), config.getClass());
        Logger.info("ConfigRegistry: Reloaded configuration '{}'", config.name());
    }

    /**
     * Reloads (re-initializes) all configurations managed by the ConfigRegistry.
     */
    public void reloadAllConfigs() {
        for (Configs config : Configs.values()) {
            reloadConfig(config);
        }
        Logger.info("ConfigRegistry: All configurations reloaded.");
    }
}
