package com.godscape.osrs.frames.panels.loadouts.magic;

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
public class OsrsLoadoutsMagicConsumablesPanel extends JPanel {

    private static volatile OsrsLoadoutsMagicConsumablesPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsLoadoutsMagicConsumablesPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_LOADOUTS_MAGIC_CONSUMABLES_PANEL);

        try {
            Logger.info("Attempting to initialize PanelGrid within OsrsLoadoutsMagicConsumablesPanel...");

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
            List<String> consumableOptions = Arrays.asList("Auto", "Custom");

            osrsGridBuilder.addSeparator("Magic Consumables Loadout");
            osrsGridBuilder.nextRow();

            // Magic-specific potions and food in a 4-column grid format
            osrsGridBuilder.addLabel(1, "Magic Potion:");
            osrsGridBuilder.addDropdown(2, "consumableSettings", "magicPotion", consumableOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Super Magic Potion:");
            osrsGridBuilder.addDropdown(4, "consumableSettings", "superMagicPotion", consumableOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Imbue Heart:");
            osrsGridBuilder.addDropdown(2, "consumableSettings", "imbueHeart", consumableOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Prayer Potion:");
            osrsGridBuilder.addDropdown(4, "consumableSettings", "prayerPotion", consumableOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Saradomin Brew:");
            osrsGridBuilder.addDropdown(2, "consumableSettings", "saradominBrew", consumableOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Food Type:");
            osrsGridBuilder.addDropdown(2, "consumableSettings", "foodType", consumableOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsLoadoutsMagicConsumablesPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsLoadoutsMagicConsumablesPanel: {}", e.getMessage());
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
