package com.godscape.osrs.preloaders;

import com.godscape.osrs.cache.OsrsProfileCache;
import com.godscape.osrs.enums.game.OsrsSkillNames;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Preloadable;
import com.godscape.system.utility.generators.HashGenerator;
import com.godscape.system.utility.validation.Normalization;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsDefaultProfilePreloader implements Preloadable {

    private final OsrsProfileCache osrsProfilesCache;

    public OsrsDefaultProfilePreloader() {
        this.osrsProfilesCache = DependencyFactory.getInstance().getInjection(OsrsProfileCache.class);
        Logger.info("OsrsDefaultProfilePreloader: Initialized.");
    }

    /**
     * Creates and returns the default profile schema and stores it in the cache if not present.
     *
     * @return The created or existing default OsrsProfileSchema instance.
     */
    public OsrsProfileSchema createDefaultProfileSchema() {
        Logger.info("OsrsDefaultProfilePreloader: Creating or retrieving default profile.");

        String profileName = "Default";
        UUID profileId = HashGenerator.generateId(profileName);

        OsrsProfileSchema existingProfile = osrsProfilesCache.getProfile(profileId);
        if (existingProfile != null) {
            Logger.warn("Profile with ID '{}' already exists. Returning existing profile.", profileId);
            return existingProfile;
        }

        Map<String, Object> settingsMap = getDefaultSettingsMap();
        OsrsProfileSchema defaultProfile = new OsrsProfileSchema(profileId, profileName, settingsMap, System.currentTimeMillis());
        osrsProfilesCache.addProfile(defaultProfile);

        Logger.info("Default profile '{}' with ID '{}' added to cache.", profileName, profileId);
        return defaultProfile;
    }

    /**
     * Retrieves the default settings as a map without creating a new profile schema.
     *
     * @return A map containing the default settings.
     */
    public Map<String, Object> getDefaultSettingsMap() {
        Map<String, Integer> skillLevels = new HashMap<>();
        for (OsrsSkillNames skill : OsrsSkillNames.values()) {
            int initialLevel = (skill == OsrsSkillNames.HITPOINTS) ? 10 : 1;
            skillLevels.put(skill.name().toLowerCase(), Normalization.clampSkillLevel(initialLevel, skill.name()));
        }

        Map<String, Object> settingsMap = new HashMap<>();
        settingsMap.put(OsrsPanels.OSRS_STATS_LEVELING_TWEAKS_PANEL.name().toLowerCase(), createLevelingTweaksMap());
        settingsMap.put(OsrsPanels.OSRS_STATS_SKILL_GOALS_PANEL.name().toLowerCase(), skillLevels);

        return settingsMap;
    }

    /**
     * Creates a default map for leveling tweaks.
     *
     * @return Map containing leveling tweaks configurations.
     */
    public Map<String, Object> createLevelingTweaksMap() {
        Map<String, Object> levelingTweaksMap = new HashMap<>();
        levelingTweaksMap.put("tickManipulation", true);
        levelingTweaksMap.put("tickConsistency", true);
        levelingTweaksMap.put("playstyle", "Balanced");
        levelingTweaksMap.put("autoprofiler", "Disabled");
        levelingTweaksMap.put("useStatBoosters", true);
        levelingTweaksMap.put("activityForesight", true);
        levelingTweaksMap.put("planAhead", true);
        return levelingTweaksMap;
    }

    /**
     * Resets a given component map to the default leveling tweaks values.
     *
     * @param componentMap The map of components to reset.
     */
    public void resetToDefault(Map<String, JComponent> componentMap) {
        Map<String, Object> defaultSettings = createLevelingTweaksMap();

        for (Map.Entry<String, Object> entry : defaultSettings.entrySet()) {
            String key = entry.getKey();
            Object defaultValue = entry.getValue();

            JComponent component = componentMap.get(key);
            if (component != null) {
                applyDefaultValueToComponent(component, defaultValue);
            }
        }
    }

    /**
     * Helper method to apply the default value to a JComponent.
     *
     * @param component The component to apply the value to.
     * @param value     The value to set.
     */
    private void applyDefaultValueToComponent(JComponent component, Object value) {
        if (component instanceof JCheckBox && value instanceof Boolean) {
            ((JCheckBox) component).setSelected((Boolean) value);
        } else if (component instanceof JComboBox && value instanceof String) {
            ((JComboBox<String>) component).setSelectedItem(value);
        } else if (component instanceof JTextField && value instanceof String) {
            ((JTextField) component).setText((String) value);
        }
    }

    @Override
    public void preload() {
        Logger.info("OsrsDefaultProfilePreloader: Preloading default profile.");
        createDefaultProfileSchema();
    }
}
