package com.godscape.osrs.utility;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.system.utility.Logger;

import java.util.Locale;
import java.util.StringJoiner;

public class OsrsKeyGenerator {

    /**
     * Generates a unique, database-friendly key in snake_case format for OSRS.
     *
     * @param panel       The OSRS Panels instance to generate a key for, can be null if unrelated to any panel.
     * @param componentId The unique identifier for the component.
     * @return A structured key in snake_case format or null if componentId is missing.
     */
    public String generateKey(Object panel, String componentId) {
        if (componentId == null || componentId.isEmpty()) {
            Logger.warn("Component ID is null or empty. Key generation is skipped.");
            return null;
        }

        StringJoiner joiner = new StringJoiner("_");

        // Add the panel's full path if provided
        if (panel != null && panel instanceof OsrsPanels) {
            joiner.add(((OsrsPanels) panel).getFullPath()); // OSRS panel path
        } else {
            Logger.warn("Invalid panel type for OSRS schema.");
        }

        // Append the componentId in lowercase snake_case
        joiner.add(componentId.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_"));

        return joiner.toString(); // Final key is already lowercase due to getFullPath
    }
}
