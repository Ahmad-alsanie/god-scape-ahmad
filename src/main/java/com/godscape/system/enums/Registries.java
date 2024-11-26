package com.godscape.system.enums;

import com.godscape.system.registries.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Registries {

    DEPENDENCY_REGISTRY(DependencyRegistry::getInstance),
    PLATFORM_REGISTRY(PlatformRegistry::getInstance),
    FACTORY_REGISTRY(FactoryRegistry::getInstance),
    MANAGER_REGISTRY(ManagersRegistry::getInstance),
    CONTROLLER_REGISTRY(ControllerRegistry::getInstance),
    INTERFACE_REGISTRY(InterfaceRegistry::new),
    SCHEMA_REGISTRY(SchemaRegistry::getInstance),
    OBSERVATION_REGISTRY(ObservationRegistry::getInstance),
    FRAME_REGISTRY(FramesRegistry::getInstance),
    PANEL_REGISTRY(PanelRegistry::getInstance),
    PRELOADER_REGISTRY(PreloaderRegistry::getInstance),
    UTILITY_REGISTRY(UtilityRegistry::getInstance),
    CONFIG_REGISTRY(ConfigRegistry::getInstance);

    private final Supplier<?> injector;

    @Override
    public String toString() {
        return this.name();
    }
}
