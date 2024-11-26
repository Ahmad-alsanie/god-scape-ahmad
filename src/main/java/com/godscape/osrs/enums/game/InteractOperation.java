package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InteractOperation {

    @Getter
    @RequiredArgsConstructor
    public enum InteractionType {
        TALK(() -> "Talk"),
        ATTACK(() -> "Attack"),
        TRADE(() -> "Trade"),
        EXAMINE(() -> "Examine"),
        USE(() -> "Use"),
        PICKUP(() -> "Pick Up"),
        DROP(() -> "Drop"),
        CAST_SPELL(() -> "Cast Spell"),
        OPEN(() -> "Open"),
        CLOSE(() -> "Close"),
        INSPECT(() -> "Inspect"),
        PICKPOCKET(() -> "Pickpocket"),
        INTERACT(() -> "Interact"),
        ENTER(() -> "Enter"),
        EXIT(() -> "Exit"),
        SEARCH(() -> "Search"),
        CHOP(() -> "Chop"),
        FISH(() -> "Fish"),
        MINE(() -> "Mine");

        private final Supplier<String> operationTypeSupplier;

        private static final Map<String, InteractionType> lookup = new HashMap<>();

        static {
            for (InteractionType interaction : InteractionType.values()) {
                lookup.put(interaction.getInteractionName(), interaction);
            }
        }

        public String getInteractionName() {
            return operationTypeSupplier.get();
        }

        public static InteractionType get(String interactionName) {
            return lookup.get(interactionName);
        }
    }
}
