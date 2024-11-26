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
public class OsrsSkillingGatheringHunterPanel extends JPanel {

    private static volatile OsrsSkillingGatheringHunterPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingGatheringHunterPanel() {
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
            List<String> fullInventory = Arrays.asList("Auto", "Bank", "Drop");
            List<String> trainingMethods = Arrays.asList("Auto", "Bird Snaring", "Box Trapping", "Net Trapping", "Deadfall Trapping", "Pitfall Trapping");
            List<String> priorityTarget = Arrays.asList("Auto", "Kebbit", "Chinchompa", "Dragon Impling", "Penguin", "Polar Kebbit", "Rabbit", "Raccoon", "Razorback", "Red Chinchompa", "Sabre-toothed Kebbit", "Spined Larupia", "Swamp Lizard", "Tortoise", "Wimpy Bird");

            osrsGridBuilder.addSeparator("Hunter Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Full Inventory:");
            osrsGridBuilder.addDropdown(2, "hunterSettings", "fullInventory", fullInventory, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Max Chinchompas:");
            osrsGridBuilder.addTextField(4, "hunterSettings", "fullInventory", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Training Method:");
            osrsGridBuilder.addDropdown(2, "hunterSettings", "attackWithSlayer", trainingMethods, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Priority Target:");
            osrsGridBuilder.addDropdown(4, "hunterSettings", "strengthWithSlayer", priorityTarget, OsrsSchemas.OSRS_PROFILE_SCHEMA);
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
