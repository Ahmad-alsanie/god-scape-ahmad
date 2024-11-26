package com.godscape.osrs.enums.core;

import com.godscape.osrs.frames.panels.adventures.*;
import com.godscape.osrs.frames.panels.finances.*;
import com.godscape.osrs.frames.panels.loadouts.OsrsLoadoutsStrategy;
import com.godscape.osrs.frames.panels.loadouts.dragons.OsrsLoadoutsDragonsConsumablesPanel;
import com.godscape.osrs.frames.panels.loadouts.dragons.OsrsLoadoutsDragonsEquipmentPanel;
import com.godscape.osrs.frames.panels.loadouts.exploration.OsrsLoadoutsExplorationConsumablesPanel;
import com.godscape.osrs.frames.panels.loadouts.exploration.OsrsLoadoutsExplorationEquipmentPanel;
import com.godscape.osrs.frames.panels.loadouts.magic.OsrsLoadoutsMagicConsumablesPanel;
import com.godscape.osrs.frames.panels.loadouts.magic.OsrsLoadoutsMagicEquipmentPanel;
import com.godscape.osrs.frames.panels.loadouts.melee.OsrsLoadoutsMeleeConsumablesPanel;
import com.godscape.osrs.frames.panels.loadouts.melee.OsrsLoadoutsMeleeEquipmentPanel;
import com.godscape.osrs.frames.panels.loadouts.metal.OsrsLoadoutsMetalDragonsConsumablesPanel;
import com.godscape.osrs.frames.panels.loadouts.metal.OsrsLoadoutsMetalDragonsEquipmentPanel;
import com.godscape.osrs.frames.panels.loadouts.ranged.OsrsLoadoutsRangedConsumablesPanel;
import com.godscape.osrs.frames.panels.loadouts.ranged.OsrsLoadoutsRangedEquipmentPanel;
import com.godscape.osrs.frames.panels.settings.antibot.OsrsSettingsAntiBotDetectionConfigPanel;
import com.godscape.osrs.frames.panels.settings.antibot.OsrsSettingsAntiBotDetectionSlidersPanel;
import com.godscape.osrs.frames.panels.settings.OsrsSettingsPathfindingPanel;
import com.godscape.osrs.frames.panels.skilling.artisan.*;
import com.godscape.osrs.frames.panels.skilling.combat.*;
import com.godscape.osrs.frames.panels.skilling.combat.OsrsSkillingGatheringFarmingPanel;
import com.godscape.osrs.frames.panels.skilling.gathering.OsrsSkillingGatheringFishingPanel;
import com.godscape.osrs.frames.panels.skilling.combat.OsrsSkillingGatheringHunterPanel;
import com.godscape.osrs.frames.panels.skilling.combat.OsrsSkillingGatheringMiningPanel;
import com.godscape.osrs.frames.panels.skilling.combat.OsrsSkillingGatheringRunecraftingPanel;
import com.godscape.osrs.frames.panels.skilling.combat.OsrsSkillingGatheringWoodcuttingPanel;
import com.godscape.osrs.frames.panels.skilling.support.OsrsSkillingSupportAgilityPanel;
import com.godscape.osrs.frames.panels.skilling.support.OsrsSkillingSupportFiremakingPanel;
import com.godscape.osrs.frames.panels.skilling.support.OsrsSkillingSupportSailingPanel;
import com.godscape.osrs.frames.panels.skilling.support.OsrsSkillingSupportThievingPanel;
import com.godscape.osrs.frames.panels.stats.*;
import com.godscape.osrs.frames.panels.wilderness.OsrsWildernessStrategyPanel;
import com.godscape.osrs.frames.panels.wilderness.dragons.OsrsWildernessDragonsConsumablesPanel;
import com.godscape.osrs.frames.panels.wilderness.dragons.OsrsWildernessDragonsEquipmentPanel;
import com.godscape.osrs.frames.panels.wilderness.exploration.OsrsWildernessExplorationConsumablesPanel;
import com.godscape.osrs.frames.panels.wilderness.exploration.OsrsWildernessExplorationEquipmentPanel;
import com.godscape.osrs.frames.panels.wilderness.magic.OsrsWildernessMagicConsumablesPanel;
import com.godscape.osrs.frames.panels.wilderness.magic.OsrsWildernessMagicEquipmentPanel;
import com.godscape.osrs.frames.panels.wilderness.melee.OsrsWildernessMeleeConsumablesPanel;
import com.godscape.osrs.frames.panels.wilderness.melee.OsrsWildernessMeleeEquipmentPanel;
import com.godscape.osrs.frames.panels.wilderness.metal.OsrsWildernessMetalDragonsConsumablesPanel;
import com.godscape.osrs.frames.panels.wilderness.metal.OsrsWildernessMetalDragonsEquipmentPanel;
import com.godscape.osrs.frames.panels.wilderness.ranged.OsrsWildernessRangedConsumablesPanel;
import com.godscape.osrs.frames.panels.wilderness.ranged.OsrsWildernessRangedEquipmentPanel;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.frames.panels.DebugPanel;
import com.godscape.system.templates.*;
import lombok.Getter;

import javax.swing.*;
import java.util.*;
import java.util.function.Supplier;

@Getter
public enum OsrsPanels {

    OSRS_MAIN_PANEL("Main", null, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(EmptyPanel.class)),
    OSRS_HEADER_PANEL("Header", OSRS_MAIN_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(EmptyPanel.class)),
    OSRS_FOOTER_PANEL("Footer", OSRS_MAIN_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(EmptyPanel.class)),

    // OSRS Stats Panels
    OSRS_STATS_PANEL("Stats", null, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(EmptyPanel.class)),
    OSRS_STATS_SKILL_GOALS_PANEL("Skill Goals", OSRS_STATS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsStatsSkillGoalsPanel.class)),
    OSRS_STATS_LEVELING_TWEAKS_PANEL("Leveling Tweaks", OSRS_STATS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsStatsLevelingTweaksPanel.class)),

    // OSRS Skilling Panels
    OSRS_SKILLING_PANEL("Skilling", null, GameVersion.OSRS, () -> null),
    OSRS_SKILLING_COMBAT_PANEL("Combat", OSRS_SKILLING_PANEL, GameVersion.OSRS, () -> null),
    OSRS_SKILLING_ATTACK_PANEL("Melee", OSRS_SKILLING_COMBAT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingCombatMeleePanel.class)),
    OSRS_SKILLING_RANGED_PANEL("Ranged", OSRS_SKILLING_COMBAT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingCombatRangedPanel.class)),
    OSRS_SKILLING_MAGIC_PANEL("Magic", OSRS_SKILLING_COMBAT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingCombatMagicPanel.class)),
    OSRS_SKILLING_PRAYER_PANEL("Prayer", OSRS_SKILLING_COMBAT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingCombatPrayerPanel.class)),
    OSRS_SKILLING_SLAYER_PANEL("Slayer", OSRS_SKILLING_COMBAT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingCombatSlayerPanel.class)),

    // OSRS Gathering Panels
    OSRS_SKILLING_GATHERING_PANEL("Gathering", OSRS_SKILLING_PANEL, GameVersion.OSRS, () -> null),
    OSRS_SKILLING_MINING_PANEL("Mining", OSRS_SKILLING_GATHERING_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingGatheringMiningPanel.class)),
    OSRS_SKILLING_WOODCUTTING_PANEL("Woodcutting", OSRS_SKILLING_GATHERING_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingGatheringWoodcuttingPanel.class)),
    OSRS_SKILLING_FISHING_PANEL("Fishing", OSRS_SKILLING_GATHERING_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingGatheringFishingPanel.class)),
    OSRS_SKILLING_HUNTER_PANEL("Hunter", OSRS_SKILLING_GATHERING_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingGatheringHunterPanel.class)),
    OSRS_SKILLING_FARMING("Farming", OSRS_SKILLING_GATHERING_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingGatheringFarmingPanel.class)),
    OSRS_SKILLING_RUNECRAFTING("Runecrafting", OSRS_SKILLING_GATHERING_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingGatheringRunecraftingPanel.class)),

    // OSRS Artisan Panels
    OSRS_SKILLING_ARTISAN_PANEL("Artisan", OSRS_SKILLING_PANEL, GameVersion.OSRS, () -> null),
    OSRS_SKILLING_COOKING_PANEL("Cooking", OSRS_SKILLING_ARTISAN_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingArtisanCookingPanel.class)),
    OSRS_SKILLING_CRAFTING_PANEL("Crafting", OSRS_SKILLING_ARTISAN_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingArtisanCraftingPanel.class)),
    OSRS_SKILLING_FLETCHING_PANEL("Fletching", OSRS_SKILLING_ARTISAN_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingArtisanFletchingPanel.class)),
    OSRS_SKILLING_SMITHING_PANEL("Smithing", OSRS_SKILLING_ARTISAN_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingArtisanSmithingPanel.class)),
    OSRS_SKILLING_HERBLORE_PANEL("Herblore", OSRS_SKILLING_ARTISAN_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingArtisanHerblorePanel.class)),
    OSRS_SKILLING_CONSTRUCTION_PANEL("Construction", OSRS_SKILLING_ARTISAN_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingArtisanConstructionPanel.class)),

    // OSRS Support Panels
    OSRS_SKILLING_SUPPORT_PANEL("Support", OSRS_SKILLING_PANEL, GameVersion.OSRS, () -> null),
    OSRS_SKILLING_FIREMAKING_PANEL("Firemaking", OSRS_SKILLING_SUPPORT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingSupportFiremakingPanel.class)),
    OSRS_SKILLING_AGILITY_PANEL("Agility", OSRS_SKILLING_SUPPORT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingSupportAgilityPanel.class)),
    OSRS_SKILLING_THIEVING_PANEL("Thieving", OSRS_SKILLING_SUPPORT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingSupportThievingPanel.class)),
    OSRS_SKILLING_SAILING_PANEL("Sailing", OSRS_SKILLING_SUPPORT_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSkillingSupportSailingPanel.class)),

    // OSRS Adventure Panels
    OSRS_ADVENTURES_PANEL("Adventures", null, GameVersion.OSRS, () -> null),
OSRS_ADVENTURES_QUESTS_PANEL("Quests", OSRS_ADVENTURES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsAdventuresQuestsPanel.class)),
    OSRS_ADVENTURES_DIARIES_PANEL("Diaries", OSRS_ADVENTURES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsAdventuresDiariesPanel.class)),
    OSRS_ADVENTURES_ACHIEVEMENTS_PANEL("Achievements", OSRS_ADVENTURES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsAdventuresAchievementsPanel.class)),
    OSRS_ADVENTURES_MINIGAMES_PANEL("Minigames", OSRS_ADVENTURES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsAdventuresMinigamesPanel.class)),
    OSRS_ADVENTURES_BOSSES_PANEL("Bosses", OSRS_ADVENTURES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsAdventuresBossesPanel.class)),
    OSRS_ADVENTURES_SLAYER_TASKS_PANEL("Slayer Tasks", OSRS_ADVENTURES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsAdventuresSlayerTasksPanel.class)),
    OSRS_ADVENTURES_TREASURE_TAILS("Treasure Trails", OSRS_ADVENTURES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsAdventuresTreasureTailsPanel.class)),
    OSRS_ADVENTURES_MONEY_MAKING_PANEL("Money Making", OSRS_ADVENTURES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsAdventuresMoneyMakingPanel.class)),

    // OSRS Finance Panels
    OSRS_FINANCES_PANEL("Finances", null, GameVersion.OSRS, () -> null),
    OSRS_FINANCES_MULE_PANEL("Mule", OSRS_FINANCES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsFinancesMulePanel.class)),
    OSRS_FINANCES_GOLD_PANEL("Gold", OSRS_FINANCES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsFinancesGoldPanel.class)),
    OSRS_FINANCES_WALLET_PANEL("Wallet", OSRS_FINANCES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsFinancesWalletPanel.class)),
    OSRS_FINANCES_LOOT_PANEL("Loot", OSRS_FINANCES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsFinancesLootPanel.class)),
    OSRS_FINANCES_BANK_PANEL("Bank", OSRS_FINANCES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsFinancesBankPanel.class)),
    OSRS_FINANCES_GRAND_EXCHANGE_PANEL("Grand Exchange", OSRS_FINANCES_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsFinancesGrandExchangePanel.class)),

    // OSRS Loadouts Panels
    OSRS_LOADOUTS_PANEL("Loadouts", null, GameVersion.OSRS, () -> null),
    OSRS_LOADOUTS_EXPLORATION_PANEL("Exploration", OSRS_LOADOUTS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_LOADOUTS_EXPLORATION_EQUIPMENT_PANEL("Equipment", OSRS_LOADOUTS_EXPLORATION_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsExplorationEquipmentPanel.class)),
    OSRS_LOADOUTS_EXPLORATION_CONSUMABLES_PANEL("Consumables", OSRS_LOADOUTS_EXPLORATION_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsExplorationConsumablesPanel.class)),

    OSRS_LOADOUTS_MELEE_PANEL("Melee", OSRS_LOADOUTS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_LOADOUTS_MELEE_EQUIPMENT_PANEL("Equipment", OSRS_LOADOUTS_MELEE_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsMeleeEquipmentPanel.class)),
    OSRS_LOADOUTS_MELEE_CONSUMABLES_PANEL("Consumables", OSRS_LOADOUTS_MELEE_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsMeleeConsumablesPanel.class)),

    OSRS_LOADOUTS_RANGED_PANEL("Ranged", OSRS_LOADOUTS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_LOADOUTS_RANGED_EQUIPMENT_PANEL("Equipment", OSRS_LOADOUTS_RANGED_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsRangedEquipmentPanel.class)),
    OSRS_LOADOUTS_RANGED_CONSUMABLES_PANEL("Consumables", OSRS_LOADOUTS_RANGED_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsRangedConsumablesPanel.class)),

    OSRS_LOADOUTS_MAGIC_PANEL("Magic", OSRS_LOADOUTS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_LOADOUTS_MAGIC_EQUIPMENT_PANEL("Equipment", OSRS_LOADOUTS_MAGIC_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsMagicEquipmentPanel.class)),
    OSRS_LOADOUTS_MAGIC_CONSUMABLES_PANEL("Consumables", OSRS_LOADOUTS_MAGIC_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsMagicConsumablesPanel.class)),

    OSRS_LOADOUTS_DRAGONS_PANEL("Dragons", OSRS_LOADOUTS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_LOADOUTS_DRAGONS_EQUIPMENT_PANEL("Equipment", OSRS_LOADOUTS_DRAGONS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsDragonsEquipmentPanel.class)),
    OSRS_LOADOUTS_DRAGONS_CONSUMABLES_PANEL("Consumables", OSRS_LOADOUTS_DRAGONS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsDragonsConsumablesPanel.class)),

    OSRS_LOADOUTS_METAL_DRAGONS_PANEL("Metal Dragons", OSRS_LOADOUTS_PANEL, GameVersion.OSRS, () ->  null),
    OSRS_LOADOUTS_METAL_DRAGONS_EQUIPMENT_PANEL("Equipment", OSRS_LOADOUTS_METAL_DRAGONS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsMetalDragonsEquipmentPanel.class)),
    OSRS_LOADOUTS_METAL_DRAGONS_CONSUMABLES_PANEL("Consumables", OSRS_LOADOUTS_METAL_DRAGONS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsMetalDragonsConsumablesPanel.class)),

    OSRS_LOADOUTS_STRATEGY_PANEL("Strategy", OSRS_LOADOUTS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsLoadoutsStrategy.class)),

    // OSRS Wilderness Panels
    OSRS_WILDERNESS_PANEL("Wilderness", null, GameVersion.OSRS, () -> null),
    OSRS_WILDERNESS_EXPLORATION_PANEL("Exploration", OSRS_WILDERNESS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_WILDERNESS_EXPLORATION_EQUIPMENT_PANEL("Equipment", OSRS_WILDERNESS_EXPLORATION_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessExplorationEquipmentPanel.class)),
    OSRS_WILDERNESS_EXPLORATION_CONSUMABLES_PANEL("Consumables", OSRS_WILDERNESS_EXPLORATION_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessExplorationConsumablesPanel.class)),

    OSRS_WILDERNESS_MELEE_PANEL("Melee", OSRS_WILDERNESS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_WILDERNESS_MELEE_EQUIPMENT_PANEL("Equipment", OSRS_WILDERNESS_MELEE_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessMeleeEquipmentPanel.class)),
    OSRS_WILDERNESS_MELEE_CONSUMABLES_PANEL("Consumables", OSRS_WILDERNESS_MELEE_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessMeleeConsumablesPanel.class)),

    OSRS_WILDERNESS_RANGED_PANEL("Ranged", OSRS_WILDERNESS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_WILDERNESS_RANGED_EQUIPMENT_PANEL("Equipment", OSRS_WILDERNESS_RANGED_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessRangedEquipmentPanel.class)),
    OSRS_WILDERNESS_RANGED_CONSUMABLES_PANEL("Consumables", OSRS_WILDERNESS_RANGED_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessRangedConsumablesPanel.class)),

    OSRS_WILDERNESS_MAGIC_PANEL("Magic", OSRS_WILDERNESS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_WILDERNESS_MAGIC_EQUIPMENT_PANEL("Equipment", OSRS_WILDERNESS_MAGIC_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessMagicEquipmentPanel.class)),
    OSRS_WILDERNESS_MAGIC_CONSUMABLES_PANEL("Consumables", OSRS_WILDERNESS_MAGIC_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessMagicConsumablesPanel.class)),

    OSRS_WILDERNESS_DRAGONS_PANEL("Dragons", OSRS_WILDERNESS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_WILDERNESS_DRAGONS_EQUIPMENT_PANEL("Equipment", OSRS_WILDERNESS_DRAGONS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessDragonsEquipmentPanel.class)),
    OSRS_WILDERNESS_DRAGONS_CONSUMABLES_PANEL("Consumables", OSRS_WILDERNESS_DRAGONS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessDragonsConsumablesPanel.class)),

    OSRs_WILDERNESS_METAL_DRAGONS_PANEL("Metal Dragons", OSRS_WILDERNESS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_WILDERNESS_METAL_DRAGONS_EQUIPMENT_PANEL("Equipment", OSRs_WILDERNESS_METAL_DRAGONS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessMetalDragonsEquipmentPanel.class)),
    OSRS_WILDERNESS_METAL_DRAGONS_CONSUMABLES_PANEL("Consumables", OSRs_WILDERNESS_METAL_DRAGONS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessMetalDragonsConsumablesPanel.class)),

    OSRS_WILDERNESS_STRATEGY_PANEL("Strategy", OSRS_WILDERNESS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsWildernessStrategyPanel.class)),

    // OSRS Settings Panels
    OSRS_SETTINGS_PANEL("Settings", null, GameVersion.OSRS, () -> null),
    OSRS_SETTINGS_PROFILE_SYNC_PANEL("Profile Sync", OSRS_SETTINGS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(EmptyPanel.class)),
    OSRS_SETTINGS_PATHFINDING_PANEL("Pathfinding", OSRS_SETTINGS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSettingsPathfindingPanel.class)),

    OSRS_SETTINGS_ANTIBOT_DETECTION_PANEL("AntiBot Detection", OSRS_SETTINGS_PANEL, GameVersion.OSRS, () -> null),
    OSRS_SETTINGS_ANTIBOT_DETECTION_SLIDERS_PANEL("Delays", OSRS_SETTINGS_ANTIBOT_DETECTION_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSettingsAntiBotDetectionSlidersPanel.class)),
    OSRS_SETTINGS_ANTIBOT_DETECTION_CONFIG_PANEL("Configuration", OSRS_SETTINGS_ANTIBOT_DETECTION_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(OsrsSettingsAntiBotDetectionConfigPanel.class)),


    OSRS_SETTINGS_THEME_PANEL("Theme", OSRS_SETTINGS_PANEL, GameVersion.OSRS, EmptyPanel::new),

    OSRS_TASK_MANAGER_PANEL("Activity Manager", null, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(EmptyPanel.class)),

    OSRS_ANALYTICS_PANEL("Analytics", null, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(EmptyPanel.class)),
    OSRS_ANALYTICS_CHARACTER_PANEL("Character", OSRS_ANALYTICS_PANEL, GameVersion.OSRS, () -> DependencyFactory.getInstance().getInjection(EmptyPanel.class)),

    DEBUG("Debug", null, GameVersion.OSRS, DebugPanel::new);

    private final String tabTitle;
    private final OsrsPanels parentPanel;
    private final GameVersion gameVersion;
    private final Supplier<? extends JPanel> supplier;

    OsrsPanels(String tabTitle, OsrsPanels parentPanel, GameVersion gameVersion, Supplier<? extends JPanel> supplier) {
        this.tabTitle = tabTitle;
        this.parentPanel = parentPanel;
        this.gameVersion = gameVersion;
        this.supplier = supplier;
    }

    public JPanel createPanel() {
        return supplier.get();
    }

    public String getFullPath() {
        List<String> path = getFullTabTitlePath();
        return String.join("_", path).toLowerCase().replaceAll("\\s+", "_");
    }

    public List<String> getFullTabTitlePath() {
        List<String> path = new ArrayList<>();
        OsrsPanels current = this;
        while (current != null) {
            path.add(0, current.getTabTitle());
            current = current.getParentPanel();
        }
        return path;
    }

    public static OsrsPanels fromName(String name) {
        for (OsrsPanels panel : OsrsPanels.values()) {
            if (panel.name().equalsIgnoreCase(name)) {
                return panel;
            }
        }
        return null;
    }
}
