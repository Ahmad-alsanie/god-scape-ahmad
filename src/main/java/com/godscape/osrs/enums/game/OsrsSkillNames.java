package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum OsrsSkillNames {
    ATTACK("Attack"),
    STRENGTH("Strength"),
    DEFENCE("Defence"),
    RANGED("Ranged"),
    MAGIC("Magic"),
    HITPOINTS("Hitpoints"),
    PRAYER("Prayer"),
    WOODCUTTING("Woodcutting"),
    FISHING("Fishing"),
    MINING("Mining"),
    SMITHING("Smithing"),
    COOKING("Cooking"),
    FIREMAKING("Firemaking"),
    CRAFTING("Crafting"),
    FLETCHING("Fletching"),
    RUNECRAFTING("Runecrafting"),
    HUNTER("Hunter"),
    CONSTRUCTION("Construction"),
    AGILITY("Agility"),
    THIEVING("Thieving"),
    FARMING("Farming"),
    SLAYER("Slayer"),
    HERBLORE("Herblore"),
    SAILING("Sailing");

    private final String skillName;

    // Static map for reverse lookup by skill name
    private static final Map<String, OsrsSkillNames> SKILL_MAP = new HashMap<>();

    static {
        for (OsrsSkillNames skill : OsrsSkillNames.values()) {
            SKILL_MAP.put(skill.getSkillName().toLowerCase(), skill);
        }
    }

    public static boolean isValidSkill(String name) {
        return name != null && SKILL_MAP.containsKey(name.toLowerCase());
    }

    public static OsrsSkillNames fromString(String name) {
        return SKILL_MAP.get(name.toLowerCase());
    }

    public static String[] getAllSkillNames() {
        return SKILL_MAP.keySet().toArray(new String[0]);
    }
}
