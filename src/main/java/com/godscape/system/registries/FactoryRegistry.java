package com.godscape.system.registries;

import com.godscape.system.enums.Factories;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

/**
 * FactoryRegistry synchronizes with the Factories enum to provide a registry of factory instances
 * using the DependencyRegistry for dependency injection.
 */
public class FactoryRegistry {

    private static volatile FactoryRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes factories by registering them with the DependencyRegistry.
     */
    private FactoryRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializeFactories();
        Logger.info("FactoryRegistry: Initialized with factories from the Factories enum.");
    }

    /**
     * Retrieves the singleton instance of FactoryRegistry.
     *
     * @return the singleton instance
     */
    public static FactoryRegistry getInstance() {
        if (instance == null) {
            synchronized (FactoryRegistry.class) {
                if (instance == null) {
                    instance = new FactoryRegistry();
                    Logger.info("FactoryRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    /**
     * Initializes factories by registering each factory enum with the DependencyRegistry.
     * Ensures that factories are managed via dependency injection.
     */
    private void initializeFactories() {
        Logger.info("FactoryRegistry: Initializing factories...");
        for (Factories factory : Factories.values()) {
            try {
                Supplier<?> supplier = factory.getSupplier(); // Assumes Factories enum has getSupplier()
                Class<?> clazz = factory.getClazz();           // Assumes Factories enum has getClazz()
                if (supplier != null && clazz != null) {
                    dependencyRegistry.registerEnumMapping(factory, supplier, clazz);
                    Logger.debug("FactoryRegistry: Registered factory '{}'", factory.name());
                } else {
                    Logger.warn("FactoryRegistry: Supplier or class for factory '{}' is null.", factory.name());
                }
            } catch (Exception e) {
                Logger.error("FactoryRegistry: Exception while registering factory '{}': {}", factory.name(), e.getMessage());
            }
        }
        Logger.info("FactoryRegistry: Factories initialization complete.");
    }

    /**
     * Retrieves the factory instance corresponding to the specified factory enum.
     *
     * @param factory the factory enum
     * @param <T>     the type of the factory
     * @return the factory instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getFactory(Factories factory) {
        return dependencyRegistry.getInjection(factory);
    }

    /**
     * Reloads (re-initializes) the specified factory.
     * This forces the DependencyRegistry to recreate the factory instance.
     *
     * @param factory the factory enum to reload
     */
    public void reloadFactory(Factories factory) {
        dependencyRegistry.registerEnumMapping(factory, factory.getSupplier(), factory.getClazz());
        Logger.info("FactoryRegistry: Reloaded factory '{}'", factory.name());
    }

    /**
     * Reloads (re-initializes) all factories managed by the FactoryRegistry.
     */
    public void reloadAllFactories() {
        for (Factories factory : Factories.values()) {
            reloadFactory(factory);
        }
        Logger.info("FactoryRegistry: All factories reloaded.");
    }
}
