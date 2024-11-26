package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ItemType {

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        WEAPON(() -> "Weapon"),
        ARMOR(() -> "Armor"),
        CONSUMABLE(() -> "Consumable"),
        MATERIAL(() -> "Material"),
        TOOL(() -> "Tool"),
        QUEST_ITEM(() -> "Quest Item"),
        RESOURCE(() -> "Resource"),
        RUNE(() -> "Rune"),
        HERB(() -> "Herb"),
        POTION(() -> "Potion"),
        AMMUNITION(() -> "Ammunition"),
        MISC(() -> "Miscellaneous");

        private final Supplier<String> typeNameSupplier;

        private static final Map<String, Type> lookup = new HashMap<>();

        static {
            for (Type type : Type.values()) {
                lookup.put(type.getTypeName(), type);
            }
        }

        public String getTypeName() {
            return typeNameSupplier.get();
        }

        public static Type get(String typeName) {
            return lookup.get(typeName);
        }
    }
}
