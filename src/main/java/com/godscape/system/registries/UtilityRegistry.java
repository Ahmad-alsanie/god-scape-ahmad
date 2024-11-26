package com.godscape.system.registries;

import com.godscape.system.enums.Utilities;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

/**
 * UtilityRegistry synchronizes with the Utilities enum to provide a registry of utility instances
 * using the DependencyRegistry for dependency injection.
 */
public class UtilityRegistry {

    private static volatile UtilityRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes utilities by registering them with the DependencyRegistry.
     */
    private UtilityRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializeUtilities();
        Logger.info("UtilityRegistry: Initialized with utilities from the Utilities enum.");
    }

    /**
     * Retrieves the singleton instance of UtilityRegistry.
     *
     * @return the singleton instance
     */
    public static UtilityRegistry getInstance() {
        if (instance == null) {
            synchronized (UtilityRegistry.class) {
                if (instance == null) {
                    instance = new UtilityRegistry();
                    Logger.info("UtilityRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    /**
     * Initializes utilities by registering each utility enum with the DependencyRegistry.
     * Ensures that utilities are managed via dependency injection.
     */
    private void initializeUtilities() {
        Logger.info("UtilityRegistry: Initializing utilities...");
        for (Utilities utility : Utilities.values()) {
            try {
                Supplier<?> supplier = utility.getSupplier(); // Assumes Utilities enum has getSupplier()
                Class<?> clazz = utility.getClazz();           // Assumes Utilities enum has getClazz()
                if (supplier != null && clazz != null) {
                    dependencyRegistry.registerEnumMapping(utility, supplier, clazz);
                    Logger.debug("UtilityRegistry: Registered utility '{}'", utility.name());
                } else {
                    Logger.warn("UtilityRegistry: Supplier or class for utility '{}' is null.", utility.name());
                }
            } catch (Exception e) {
                Logger.error("UtilityRegistry: Exception while registering utility '{}': {}", utility.name(), e.getMessage());
            }
        }
        Logger.info("UtilityRegistry: Utilities initialization complete.");
    }

    /**
     * Retrieves the utility instance corresponding to the specified utility enum.
     *
     * @param utility the utility enum
     * @param <T>     the type of the utility
     * @return the utility instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getUtility(Utilities utility) {
        return dependencyRegistry.getInjection(utility);
    }

    /**
     * Reloads (re-initializes) the specified utility.
     * This forces the DependencyRegistry to recreate the utility instance.
     *
     * @param utility the utility enum to reload
     */
    public void reloadUtility(Utilities utility) {
        dependencyRegistry.registerEnumMapping(utility, utility.getSupplier(), utility.getClazz());
        Logger.info("UtilityRegistry: Reloaded utility '{}'", utility.name());
    }

    /**
     * Reloads (re-initializes) all utilities managed by the UtilityRegistry.
     */
    public void reloadAllUtilities() {
        for (Utilities utility : Utilities.values()) {
            reloadUtility(utility);
        }
        Logger.info("UtilityRegistry: All utilities reloaded.");
    }
}
