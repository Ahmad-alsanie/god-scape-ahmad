package com.godscape.osrs.schemas;

import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.osrs.enums.game.EquipmentSlot;
import com.godscape.osrs.enums.game.OsrsSkillNames;
import com.godscape.system.schemas.BaseSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class OsrsCharacterSchema implements Serializable, BaseSchema {

    private static final long serialVersionUID = 1L;

    // ----------------- Basic Information -----------------
    private UUID characterId;  // Add the characterId field
    private String characterName;
    private boolean isMember = false;
    private boolean isIronman = false;
    private long totalRunTime = 0L;
    private long sessionRuntime = 0L;
    private boolean isSprinting = false;
    private boolean isInteracting = false;
    private boolean isIdle = false;
    private boolean isAFK = false;
    private boolean isResting = false;

    // ----------------- Bank -----------------
    private final Map<String, Integer> bankItems = new ConcurrentHashMap<>();
    private long goldCoins = 0L;
    private final Map<String, Integer> bankTabs = new ConcurrentHashMap<>();

    // ----------------- Combat -----------------
    private int combatLevel = 0;
    private int hitpoints = 0;
    private int maxHitpoints = 0;
    private int prayerPoints = 0;
    private int maxPrayerPoints = 0;
    private int specialAttackEnergy = 0;
    private int runEnergy = 0;
    private boolean isPoisoned = false;
    private boolean isVenomed = false;
    private boolean isInCombat = false;
    private boolean isSkulled = false;
    private final Map<String, Long> activeBuffs = new ConcurrentHashMap<>();
    private final Map<String, Long> activeDebuffs = new ConcurrentHashMap<>();
    private final Map<String, Integer> combatStyles = new ConcurrentHashMap<>();
    private String currentTarget;
    private String nextTarget;
    private String weaponEquipped;
    private String armorEquipped;
    private double currentEnemyHealth = 0.0;
    private double enemyMaxHealth = 0.0;
    private boolean isEnemyDefeated = false;

    // ----------------- Grand Exchange Offers -----------------
    private final Map<String, String> grandExchangeOffers = new ConcurrentHashMap<>();

    // ----------------- Inventory -----------------
    private final Map<String, Integer> inventoryItems = new ConcurrentHashMap<>();
    private final Map<String, Integer> equippedInventoryItems = new ConcurrentHashMap<>();

    // ----------------- Equipment -----------------
    private final Map<EquipmentSlot.Slot, String> equippedItems = new EnumMap<>(EquipmentSlot.Slot.class);
    private final Map<EquipmentSlot.Slot, Integer> equipmentStats = new EnumMap<>(EquipmentSlot.Slot.class);

    // ----------------- Location -----------------
    private String currentArea;
    private final Set<String> areasVisited = ConcurrentHashMap.newKeySet();
    private final double[] currentPosition = new double[3];
    private final double[] lastKnownPosition = new double[3];
    private String currentWeather;
    private String currentRegion;
    private final List<String> landmarksVisited = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Instant> regionEntryTimes = new ConcurrentHashMap<>();

    // ----------------- Skills -----------------
    private int skillTotal = 0;
    private final EnumMap<OsrsSkillNames, Integer> skills = new EnumMap<>(OsrsSkillNames.class);
    private final Map<String, Integer> skillExperience = new ConcurrentHashMap<>();
    private final Map<String, Integer> skillLevels = new ConcurrentHashMap<>();
    private final Map<String, Integer> skillGoals = new ConcurrentHashMap<>();

    // ----------------- Additional Fields -----------------
    private String notes;
    private boolean isSynced = false;
    private boolean isActivated = false;

    // ----------------- Getter and Setter for characterId -----------------
    public UUID getCharacterId() {
        return characterId;
    }

    public void setCharacterId(UUID characterId) {
        this.characterId = characterId;
    }

    @Override
    public Map<String, Object> extractFields() {
        Map<String, Object> fields = new LinkedHashMap<>();

        // Adding basic information
        fields.put("characterId", characterId);  // Include characterId in the extracted fields
        fields.put("characterName", characterName);
        fields.put("isMember", isMember);
        fields.put("isIronman", isIronman);
        fields.put("totalRunTime", totalRunTime);
        fields.put("sessionRuntime", sessionRuntime);
        fields.put("isSprinting", isSprinting);
        fields.put("isInteracting", isInteracting);
        fields.put("isIdle", isIdle);
        fields.put("isAFK", isAFK);
        fields.put("isResting", isResting);

        // Adding bank information
        fields.put("bankItems", bankItems);
        fields.put("goldCoins", goldCoins);
        fields.put("bankTabs", bankTabs);

        // Adding combat information
        fields.put("combatLevel", combatLevel);
        fields.put("hitpoints", hitpoints);
        fields.put("maxHitpoints", maxHitpoints);
        fields.put("prayerPoints", prayerPoints);
        fields.put("maxPrayerPoints", maxPrayerPoints);
        fields.put("specialAttackEnergy", specialAttackEnergy);
        fields.put("runEnergy", runEnergy);
        fields.put("isPoisoned", isPoisoned);
        fields.put("isVenomed", isVenomed);
        fields.put("isInCombat", isInCombat);
        fields.put("isSkulled", isSkulled);
        fields.put("activeBuffs", activeBuffs);
        fields.put("activeDebuffs", activeDebuffs);
        fields.put("combatStyles", combatStyles);
        fields.put("currentTarget", currentTarget);
        fields.put("nextTarget", nextTarget);
        fields.put("weaponEquipped", weaponEquipped);
        fields.put("armorEquipped", armorEquipped);
        fields.put("currentEnemyHealth", currentEnemyHealth);
        fields.put("enemyMaxHealth", enemyMaxHealth);
        fields.put("isEnemyDefeated", isEnemyDefeated);

        // Adding grand exchange offers
        fields.put("grandExchangeOffers", grandExchangeOffers);

        // Adding inventory information
        fields.put("inventoryItems", inventoryItems);
        fields.put("equippedInventoryItems", equippedInventoryItems);

        // Adding equipment information
        fields.put("equippedItems", equippedItems);
        fields.put("equipmentStats", equipmentStats);

        // Adding location information
        fields.put("currentArea", currentArea);
        fields.put("areasVisited", areasVisited);
        fields.put("currentPosition", currentPosition);
        fields.put("lastKnownPosition", lastKnownPosition);
        fields.put("currentWeather", currentWeather);
        fields.put("currentRegion", currentRegion);
        fields.put("landmarksVisited", landmarksVisited);
        fields.put("regionEntryTimes", regionEntryTimes);

        // Adding skills information
        fields.put("skillTotal", skillTotal);
        fields.put("skills", skills);
        fields.put("skillExperience", skillExperience);
        fields.put("skillLevels", skillLevels);
        fields.put("skillGoals", skillGoals);

        // Adding additional fields
        fields.put("notes", notes);
        fields.put("isSynced", isSynced);
        fields.put("isActivated", isActivated);

        return fields;
    }

    @Override
    public String getCacheName() {
        return OsrsSchemas.OSRS_CHARACTER_SCHEMA.getCacheName(); // Assuming this is correct
    }

    @Override
    public OsrsCharacterSchema getSchemaInstance() {
        return new OsrsCharacterSchema();
    }

    @Override
    public void setSettingsMap(Map<String, Object> settingsMap) {
        if (settingsMap == null) {
            throw new UnsupportedOperationException("setSettingsMap() cannot accept null settings.");
        }

        // Update the schema fields based on the settingsMap
        settingsMap.forEach((key, value) -> {
            switch (key) {
                case "characterName":
                    this.characterName = value.toString();
                    break;
                case "isMember":
                    this.isMember = Boolean.parseBoolean(value.toString());
                    break;
                case "isIronman":
                    this.isIronman = Boolean.parseBoolean(value.toString());
                    break;
                case "totalRunTime":
                    this.totalRunTime = Long.parseLong(value.toString());
                    break;
                case "sessionRuntime":
                    this.sessionRuntime = Long.parseLong(value.toString());
                    break;
                case "skillGoals":
                    if (value instanceof Map) {
                        this.skillGoals.putAll((Map<String, Integer>) value);
                    }
                    break;
                // Add more cases for other fields as needed
                default:
                    throw new UnsupportedOperationException("setSettingsMap() does not support the field: " + key);
            }
        });
    }

}
