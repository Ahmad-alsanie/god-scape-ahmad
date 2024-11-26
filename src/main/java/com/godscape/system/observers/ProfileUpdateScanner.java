package com.godscape.system.observers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.controllers.SettingsController;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.fuse.SettingsFuse;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Optional;

@Singleton
public class ProfileUpdateScanner {

    private final SettingsController settingsController;

    /**
     * Constructor that injects `SettingsController` using `DependencyFactory`.
     */
    public ProfileUpdateScanner() {
        // Inject SettingsController
        this.settingsController = DependencyFactory.getInstance().getInjection(SettingsController.class);

        // Check if dependency is properly injected
        if (this.settingsController == null) {
            throw new IllegalStateException("ProfileUpdateScanner: Failed to inject SettingsController.");
        } else {
            Logger.info("ProfileUpdateScanner: Initialized successfully.");
        }
    }

    /**
     * Attaches the appropriate listener based on component type, updating settings immediately on any change.
     *
     * @param component The Swing component to attach the listener to.
     * @param key       The setting key associated with the component.
     * @param schema    The schema Enum to identify the panel type.
     */
    public void attachListener(JComponent component, String key, Enum<?> schema) {
        if (component instanceof JComboBox) {
            attachDropdownListener((JComboBox<?>) component, key, schema);
        } else if (component instanceof JCheckBox) {
            attachCheckboxListener((JCheckBox) component, key, schema);
        } else if (component instanceof JTextField) {
            attachTextFieldListener((JTextField) component, key, schema);
        } else if (component instanceof JSpinner) {
            attachSpinnerListener((JSpinner) component, key, schema);
        } else {
            Logger.warn("ProfileUpdateScanner: Unsupported component type for key '{}'", key);
        }
    }

    private void attachDropdownListener(JComboBox<?> dropdown, String key, Enum<?> schema) {
        dropdown.setSelectedItem(Optional.ofNullable(loadSetting(schema, key)).orElse(dropdown.getItemAt(0)));

        dropdown.addActionListener(e -> {
            Object selectedValue = dropdown.getSelectedItem();
            saveSetting(schema, key, selectedValue);
            Logger.info("Dropdown '{}' updated to '{}'", key, selectedValue);
        });
    }

    private void attachCheckboxListener(JCheckBox checkbox, String key, Enum<?> schema) {
        checkbox.setSelected(Optional.ofNullable(loadSetting(schema, key))
                .map(val -> val instanceof Boolean ? (Boolean) val : Boolean.parseBoolean(val.toString()))
                .orElse(false));

        checkbox.addItemListener(e -> {
            boolean isSelected = checkbox.isSelected();
            saveSetting(schema, key, isSelected);
            Logger.info("Checkbox '{}' updated to '{}'", key, isSelected);
        });
    }

    private void attachTextFieldListener(JTextField textField, String key, Enum<?> schema) {
        textField.setText(Optional.ofNullable(loadSetting(schema, key)).map(Object::toString).orElse(""));

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateSetting(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateSetting(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateSetting(); }

            private void updateSetting() {
                String text = textField.getText().trim();
                saveSetting(schema, key, text);
                Logger.info("TextField '{}' updated to '{}'", key, text);
            }
        });
    }

    private void attachSpinnerListener(JSpinner spinner, String key, Enum<?> schema) {
        spinner.setValue(Optional.ofNullable(loadSetting(schema, key)).orElse(spinner.getValue()));

        spinner.addChangeListener(e -> {
            Object value = spinner.getValue();
            saveSetting(schema, key, value);
            Logger.info("Spinner '{}' updated to '{}'", key, value);
        });
    }

    private Object loadSetting(Enum<?> schema, String key) {
        return settingsController.loadSetting(schema, key);
    }

    private void saveSetting(Enum<?> schema, String key, Object value) {
        Logger.info("Saving setting for key '{}'", key);
        settingsController.saveSetting(schema, key, value);
    }
}
