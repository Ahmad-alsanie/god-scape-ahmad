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
public class OsrsSkillingCombatSlayerPanel extends JPanel {

    private static volatile OsrsSkillingCombatMagicPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingCombatSlayerPanel() {
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
            List<String> slayerMasters = Arrays.asList(
                    "Auto",
                    "Turael (1)",
                    "Spria (1)",        // Alternative to Turael
                    "Mazchna (20)",
                    "Vannaka (40)",
                    "Chaeldar (70)",
                    "Konar (75)",
                    "Nieve (85)",
                    "Duradel (100)"
            );

            osrsGridBuilder.addSeparator("Slayer Settings");
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Full Inventory:");
            osrsGridBuilder.addDropdown(2, "slayerSettings", "fullInventory", fullInventory, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Slayer Master:");
            osrsGridBuilder.addDropdown(4, "slayerSettings", "slayerMaster", slayerMasters, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addSeparator("Unlockables");
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Slayer Helm:");
            osrsGridBuilder.addObtainbox(2, "slayerSettings", "obtainSlayerHelm", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "← ← Imbue:");
            osrsGridBuilder.addObtainbox(4, "slayerSettings", "imbueSlayerHelm", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Slayer Ring:");
            osrsGridBuilder.addObtainbox(2, "slayerSettings", "obtainRingOfSlayer", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "← ← Eternal:");
            osrsGridBuilder.addObtainbox(4, "slayerSettings", "imbueRingOfSlayer", OsrsSchemas.OSRS_PROFILE_SCHEMA);
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
