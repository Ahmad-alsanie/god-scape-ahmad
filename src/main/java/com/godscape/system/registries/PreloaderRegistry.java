package com.godscape.system.registries;

import com.godscape.system.enums.Preloaders;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

public class PreloaderRegistry {

    private static volatile PreloaderRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    private PreloaderRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializePreloaders();
        Logger.info("PreloaderRegistry: Initialized with preloaders from the Preloaders enum.");
    }

    public static PreloaderRegistry getInstance() {
        if (instance == null) {
            synchronized (PreloaderRegistry.class) {
                if (instance == null) {
                    instance = new PreloaderRegistry();
                    Logger.info("PreloaderRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    private void initializePreloaders() {
        Logger.info("PreloaderRegistry: Initializing preloaders...");
        for (Preloaders preloader : Preloaders.values()) {
            try {
                Supplier<?> supplier = preloader.getSupplier();
                Class<?> clazz = preloader.getClazz();
                if (supplier != null && clazz != null) {
                    dependencyRegistry.registerEnumMapping(preloader, supplier, clazz);
                    Logger.debug("PreloaderRegistry: Registered preloader '{}'", preloader.name());
                } else {
                    Logger.warn("PreloaderRegistry: Supplier or class for preloader '{}' is null.", preloader.name());
                }
            } catch (Exception e) {
                Logger.error("PreloaderRegistry: Exception while registering preloader '{}': {}", preloader.name(), e.getMessage());
            }
        }
        Logger.info("PreloaderRegistry: Preloaders initialization complete.");
    }

    public <T> T getPreloader(Preloaders preloader) {
        return dependencyRegistry.getInjection(preloader);
    }
}
