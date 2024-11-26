package com.godscape.system.factories;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Controllers;
import com.godscape.system.enums.Registries;
import com.godscape.system.utility.Logger;

@Singleton
public class ControllerFactory {

    private final DependencyFactory dependencyFactory;

    private ControllerFactory() {
        Logger.info("Initializing ControllerFactory...");
        this.dependencyFactory = DependencyFactory.getInstance();
    }

    @SuppressWarnings("unchecked")
    public <T> T getController(Controllers controllerEnum) {
        Logger.info("Retrieving controller: {}", controllerEnum.name());
        return (T) dependencyFactory.getInjection(Registries.valueOf(controllerEnum.name()));
    }
}
