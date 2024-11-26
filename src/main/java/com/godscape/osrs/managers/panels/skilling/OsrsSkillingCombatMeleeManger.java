package com.godscape.osrs.managers.panels.skilling;

import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import javax.swing.*;

@Singleton
public class OsrsSkillingCombatMeleeManger {

    private final OsrsSettingsController settingsController;

    public OsrsSkillingCombatMeleeManger() {
        this.settingsController = DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);
        Logger.info("OsrsSkillingCombatMeleeManager: Initialized.");
    }

    public void loadSettings(JComboBox<String> fullInventoryDropdown, JCheckBox attackWithSlayerCheckbox, JCheckBox strengthWithSlayerCheckbox,
                             JCheckBox defenceWithSlayerCheckbox, JCheckBox hitpointsWithSlayerCheckbox) {
        fullInventoryDropdown.setSelectedItem(settingsController.loadSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "fullInventory"));
        attackWithSlayerCheckbox.setSelected((Boolean) settingsController.loadSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "attackWithSlayer"));
        strengthWithSlayerCheckbox.setSelected((Boolean) settingsController.loadSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "strengthWithSlayer"));
        defenceWithSlayerCheckbox.setSelected((Boolean) settingsController.loadSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "defenceWithSlayer"));
        hitpointsWithSlayerCheckbox.setSelected((Boolean) settingsController.loadSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "hitpointsWithSlayer"));
        Logger.info("OsrsSkillingCombatMeleeManager: Settings loaded.");
    }

    public void saveSettings(JComboBox<String> fullInventoryDropdown, JCheckBox attackWithSlayerCheckbox, JCheckBox strengthWithSlayerCheckbox,
                             JCheckBox defenceWithSlayerCheckbox, JCheckBox hitpointsWithSlayerCheckbox) {
        settingsController.saveSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "fullInventory", fullInventoryDropdown.getSelectedItem());
        settingsController.saveSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "attackWithSlayer", attackWithSlayerCheckbox.isSelected());
        settingsController.saveSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "strengthWithSlayer", strengthWithSlayerCheckbox.isSelected());
        settingsController.saveSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "defenceWithSlayer", defenceWithSlayerCheckbox.isSelected());
        settingsController.saveSetting(OsrsPanels.OSRS_SKILLING_ATTACK_PANEL, "hitpointsWithSlayer", hitpointsWithSlayerCheckbox.isSelected());
        Logger.info("OsrsSkillingCombatMeleeManager: Settings saved.");
    }
}
