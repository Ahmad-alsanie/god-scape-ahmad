package com.godscape.rs3.utility;

import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.utility.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Rs3BackupToJson {

    private final Gson gson;

    public Rs3BackupToJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        Logger.info("Rs3BackupToJson: Initialized with Gson for RS3 profiles.");
    }

    public void saveProfiles(Collection<Rs3ProfileSchema> profiles, String targetDirectory) {
        String filePath = Paths.get(targetDirectory, "rs3_profiles.json").toString();

        try (FileWriter writer = new FileWriter(filePath)) {
            Logger.info("Rs3BackupToJson: Saving RS3 profiles to {}.", filePath);
            gson.toJson(profiles, writer);
            Logger.info("Rs3BackupToJson: Successfully saved RS3 profiles to JSON.");
        } catch (IOException e) {
            Logger.error("Rs3BackupToJson: Error saving RS3 profiles to JSON - {}", e.getMessage());
        }
    }

    public List<Rs3ProfileSchema> loadProfiles(String targetDirectory) {
        String filePath = Paths.get(targetDirectory, "rs3_profiles.json").toString();

        try (FileReader reader = new FileReader(filePath)) {
            Logger.info("Rs3BackupToJson: Loading RS3 profiles from {}.", filePath);
            return gson.fromJson(reader, new com.google.gson.reflect.TypeToken<List<Rs3ProfileSchema>>() {}.getType());
        } catch (IOException e) {
            Logger.error("Rs3BackupToJson: Failed to load RS3 profiles from JSON - {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
