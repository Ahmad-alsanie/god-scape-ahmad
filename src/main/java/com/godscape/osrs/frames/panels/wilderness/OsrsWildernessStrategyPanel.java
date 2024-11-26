package com.godscape.osrs.frames.panels.wilderness;

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
public class OsrsWildernessStrategyPanel extends JPanel {

    private static volatile OsrsWildernessStrategyPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsWildernessStrategyPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_WILDERNESS_STRATEGY_PANEL);

        try {
            Logger.info("Initializing PanelGrid in OsrsWildernessStrategyPanel...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            // Define options for wilderness strategy settings
            List<String> toggleOptions = Arrays.asList("Enabled", "Disabled");
            List<String> riskLevels = Arrays.asList("Low", "Medium", "High");

            osrsGridBuilder.addSeparator("Wilderness Strategy Settings");
            osrsGridBuilder.nextRow();

            // Wilderness-specific settings in a 4-column grid format
            osrsGridBuilder.addLabel(1, "Prayer Flicking:");
            osrsGridBuilder.addDropdown(2, "wildernessStrategy", "prayerFlicking", toggleOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Avoid Combat:");
            osrsGridBuilder.addDropdown(4, "wildernessStrategy", "avoidCombat", toggleOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Risk Management:");
            osrsGridBuilder.addDropdown(2, "wildernessStrategy", "riskLevel", riskLevels, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Escape Route:");
            osrsGridBuilder.addDropdown(4, "wildernessStrategy", "escapeRoute", toggleOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Energy Conservation:");
            osrsGridBuilder.addCheckbox(2, "wildernessStrategy", "energyConservation", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Food Priority:");
            osrsGridBuilder.addDropdown(4, "wildernessStrategy", "foodPriority", toggleOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Healing Threshold:");
            osrsGridBuilder.addTextField(2, "wildernessStrategy", "healingThreshold", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3,"Never Risk Destroyables:");
            osrsGridBuilder.addCheckbox(4, "wildernessStrategy", "neverRiskDestroyables", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsWildernessStrategyPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsWildernessStrategyPanel: {}", e.getMessage());
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
