package com.godscape.system.managers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.enums.Preloaders;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.preloaders.GlobalSettingsPreloader;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.managers.OsrsPreloadManager;
import com.godscape.rs3.managers.Rs3PreloadManager;

@Singleton
public class PreloadManager {

    private final PlatformFactory platformFactory;
    private final DependencyFactory dependencyFactory;

    public PreloadManager() {
        this.dependencyFactory = DependencyFactory.getInstance();
        this.platformFactory = dependencyFactory.getInjection(PlatformFactory.class);
        Logger.info("PreloadManager: Initialized.");
    }

    /**
     * Detects the game version and loads the appropriate preloaders.
     */
    public void loadPreloader() {
        preloadGlobalSettings();
        preloadGameSpecificManager();
    }

    /**
     * Loads and runs the GlobalSettingsPreloader.
     */
    private void preloadGlobalSettings() {
        GlobalSettingsPreloader globalSettingsPreloader = dependencyFactory.getInjection(GlobalSettingsPreloader.class);
        if (globalSettingsPreloader != null) {
            globalSettingsPreloader.preload();
            Logger.info("PreloadManager: Global settings preloaded successfully.");
        } else {
            Logger.error("PreloadManager: Failed to load global settings preloader.");
        }
    }

    /**
     * Determines and loads the appropriate game-specific preload manager.
     */
    private void preloadGameSpecificManager() {
        GameVersion currentGameVersion = platformFactory.getCurrentGameVersion();

        if (currentGameVersion == GameVersion.OSRS) {
            OsrsPreloadManager osrsManager = dependencyFactory.getInjection(OsrsPreloadManager.class);
            if (osrsManager != null) {
                osrsManager.initializePreload();
                Logger.info("PreloadManager: OSRS preloader manager initialized.");
            } else {
                Logger.error("PreloadManager: Failed to initialize OSRS preloader manager.");
            }
        } else if (currentGameVersion == GameVersion.RS3) {
            Rs3PreloadManager rs3Manager = dependencyFactory.getInjection(Rs3PreloadManager.class);
            if (rs3Manager != null) {
                rs3Manager.initializePreload();
                Logger.info("PreloadManager: RS3 preloader manager initialized.");
            } else {
                Logger.error("PreloadManager: Failed to initialize RS3 preloader manager.");
            }
        } else {
            Logger.warn("PreloadManager: No preloader manager found for {}", currentGameVersion);
        }
    }
}
