package com.godscape.rs3.preloaders;

import com.godscape.rs3.enums.game.Rs3SkillNames;
import com.godscape.rs3.managers.Rs3CacheManager;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.rs3.enums.core.Rs3Panels;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.interfaces.mark.Preloadable;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.validation.Normalization;
import com.godscape.system.utility.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Preloader responsible for loading the RS3 Level Spread Profile into the cache.
 */
@Singleton
public class Rs3LevelSpreadProfilePreloader implements Preloadable {

    private final Rs3CacheManager rs3CacheManager;
    private final Rs3DefaultProfilePreloader rs3DefaultPreloader;
    private final List<Integer> levelSpread = Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99);

    public Rs3LevelSpreadProfilePreloader() {
        this.rs3CacheManager = DependencyFactory.getInstance().getInjection(Rs3CacheManager.class);
        this.rs3DefaultPreloader = DependencyFactory.getInstance().getInjection(Rs3DefaultProfilePreloader.class);
        Logger.info("Rs3LevelSpreadProfilePreloader: Initialized.");
    }

    @Override
    public void preload() {
        Logger.info("Rs3LevelSpreadProfilePreloader: Loading level spread profile.");

        Rs3ProfileSchema defaultProfile = rs3DefaultPreloader.createDefaultProfile();
        Map<String, Object> settingsMap = new HashMap<>(defaultProfile.getSettingsMap());

        settingsMap.put(
                Rs3Panels.RS3_STATS_SKILL_GOALS_PANEL.name().toLowerCase(),
                createSkillGoals()
        );

        Rs3ProfileSchema levelSpreadProfile = new Rs3ProfileSchema(
                UUID.nameUUIDFromBytes("LevelSpread".getBytes()),
                "LevelSpread",
                settingsMap,
                "Level spread profile with levels " + levelSpread,
                System.currentTimeMillis()
        );

        rs3CacheManager.addProfilesToCache(Collections.singletonList(levelSpreadProfile));
        Logger.info("Level spread profile created with name 'LevelSpread' and added to cache.");
    }

    /**
     * Creates skill goals based on the level spread and clamps values as necessary.
     *
     * @return A map of skill names to their respective goal levels.
     */
    private Map<String, Integer> createSkillGoals() {
        return IntStream.range(0, Rs3SkillNames.values().length)
                .boxed()
                .collect(Collectors.toMap(
                        i -> Rs3SkillNames.values()[i].getSkillName().toLowerCase(),
                        i -> Normalization.clampSkillLevel(levelSpread.get(i % levelSpread.size()), Rs3SkillNames.values()[i].getSkillName())
                ));
    }
}
