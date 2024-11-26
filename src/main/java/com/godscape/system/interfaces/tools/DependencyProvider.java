package com.godscape.system.interfaces.tools;

import java.util.function.Supplier;

public interface DependencyProvider {
    Supplier<?> getSupplier();
}
