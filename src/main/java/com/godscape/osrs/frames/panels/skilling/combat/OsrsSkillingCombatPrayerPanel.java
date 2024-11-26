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
public class OsrsSkillingCombatPrayerPanel extends JPanel {

    private static volatile OsrsSkillingCombatMagicPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsSkillingCombatPrayerPanel() {
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

            List<String> boneHandler = Arrays.asList("Auto", "Bank", "Bury");
            List<String> trainingMethod = Arrays.asList("Auto", "Slayer", "Altar", "Gilded Altar", "Chaos Altar", "Wilderness Altar", "Ectofuntus");
            List<String> collection = Arrays.asList("Auto", "This node only", "This node & better", "This node & worse");
            List<String> prayerPreservation = Arrays.asList("Auto", "Disabled", "Holy Wrench", "Prayer Cape", "Ring of the Gods", "Ring of the Gods (i)", "Ring of the Gods (u)");
            List<String> boneThreshold = Arrays.asList(
                    "Auto",              // Custom option
                    "Bones",             // 4.5 XP
                    "Burnt Bones",       // 4.5 XP
                    "Monkey Bones",      // 5 XP
                    "Bat Bones",         // 5 XP
                    "Wolf Bones",        // 5 XP
                    "Small Zombie Monkey Bones", // 5 XP
                    "Big Bones",         // 15 XP
                    "Jogre Bones",       // 15 XP
                    "Zogre Bones",       // 22.5 XP
                    "Shaikahan Bones",   // 25 XP
                    "Baboon Bones",      // 25 XP
                    "Baboon Bones (Yellow)",  // 25 XP
                    "Baboon Bones (Blue)",    // 25 XP
                    "Baboon Bones (Red)",     // 25 XP
                    "Baboon Bones (White)",   // 25 XP
                    "Long Bones",        // 15 XP (Can also be used for Construction experience if turned in)
                    "Curved Bones",      // 15 XP (Can also be used for Construction experience if turned in)
                    "Baby Dragon Bones", // 30 XP
                    "Wyvern Bones",      // 50 XP
                    "Dragon Bones",      // 72 XP
                    "Wyrm Bones",        // 50 XP
                    "Drake Bones",       // 80 XP
                    "Fayrg Bones",       // 84 XP
                    "Raurg Bones",       // 96 XP
                    "Dagannoth Bones",   // 125 XP
                    "Hydra Bones",       // 110 XP
                    "Lava Dragon Bones", // 85 XP (120 XP in Wilderness)
                    "Ourg Bones",        // 140 XP
                    "Superior Dragon Bones" // 150 XP
            );

            osrsGridBuilder.addSeparator("Prayer Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Handler:");
            osrsGridBuilder.addDropdown(2, "prayerSettings", "boneHandler", boneHandler, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Training Method:");
            osrsGridBuilder.addDropdown(4, "prayerSettings", "trainingMethod", trainingMethod, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Collection:");
            osrsGridBuilder.addDropdown(2, "prayerSettings", "collection", collection, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Bone Node:");
            osrsGridBuilder.addDropdown(4, "prayerSettings", "boneNode", boneThreshold, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1,"Holy Wrench:");
            osrsGridBuilder.addObtainbox(2, "prayerSettings", "prayerPreservation", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Ring of the Gods:");
            osrsGridBuilder.addObtainbox(2, "prayerSettings", "prayerPreservation", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "← ← Imbue:");
            osrsGridBuilder.addObtainbox(4, "prayerSettings", "prayerPreservation", OsrsSchemas.OSRS_PROFILE_SCHEMA);
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
