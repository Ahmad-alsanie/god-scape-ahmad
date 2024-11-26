package com.godscape.rs3.utility;

import com.godscape.rs3.enums.core.Rs3Panels;
import com.godscape.system.utility.Logger;

import java.util.Locale;
import java.util.StringJoiner;

public class Rs3KeyGenerator {

    /**
     * Generates a unique, database-friendly key in snake_case format for RS3.
     *
     * @param panel       The RS3 Panels instance to generate a key for, can be null if unrelated to any panel.
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
        if (panel != null && panel instanceof Rs3Panels) {
            joiner.add(((Rs3Panels) panel).getFullPath()); // RS3 panel path
        } else {
            Logger.warn("Invalid panel type for RS3 schema.");
        }

        // Append the componentId in lowercase snake_case
        joiner.add(componentId.toLowerCase(Locale.ROOT).replaceAll("\\s+", "_"));

        return joiner.toString(); // Final key is already lowercase due to getFullPath
    }
}
