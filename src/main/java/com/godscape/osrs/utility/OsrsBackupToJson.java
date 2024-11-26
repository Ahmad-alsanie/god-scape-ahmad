package com.godscape.osrs.utility;

import com.godscape.osrs.schemas.OsrsProfileSchema;
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

public class OsrsBackupToJson {

    private final Gson gson;

    public OsrsBackupToJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        Logger.info("OsrsBackupToJson: Initialized with Gson for OSRS profiles.");
    }

    public void saveProfiles(Collection<OsrsProfileSchema> profiles, String targetDirectory) {
        String filePath = Paths.get(targetDirectory, "osrs_profiles.json").toString();

        try (FileWriter writer = new FileWriter(filePath)) {
            Logger.info("OsrsBackupToJson: Saving OSRS profiles to {}.", filePath);
            gson.toJson(profiles, writer);
            Logger.info("OsrsBackupToJson: Successfully saved OSRS profiles to JSON.");
        } catch (IOException e) {
            Logger.error("OsrsBackupToJson: Error saving OSRS profiles to JSON - {}", e.getMessage());
        }
    }

    public List<OsrsProfileSchema> loadProfiles(String targetDirectory) {
        String filePath = Paths.get(targetDirectory, "osrs_profiles.json").toString();

        try (FileReader reader = new FileReader(filePath)) {
            Logger.info("OsrsBackupToJson: Loading OSRS profiles from {}.", filePath);
            return gson.fromJson(reader, new com.google.gson.reflect.TypeToken<List<OsrsProfileSchema>>() {}.getType());
        } catch (IOException e) {
            Logger.error("OsrsBackupToJson: Failed to load OSRS profiles from JSON - {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
