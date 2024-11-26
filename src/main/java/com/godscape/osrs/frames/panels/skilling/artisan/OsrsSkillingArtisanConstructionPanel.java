package com.godscape.osrs.frames.panels.skilling.artisan;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.system.factories.DependencyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class OsrsSkillingArtisanConstructionPanel extends JPanel {

    private static volatile OsrsSkillingArtisanConstructionPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingArtisanConstructionPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_SKILLING_CONSTRUCTION_PANEL);

        try {
            Logger.info("Attempting to initialize PanelGrid within OsrsSkillingArtisanConstructionPanel...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            // Define dropdown and checkbox options
            List<String> buildingStyle = Arrays.asList("Auto", "Disabled", "Strictly Leveling", "Teleport Friendly", "Max House", "Max Mansion");
            List<String> houseLocation = Arrays.asList("Auto", "Disabled", "Rimmington", "Taverley", "Pollnivneach", "Rellekka", "Brimhaven", "Yanille", "Hosidius", "Prifddinas");
            List<String> maxServant = Arrays.asList("Auto", "Disabled", "Rick", "Maid", "Butler", "Demon Butler");
            List<String> capeRack = Arrays.asList("Auto", "Disabled", "Construction Cape", "Crafting Cape", "Fishing Cape", "Magic Cape", "Quest Point Cape", "Runecrafting Cape", "Achievement Diary Cape", "Max Cape");

            osrsGridBuilder.addSeparator("Construction Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Building Style:");
            osrsGridBuilder.addDropdown(2, "constructionSettings", "buildingStyle", buildingStyle, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "House Location:");
            osrsGridBuilder.addDropdown(4, "constructionSettings", "houseLocation", houseLocation, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Max Servant:");
            osrsGridBuilder.addDropdown(2, "constructionSettings", "maxServant", maxServant, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Cape Rack:");
            osrsGridBuilder.addDropdown(4, "constructionSettings", "capeRack", capeRack, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Use Teleports:");
            osrsGridBuilder.addCheckbox(2, "constructionSettings", "useTeleports", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Use Wardrobe:");
            osrsGridBuilder.addCheckbox(4, "constructionSettings", "useWardrobe", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Use Magic Altar:");
            osrsGridBuilder.addCheckbox(2, "constructionSettings", "useMagicalAltar", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Use Prayer Altar:");
            osrsGridBuilder.addCheckbox(4, "constructionSettings", "usePrayerAltar", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Use Dressing Table:");
            osrsGridBuilder.addCheckbox(2, "constructionSettings", "useDressingTable", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Use Pet Menagerie:");
            osrsGridBuilder.addCheckbox(4, "constructionSettings", "usePetMenagerie", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Carpenters Outfit:");
            osrsGridBuilder.addObtainbox(2, "constructionSettings", "obtainCarpentersOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Conservative Spending");
            osrsGridBuilder.addCheckbox(4, "constructionSettings", "conservativeSpending", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "House Budget");
            osrsGridBuilder.addTextField(2, "constructionSettings", "useDressingTable", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Leveling Budget");
            osrsGridBuilder.addTextField(4, "constructionSettings", "useDressingTable", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsSkillingArtisanConstructionPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsSkillingArtisanConstructionPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }

    public OsrsGridBuilder getPanelGrid() {
        return osrsGridBuilder;
    }
}
