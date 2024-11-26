package com.godscape.system.schemas;

import java.util.Map;

public interface BaseSchema {

    /**
     * Returns the cache name associated with the schema.
     *
     * @return The cache name.
     */
    String getCacheName();

    /**
     * Provides an instance of the schema.
     *
     * @return A new instance of the schema.
     */
    Object getSchemaInstance();

    /**
     * Extracts fields from the schema into a flat map.
     *
     * @return A map containing field names and their corresponding values.
     */
    Map<String, Object> extractFields();

    /**
     * Retrieves the settings map from the schema.
     *
     * @return A map containing the schema's settings.
     */
    default Map<String, Object> getSettingsMap() {
        throw new UnsupportedOperationException("getSettingsMap() is not implemented for this schema.");
    }

    /**
     * Updates the settings map in the schema.
     *
     * @param settingsMap The new settings map to set.
     */
    default void setSettingsMap(Map<String, Object> settingsMap) {
        throw new UnsupportedOperationException("setSettingsMap() is not implemented for this schema.");
    }
}
