package com.godscape.osrs.frames.panels.skilling.combat;

import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class OsrsSkillingCombatMagicPanel extends JPanel {

    private final OsrsGridBuilder gridBuilder;
    private final Map<String, JComboBox<String>> dropdownFields = new HashMap<>();
    private final Map<String, JCheckBox> checkboxFields = new HashMap<>();
    private final OsrsSettingsController settingsController;

    public OsrsSkillingCombatMagicPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setOpaque(false);

        gridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_SKILLING_MAGIC_PANEL);
        settingsController = DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);

        try {
            Logger.info("Initializing OsrsSkillingCombatMagicPanel...");

            initializeComponents();
            add(gridBuilder.getScrollPane(), BorderLayout.CENTER);

            Logger.info("OsrsSkillingCombatMagicPanel layout initialized successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsSkillingCombatMagicPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeComponents() {
        addDropdownWithListener("Full Inventory", "magicSettings", "fullInventory", createList("Auto", "Bank", "Drop"));
        addDropdownWithListener("Training Method", "magicSettings", "trainingMethod", createList("Auto", "Combat", "Slayer", "Teleport", "Alchemy", "Splash", "Reanimation"));
        gridBuilder.nextRow();

        addDropdownWithListener("Spell Book", "magicSettings", "spellBook", createList("Auto", "Standard", "Ancient", "Lunar", "Arceuus"));
        addDropdownWithListener("Cape Preference", "magicSettings", "capePreference", createList("Auto", "Saradomin", "Zamorak", "Guthix"));
        gridBuilder.nextRow();

        gridBuilder.addSeparator("");
        gridBuilder.nextRow();

        addCheckboxWithListener("Magic with Slayer", "magicSettings", "magicWithSlayer");
        addCheckboxWithListener("Chaos Gauntlets", "magicSettings", "obtainChaosGauntlets");
        gridBuilder.nextRow();

        addCheckboxWithListener("God Cape", "magicSettings", "obtainGodCape");
        addCheckboxWithListener("Imbued God Cape", "magicSettings", "imbuedGodCape");
        gridBuilder.nextRow();
    }

    private void addDropdownWithListener(String label, String namespace, String componentId, List<String> options) {
        gridBuilder.addLabel(1, label);
        JComboBox<String> dropdown = new JComboBox<>(options.toArray(new String[0]));
        dropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = (String) dropdown.getSelectedItem();
                settingsController.saveSetting(OsrsPanels.OSRS_SKILLING_MAGIC_PANEL, componentId, selectedValue);
                Logger.info("Saved dropdown setting: '{}' = '{}'", componentId, selectedValue);
            }
        });
        gridBuilder.addComponent(2, namespace, componentId, dropdown, OsrsPanels.OSRS_SKILLING_MAGIC_PANEL);
        dropdownFields.put(componentId, dropdown);
    }

    private void addCheckboxWithListener(String label, String namespace, String componentId) {
        gridBuilder.addLabel(1, label);
        JCheckBox checkbox = new JCheckBox();
        checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = checkbox.isSelected();
                settingsController.saveSetting(OsrsPanels.OSRS_SKILLING_MAGIC_PANEL, componentId, isSelected);
                Logger.info("Saved checkbox setting: '{}' = '{}'", componentId, isSelected);
            }
        });
        gridBuilder.addComponent(2, namespace, componentId, checkbox, OsrsPanels.OSRS_SKILLING_MAGIC_PANEL);
        checkboxFields.put(componentId, checkbox);
    }

    public void loadSettings(Map<String, Object> settings) {
        for (Map.Entry<String, Object> entry : settings.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (dropdownFields.containsKey(key)) {
                JComboBox<String> dropdown = dropdownFields.get(key);
                dropdown.setSelectedItem(value.toString());
            } else if (checkboxFields.containsKey(key)) {
                JCheckBox checkbox = checkboxFields.get(key);
                checkbox.setSelected(Boolean.parseBoolean(value.toString()));
            }
        }
        Logger.info("OsrsSkillingCombatMagicPanel: Settings loaded successfully.");
    }

    public Map<String, Object> getCurrentSettings() {
        Map<String, Object> settings = new HashMap<>();
        for (Map.Entry<String, JComboBox<String>> entry : dropdownFields.entrySet()) {
            settings.put(entry.getKey(), entry.getValue().getSelectedItem());
        }
        for (Map.Entry<String, JCheckBox> entry : checkboxFields.entrySet()) {
            settings.put(entry.getKey(), entry.getValue().isSelected());
        }
        return settings;
    }

    private List<String> createList(String... items) {
        return Arrays.asList(items);
    }
}
