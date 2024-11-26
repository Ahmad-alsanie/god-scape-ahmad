package com.godscape.osrs.preloaders;

import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.SerializableFactory;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.ResourceUtils;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class OsrsLoadPremadeProfilesToCache {

    private static final String OSRS_RESOURCE_DIR = "resources/osrs/";

    public OsrsLoadPremadeProfilesToCache() {
        Logger.info("OsrsLoadPremadeProfilesToCache: Initialization complete.");
    }

    /**
     * Loads all premade OSRS profiles from the resource directory.
     */
    public void loadAllPremadeProfiles() {
        Logger.info("OsrsLoadPremadeProfilesToCache: Starting to load premade profiles.");
        List<IdentifiedDataSerializable> profilesToLoad = new ArrayList<>();

        try {
            List<String> resourceFiles = ResourceUtils.listResourceFiles(OSRS_RESOURCE_DIR);

            if (resourceFiles.isEmpty()) {
                Logger.warn("OsrsLoadPremadeProfilesToCache: No profile files found in '{}'.", OSRS_RESOURCE_DIR);
                return;
            }

            for (String resourcePath : resourceFiles) {
                loadProfilesFromResource(resourcePath, profilesToLoad);
            }

            if (!profilesToLoad.isEmpty()) {
                OsrsCacheController cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);
                profilesToLoad.forEach(profile -> cacheController.addProfile((OsrsProfileSchema) profile));
                Logger.info("OsrsLoadPremadeProfilesToCache: Added {} profiles to the OSRS cache.", profilesToLoad.size());
            } else {
                Logger.info("OsrsLoadPremadeProfilesToCache: No profiles loaded from resources.");
            }

        } catch (IOException e) {
            Logger.error("OsrsLoadPremadeProfilesToCache: Error listing resource files: {}", e.getMessage(), e);
        }
    }

    /**
     * Loads profiles from a single resource file and adds them to the provided list.
     *
     * @param resourcePath   Path to the resource file.
     * @param profilesToLoad List to add loaded profiles.
     */
    private void loadProfilesFromResource(String resourcePath, List<IdentifiedDataSerializable> profilesToLoad) {
        Logger.info("OsrsLoadPremadeProfilesToCache: Loading profiles from resource '{}'.", resourcePath);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                Logger.error("OsrsLoadPremadeProfilesToCache: Resource '{}' not found.", resourcePath);
                return;
            }

            try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                List<IdentifiedDataSerializable> profiles = deserializeProfiles(objectInputStream);
                if (!profiles.isEmpty()) {
                    profilesToLoad.addAll(profiles);
                    Logger.info("OsrsLoadPremadeProfilesToCache: Loaded {} profiles from '{}'.", profiles.size(), resourcePath);
                } else {
                    Logger.warn("OsrsLoadPremadeProfilesToCache: No profiles found in '{}'.", resourcePath);
                }
            } catch (IOException e) {
                Logger.error("OsrsLoadPremadeProfilesToCache: Error deserializing profiles from '{}': {}", resourcePath, e.getMessage(), e);
            }

        } catch (IOException e) {
            Logger.error("OsrsLoadPremadeProfilesToCache: I/O Error reading resource '{}': {}", resourcePath, e.getMessage(), e);
        }
    }

    /**
     * Deserializes profiles from an ObjectInputStream based on the current game version.
     *
     * @param inputStream The ObjectInputStream to read from.
     * @return A list of deserialized profiles.
     * @throws IOException If an I/O error occurs.
     */
    private List<IdentifiedDataSerializable> deserializeProfiles(ObjectInputStream inputStream) throws IOException {
        List<IdentifiedDataSerializable> profiles = new ArrayList<>();
        SerializableFactory serializableFactory = new SerializableFactory();

        try {
            while (true) {
                IdentifiedDataSerializable profile = (IdentifiedDataSerializable) inputStream.readObject();
                profiles.add(profile);
            }
        } catch (ClassNotFoundException e) {
            Logger.error("OsrsLoadPremadeProfilesToCache: Failed to deserialize profile due to missing class: {}", e.getMessage(), e);
        } catch (EOFException e) {
            // End of stream reached, do nothing
        } catch (IOException e) {
            if (!e.getMessage().contains("EOF")) {
                Logger.error("OsrsLoadPremadeProfilesToCache: I/O error during deserialization: {}", e.getMessage(), e);
                throw e;
            }
        }
        return profiles;
    }

    /**
     * Clears the OSRS profile cache and reloads profiles from resources.
     */
    public void reloadProfiles() {
        Logger.info("OsrsLoadPremadeProfilesToCache: Reloading profiles.");
        DependencyFactory.getInstance().getInjection(OsrsCacheController.class).clearCache();
        loadAllPremadeProfiles();
    }
}
