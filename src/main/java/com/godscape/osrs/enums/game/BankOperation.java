package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BankOperation {

    @Getter
    @RequiredArgsConstructor
    public enum Operation {
        DEPOSIT(() -> "Deposit"), // Supplier for deposit action
        WITHDRAW(() -> "Withdraw"), // Supplier for withdraw action
        CHECK_BALANCE(() -> "Check Balance"), // Supplier for checking bank balance
        DEPOSIT_ALL(() -> "Deposit All"), // Supplier for deposit all items
        WITHDRAW_ALL(() -> "Withdraw All"), // Supplier for withdraw all items
        OPEN_BANK(() -> "Open Bank"), // Supplier for opening bank interface
        CLOSE_BANK(() -> "Close Bank"); // Supplier for closing bank interface

        private final Supplier<String> operationNameSupplier; // Supplier for the operation name

        // Static map for reverse lookup by operation name
        private static final Map<String, Operation> lookup = new HashMap<>();

        static {
            for (Operation op : Operation.values()) {
                lookup.put(op.getOperationName(), op); // Populate the map with operation names for reverse lookup
            }
        }

        // Retrieve the operation name using the supplier
        public String getOperationName() {
            return operationNameSupplier.get();
        }

        // Method to retrieve an operation by its name
        public static Operation get(String operationName) {
            return lookup.get(operationName); // Perform lookup based on the operation name
        }
    }
}
