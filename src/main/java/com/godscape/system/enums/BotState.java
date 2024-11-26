package com.godscape.system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum BotState {
    STOPPED(() -> "Stopped"),   // Supplier for stopped state
    RUNNING(() -> "Running"),   // Supplier for running state
    PAUSED(() -> "Paused");     // Supplier for paused state

    private final Supplier<String> stateNameSupplier; // Supplier for state name

    // Get the state name using the supplier
    public String getStateName() {
        return stateNameSupplier.get(); // Retrieve the state name using the supplier
    }

    @Override
    public String toString() {
        return getStateName(); // Return the state name using the supplier
    }

    // Static method to get BotState from a string
    public static BotState fromString(String stateName) {
        for (BotState state : BotState.values()) {
            if (state.getStateName().equalsIgnoreCase(stateName)) {
                return state; // Return matching enum for the given string
            }
        }
        return null; // Return null if no match is found
    }
}
