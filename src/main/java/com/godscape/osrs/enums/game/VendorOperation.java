package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum VendorOperation {
    BUY(() -> "Buy Item"),
    SELL(() -> "Sell Item"),
    TRADE(() -> "Trade Items"),
    VIEW_STOCK(() -> "View Stock"),
    CANCEL_TRADE(() -> "Cancel Trade"),
    REQUEST_QUOTE(() -> "Request Price Quote");

    private final Supplier<String> operationNameSupplier;

    // Reverse lookup map
    private static final Map<String, VendorOperation> lookup = new HashMap<>();

    static {
        for (VendorOperation operation : VendorOperation.values()) {
            lookup.put(operation.getOperationName(), operation);
        }
    }

    public String getOperationName() {
        return operationNameSupplier.get();
    }

    public static VendorOperation get(String operationName) {
        return lookup.get(operationName);
    }
}
