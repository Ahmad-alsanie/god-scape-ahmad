package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum EnvironmentType {
    FOREST(() -> "Forest"),
    DESERT(() -> "Desert"),
    MOUNTAIN(() -> "Mountain"),
    DUNGEON(() -> "Dungeon"),
    SWAMP(() -> "Swamp"),
    CITY(() -> "City");

    private final Supplier<String> typeSupplier;

    public String getType() {
        return typeSupplier.get();
    }
}
