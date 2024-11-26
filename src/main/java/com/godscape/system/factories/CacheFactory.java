package com.godscape.system.factories;

import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.rs3.controllers.Rs3CacheController;
import com.godscape.rs3.managers.Rs3CacheManager;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;

//TODO: auto clear dache after x getinjections
@Singleton
public class CacheFactory {

    private static volatile CacheFactory instance;

    private CacheFactory() {
        Logger.info("CacheFactory: Initialization complete.");
    }

    public Object getCacheManager() {
        GameVersion gameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();
        switch (gameVersion) {
            case OSRS:
                return DependencyFactory.getInstance().getInjection(OsrsCacheController.class);
            case RS3:
                return DependencyFactory.getInstance().getInjection(Rs3CacheController.class);
            default:
                throw new IllegalStateException("Unknown or unsupported GameVersion: " + gameVersion);
        }
    }
}
