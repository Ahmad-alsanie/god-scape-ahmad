package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum QuestStatus {
    NOT_STARTED(() -> "Not Started"),
    IN_PROGRESS(() -> "In Progress"),
    COMPLETED(() -> "Completed"),
    FAILED(() -> "Failed");

    private final Supplier<String> statusSupplier;

    // Reverse lookup map
    private static final Map<String, QuestStatus> lookup = new HashMap<>();

    static {
        for (QuestStatus status : QuestStatus.values()) {
            lookup.put(status.getStatus(), status);
        }
    }

    public String getStatus() {
        return statusSupplier.get();
    }

    public static QuestStatus get(String status) {
        return lookup.get(status);
    }
}
