package com.godscape.osrs.preloaders;

import com.godscape.osrs.cache.OsrsProfileCache;
import com.godscape.osrs.enums.game.OsrsSkillNames;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.interfaces.mark.Preloadable;
import com.godscape.system.utility.generators.HashGenerator;
import com.godscape.system.utility.validation.Normalization;
import com.godscape.system.utility.Logger;
import com.godscape.system.factories.DependencyFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsLevelSpreadProfilePreloader implements Preloadable {

    private static volatile OsrsLevelSpreadProfilePreloader instance;
    private final OsrsProfileCache osrsProfilesCache;
    private final OsrsDefaultProfilePreloader osrsDefaultPreloader;

    private final int[] levelSpread = {10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99};

    private OsrsLevelSpreadProfilePreloader() {
        this.osrsProfilesCache = DependencyFactory.getInstance().getInjection(OsrsProfileCache.class);
        this.osrsDefaultPreloader = DependencyFactory.getInstance().getInjection(OsrsDefaultProfilePreloader.class);
        Logger.info("OsrsLevelSpreadProfilePreloader: Initialized.");
    }

    @Override
    public void preload() {
        loadLevelSpreadProfiles();
    }

    private void loadLevelSpreadProfiles() {
        Logger.info("OsrsLevelSpreadProfilePreloader: Loading level spread profiles.");

        // Fetch the default settings map from the default profile preloader
        Map<String, Object> baseSettingsMap = osrsDefaultPreloader.getDefaultSettingsMap();

        for (int level : levelSpread) {
            String profileName = "Level: " + level;
            UUID profileId = HashGenerator.generateId(profileName);

            if (osrsProfilesCache.getProfile(profileId) != null) {
                Logger.warn("Level spread profile '{}' already exists. Skipping.", profileName);
                continue;
            }

            // Customize skill goals for each profile using the target level
            Map<String, Object> settingsMap = new HashMap<>(baseSettingsMap);
            settingsMap.put(OsrsPanels.OSRS_STATS_SKILL_GOALS_PANEL.name().toLowerCase(), createLevelSpreadSkillGoals(level));

            OsrsProfileSchema levelSpreadProfile = new OsrsProfileSchema(
                    profileId,
                    profileName,
                    settingsMap,
                    System.currentTimeMillis()
            );

            osrsProfilesCache.addProfile(levelSpreadProfile);
            Logger.info("Level spread profile '{}' with ID '{}' added to cache.", profileName, profileId);
        }
    }

    private Map<String, Integer> createLevelSpreadSkillGoals(int level) {
        Map<String, Integer> skillGoals = new HashMap<>();
        for (OsrsSkillNames skill : OsrsSkillNames.values()) {
            skillGoals.put(skill.name().toLowerCase(), Normalization.clampSkillLevel(level, skill.name()));
        }
        return skillGoals;
    }
}
