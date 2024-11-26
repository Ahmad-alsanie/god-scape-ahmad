    package com.godscape.system.enums;

    import com.godscape.system.factories.*;
    import lombok.Getter;
    import lombok.RequiredArgsConstructor;

    import java.util.function.Supplier;

    @Getter
    @RequiredArgsConstructor
    public enum Factories {

        PLATFORM_FACTORY(PlatformFactory.class, () -> DependencyFactory.getInstance().getInjection(PlatformFactory.class)),
        MANAGER_FACTORY(ManagerFactory.class, () -> DependencyFactory.getInstance().getInjection(ManagerFactory.class)),
        CONTROLLER_FACTORY(ControllerFactory.class, () -> DependencyFactory.getInstance().getInjection(ControllerFactory.class)),
        INTERFACE_FACTORY(InterfaceFactory.class, () -> DependencyFactory.getInstance().getInjection(InterfaceFactory.class)),
        OBSERVATION_FACTORY(ObservationFactory.class, () -> DependencyFactory.getInstance().getInjection(ObservationFactory.class)),
        SERIALIZABLE_FACTORY(SerializableFactory.class, () -> new SerializableFactory()),
        SCHEMA_FACTORY(SchemaFactory.class, () -> DependencyFactory.getInstance().getInjection(SchemaFactory.class)),
        FRAME_FACTORY(FrameFactory.class, () -> DependencyFactory.getInstance().getInjection(FrameFactory.class)),
        PANEL_FACTORY(PanelFactory.class, () -> DependencyFactory.getInstance().getInjection(PanelFactory.class)),
        THEME_FACTORY(ThemeFactory.class, () -> DependencyFactory.getInstance().getInjection(ThemeFactory.class)),
        UTILITY_FACTORY(UtilityFactory.class, () -> DependencyFactory.getInstance().getInjection(UtilityFactory.class));

        private final Class<?> clazz;
        private final Supplier<?> supplier;

        @Override
        public String toString() {
            return this.name();
        }

        @SuppressWarnings("unchecked")
        public <T> T getFactory() {
            return (T) supplier.get();
        }
    }
