package com.godscape.system.factories;

import com.godscape.system.enums.Registries;
import com.godscape.system.registries.DependencyRegistry;
// Removed Logger import
// import com.godscape.system.utility.Logger;

public class DependencyFactory {

    private final DependencyRegistry registry;

    private DependencyFactory() {
        registry = DependencyRegistry.getInstance();
        initializeRegistries(); // Dynamically load registries from the Registries enum
        // Use System.out instead of Logger
        System.out.println("DependencyFactory: Initialization complete.");
    }

    private static class Holder {
        private static final DependencyFactory INSTANCE = new DependencyFactory();
    }

    public static DependencyFactory getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Initializes registries as specified in the Registries enum.
     */
    private void initializeRegistries() {
        for (Registries registryEnum : Registries.values()) {
            try {
                registryEnum.getInjector().get(); // Corrected line
                // Use System.out instead of Logger
                System.out.println("DependencyFactory: Initialized registry " + registryEnum.name());
            } catch (Exception e) {
                // Use System.err instead of Logger
                System.err.println("DependencyFactory: Failed to initialize registry " + registryEnum.name() + ": " + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getInjection(Class<T> clazz) {
        return (T) registry.getInjection(clazz);
    }

    public <T> T getInjection(Enum<?> enumValue) {
        Class<T> mappedClass = registry.getClassForEnum(enumValue);
        if (mappedClass != null) {
            return getInjection(mappedClass);
        }
        // Use System.err instead of Logger
        System.err.println("DependencyFactory: No mapping found for enum " + enumValue);
        throw new IllegalArgumentException("No mapping found for enum: " + enumValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(String key) {
        return (T) registry.getInstance(key);
    }
}
