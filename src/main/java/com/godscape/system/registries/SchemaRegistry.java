package com.godscape.system.registries;

import com.godscape.system.enums.Schemas;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

/**
 * SchemaRegistry synchronizes with the Schemas enum to provide a registry of schema instances
 * using the DependencyRegistry for dependency injection.
 */
public class SchemaRegistry {

    private static volatile SchemaRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes schemas by registering them with the DependencyRegistry.
     */
    private SchemaRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializeSchemas();
        Logger.info("SchemaRegistry: Initialized with schemas from the Schemas enum.");
    }

    /**
     * Retrieves the singleton instance of SchemaRegistry.
     *
     * @return the singleton instance
     */
    public static SchemaRegistry getInstance() {
        if (instance == null) {
            synchronized (SchemaRegistry.class) {
                if (instance == null) {
                    instance = new SchemaRegistry();
                    Logger.info("SchemaRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    /**
     * Initializes schemas by registering each schema enum with the DependencyRegistry.
     * Ensures that schemas are managed via dependency injection.
     */
    private void initializeSchemas() {
        Logger.info("SchemaRegistry: Initializing schemas...");
        for (Schemas schema : Schemas.values()) {
            try {
                Supplier<?> supplier = schema.getSupplier(); // Assumes Schemas enum has getSupplier()
                Class<?> clazz = schema.getClazz();          // Assumes Schemas enum has getClazz()
                if (supplier != null && clazz != null) {
                    dependencyRegistry.registerEnumMapping(schema, supplier, clazz);
                    Logger.debug("SchemaRegistry: Registered schema '{}'", schema.name());
                } else {
                    Logger.warn("SchemaRegistry: Supplier or class for schema '{}' is null.", schema.name());
                }
            } catch (Exception e) {
                Logger.error("SchemaRegistry: Exception while registering schema '{}': {}", schema.name(), e.getMessage());
            }
        }
        Logger.info("SchemaRegistry: Schemas initialization complete.");
    }

    /**
     * Retrieves the schema instance corresponding to the specified schema enum.
     *
     * @param schema the schema enum
     * @param <T>    the type of the schema
     * @return the schema instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getSchema(Schemas schema) {
        return dependencyRegistry.getInjection(schema);
    }

    /**
     * Reloads (re-initializes) the specified schema.
     * This forces the DependencyRegistry to recreate the schema instance.
     *
     * @param schema the schema enum to reload
     */
    public void reloadSchema(Schemas schema) {
        dependencyRegistry.registerEnumMapping(schema, schema.getSupplier(), schema.getClazz());
        Logger.info("SchemaRegistry: Reloaded schema '{}'", schema.name());
    }

    /**
     * Reloads (re-initializes) all schemas managed by the SchemaRegistry.
     */
    public void reloadAllSchemas() {
        for (Schemas schema : Schemas.values()) {
            reloadSchema(schema);
        }
        Logger.info("SchemaRegistry: All schemas reloaded.");
    }
}
