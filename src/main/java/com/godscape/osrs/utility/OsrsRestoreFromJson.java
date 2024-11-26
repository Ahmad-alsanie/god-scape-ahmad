// OsrsRestoreFromJson.java
package com.godscape.osrs.utility;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.godscape.system.utility.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class OsrsRestoreFromJson {

    private final Gson gson;

    public OsrsRestoreFromJson() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        Logger.info("OsrsRestoreFromJson: Initialized with Gson.");
    }

    public List<OsrsProfileSchema> loadProfiles(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Type profileListType = new TypeToken<List<OsrsProfileSchema>>() {}.getType();
            return gson.fromJson(reader, profileListType);
        } catch (IOException e) {
            Logger.error("OsrsRestoreFromJson: Failed to load profiles - {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
