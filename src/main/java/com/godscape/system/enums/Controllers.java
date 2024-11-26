package com.godscape.system.enums;

import com.godscape.osrs.enums.core.OsrsControllers;
import com.godscape.rs3.enums.core.Rs3Controllers;
import com.godscape.system.controllers.BotController;
import com.godscape.system.controllers.BaseObservationController;
import com.godscape.system.controllers.SettingsController;
import com.godscape.system.factories.DependencyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Controllers {

    BOT_CONTROLLER(BotController.class, () -> DependencyFactory.getInstance().getInjection(BotController.class)),
    SETTINGS_CONTROLLER(SettingsController.class, () -> DependencyFactory.getInstance().getInjection(SettingsController.class)),
    OBSERVATION_CONTROLLER(BaseObservationController.class, () -> DependencyFactory.getInstance().getInjection(BaseObservationController.class)),

    OSRS_CONTROLLERS(OsrsControllers.class, () -> OsrsControllers.class),
    RS3_CONTROLLERS(Rs3Controllers.class, () -> Rs3Controllers.class);

    private final Class<?> clazz;
    private final Supplier<?> supplier;

    @Override
    public String toString() {
        return this.name();
    }

    @SuppressWarnings("unchecked")
    public <T> T getController() {
        return (T) supplier.get();
    }
}
