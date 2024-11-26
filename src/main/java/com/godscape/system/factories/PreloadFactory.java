package com.godscape.system.factories;

import com.godscape.system.enums.GameVersion;
import com.godscape.system.enums.Preloaders;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.preloaders.BaseProfilePreloader;
import com.godscape.system.preloaders.GlobalSettingsPreloader;
import com.godscape.system.preloaders.ThemePreloader;
import com.godscape.system.utility.Logger;

public class PreloadFactory {

    private static volatile PreloadFactory instance;
    private final PlatformFactory platformFactory;

    private PreloadFactory() {
        this.platformFactory = DependencyFactory.getInstance().getInjection(PlatformFactory.class);
        Logger.info("PreloadFactory: Initialized.");
    }

    public static PreloadFactory getInstance() {
        if (instance == null) {
            synchronized (PreloadFactory.class) {
                if (instance == null) {
                    instance = new PreloadFactory();
                }
            }
        }
        return instance;
    }

    public Object getPreloader(Preloaders preloader) {
        GameVersion currentGameVersion = platformFactory.getCurrentGameVersion();
        Logger.info("PreloadFactory: Detecting preloader for game version: {}", currentGameVersion);

        // Check for GLOBAL_SETTINGS_PRELOADER without casting
        if (preloader == Preloaders.GLOBAL_SETTINGS_PRELOADER) {
            GlobalSettingsPreloader globalSettingsPreloader = (GlobalSettingsPreloader) preloader.getSupplier().get();
            globalSettingsPreloader.preload();
            return globalSettingsPreloader;
        }

        // Check for THEME_PRELOADER without game version restriction
        if (preloader == Preloaders.THEME_PRELOADER) {
            ThemePreloader themePreloader = (ThemePreloader) preloader.getSupplier().get();
            themePreloader.initializeDatabase(null);  // Supply GlobalSettingsSchema if needed
            return themePreloader;
        }

        // Handle game-specific preloaders
        if (currentGameVersion == GameVersion.OSRS && preloader == Preloaders.OSRS_PROFILE_PRELOADER) {
            return getAndCheckBaseProfilePreloader(preloader);
        } else if (currentGameVersion == GameVersion.RS3 && preloader == Preloaders.RS3_PROFILE_PRELOADER) {
            return getAndCheckBaseProfilePreloader(preloader);
        }

        Logger.warn("PreloadFactory: Incompatible preloader {} for game version {}.", preloader.name(), currentGameVersion);
        return null;
    }

    private BaseProfilePreloader getAndCheckBaseProfilePreloader(Preloaders preloader) {
        Object obj = preloader.getSupplier().get();
        if (obj instanceof BaseProfilePreloader) {
            return (BaseProfilePreloader) obj;
        }
        Logger.error("PreloadFactory: Failed to cast preloader '{}' to BaseProfilePreloader.", preloader.name());
        return null;
    }
}
