package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum DamageType {
    PHYSICAL(() -> "Physical"), // Supplier for physical damage
    MAGICAL(() -> "Magical"), // Supplier for magical damage
    FIRE(() -> "Fire"), // Supplier for fire damage
    POISON(() -> "Poison"), // Supplier for poison damage
    LIGHTNING(() -> "Lightning"); // Supplier for lightning damage

    private final Supplier<String> typeSupplier; // Supplier for the damage type name

    public String getType() {
        return typeSupplier.get(); // Retrieve the damage type name from the supplier
    }
}
