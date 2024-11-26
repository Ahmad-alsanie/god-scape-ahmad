package com.godscape.system.utility.builders;

import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.rs3.controllers.Rs3SettingController;
import com.godscape.rs3.enums.core.Rs3Panels;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class BaseComponentBuilder {

    private final Object controller;
    private final Enum<?> schema;

    public BaseComponentBuilder(Object controller, Enum<?> schema) {
        this.controller = controller;
        this.schema = schema;
    }

    public JLabel createLabel(String text, int alignment) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(alignment);
        label.setOpaque(false);
        label.setPreferredSize(new Dimension(100, 30));
        return label;
    }

    public JCheckBox createCheckbox(String key, String category, GridBagConstraints constraints, boolean initialState) {
        JCheckBox checkbox = new JCheckBox();
        checkbox.setSelected(initialState);
        checkbox.setOpaque(false);

        // Add listener to update settings on state change
        checkbox.addItemListener(e -> {
            boolean isSelected = checkbox.isSelected();
            saveSetting(key, isSelected);
            Logger.debug("Checkbox for key '{}' changed to: {}", key, isSelected);
        });
        return checkbox;
    }

    public JComboBox<String> createDropdown(String key, String category, List<String> options, String initialValue) {
        JComboBox<String> dropdown = new JComboBox<>(options.toArray(new String[0]));
        dropdown.setSelectedItem(initialValue);
        dropdown.setOpaque(false);

        // Add listener to update settings on selection change
        dropdown.addActionListener(e -> {
            String selected = (String) dropdown.getSelectedItem();
            saveSetting(key, selected);
            Logger.debug("Dropdown for key '{}' selected: {}", key, selected);
        });
        return dropdown;
    }

    private void saveSetting(String key, Object value) {
        if (controller instanceof OsrsSettingsController) {
            ((OsrsSettingsController) controller).saveSetting((OsrsPanels) schema, key, value);
        } else if (controller instanceof Rs3SettingController) {
            ((Rs3SettingController) controller).saveSetting((Rs3Panels) schema, key, value);
        }
    }
}
