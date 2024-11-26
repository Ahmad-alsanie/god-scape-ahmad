package com.godscape.rs3.enums.core;

import com.godscape.rs3.schemas.Rs3CharacterSchema;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Schemable;
import com.godscape.system.schemas.BaseSchema;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
public enum Rs3Schemas implements Schemable {

    RS3_PROFILE_SCHEMA(() -> DependencyFactory.getInstance().getInjection(Rs3ProfileSchema.class), "Rs3ProfilesCache"),
    RS3_CHARACTER_SCHEMA(() -> DependencyFactory.getInstance().getInjection(Rs3CharacterSchema.class), "Rs3CharacterCache");

    private final Supplier<BaseSchema> supplier;
    private final String cacheName;

    Rs3Schemas(Supplier<BaseSchema> supplier, String cacheName) {
        this.supplier = supplier;
        this.cacheName = cacheName;
    }

    @Override
    public BaseSchema getSchema() {
        return supplier.get();
    }

    public BaseSchema getSchemaInstance() {
        return supplier.get();
    }

    @Override
    public String toString() {
        return cacheName;
    }
}
