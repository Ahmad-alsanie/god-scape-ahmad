package com.godscape.system.enums;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.rs3.enums.core.Rs3Panels;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Panels {

    OSRS_PANELS(OsrsPanels.class, () -> OsrsPanels.class),
    RS3_PANELS(Rs3Panels.class, () -> Rs3Panels.class);

    private final Class<?> clazz;
    private final Supplier<?> supplier;

    @Override
    public String toString() {
        return this.name();
    }

    @SuppressWarnings("unchecked")
    public <T> T getPanel() {
        return (T) supplier.get();
    }
}
