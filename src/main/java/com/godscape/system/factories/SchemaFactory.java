package com.godscape.system.factories;

import com.godscape.osrs.schemas.OsrsCharacterSchema;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.schemas.Rs3CharacterSchema;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.enums.GameVersion;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import com.godscape.system.utility.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SchemaFactory {

    public static final int FACTORY_ID = 1; // Factory ID for Hazelcast

    private static volatile SchemaFactory instance; // Singleton instance
    private final Map<Class<?>, IdentifiedDataSerializable> schemaMap = new ConcurrentHashMap<>(); // Cache for schema instances

    private SchemaFactory() {} // Private constructor for singleton pattern

    public static SchemaFactory getInstance() {
        if (instance == null) {
            synchronized (SchemaFactory.class) {
                if (instance == null) {
                    instance = new SchemaFactory();
                    Logger.info("SchemaFactory singleton instance created.");
                }
            }
        }
        return instance;
    }

    // Retrieves the schema based on the provided schema class and caches it
    public IdentifiedDataSerializable getSchema(Class<?> schemaClass) {
        if (schemaClass == null) {
            Logger.error("Schema class is null. Cannot retrieve schema.");
            return null;
        }
        return schemaMap.computeIfAbsent(schemaClass, key -> {
            Logger.debug("Retrieving schema for class: {}", key.getSimpleName());
            return createSchema(schemaClass);
        });
    }

    private IdentifiedDataSerializable createSchema(Class<?> schemaClass) {
        GameVersion currentGameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();

        if (currentGameVersion == null) {
            Logger.error("Cannot create schema. Current game version is null.");
            return null;
        }

        switch (currentGameVersion) {
            case OSRS:
                Logger.info("Creating OSRS schema for class: {}", schemaClass.getSimpleName());
                return createOsrsSchema(schemaClass);
            case RS3:
                Logger.info("Creating RS3 schema for class: {}", schemaClass.getSimpleName());
                return createRs3Schema(schemaClass);
            default:
                Logger.error("Unknown game version: {}", currentGameVersion);
                return null;
        }
    }

    private IdentifiedDataSerializable createOsrsSchema(Class<?> schemaClass) {
        if (schemaClass.equals(OsrsProfileSchema.class)) {
            Logger.debug("Creating OSRS profile schema.");
            return new OsrsProfileSchema();
        } else if (schemaClass.equals(OsrsCharacterSchema.class)) {
            Logger.debug("Creating OSRS character schema.");
            return (IdentifiedDataSerializable) new OsrsCharacterSchema();
        } else {
            Logger.error("Unknown schema class for OSRS: {}", schemaClass.getSimpleName());
            return null;
        }
    }

    private IdentifiedDataSerializable createRs3Schema(Class<?> schemaClass) {
        if (schemaClass.equals(Rs3ProfileSchema.class)) {
            Logger.debug("Creating RS3 profile schema.");
            return new Rs3ProfileSchema();
        } else if (schemaClass.equals(Rs3CharacterSchema.class)) {
            Logger.debug("Creating RS3 character schema.");
            return (IdentifiedDataSerializable) new Rs3CharacterSchema();
        } else {
            Logger.error("Unknown schema class for RS3: {}", schemaClass.getSimpleName());
            return null;
        }
    }
}
