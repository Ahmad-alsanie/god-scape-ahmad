package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EquipmentSlot {

    @Getter
    @RequiredArgsConstructor
    public enum Slot {
        HEAD(() -> "Head"),
        BODY(() -> "Body"),
        LEGS(() -> "Legs"),
        FEET(() -> "Feet"),
        HANDS(() -> "Hands"),
        WEAPON(() -> "Weapon"),
        SHIELD(() -> "Shield"),
        CAPE(() -> "Cape"),
        AMULET(() -> "Amulet"),
        RING(() -> "Ring"),
        QUIVER(() -> "Quiver");

        private final Supplier<String> slotNameSupplier;

        // Reverse lookup map
        private static final Map<String, Slot> lookup = new HashMap<>();

        static {
            for (Slot slot : Slot.values()) {
                lookup.put(slot.getSlotName(), slot);
            }
        }

        public String getSlotName() {
            return slotNameSupplier.get();
        }

        public static Slot get(String slotName) {
            return lookup.get(slotName);
        }
    }
}
