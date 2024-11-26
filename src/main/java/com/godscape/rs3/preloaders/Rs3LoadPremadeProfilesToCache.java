package com.godscape.rs3.preloaders;

import com.godscape.rs3.controllers.Rs3ProfileController;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.ResourceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading premade RS3 profiles from JSON files within the JAR's resource directory into the cache.
 */
public class Rs3LoadPremadeProfilesToCache {

    private final Rs3ProfileController profileController;
    private final Gson gson = new Gson();

    // Resource directory path for RS3 JSON profiles, configured in ConfigKeys
    private static final String RS3_JSON_RESOURCE_DIR = "resources\\rs3\\";

    // Retrieve dependencies via DependencyFactory
    private static final DependencyFactory dependencyFactory = DependencyFactory.getInstance();

    public Rs3LoadPremadeProfilesToCache() {
        this.profileController = dependencyFactory.getInjection(Rs3ProfileController.class);
        Logger.info("Rs3LoadPremadeProfilesToCache: Initialization complete.");
        loadAllPremadeProfiles();  // Load profiles during initialization
    }

    /**
     * Loads all premade RS3 profiles from the JSON resource directory.
     */
    private void loadAllPremadeProfiles() {
        Logger.info("Rs3LoadPremadeProfilesToCache: Starting to load premade profiles.");
        List<Rs3ProfileSchema> profilesToLoad = new ArrayList<>();

        try {
            List<String> jsonFiles = ResourceUtils.listResourceFiles(RS3_JSON_RESOURCE_DIR);

            if (jsonFiles.isEmpty()) {
                Logger.warn("Rs3LoadPremadeProfilesToCache: No JSON files found in '{}'.", RS3_JSON_RESOURCE_DIR);
                return;
            }

            for (String resourcePath : jsonFiles) {
                loadProfilesFromResource(resourcePath, profilesToLoad);
            }

            if (!profilesToLoad.isEmpty()) {
                profilesToLoad.forEach(profileController::addProfile);  // Use controller to add profiles
                Logger.info("Rs3LoadPremadeProfilesToCache: Added {} profiles to the RS3 cache from JSON.", profilesToLoad.size());
            } else {
                Logger.info("Rs3LoadPremadeProfilesToCache: No profiles loaded from JSON resources.");
            }

        } catch (IOException e) {
            Logger.error("Rs3LoadPremadeProfilesToCache: Error listing JSON resource files: {}", e.getMessage(), e);
        }
    }

    /**
     * Loads profiles from a single JSON resource file and adds them to the provided list.
     *
     * @param resourcePath   Path to the resource file.
     * @param profilesToLoad List to add loaded profiles.
     */
    private void loadProfilesFromResource(String resourcePath, List<Rs3ProfileSchema> profilesToLoad) {
        Logger.info("Rs3LoadPremadeProfilesToCache: Loading profiles from JSON resource '{}'.", resourcePath);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                Logger.error("Rs3LoadPremadeProfilesToCache: Resource '{}' not found.", resourcePath);
                return;
            }
            List<Rs3ProfileSchema> profiles = gson.fromJson(new InputStreamReader(inputStream), new TypeToken<List<Rs3ProfileSchema>>() {}.getType());
            if (profiles != null) {
                profilesToLoad.addAll(profiles);
                Logger.info("Rs3LoadPremadeProfilesToCache: Loaded {} profiles from '{}'.", profiles.size(), resourcePath);
            } else {
                Logger.warn("Rs3LoadPremadeProfilesToCache: No profiles found in '{}'.", resourcePath);
            }
        } catch (IOException e) {
            Logger.error("Rs3LoadPremadeProfilesToCache: Error reading JSON resource '{}': {}", resourcePath, e.getMessage(), e);
        } catch (com.google.gson.JsonSyntaxException e) {
            Logger.error("Rs3LoadPremadeProfilesToCache: JSON syntax error in resource '{}': {}", resourcePath, e.getMessage(), e);
        }
    }

    /**
     * Clears the RS3 profile cache and reloads profiles from resources.
     */
    public void reloadProfiles() {
        Logger.info("Rs3LoadPremadeProfilesToCache: Reloading profiles.");
        profileController.clearCache();  // Use controller to clear cache
        loadAllPremadeProfiles();
    }
}
