package com.godscape.system.cache.backup;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class RestoreFromJson {

    private final Gson gson;
    private final GameVersion gameVersion;

    public RestoreFromJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Retrieve GameVersion from PlatformFactory
        GameVersion retrievedGameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();

        if (retrievedGameVersion == null) {
            throw new IllegalStateException("GameVersion not available from PlatformFactory.");
        }

        this.gameVersion = retrievedGameVersion;

        Logger.info("RestoreFromJson: Initialized with GameVersion {} and Gson.", gameVersion);
    }

    public List<?> loadProfiles(String targetDirectory) {
        String filePath = Paths.get(targetDirectory, gameVersion == GameVersion.OSRS ? "osrs_profiles.json" : "rs3_profiles.json").toString();

        try (FileReader reader = new FileReader(filePath)) {
            Type profileListType = gameVersion == GameVersion.OSRS
                    ? new TypeToken<List<OsrsProfileSchema>>() {}.getType()
                    : new TypeToken<List<Rs3ProfileSchema>>() {}.getType();
            return gson.fromJson(reader, profileListType);
        } catch (IOException e) {
            Logger.error("RestoreFromJson: Failed to load profiles from {} - {}", filePath, e.getMessage());
            return Collections.emptyList();
        }
    }
}
