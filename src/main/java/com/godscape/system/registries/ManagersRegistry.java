package com.godscape.system.registries;

import com.godscape.system.enums.Managers;
import com.godscape.system.utility.Logger;
import com.godscape.system.factories.DependencyFactory;
import java.util.function.Supplier;

public class ManagersRegistry {

    private static volatile ManagersRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    private ManagersRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializeManagers();
        Logger.info("ManagersRegistry: Initialized with managers from the Managers enum.");
    }

    public static ManagersRegistry getInstance() {
        if (instance == null) {
            synchronized (ManagersRegistry.class) {
                if (instance == null) {
                    instance = new ManagersRegistry();
                    Logger.info("ManagersRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    private void initializeManagers() {
        Logger.info("ManagersRegistry: Initializing managers...");
        for (Managers manager : Managers.values()) {
            registerManager(manager);
        }
        Logger.info("ManagersRegistry: Managers initialization complete.");
    }

    private void registerManager(Managers manager) {
        try {
            Supplier<?> supplier = manager.getSupplier();
            Class<?> clazz = manager.getClazz();

            if (clazz != null && supplier != null) {
                dependencyRegistry.registerEnumMapping(manager, supplier, clazz);
                Logger.debug("ManagersRegistry: Registered manager '{}'", manager.name());
            } else {
                Logger.warn("ManagersRegistry: Supplier or class is null for manager '{}'.", manager.name());
            }
        } catch (Exception e) {
            Logger.error("ManagersRegistry: Exception while registering manager '{}': {}", manager.name(), e.getMessage(), e);
        }
    }

    public <T> T getManager(Managers manager) {
        T managerInstance = (T) dependencyRegistry.getInjection(manager);
        if (managerInstance == null) {
            Logger.warn("ManagersRegistry: Retrieved manager '{}' is null.", manager.name());
        } else {
            Logger.debug("ManagersRegistry: Retrieved manager '{}' instance: {}", manager.name(), managerInstance);
        }
        return managerInstance;
    }

    public void reloadManager(Managers manager) {
        Logger.info("ManagersRegistry: Reloading manager '{}'", manager.name());
        registerManager(manager);
        Logger.debug("ManagersRegistry: Reloaded manager '{}'", manager.name());
    }

    public void reloadAllManagers() {
        Logger.info("ManagersRegistry: Reloading all managers...");
        for (Managers manager : Managers.values()) {
            reloadManager(manager);
        }
        Logger.info("ManagersRegistry: All managers reloaded.");
    }
}
