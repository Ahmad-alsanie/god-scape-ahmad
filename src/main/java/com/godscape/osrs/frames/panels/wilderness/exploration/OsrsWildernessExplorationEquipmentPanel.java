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
public class OsrsWildernessExplorationEquipmentPanel extends JPanel {

    private static volatile OsrsWildernessExplorationEquipmentPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsWildernessExplorationEquipmentPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_WILDERNESS_EXPLORATION_EQUIPMENT_PANEL);

        try {
            Logger.info("Initializing PanelGrid in OsrsWildernessExplorationEquipmentPanel...");

            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            List<String> equipmentOptions = Arrays.asList("Auto", "Best Available", "Custom");

            osrsGridBuilder.addSeparator("Wilderness Exploration Equipment Loadout");
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Weapon:");
            osrsGridBuilder.addDropdown(2, "equipmentSettings", "weapon", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Shield:");
            osrsGridBuilder.addDropdown(4, "equipmentSettings", "shield", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Special Attack:");
            osrsGridBuilder.addDropdown(2, "equipmentSettings", "specialAttack", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Ammo:");
            osrsGridBuilder.addDropdown(4, "equipmentSettings", "ammo", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addSeparator(null);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Helmet:");
            osrsGridBuilder.addDropdown(2, "equipmentSettings", "helmet", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Cape:");
            osrsGridBuilder.addDropdown(4, "equipmentSettings", "cape", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Chestplate:");
            osrsGridBuilder.addDropdown(2, "equipmentSettings", "chestplate", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Legs:");
            osrsGridBuilder.addDropdown(4, "equipmentSettings", "legs", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Gloves:");
            osrsGridBuilder.addDropdown(2, "equipmentSettings", "gloves", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Boots:");
            osrsGridBuilder.addDropdown(4, "equipmentSettings", "boots", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Amulet:");
            osrsGridBuilder.addDropdown(2, "equipmentSettings", "amulet", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Ring:");
            osrsGridBuilder.addDropdown(4, "equipmentSettings", "ring", equipmentOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsWildernessExplorationEquipmentPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsWildernessExplorationEquipmentPanel: {}", e.getMessage());
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
