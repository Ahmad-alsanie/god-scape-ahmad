package com.godscape.rs3.enums.core;

import com.godscape.rs3.preloaders.*;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Preloadable;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public enum Rs3Preloaders implements Preloadable {

    RS3_DEFAULT_PROFILE_PRELOADER(() -> DependencyFactory.getInstance().getInjection(Rs3DefaultProfilePreloader.class)),
    RS3_CURRENT_STATS_PROFILE_PRELOADER(() -> DependencyFactory.getInstance().getInjection(Rs3CurrentStatsProfilePreloader.class)),
    RS3_LEVEL_SPREAD_PRELOADER(() -> DependencyFactory.getInstance().getInjection(Rs3LevelSpreadProfilePreloader.class)),
    RS3_PREMADE_PROFILES_PRELOADER(() -> (Preloadable) DependencyFactory.getInstance().getInjection(Rs3PremadeProfilesPreloader.class));

    private final Supplier<? extends Preloadable> supplier;

    Rs3Preloaders(Supplier<? extends Preloadable> supplier) {
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
}
