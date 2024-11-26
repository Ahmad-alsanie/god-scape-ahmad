package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GrandExchangeOperation {

    @Getter
    @RequiredArgsConstructor
    public enum Operation {
        BUY(() -> "Buy"),
        SELL(() -> "Sell"),
        COLLECT(() -> "Collect"),
        CANCEL_OFFER(() -> "Cancel Offer"),
        CHECK_OFFER_STATUS(() -> "Check Offer Status"),
        VIEW_HISTORY(() -> "View History"),
        OPEN_GE(() -> "Open Grand Exchange"),
        CLOSE_GE(() -> "Close Grand Exchange");

        private final Supplier<String> operationNameSupplier;

        private static final Map<String, Operation> lookup = new HashMap<>();

        static {
            for (Operation op : Operation.values()) {
                lookup.put(op.getOperationName(), op);
            }
        }

        public String getOperationName() {
            return operationNameSupplier.get();
        }

        public static Operation get(String operationName) {
            return lookup.get(operationName);
        }
    }
}
