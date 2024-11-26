package com.godscape.osrs.frames.panels.settings;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.osrs.enums.game.Pathfinding;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.managers.panels.settings.OsrsSettingsPathfindingManager;
import com.godscape.system.factories.DependencyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class OsrsSettingsPathfindingPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsSettingsPathfindingManager pathfindingManager;

    public OsrsSettingsPathfindingPanel() {
        setLayout(new BorderLayout());
        this.pathfindingManager = DependencyFactory.getInstance().getInjection(OsrsSettingsPathfindingManager.class);

        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_SETTINGS_PATHFINDING_PANEL);

        try {
            Logger.info("Initializing OsrsSettingsPathfindingPanel...");

            UUID profileId = profileIdOrDefault();

            List<String> algorithmOptions = Arrays.stream(Pathfinding.values())
                    .map(Pathfinding::getAlgorithmName)
                    .collect(Collectors.toList());

            osrsGridBuilder.addSeparator("Pathfinding Settings");
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Algorithm:");
            osrsGridBuilder.addDropdown(2, "pathfindingSettings", "algorithm", algorithmOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Fairy Rings:");
            osrsGridBuilder.addCheckbox(2, "pathfindingSettings", "fairyRings", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Spirit Trees:");
            osrsGridBuilder.addCheckbox(4, "pathfindingSettings", "spiritTrees", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Magic Teleports:");
            osrsGridBuilder.addCheckbox(2, "pathfindingSettings", "magicTeleports", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Jewellery Teleports:");
            osrsGridBuilder.addCheckbox(4, "pathfindingSettings", "jewelleryTeleports", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Tablet Teleports:");
            osrsGridBuilder.addCheckbox(2, "pathfindingSettings", "tabletTeleports", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Agility Shortcuts:");
            osrsGridBuilder.addCheckbox(4, "pathfindingSettings", "agilityShortcuts", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("OsrsSettingsPathfindingPanel layout successfully initialized.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsSettingsPathfindingPanel: {}", e.getMessage());
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
