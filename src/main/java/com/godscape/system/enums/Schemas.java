package com.godscape.system.enums;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.schemas.GlobalSettingsSchema;
import com.godscape.system.schemas.ThemeSchema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Schemas {

    THEME_SCHEMA(ThemeSchema.class, () -> DependencyFactory.getInstance().getInjection(ThemeSchema.class)),
    GLOBAL_SETTINGS_SCHEMA(GlobalSettingsSchema.class, () -> DependencyFactory.getInstance().getInjection(GlobalSettingsSchema.class)),

    OSRS_SCHEMAS(OsrsProfileSchema.class, () -> DependencyFactory.getInstance().getInjection(OsrsProfileSchema.class)),
    RS3_SCHEMAS(Rs3ProfileSchema.class, () -> DependencyFactory.getInstance().getInjection(Rs3ProfileSchema.class));

    private final Class<?> clazz;
    private final Supplier<?> supplier;

    @Override
    public String toString() {
        return this.name();
    }

    @SuppressWarnings("unchecked")
    public <T> T getSchema() {
        return (T) supplier.get();
    }
}
