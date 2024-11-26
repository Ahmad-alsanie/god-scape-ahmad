package com.godscape.osrs.frames.panels.stats;

import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.interfaces.mark.Observable;
import com.godscape.system.utility.Logger;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.observers.ProfileUpdateScanner;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class OsrsStatsLevelingTweaksPanel extends JPanel implements Observable {

    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsSettingsController settingsController;
    private final ProfileUpdateScanner profileUpdateScanner;
    private final Map<String, Object> tweaks = new HashMap<>();

    public OsrsStatsLevelingTweaksPanel() {
        setLayout(new BorderLayout());
        this.settingsController = DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);
        this.profileUpdateScanner = DependencyFactory.getInstance().getInjection(ProfileUpdateScanner.class);
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_STATS_LEVELING_TWEAKS_PANEL);

        try {
            Logger.info("Initializing PanelGrid within OsrsStatsLevelingTweaksPanel...");

            // Define settings options
            List<String> playstyle = Arrays.asList("Balanced", "GP/hour", "XP/hour");
            List<String> tickManipulation = Arrays.asList("Auto", "Disabled", "3-tick", "4-tick", "5-tick", "6-tick");
            List<String> tickConsistency = Arrays.asList("Auto", "Disabled", "10%", "25%", "50%", "75%", "100%");
            List<String> autoProfiler = Arrays.asList("Disabled", "Curb Levels", "+1", "+3", "+5", "+10");
            List<String> statBoosters = Arrays.asList("Auto", "Disabled", "Expensive", "Cheap");

            // Add components with ProfileUpdateScanner listeners
            osrsGridBuilder.addSeparator("Leveling Tweaks");
            osrsGridBuilder.nextRow();

            addDropdownWithScanner(1, "Tick Manipulation:", "gameSettings", "tickManipulation", tickManipulation, OsrsSchemas.OSRS_CHARACTER_SCHEMA);
            addDropdownWithScanner(3, "Tick Consistency:", "gameSettings", "tickConsistency", tickConsistency, OsrsSchemas.OSRS_CHARACTER_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addSeparator(null);
            osrsGridBuilder.nextRow();

            addDropdownWithScanner(1, "Playstyle:", "gameSettings", "playstyle", playstyle, OsrsSchemas.OSRS_CHARACTER_SCHEMA);
            addDropdownWithScanner(3, "Autoprofiler:", "gameSettings", "autoProfiler", autoProfiler, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            addDropdownWithScanner(1, "Use Stat Boosters:", "gameSettings", "useStatBoosters", statBoosters, OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            osrsGridBuilder.addSeparator(null);
            osrsGridBuilder.nextRow();

            addCheckboxWithScanner(1, "Activity Foresight:", "gameSettings", "activityForesight", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            addCheckboxWithScanner(3, "Plan Ahead:", "gameSettings", "planAhead", OsrsSchemas.OSRS_PROFILE_SCHEMA);
            osrsGridBuilder.nextRow();

            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsStatsLevelingTweaksPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing PanelGrid in OsrsStatsLevelingTweaksPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public OsrsGridBuilder getPanelGrid() {
        return osrsGridBuilder;
    }

    public void updateTweaks(Map<String, Object> newTweaks) {
        tweaks.clear();
        tweaks.putAll(newTweaks);
        Logger.info("OsrsStatsLevelingTweaksPanel: Updated tweaks with provided configuration.");
    }

    public Map<String, Object> getCurrentTweaks() {
        Logger.info("OsrsStatsLevelingTweaksPanel: Retrieved current tweak settings.");
        return new HashMap<>(tweaks);
    }

    // Wrapper methods to add components with ProfileUpdateScanner listeners
    private void addDropdownWithScanner(int column, String label, String category, String key, List<String> options, Enum<?> schema) {
        osrsGridBuilder.addLabel(column, label);
        osrsGridBuilder.addDropdown(column + 1, category, key, options, schema);
        attachScannerToComponent(category, key, schema);
    }

    private void addCheckboxWithScanner(int column, String label, String category, String key, Enum<?> schema) {
        osrsGridBuilder.addLabel(column, label);
        osrsGridBuilder.addCheckbox(column + 1, category, key, schema);
        attachScannerToComponent(category, key, schema);
    }

    private void attachScannerToComponent(String category, String key, Enum<?> schema) {
        Logger.info("Attaching scanner to component with key '{}'", key);
        Logger.info("Schema: {}", schema);
        Logger.info("Category: {}", category);
        String fullKey = category + "." + key;
        JComponent component = osrsGridBuilder.getComponentByKey(fullKey);
        if (component != null) {
            profileUpdateScanner.attachListener(component, fullKey, schema);
        } else {
            Logger.warn("Component with key '{}' not found for attaching scanner", fullKey);
        }
    }
}
