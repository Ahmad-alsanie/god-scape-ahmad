package com.godscape.rs3.managers;

import com.godscape.rs3.preloaders.Rs3ProfilePreloader;
import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.enums.ConfigKeys;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

public class Rs3PreloadManager {

    private final Rs3ProfilePreloader profilePreloader;

    public Rs3PreloadManager() {
        this.profilePreloader = Rs3ProfilePreloader.getInstance();
    }

    public void initializePreload() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        if (dependencyFactory.getInjection(GodscapeConfig.class).getBoolean(ConfigKeys.RS3_PRELOAD_PROFILES, true)) {
            Logger.info("Rs3PreloadManager: Preloading is enabled. Running profile preloaders.");
            profilePreloader.preload();
        } else {
            Logger.info("Rs3PreloadManager: Preloading is disabled. Skipping profile preload.");
        }
    }
}
