package com.godscape.rs3.enums.core;

import com.godscape.system.factories.DependencyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Rs3Managers {

    RS3_HEADER_MANAGER(null,null),
    RS3_FOOTER_MANAGER(null,null);

    private final Class<?> clazz;
    private final Supplier<?> supplier;

    @Override
    public String toString() {
        return this.name();
    }

    @SuppressWarnings("unchecked")
    public <T> T getManager() {
        return (T) supplier.get();
    }
}
