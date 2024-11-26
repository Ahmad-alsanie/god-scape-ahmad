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
public class OsrsSkillingGatheringMiningPanel extends JPanel {

    private static volatile OsrsSkillingGatheringMiningPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingGatheringMiningPanel() {
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
            List<String> fullInventory = Arrays.asList("Auto", "Bank", "Drop", "Smelting", "Smithing");
            List<String> maximumPickaxe = Arrays.asList("Auto", "Bronze", "Iron", "Steel", "Black", "Mithril", "Adamant", "Rune", "Dragon", "3rd Age");
            List<String> collection = Arrays.asList("Auto", "This node only", "This node & better", "This node & worse");
            List<String> treeNodes = Arrays.asList("Auto", "Copper", "Tin", "Iron", "Coal", "Mithril", "Adamant", "Runite", "Amethyst");
            List<String> location = Arrays.asList("Auto", "Draynor", "Varrock", "Falador", "Lumbridge", "Al Kharid", "Edgeville", "Rimmington", "Port Sarim", "Taverley", "Burthorpe", "Catherby", "Seers' Village", "Tree Gnome Stronghold", "Lletya", "Prifddinas", "Miscellania", "Etceteria", "Keldagrim", "Karamja", "Shilo Village", "Tai Bwo Wannai", "Zanaris", "Ape Atoll", "Fossil Island", "Morytania", "Mos Le'Harmless", "Piscatoris", "Tirannwn", "Wilderness", "Zeah");
            List<String> wondering = Arrays.asList("Auto", "This area only", "Proximity areas");

            osrsGridBuilder.addSeparator("Mining Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Full Inventory:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "fullInventory", fullInventory, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Maximum Pickaxe:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "maximumAxe", maximumPickaxe, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Collection:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "collection", collection, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Ore Node:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "maximumAxe", treeNodes, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Wondering:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "wondering", wondering, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Location:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "maximumAxe", location, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Chisel Gems:");
            osrsGridBuilder.addObtainbox(2, "gatheringSettings", "chiselGems", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Upgrade Tool:");
            osrsGridBuilder.addObtainbox(4, "gatheringSettings", "upgradeTool", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Prospectors Outfit:");
            osrsGridBuilder.addObtainbox(2, "gatheringSettings", "obtainProspectorsOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "← ← Golden:");
            osrsGridBuilder.addObtainbox(4, "gatheringSettings", "goldenProspectorsOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
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
