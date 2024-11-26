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
public class OsrsSkillingGatheringRunecraftingPanel extends JPanel {

    private static volatile OsrsSkillingGatheringRunecraftingPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingGatheringRunecraftingPanel() {
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

            List<String> fullInventory = Arrays.asList("Auto", "Bank", "Drop");
            List<String> pouches = Arrays.asList("Auto", "Small", "Medium", "Large", "Giant");
            List<String> collection = Arrays.asList("Auto", "This rune only", "This rune & better", "This rune & worse");
            List<String> runeNodes = Arrays.asList("Auto", "Air", "Mind", "Water", "Earth", "Fire", "Body", "Cosmic", "Chaos", "Astral", "Nature", "Law", "Death", "Blood", "Soul", "Wrath", "Armadyl", "Ancient", "Zaros", "Seren", "Luminous", "Dark", "Draconic", "Elemental", "Catalytic", "Blood", "Soul", "Wrath", "Armadyl", "Ancient", "Zaros", "Seren", "Luminous", "Dark", "Draconic", "Elemental", "Catalytic");
            List<String> wondering = Arrays.asList("Auto", "This area only", "Proximity areas");
            List<String> trainingMethods = Arrays.asList("Auto", "Altars", "Ourania Altar", "Guardians of the Rift", "Lava Runes", "Abyss", "ZMI Altar", "Blood Altar", "Soul Altar", "Wrath Altar", "Armadyl Altar", "Ancient Altar", "Zaros Altar", "Seren Altar", "Luminous Altar", "Dark Altar", "Draconic Altar", "Elemental Altar", "Catalytic Altar");

            osrsGridBuilder.addSeparator("Runecrafting Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Full Inventory:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "fullInventory", fullInventory, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Training Method:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "trainingMethod", trainingMethods, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Collection:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "collection", collection, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Rune Node:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "maximumAxe", runeNodes, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Wondering:");
            osrsGridBuilder.addDropdown(2, "gatheringSettings", "wondering", wondering, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Maximum Pouch:");
            osrsGridBuilder.addDropdown(4, "gatheringSettings", "maximumPouch", pouches, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(3, "Upgrade Pouch:");
            osrsGridBuilder.addObtainbox(4, "gatheringSettings", "upgradeTool", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(1, "Raiments of the Eye:");
            osrsGridBuilder.addObtainbox(2, "gatheringSettings", "obtainProspectorsOutfit", OsrsSchemas.OSRS_PROFILE_SCHEMA);
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
