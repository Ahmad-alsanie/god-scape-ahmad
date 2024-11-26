package com.godscape.system.templates;

import com.godscape.osrs.enums.game.OsrsSkillNames;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.system.utility.validation.Normalization;

import java.util.HashMap;
import java.util.Map;

public class DefaultSettingsTemplate {

    /**
     * Provides the default skill levels for a new profile.
     *
     * @return A map with skill names as keys and default levels as values.
     */
    public Map<String, Integer> getDefaultSkillLevels() {
        Map<String, Integer> skillLevels = new HashMap<>();
        for (OsrsSkillNames skill : OsrsSkillNames.values()) {
            int initialLevel = (skill == OsrsSkillNames.HITPOINTS) ? 10 : 1;
            skillLevels.put(skill.name().toLowerCase(), Normalization.clampSkillLevel(initialLevel, skill.name()));
        }
        return skillLevels;
    }

    /**
     * Provides the default leveling tweaks configuration for a new profile.
     *
     * @return A map with settings for leveling tweaks.
     */
    public Map<String, Object> getLevelingTweaksConfig() {
        Map<String, Object> levelingTweaksMap = new HashMap<>();
        levelingTweaksMap.put("mode", "Normal");
        levelingTweaksMap.put("playstyle", "Balanced");
        levelingTweaksMap.put("membership", true);
        levelingTweaksMap.put("autoprofiler", true);
        return levelingTweaksMap;
    }
}
