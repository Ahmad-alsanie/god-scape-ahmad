package com.godscape.osrs.managers;

import com.godscape.osrs.preloaders.OsrsPreloadLauncher;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.enums.ConfigKeys;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.preloaders.GlobalSettingsPreloader;
import com.godscape.system.schemas.GlobalSettingsSchema;
import com.godscape.system.utility.Logger;

@Singleton
public class OsrsPreloadManager {

    private final OsrsPreloadLauncher profilePreloader;

    // Constructor that initializes using the singleton instance
    public OsrsPreloadManager() {
        this.profilePreloader = DependencyFactory.getInstance().getInjection(OsrsPreloadLauncher.class); // Use singleton instance
    }

    public void initializePreload() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        if (dependencyFactory.getInjection(GodscapeConfig.class).getBoolean(ConfigKeys.OSRS_PRELOAD_PROFILES, true)) {
            Logger.info("OsrsPreloadManager: Preloading is enabled. Running profile preloaders.");
            profilePreloader.preload();
        } else {
            Logger.info("OsrsPreloadManager: Preloading is disabled. Skipping profile preload.");
        }
    }
}
