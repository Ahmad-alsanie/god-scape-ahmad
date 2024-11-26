package com.godscape.osrs.frames.panels.loadouts;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.system.factories.DependencyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class OsrsLoadoutsStrategy extends JPanel {

    private static volatile OsrsLoadoutsStrategy instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsLoadoutsStrategy() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_LOADOUTS_STRATEGY_PANEL);

        try {
            Logger.info("Initializing PanelGrid in OsrsLoadoutsStrategy...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            // Define options for strategy settings
            List<String> toggleOptions = Arrays.asList("Enabled", "Disabled");
            List<String> combatStyles = Arrays.asList("Auto", "Melee", "Ranged", "Magic");

            osrsGridBuilder.addSeparator("General Strategy Settings");
            osrsGridBuilder.nextRow();

            // Strategy settings in a 4-column grid format
            osrsGridBuilder.addLabel(1, "Prayer Flicking:");
            osrsGridBuilder.addDropdown(2, "strategySettings", "prayerFlicking", toggleOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Avoid Combat:");
            osrsGridBuilder.addDropdown(4, "strategySettings", "avoidCombat", toggleOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Combat Style:");
            osrsGridBuilder.addDropdown(2, "strategySettings", "combatStyle", combatStyles, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Auto-Healing:");
            osrsGridBuilder.addCheckbox(4, "strategySettings", "autoHealing", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Potion Usage:");
            osrsGridBuilder.addDropdown(2, "strategySettings", "usePotions", toggleOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Energy Conservation:");
            osrsGridBuilder.addCheckbox(4, "strategySettings", "energyConservation", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Healing Threshold:");
            osrsGridBuilder.addTextField(2, "wildernessStrategy", "healingThreshold", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsLoadoutsStrategy layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsLoadoutsStrategy: {}", e.getMessage());
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
