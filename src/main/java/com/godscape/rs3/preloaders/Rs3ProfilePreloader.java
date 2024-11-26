package com.godscape.rs3.preloaders;

import com.godscape.rs3.enums.core.Rs3Preloaders;
import com.godscape.system.interfaces.mark.Preloadable;
import com.godscape.system.preloaders.BaseProfilePreloader;
import com.godscape.system.utility.Logger;

public class Rs3ProfilePreloader extends BaseProfilePreloader implements Preloadable {

    // Singleton instance
    private static volatile Rs3ProfilePreloader instance;

    // Private constructor to prevent instantiation
    private Rs3ProfilePreloader() {
        Logger.info("Rs3ProfilePreloader: Singleton instance created.");
    }

    // Public method to retrieve the singleton instance
    public static Rs3ProfilePreloader getInstance() {
        if (instance == null) {
            synchronized (Rs3ProfilePreloader.class) {
                if (instance == null) {
                    instance = new Rs3ProfilePreloader();
                }
            }
        }
        return instance;
    }

    @Override
    public void preload() {
        Logger.info("Rs3ProfilePreloader: Starting RS3-specific preloading tasks.");
        runAllPreloaders();
    }

    private void runAllPreloaders() {
        for (Rs3Preloaders preloader : Rs3Preloaders.values()) {
            try {
                preloader.preload();
            } catch (Exception e) {
                Logger.error("Rs3ProfilePreloader: Error running preloader '{}': {}", preloader, e.getMessage());
            }
        }
    }
}
