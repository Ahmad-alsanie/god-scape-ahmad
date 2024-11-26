package com.godscape.system.factories;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.utility.Logger;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

/**
 * SerializableFactory is responsible for creating instances of IdentifiedDataSerializable classes
 * based on their typeId.
 */
public class SerializableFactory implements DataSerializableFactory {

    public static final int FACTORY_ID = 1;

    // Unique type IDs for each serializable class
    public static final int OSRS_PROFILE_SCHEMA_ID = 1; // Unique ID for OsrsProfileSchema
    public static final int RS3_PROFILE_SCHEMA_ID = 3;  // Unique ID for Rs3ProfileSchema
    public static final int THEME_SCHEMA_ID = 2;        // Unique ID for ThemeSchema

    @Override
    public IdentifiedDataSerializable create(int typeId) {
        switch (typeId) {
            case OSRS_PROFILE_SCHEMA_ID:
            case RS3_PROFILE_SCHEMA_ID:
                return createProfileSchemaForCurrentGameVersion();
            case THEME_SCHEMA_ID:
                return createThemeSchema();
            default:
                Logger.error("SerializableFactory: No matching type for typeId: {}", typeId);
                return null;
        }
    }

    /**
     * Creates and returns a new profile schema for the current game version (OSRS or RS3).
     *
     * @return A new profile schema instance based on the current game version.
     */
    private IdentifiedDataSerializable createProfileSchemaForCurrentGameVersion() {
        GameVersion currentGameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();

        if (currentGameVersion == GameVersion.OSRS) {
            Logger.debug("SerializableFactory: Creating new OSRS OsrsProfileSchema instance.");
            return new OsrsProfileSchema();
        } else if (currentGameVersion == GameVersion.RS3) {
            Logger.debug("SerializableFactory: Creating new RS3 Rs3ProfileSchema instance.");
            return new Rs3ProfileSchema();
        }

        Logger.error("SerializableFactory: Unknown game version - {}", currentGameVersion);
        return null; // Return null if the game version is not recognized
    }

    /**
     * Creates and returns a new ThemeSchema instance.
     *
     * @return A new ThemeSchema instance.
     */
    private IdentifiedDataSerializable createThemeSchema() {
        Logger.debug("SerializableFactory: Creating new ThemeSchema instance.");
        return new ThemeSchema();
    }
}
