package com.godscape.osrs.preloaders;

import com.godscape.osrs.enums.core.OsrsPreloaders;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Preloadable;
import com.godscape.system.preloaders.BaseProfilePreloader;
import com.godscape.system.preloaders.GlobalSettingsPreloader;
import com.godscape.system.utility.Logger;

/**
 * Singleton class responsible for launching all OSRS-specific preloaders.
 */
@Singleton
public class OsrsPreloadLauncher extends BaseProfilePreloader implements Preloadable {

    /**
     * Private constructor to enforce Singleton pattern.
     */
    public OsrsPreloadLauncher() {
        Logger.info("OsrsPreloadLauncher: Singleton instance created.");
    }

    /**
     * Inner static class responsible for holding the Singleton instance.
     */
    private static class Holder {
        private static final OsrsPreloadLauncher INSTANCE = new OsrsPreloadLauncher();
    }

    /**
     * Initiates the preloading process by running all defined preloaders.
     */
    @Override
    public void preload() {
        Logger.info("OsrsPreloadLauncher: Starting OSRS-specific preloading tasks.");
        runAllPreloaders();
    }

    /**
     * Iterates through the OsrsPreloaders enum and executes each preloader.
     */
    private void runAllPreloaders() {
        for (OsrsPreloaders preloader : OsrsPreloaders.values()) {
            try {
                DependencyFactory.getInstance().getInjection(GlobalSettingsPreloader.class).preload();
                Logger.info("OsrsPreloadLauncher: Running preloader '{}'.", preloader.name());
                preloader.preload();
                Logger.info("OsrsPreloadLauncher: Successfully ran preloader '{}'.", preloader.name());
            } catch (Exception e) {
                Logger.error("OsrsPreloadLauncher: Error running preloader '{}': {}", preloader.name(), e.getMessage(), e);
            }
        }
    }
}
