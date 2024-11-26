package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum CharacterState {

    IDLE(() -> "Idle"), // Supplier for idle state
    WALKING(() -> "Walking"), // Supplier for walking state
    RUNNING(() -> "Running"), // Supplier for running state
    ATTACKING(() -> "Attacking"), // Supplier for attacking state
    DEFENDING(() -> "Defending"), // Supplier for defending state
    DEAD(() -> "Dead"), // Supplier for dead state
    INTERACTING(() -> "Interacting"); // Supplier for interacting state

    private final Supplier<String> stateNameSupplier; // Supplier to lazily provide state name

    public String getStateName() {
        return stateNameSupplier.get(); // Retrieve the state name from the Supplier
    }
}
