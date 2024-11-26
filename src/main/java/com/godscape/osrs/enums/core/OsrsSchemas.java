package com.godscape.osrs.enums.core;

import com.godscape.osrs.schemas.OsrsCharacterSchema;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Schemable;
import com.godscape.system.schemas.BaseSchema;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public enum OsrsSchemas implements Schemable {

    OSRS_PROFILE_SCHEMA(() -> DependencyFactory.getInstance().getInjection(OsrsProfileSchema.class), "OsrsProfilesCache"),
    OSRS_CHARACTER_SCHEMA(() -> DependencyFactory.getInstance().getInjection(OsrsCharacterSchema.class), "OsrsCharacterCache");

    private final Supplier<BaseSchema> supplier;
    private final String cacheName;

    public String getCacheName() {
        return cacheName;
    }

    OsrsSchemas(Supplier<BaseSchema> supplier, String cacheName) {
        this.supplier = supplier;
        this.cacheName = cacheName;
    }

    @Override
    public BaseSchema getSchema() {
        return supplier.get();
    }

    @Override
    public String toString() {
        return cacheName;
    }
}
