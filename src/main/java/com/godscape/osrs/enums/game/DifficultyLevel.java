package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum DifficultyLevel {
    EASY(() -> "Easy"), // Supplier for Easy difficulty
    NORMAL(() -> "Normal"), // Supplier for Normal difficulty
    HARD(() -> "Hard"), // Supplier for Hard difficulty
    EXPERT(() -> "Expert"), // Supplier for Expert difficulty
    NIGHTMARE(() -> "Nightmare"); // Supplier for Nightmare difficulty

    private final Supplier<String> levelSupplier; // Supplier for the difficulty level

    public String getLevel() {
        return levelSupplier.get(); // Retrieve the difficulty level from the supplier
    }
}
