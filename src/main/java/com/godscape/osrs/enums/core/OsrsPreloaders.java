package com.godscape.osrs.enums.core;

import com.godscape.osrs.preloaders.*;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Preloadable;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public enum OsrsPreloaders implements Preloadable {

    OSRS_DEFAULT_PROFILE_PRELOADER(
            OsrsDefaultProfilePreloader.class,
            () -> DependencyFactory.getInstance().getInjection(OsrsDefaultProfilePreloader.class)
    ),
    OSRS_CURRENT_STATS_PROFILE_PRELOADER(
            OsrsCurrentStatsProfilePreloader.class,
            () -> DependencyFactory.getInstance().getInjection(OsrsCurrentStatsProfilePreloader.class)
    ),
    OSRS_LEVEL_SPREAD_PROFILES_PRELOADER(
            OsrsLevelSpreadProfilePreloader.class,
            () -> DependencyFactory.getInstance().getInjection(OsrsLevelSpreadProfilePreloader.class)
    ),
    OSRS_PREMADE_PROFILES_PRELOADER(
            OsrsPremadeProfilesPreloader.class,
            () -> DependencyFactory.getInstance().getInjection(OsrsPremadeProfilesPreloader.class)
    );

    private final Class<? extends Preloadable> clazz;
    private final Supplier<? extends Preloadable> supplier;

    OsrsPreloaders(Class<? extends Preloadable> clazz, Supplier<? extends Preloadable> supplier) {
        this.clazz = clazz;
        this.supplier = supplier;
    }

    @Override
    public void preload() {
        Preloadable instance = supplier.get();
        if (instance != null) {
            instance.preload();
        } else {
            throw new IllegalStateException("Preloadable instance not found for " + this.name());
        }
    }

    public Class<? extends Preloadable> getClazz() {
        return clazz;
    }
}
