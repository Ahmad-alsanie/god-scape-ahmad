package com.godscape.system.enums;

import com.godscape.osrs.enums.core.OsrsPreloaders;
import com.godscape.osrs.preloaders.OsrsPreloadLauncher;
import com.godscape.rs3.enums.core.Rs3Preloaders;
import com.godscape.rs3.preloaders.Rs3ProfilePreloader;
import com.godscape.system.preloaders.GlobalSettingsPreloader;
import com.godscape.system.preloaders.ThemePreloader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Preloaders {

    GLOBAL_SETTINGS_PRELOADER(GlobalSettingsPreloader.class, GlobalSettingsPreloader::getInstance),
    THEME_PRELOADER(ThemePreloader.class, ThemePreloader::getInstance),
    OSRS_PROFILE_PRELOADER(OsrsPreloadLauncher.class, OsrsPreloadLauncher::new),
    RS3_PROFILE_PRELOADER(Rs3ProfilePreloader.class, Rs3ProfilePreloader::getInstance),

    OSRS_PRELOADERS(OsrsPreloaders.class, () -> OsrsPreloaders.class),  // Grouped enum with supplier
    RS3_PRELOADERS(Rs3Preloaders.class, () -> Rs3Preloaders.class);     // Grouped enum with supplier

    private final Class<?> clazz;
    private final Supplier<?> supplier;

    @Override
    public String toString() {
        return this.name();
    }

    @SuppressWarnings("unchecked")
    public <T> T getPreloader() {
        return (T) supplier.get();
    }
}
