package com.godscape.osrs.frames.panels.finances;

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
import java.util.UUID;

@Singleton
public class OsrsFinancesGoldPanel extends JPanel {

    private static volatile OsrsFinancesGoldPanel instance;
    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;

    public OsrsFinancesGoldPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_FINANCES_GOLD_PANEL);

        try {
            Logger.info("Initializing PanelGrid in OsrsFinancesGoldPanel...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            osrsGridBuilder.addSeparator("Gold Transfer Settings");
            osrsGridBuilder.nextRow();

            // Auto Transfer Gold Checkbox
            osrsGridBuilder.addLabel(1, "Auto Transfer Gold:");
            osrsGridBuilder.addCheckbox(2, "goldSettings", "autoTransferGold", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Withdraw Amount:");
            osrsGridBuilder.addTextField(4, "goldSettings", "withdrawAmount", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Max Account Value Field
            osrsGridBuilder.addLabel(1, "Min Gold Amount:");
            osrsGridBuilder.addTextField(2, "goldSettings", "maxGoldAmount", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Max Gold Amount:");
            osrsGridBuilder.addTextField(4, "goldSettings", "maxGoldAmount", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addSeparator(null);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Max Account Value:");
            osrsGridBuilder.addTextField(2, "goldSettings", "maxAccountValue", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Max Weapon Value:");
            osrsGridBuilder.addTextField(4, "goldSettings", "maxWeaponValue", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Max Armour Value:");
            osrsGridBuilder.addTextField(2, "goldSettings", "maxArmourValue", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Max Accessory Value:");
            osrsGridBuilder.addTextField(4, "goldSettings", "maxAccessoryValue", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsFinancesGoldPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsFinancesGoldPanel: {}", e.getMessage());
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
