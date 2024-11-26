package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Pathfinding {
    A_STAR(() -> "A* Algorithm"),
    DIJKSTRA(() -> "Dijkstra's Algorithm"),
    BFS(() -> "Breadth-First Search"),
    DFS(() -> "Depth-First Search"),
    GREEDY_BEST_FIRST(() -> "Greedy Best-First Search"),
    JPS(() -> "Jump Point Search");

    private final Supplier<String> algorithmNameSupplier;

    // Reverse lookup map
    private static final Map<String, Pathfinding> lookup = new HashMap<>();

    static {
        for (Pathfinding algorithm : Pathfinding.values()) {
            lookup.put(algorithm.getAlgorithmName(), algorithm);
        }
    }

    public String getAlgorithmName() {
        return algorithmNameSupplier.get();
    }

    public static Pathfinding get(String algorithmName) {
        return lookup.get(algorithmName);
    }
}
