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
public class OsrsSkillingCombatRangedPanel extends JPanel {

    private static volatile OsrsSkillingCombatMagicPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingCombatRangedPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_STATS_LEVELING_TWEAKS_PANEL);

        try {
            Logger.info("Attempting to initialize PanelGrid within OsrsTweaksPanel...");

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
            List<String> arrowCollection = Arrays.asList("Auto", "Ava", "Manual", "Disabled");
            List<String> trainingMethod = Arrays.asList("Auto", "Chinchompa", "Slayer");

            osrsGridBuilder.addSeparator("Ranged Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Full Inventory:");
            osrsGridBuilder.addDropdown(2, "rangedSettings", "fullInventory", fullInventory, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Arrow Collection:");
            osrsGridBuilder.addDropdown(4, "rangedSettings", "arrowCollection", arrowCollection, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Training Method:");
            osrsGridBuilder.addDropdown(4, "rangedSettings", "trainingMethod", trainingMethod, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(1, "Cannon:");
            osrsGridBuilder.addCheckbox(2, "rangedSettings", "useCannon", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addSeparator(null);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Ranged with Slayer:");
            osrsGridBuilder.addCheckbox(2, "rangedSettings", "rangedWithSlayer", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Ava's Attractor:");
            osrsGridBuilder.addObtainbox(4, "rangedSettings", "obtainAvasAttractor", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Ava's Accumulator:");
            osrsGridBuilder.addObtainbox(2, "rangedSettings", "obtainAvasAccumulator", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Ava's Assembler:");
            osrsGridBuilder.addObtainbox(4, "rangedSettings", "imbueAvasAssembler", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();



            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsTweaksPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsTweaksPanel: {}", e.getMessage());
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
