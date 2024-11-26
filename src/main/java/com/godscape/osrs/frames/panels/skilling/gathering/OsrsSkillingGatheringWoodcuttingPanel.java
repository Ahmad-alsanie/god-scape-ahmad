package com.godscape.osrs.frames.panels.skilling.combat;

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
public class OsrsSkillingGatheringWoodcuttingPanel extends JPanel {

    private static volatile OsrsSkillingGatheringWoodcuttingPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingGatheringWoodcuttingPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL);

        try {
            Logger.info("Attempting to initialize PanelGrid within OsrsSkillingCombatAttackPanel...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            // Define dropdown options for configurations
            List<String> fullInventory = Arrays.asList("Auto", "Bank", "Drop", "Fletching", "Firemaking");
            List<String> maximumAxe = Arrays.asList("Auto", "Bronze", "Iron", "Steel", "Black", "Mithril", "Adamant", "Rune", "Dragon", "3rd Age");
            List<String> collection = Arrays.asList("Auto", "This node only", "This node & better", "This node & worse");
            List<String> treeNodes = Arrays.asList("Auto", "Normal", "Oak", "Willow", "Maple", "Yew", "Magic");
            List<String> location = Arrays.asList("Auto", "Varrock", "Lumbridge", "Draynor", "Seers", "Falador", "Taverley", "Catherby", "Tree Gnome Stronghold");
            List<String> wondering = Arrays.asList("Auto", "This area only", "Proximity areas");

            osrsGridBuilder.addSeparator("Woodcutting Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Full Inventory:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "fullInventory", fullInventory, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Maximum Axe:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "maximumAxe", maximumAxe, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Collection:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "collection", collection, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Tree Node:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "maximumAxe", treeNodes, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Wondering:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "wondering", wondering, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Location:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "maximumAxe", location, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Bird Nests:");
            osrsGridBuilder.addObtainbox(2, "gatheringSettings", "lootBirdNest", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Upgrade Tool:");
            osrsGridBuilder.addObtainbox(4, "gatheringSettings", "upgradeTool", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Lumberjack Outfit:");
            osrsGridBuilder.addObtainbox(2, "gatheringSettings", "obtainLumberjackOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Forestry Outfit:");
            osrsGridBuilder.addObtainbox(4, "gatheringSettings", "obtainForestryOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Special Attack:");
            osrsGridBuilder.addCheckbox(2, "gatheringSettings", "specialAttack", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsSkillingCombatAttackPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsSkillingCombatAttackPanel: {}", e.getMessage());
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
