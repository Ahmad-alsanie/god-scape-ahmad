package com.godscape.system.enums;

import java.io.Serializable;

public enum GameVersion {

    OSRS("Old School RuneScape"),
    RS3("RuneScape 3"),
    UNKNOWN("Unknown");

    private final String displayName;

    // Enum constructor (implicitly private)
    GameVersion(String displayName) {
        this.displayName = displayName;
    }

    // Getter method for displayName
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
