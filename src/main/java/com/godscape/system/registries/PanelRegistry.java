package com.godscape.system.registries;

import com.godscape.system.enums.Panels;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

/**
 * PanelRegistry synchronizes with the Panels enum to provide a registry of panel instances
 * using the DependencyRegistry for dependency injection.
 */
public class PanelRegistry {

    private static volatile PanelRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes panels by registering them with the DependencyRegistry.
     */
    private PanelRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializePanels();
        Logger.info("PanelRegistry: Initialized with panels from the Panels enum.");
    }

    /**
     * Retrieves the singleton instance of PanelRegistry.
     *
     * @return the singleton instance
     */
    public static PanelRegistry getInstance() {
        if (instance == null) {
            synchronized (PanelRegistry.class) {
                if (instance == null) {
                    instance = new PanelRegistry();
                    Logger.info("PanelRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    /**
     * Initializes panels by registering each panel enum with the DependencyRegistry.
     * Ensures that panels are managed via dependency injection.
     */
    private void initializePanels() {
        Logger.info("PanelRegistry: Initializing panels...");
        for (Panels panel : Panels.values()) {
            try {
                Supplier<?> supplier = panel.getSupplier(); // Assumes Panels enum has getSupplier()
                Class<?> clazz = panel.getClazz();          // Assumes Panels enum has getClazz()
                if (supplier != null && clazz != null) {
                    dependencyRegistry.registerEnumMapping(panel, supplier, clazz);
                    Logger.debug("PanelRegistry: Registered panel '{}'", panel.name());
                } else {
                    Logger.warn("PanelRegistry: Supplier or class for panel '{}' is null.", panel.name());
                }
            } catch (Exception e) {
                Logger.error("PanelRegistry: Exception while registering panel '{}': {}", panel.name(), e.getMessage());
            }
        }
        Logger.info("PanelRegistry: Panels initialization complete.");
    }

    /**
     * Retrieves the panel instance corresponding to the specified panel enum.
     *
     * @param panel the panel enum
     * @param <T>   the type of the panel
     * @return the panel instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getPanel(Panels panel) {
        return dependencyRegistry.getInjection(panel);
    }

    /**
     * Reloads (re-initializes) the specified panel.
     * This forces the DependencyRegistry to recreate the panel instance.
     *
     * @param panel the panel enum to reload
     */
    public void reloadPanel(Panels panel) {
        dependencyRegistry.registerEnumMapping(panel, panel.getSupplier(), panel.getClazz());
        Logger.info("PanelRegistry: Reloaded panel '{}'", panel.name());
    }

    /**
     * Reloads (re-initializes) all panels managed by the PanelRegistry.
     */
    public void reloadAllPanels() {
        for (Panels panel : Panels.values()) {
            reloadPanel(panel);
        }
        Logger.info("PanelRegistry: All panels reloaded.");
    }
}
