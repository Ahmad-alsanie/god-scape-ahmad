// Rs3RestoreFromJson.java
package com.godscape.rs3.utility;

import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
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

public class Rs3RestoreFromJson {

    private final Gson gson;
    private final GameVersion gameVersion;

    public Rs3RestoreFromJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Retrieve GameVersion automatically without passing through the constructor
        GameVersion retrievedGameVersion = DependencyFactory.getInstance().getInjection(GameVersion.class);

        if (retrievedGameVersion == null) {
            throw new IllegalStateException("GameVersion not available in DependencyFactory.");
        }

        this.gameVersion = retrievedGameVersion;

        Logger.info("Rs3RestoreFromJson: Initialized with GameVersion {} and Gson.", gameVersion);
    }

    public List<Rs3ProfileSchema> loadProfiles(String targetDirectory) {
        String filePath = Paths.get(targetDirectory, "rs3_profiles.json").toString();

        try (FileReader reader = new FileReader(filePath)) {
            Type profileListType = new TypeToken<List<Rs3ProfileSchema>>() {}.getType();
            return gson.fromJson(reader, profileListType);
        } catch (IOException e) {
            Logger.error("Rs3RestoreFromJson: Failed to load profiles from {} - {}", filePath, e.getMessage());
            return Collections.emptyList();
        }
    }
}
