package com.godscape.osrs.frames.panels.skilling.gathering;

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
public class OsrsSkillingGatheringFishingPanel extends JPanel {

    private static volatile OsrsSkillingGatheringFishingPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingGatheringFishingPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_STATS_SKILL_GOALS_PANEL);

        try {
            Logger.info("Attempting to initialize PanelGrid within OsrsSkillingGatheringFishingPanel...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            List<String> fullInventory = Arrays.asList("Auto", "Bank", "Drop", "Cook");
            List<String> maximumHarpoon = Arrays.asList("Auto", "Harpoon", "Barbed harpoon", "Dragon harpoon", "Infernal harpoon", "Crystal harpoon", "Trailblazer harpoon");
            List<String> collectionMethod = Arrays.asList("Auto", "This node only", "This node & better", "This node & worse");
            List<String> fishType = Arrays.asList("Auto", "Shrimp", "Sardine", "Trout", "Salmon", "Tuna", "Swordfish", "Shark");

            osrsGridBuilder.addSeparator("Fishing Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Full Inventory:");
            osrsGridBuilder.addDropdown(2, "fishingSettings", "fullInventory", fullInventory, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Max Harpoon:");
            osrsGridBuilder.addDropdown(4, "fishingSettings", "maxHarpoon", maximumHarpoon, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Collection Method:");
            osrsGridBuilder.addDropdown(2, "fishingSettings", "collectionMethod", collectionMethod, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Fish Type:");
            osrsGridBuilder.addDropdown(4, "fishingSettings", "fishType", fishType, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Special Attack:");
            osrsGridBuilder.addCheckbox(2, "fishingSettings", "specialAttack", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Auto Upgrade:");
            osrsGridBuilder.addObtainbox(4, "fishingSettings", "autoUpgrade", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Angler's Outfit:");
            osrsGridBuilder.addObtainbox(2, "fishingSettings", "obtainAnglersOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "← ← Spirit");
            osrsGridBuilder.addObtainbox(4, "fishingSettings", "spiritAnglersOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Fish Sack:");
            osrsGridBuilder.addObtainbox(2, "fishingSettings", "obtainFishSack", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Fish Barrel:");
            osrsGridBuilder.addObtainbox(4, "fishingSettings", "obtainFishingBarrel", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Special Attack:");
            osrsGridBuilder.addCheckbox(2, "fishingSettings", "specialAttack", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsSkillingGatheringFishingPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsSkillingGatheringFishingPanel: {}", e.getMessage());
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
