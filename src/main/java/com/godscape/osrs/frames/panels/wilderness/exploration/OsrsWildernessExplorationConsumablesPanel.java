package com.godscape.osrs.frames.panels.wilderness.exploration;

import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class OsrsWildernessExplorationConsumablesPanel extends JPanel {

    private static volatile OsrsWildernessExplorationConsumablesPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsWildernessExplorationConsumablesPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_WILDERNESS_EXPLORATION_CONSUMABLES_PANEL);

        try {
            Logger.info("Attempting to initialize PanelGrid within OsrsWildernessExplorationConsumablesPanel...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            // Define options for consumables
            List<String> potionOptions = Arrays.asList("Auto", "Custom");

            osrsGridBuilder.addSeparator("Exploration Consumables Loadout");
            osrsGridBuilder.nextRow();

            // Exploration-specific potions and settings in a 4-column grid format
            osrsGridBuilder.addLabel(1, "Energy Potion:");
            osrsGridBuilder.addDropdown(2, "explorationConsumables", "useEnergyPotions", potionOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Stamina Potion:");
            osrsGridBuilder.addDropdown(4, "explorationConsumables", "useStaminaPotions", potionOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsWildernessExplorationConsumablesPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsWildernessExplorationConsumablesPanel: {}", e.getMessage());
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
