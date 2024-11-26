package com.godscape.system.utility.generators;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;

@Singleton
public class BaseKeyGenerator {

    private static final String DELIMITER = "_";

    /**
     * Generates a unique, database-friendly key in snake_case format based on the panel.
     *
     * @param panel       The Panels instance (OsrsPanels) to generate a key for.
     * @param componentId The unique identifier for the component.
     * @return A structured key in snake_case format or null if componentId is missing.
     */
    public String generateKey(Enum<?> panel, String componentId) {
        if (componentId == null || componentId.trim().isEmpty()) {
            Logger.warn("BaseKeyGenerator: Component ID is missing. Cannot generate key.");
            return null;
        }

        StringBuilder keyBuilder = new StringBuilder();

        // Append the schema prefix based on the platform
        GameVersion gameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();
        if (gameVersion == GameVersion.OSRS) {
            keyBuilder.append("osrs").append(DELIMITER);
        } else if (gameVersion == GameVersion.RS3) {
            keyBuilder.append("rs3").append(DELIMITER);
        } else {
            Logger.warn("BaseKeyGenerator: Unrecognized game version. Defaulting key prefix.");
            keyBuilder.append("unknown").append(DELIMITER);
        }

        // Append the panel path if provided
        if (panel != null) {
            if (!(panel instanceof Enum<?>)) {
                Logger.warn("BaseKeyGenerator: Panel is not an instance of Enum. Received: {}", panel.getClass().getSimpleName());
                return null;
            }
            String panelName = panel.name();

            // Strip "OSRS_" or "RS3_" prefix if present
            if (panelName.startsWith("OSRS_") || panelName.startsWith("RS3_")) {
                panelName = panelName.substring(panelName.indexOf("_") + 1);
            }

            String panelPath = convertToSnakeCase(panelName);
            if (panelPath.isEmpty()) {
                Logger.warn("BaseKeyGenerator: Converted panel path is empty. Panel: {}", panel);
                return null;
            }
            keyBuilder.append(panelPath).append(DELIMITER);
        }

        // Append the component ID
        String componentName = convertToSnakeCase(componentId);
        if (componentName.isEmpty()) {
            Logger.warn("BaseKeyGenerator: Converted component name is empty. Component ID: {}", componentId);
            return null;
        }
        keyBuilder.append(componentName);

        String generatedKey = keyBuilder.toString();
        Logger.debug("BaseKeyGenerator: Generated key '{}'", generatedKey);
        return generatedKey;
    }

    /**
     * Converts a string or enum name to snake_case.
     *
     * @param input The input string or enum name.
     * @return The snake_case representation.
     */
    private String convertToSnakeCase(String input) {
        if (input == null) {
            Logger.warn("BaseKeyGenerator: Input to convertToSnakeCase is null.");
            return "";
        }

        // Replace non-alphanumeric characters with underscores and convert to lowercase
        String snakeCase = input.trim().toLowerCase().replaceAll("[^a-z0-9]+", "_");
        Logger.debug("BaseKeyGenerator: Converted '{}' to snake_case '{}'", input, snakeCase);
        return snakeCase;
    }
}
