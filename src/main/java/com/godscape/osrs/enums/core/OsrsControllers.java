package com.godscape.osrs.enums.core;

import com.godscape.osrs.controllers.*;
import com.godscape.system.factories.DependencyFactory;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public enum OsrsControllers {

    OSRS_CACHE_CONTROLLER(() -> DependencyFactory.getInstance().getInjection(OsrsCacheController.class)),
    OSRS_PROFILE_CONTROLLER(() -> DependencyFactory.getInstance().getInjection(OsrsProfilesController.class)),
    OSRS_CHARACTER_CONTROLLER(() -> DependencyFactory.getInstance().getInjection(OsrsCharacterController.class)),
    OSRS_SETTINGS_CONTROLLER(() -> DependencyFactory.getInstance().getInjection(OsrsSettingsController.class)),
    OSRS_BACKUP_CONTROLLER(() -> DependencyFactory.getInstance().getInjection(OsrsBackupController.class));


    private final Supplier<?> supplier;

    OsrsControllers(Supplier<?> supplier) {
        this.supplier = supplier;
    }

    public <T> T getController() {
        return (T) supplier.get();
    }
}
