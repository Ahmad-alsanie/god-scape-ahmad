package com.godscape.system.registries;

import com.godscape.system.enums.Observations;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

/**
 * ObservationRegistry synchronizes with the Observations enum to provide a registry of observation instances
 * using the DependencyRegistry for dependency injection.
 */
public class ObservationRegistry {

    private static volatile ObservationRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes observations by registering them with the DependencyRegistry.
     */
    private ObservationRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializeObservations();
        Logger.info("ObservationRegistry: Initialized with observations from the Observations enum.");
    }

    /**
     * Retrieves the singleton instance of ObservationRegistry.
     *
     * @return the singleton instance
     */
    public static ObservationRegistry getInstance() {
        if (instance == null) {
            synchronized (ObservationRegistry.class) {
                if (instance == null) {
                    instance = new ObservationRegistry();
                    Logger.info("ObservationRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    /**
     * Initializes observations by registering each observation enum with the DependencyRegistry.
     * Ensures that observations are managed via dependency injection.
     */
    private void initializeObservations() {
        Logger.info("ObservationRegistry: Initializing observations...");
        for (Observations observation : Observations.values()) {
            try {
                Supplier<?> supplier = observation.getSupplier(); // Assumes Observations enum has getSupplier()
                Class<?> clazz = observation.getClazz();           // Assumes Observations enum has getClazz()
                if (supplier != null && clazz != null) {
                    dependencyRegistry.registerEnumMapping(observation, supplier, clazz);
                    Logger.debug("ObservationRegistry: Registered observation '{}'", observation.name());
                } else {
                    Logger.warn("ObservationRegistry: Supplier or class for observation '{}' is null.", observation.name());
                }
            } catch (Exception e) {
                Logger.error("ObservationRegistry: Exception while registering observation '{}': {}", observation.name(), e.getMessage());
            }
        }
        Logger.info("ObservationRegistry: Observations initialization complete.");
    }

    /**
     * Retrieves the observation instance corresponding to the specified observation enum.
     *
     * @param observation the observation enum
     * @param <T>         the type of the observation
     * @return the observation instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getObservation(Observations observation) {
        return dependencyRegistry.getInjection(observation);
    }

    /**
     * Reloads (re-initializes) the specified observation.
     * This forces the DependencyRegistry to recreate the observation instance.
     *
     * @param observation the observation enum to reload
     */
    public void reloadObservation(Observations observation) {
        dependencyRegistry.registerEnumMapping(observation, observation.getSupplier(), observation.getClazz());
        Logger.info("ObservationRegistry: Reloaded observation '{}'", observation.name());
    }

    /**
     * Reloads (re-initializes) all observations managed by the ObservationRegistry.
     */
    public void reloadAllObservations() {
        for (Observations observation : Observations.values()) {
            reloadObservation(observation);
        }
        Logger.info("ObservationRegistry: All observations reloaded.");
    }
}
