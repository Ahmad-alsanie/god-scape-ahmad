package com.godscape.osrs.frames.panels.settings.antibot;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.osrs.managers.panels.settings.antibot.OsrsSettingsAntiBotDetectionConfigManager;
import com.godscape.system.factories.DependencyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class OsrsSettingsAntiBotDetectionConfigPanel extends JPanel {

    private static volatile OsrsSettingsAntiBotDetectionConfigPanel instance;
    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsSettingsAntiBotDetectionConfigManager antiBotConfigManager;

    public OsrsSettingsAntiBotDetectionConfigPanel() {
        setLayout(new BorderLayout());
        this.antiBotConfigManager = DependencyFactory.getInstance().getInjection(OsrsSettingsAntiBotDetectionConfigManager.class);

        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_SETTINGS_ANTIBOT_DETECTION_CONFIG_PANEL);

        try {
            Logger.info("Initializing AntiBot Detection Config Panel...");

            // Use manager to ensure profile setup
            UUID profileId = profileIdOrDefault();

            // Define dropdown options for anti-bot configuration
            List<String> detectionSensitivity = Arrays.asList("Low", "Medium", "High");
            List<String> randomizationFrequency = Arrays.asList("Rare", "Occasional", "Frequent");
            List<String> breakPattern = Arrays.asList("Short Breaks", "Long Breaks", "Randomized");
            List<String> cameraBehavior = Arrays.asList("None", "Periodic", "Continuous");

            osrsGridBuilder.addSeparator("Anti-Bot Detection Settings");
            osrsGridBuilder.nextRow();

            // Add configurations to the grid
            osrsGridBuilder.addLabel(1, "Detection Sensitivity:");
            osrsGridBuilder.addDropdown(2, "antiBotSettings", "detectionSensitivity", detectionSensitivity, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Randomization Frequency:");
            osrsGridBuilder.addDropdown(4, "antiBotSettings", "randomizationFrequency", randomizationFrequency, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "Break Pattern:");
            osrsGridBuilder.addDropdown(2, "antiBotSettings", "breakPattern", breakPattern, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Camera Behavior:");
            osrsGridBuilder.addDropdown(4, "antiBotSettings", "cameraBehavior", cameraBehavior, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addSeparator(null);
            osrsGridBuilder.nextRow();

            // Additional configurations with checkboxes for boolean settings
            osrsGridBuilder.addLabel(1, "Enable Mouse Path Randomization:");
            osrsGridBuilder.addCheckbox(2, "antiBotSettings", "mousePathRandomization", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Enable Click Timing Variation:");
            osrsGridBuilder.addCheckbox(4, "antiBotSettings", "clickTimingVariation", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addLabel(1, "AFK Detection:");
            osrsGridBuilder.addCheckbox(2, "antiBotSettings", "afkDetection", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.addLabel(3, "Enable Idle Breaks:");
            osrsGridBuilder.addCheckbox(4, "antiBotSettings", "idleBreaks", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("AntiBot Detection Config Panel initialized successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing AntiBot Detection Config Panel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private UUID profileIdOrDefault() {
        UUID currentProfileId = antiBotConfigManager.getCurrentProfileId();
        return currentProfileId != null ? currentProfileId : UUID.randomUUID();
    }

    public OsrsGridBuilder getPanelGrid() {
        return osrsGridBuilder;
    }
}
