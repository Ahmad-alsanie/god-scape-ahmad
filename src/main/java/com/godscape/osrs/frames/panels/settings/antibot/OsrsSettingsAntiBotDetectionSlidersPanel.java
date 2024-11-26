package com.godscape.osrs.frames.panels.settings.antibot;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.system.factories.DependencyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class OsrsSettingsAntiBotDetectionSlidersPanel extends JPanel {

    private final OsrsGridBuilder osrsGridBuilder;
    private final OsrsCacheController cacheController;
    private final Map<String, JSlider> sliders = new HashMap<>();

    public OsrsSettingsAntiBotDetectionSlidersPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        // Initialize OsrsGridBuilder for the panel layout
        osrsGridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_SETTINGS_ANTIBOT_DETECTION_PANEL);

        try {
            Logger.info("Initializing OsrsSettingsAntiBotDetection...");

            osrsGridBuilder.addSeparator("Anti-Bot Detection Settings");
            osrsGridBuilder.nextRow();

            // Add anti-bot detection options
            addOption("Mouse Speed Variation");
            addOption("Click Interval Variation");
            addOption("Path Randomization");
            //addOption("AFK Break Frequency");
            //addOption("Randomized Camera");

            // Add the grid to the panel
            add(osrsGridBuilder.getScrollPane(), BorderLayout.CENTER);
            Logger.info("PanelGrid added to OsrsSettingsAntiBotDetection layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsSettingsAntiBotDetection: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void addOption(String optionName) {
        String key = optionName.toLowerCase().replace(" ", "_");

        // Option name label spanning full width
        osrsGridBuilder.addLabel(1, optionName + ":");
        osrsGridBuilder.nextRow();

        // Enable checkbox with minimal left padding and alignment to the left
        GridBagConstraints checkboxConstraints = new GridBagConstraints();
        checkboxConstraints.gridx = 0;
        checkboxConstraints.gridy = osrsGridBuilder.getCurrentRow();
        checkboxConstraints.anchor = GridBagConstraints.WEST;  // Align to the left
        checkboxConstraints.insets = new Insets(5, 0, 5, 10);  // Reduce left padding
        osrsGridBuilder.addCheckbox(1, "antiBotSettings", key + "_enabled", OsrsSchemas.OSRS_PROFILE_SCHEMA);

        // Slider with padding, spanning 75% width
        JSlider slider = new JSlider(1, 100);
        slider.setOpaque(false);
        slider.setValue(50);  // Default to 50%
        sliders.put(key, slider);

        GridBagConstraints sliderConstraints = new GridBagConstraints();
        sliderConstraints.gridx = 1;
        sliderConstraints.gridy = osrsGridBuilder.getCurrentRow();
        sliderConstraints.gridwidth = 2;
        sliderConstraints.weightx = 0.75;
        sliderConstraints.fill = GridBagConstraints.HORIZONTAL;
        sliderConstraints.insets = new Insets(5, 5, 5, 5);  // Apply padding around the slider as needed
        osrsGridBuilder.addComponent("antiBotSettings", key + "_slider", slider, OsrsSchemas.OSRS_PROFILE_SCHEMA, sliderConstraints);

        // Text field with padding
        JTextField valueField = new JTextField(3);
        valueField.setText(String.valueOf(slider.getValue()));
        valueField.setEditable(false);

        GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.gridx = 3;
        textFieldConstraints.gridy = osrsGridBuilder.getCurrentRow();
        textFieldConstraints.weightx = 0.1;
        textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraints.insets = new Insets(5, 5, 5, 10);  // Apply padding around the text field as needed
        osrsGridBuilder.addComponent("antiBotSettings", key + "_value", valueField, OsrsSchemas.OSRS_PROFILE_SCHEMA, textFieldConstraints);

        // Sync slider and text field values
        slider.addChangeListener(e -> valueField.setText(String.valueOf(slider.getValue())));

        // Move to the next row for the next option
        osrsGridBuilder.nextRow();
    }
}