package com.godscape.osrs.enums.core;

import com.godscape.osrs.managers.*;
import com.godscape.osrs.managers.panels.OsrsFooterManager;
import com.godscape.osrs.managers.panels.OsrsHeaderManager;
import com.godscape.system.factories.DependencyFactory;
import lombok.Getter;

import java.util.function.Supplier;

/**
 * Enum for managing OSRS-specific manager classes with lazy loading and compile-time safety.
 */
@Getter
public enum OsrsManagers {

    OSRS_CHARACTER_MANAGER(() -> DependencyFactory.getInstance().getInjection(OsrsCharacterManager.class)),
    OSRS_PROFILE_MANAGER(() -> DependencyFactory.getInstance().getInjection(OsrsProfilesManager.class)),
    OSRS_CACHE_MANAGER(() -> DependencyFactory.getInstance().getInjection(OsrsCacheManager.class)),
    OSRS_OBSERVATION_MANAGER(() -> DependencyFactory.getInstance().getInjection(OsrsObservationManager.class)),
    OSRS_SETTINGS_MANAGER(() -> DependencyFactory.getInstance().getInjection(OsrsSettingsManager.class)),

    OSRS_FOOTER_MANAGER(() -> DependencyFactory.getInstance().getInjection(OsrsFooterManager.class)),
    OSRS_HEADER_MANAGER(() -> DependencyFactory.getInstance().getInjection(OsrsHeaderManager.class));

    private final Supplier<?> supplier;

    OsrsManagers(Supplier<?> supplier) {
        this.supplier = supplier;
    }

    public <T> T getManager() {
        return (T) supplier.get();
    }
}
