package com.godscape.rs3.enums.game;

import java.util.HashMap;
import java.util.Map;

public enum Rs3SkillNames {
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
    DIVINATION("Divination"),
    INVENTION("Invention"),
    DUNGEONEERING("Dungeoneering"),
    ARCHAEOLOGY("Archaeology");

    private final String skillName;

    // Constructor for enum constants
    Rs3SkillNames(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() {
        return skillName;
    }

    // Static map for reverse lookup by skill name
    private static final Map<String, Rs3SkillNames> SKILL_MAP = new HashMap<>();

    static {
        for (Rs3SkillNames skill : Rs3SkillNames.values()) {
            SKILL_MAP.put(skill.getSkillName().toLowerCase(), skill);
        }
    }

    public static boolean isValidSkill(String name) {
        return name != null && SKILL_MAP.containsKey(name.toLowerCase());
    }

    public static Rs3SkillNames fromString(String name) {
        return SKILL_MAP.get(name.toLowerCase());
    }

    public static String[] getAllSkillNames() {
        return SKILL_MAP.keySet().toArray(new String[0]);
    }
}
