package com.godscape.system.registries;

import com.godscape.system.enums.Controllers;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

public class ControllerRegistry {

    private static volatile ControllerRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    private ControllerRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializeControllers();
        Logger.info("ControllerRegistry: Initialized with controllers from the Controllers enum.");
    }

    public static ControllerRegistry getInstance() {
        if (instance == null) {
            synchronized (ControllerRegistry.class) {
                if (instance == null) {
                    instance = new ControllerRegistry();
                    Logger.info("ControllerRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    private void initializeControllers() {
        Logger.info("ControllerRegistry: Initializing controllers...");
        for (Controllers controller : Controllers.values()) {
            try {
                Supplier<?> supplier = controller.getSupplier();
                Class<?> clazz = controller.getClazz();
                if (supplier != null) {
                    dependencyRegistry.registerEnumMapping(controller, supplier, clazz);
                    Logger.debug("ControllerRegistry: Registered controller '{}'", controller.name());
                } else {
                    Logger.warn("ControllerRegistry: Supplier for controller '{}' is null.", controller.name());
                }
            } catch (Exception e) {
                Logger.error("ControllerRegistry: Exception while registering controller '{}': {}", controller.name(), e.getMessage());
            }
        }
        Logger.info("ControllerRegistry: Controllers initialization complete.");
    }
}
