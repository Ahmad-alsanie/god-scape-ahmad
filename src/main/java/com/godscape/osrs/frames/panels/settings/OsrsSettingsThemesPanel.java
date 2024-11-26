package com.godscape.osrs.frames.panels.settings;

import com.godscape.osrs.managers.panels.settings.OsrsThemesManager;
import com.godscape.system.enums.Themes;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.templates.EmptyPanel;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.StringConvert;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Arrays;

public class OsrsSettingsThemesPanel extends JPanel {

    private final OsrsThemesManager osrsThemesManager;

    private JComboBox<String> themeDropdown;
    private JComboBox<String> intensityDropdown;
    private JCheckBox smoothCornersCheckbox;
    private JCheckBox smoothButtonsCheckbox;
    private JCheckBox animationsCheckbox;
    private JCheckBox transparencyCheckbox;
    private JCheckBox highlightsCheckbox;
    private JCheckBox shadowsCheckbox;

    private Themes currentTheme;
    private boolean isProgrammaticallySetting = false;
    private float panelOpacity = 1.0f;
    private JPanel themePreviewPanel = null;

    public OsrsSettingsThemesPanel(OsrsThemesManager osrsThemesManager) {
        this.osrsThemesManager = osrsThemesManager;
        setLayout(new BorderLayout());
        setOpaque(false);
        setupUIComponents();
        loadSettingsFromCache();
    }

    private void setupUIComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0.5;

        String[] capitalizedThemes = Arrays.stream(Themes.values())
                .map(Enum::name)
                .map(StringConvert::capitalize)
                .toArray(String[]::new);

        themeDropdown = createDropdown(capitalizedThemes, "Theme Selection", 0, 0, mainPanel, gbc);
        intensityDropdown = createDropdown(new String[]{"Disabled", "1", "2", "3", "4", "5", "6", "7", "8", "9", "Maximum"}, "Intensity", 2, 0, mainPanel, gbc);

        smoothCornersCheckbox = createCheckbox("Smooth Corners:", 1, 0, mainPanel, gbc);
        smoothButtonsCheckbox = createCheckbox("Smooth Buttons:", 1, 2, mainPanel, gbc);
        animationsCheckbox = createCheckbox("Animations:", 2, 0, mainPanel, gbc);
        transparencyCheckbox = createCheckbox("Transparency:", 2, 2, mainPanel, gbc);
        highlightsCheckbox = createCheckbox("Highlights:", 3, 0, mainPanel, gbc);
        shadowsCheckbox = createCheckbox("Shadows:", 3, 2, mainPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        JButton applyButton = new JButton("Apply");
        applyButton.setPreferredSize(new Dimension(150, 40));
        applyButton.addActionListener(e -> applyChanges());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(applyButton);
        add(buttonPanel, BorderLayout.SOUTH);

        themeDropdown.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && !isProgrammaticallySetting) {
                previewTheme();
                updatePanelOpacity();
            }
        });

        currentTheme = Themes.valueOf(capitalizedThemes[themeDropdown.getSelectedIndex()].toUpperCase());
    }

    private JComboBox<String> createDropdown(String[] items, String labelText, int gridx, int gridy, JPanel panel, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText + ":", SwingConstants.RIGHT);
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(label, gbc);

        JComboBox<String> dropdown = new JComboBox<>(items);
        gbc.gridx = gridx + 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(dropdown, gbc);

        return dropdown;
    }

    private JCheckBox createCheckbox(String labelText, int row, int col, JPanel panel, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText, SwingConstants.RIGHT);
        gbc.gridx = col;
        gbc.gridy = row;
        panel.add(label, gbc);

        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        checkboxPanel.setOpaque(false);

        JCheckBox checkbox = new JCheckBox();
        checkbox.setOpaque(false);
        JLabel statusLabel = new JLabel(checkbox.isSelected() ? "Enabled" : "Disabled");

        checkbox.addItemListener(e -> statusLabel.setText(checkbox.isSelected() ? "Enabled" : "Disabled"));

        checkboxPanel.add(checkbox);
        checkboxPanel.add(statusLabel);

        gbc.gridx = col + 1;
        panel.add(checkboxPanel, gbc);

        return checkbox;
    }

    private void loadSettingsFromCache() {
        ThemeSchema themeSchema = osrsThemesManager.fetchCurrentThemeConfig();
        if (themeSchema != null) {
            applyThemeSchemaToUI(themeSchema);
        } else {
            setDefaultSettings();
            JOptionPane.showMessageDialog(this, "Failed to load theme settings from the cache. Using default values.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void applyThemeSchemaToUI(ThemeSchema themeSchema) {
        isProgrammaticallySetting = true;
        try {
            themeDropdown.setSelectedItem(themeSchema.getSelectedTheme().name());
            intensityDropdown.setSelectedItem(themeSchema.getIntensity());
            smoothCornersCheckbox.setSelected(themeSchema.isSmoothCorners());
            smoothButtonsCheckbox.setSelected(themeSchema.isSmoothButtons());
            animationsCheckbox.setSelected(themeSchema.isAnimations());
            transparencyCheckbox.setSelected(themeSchema.isTransparency());
            highlightsCheckbox.setSelected(themeSchema.isHighlights());
            shadowsCheckbox.setSelected(themeSchema.isShadows());
        } finally {
            isProgrammaticallySetting = false;
        }
    }

    private void setDefaultSettings() {
        isProgrammaticallySetting = true;
        try {
            themeDropdown.setSelectedIndex(0);
            intensityDropdown.setSelectedIndex(0);
            smoothCornersCheckbox.setSelected(false);
            smoothButtonsCheckbox.setSelected(false);
            animationsCheckbox.setSelected(false);
            transparencyCheckbox.setSelected(false);
            highlightsCheckbox.setSelected(false);
            shadowsCheckbox.setSelected(false);
        } finally {
            isProgrammaticallySetting = false;
        }
    }

    private void applyChanges() {
        String selectedThemeName = (String) themeDropdown.getSelectedItem();
        if (selectedThemeName == null) {
            Logger.warn("OsrsThemesPanel: No theme selected.");
            return;
        }
        Themes selectedTheme = Themes.valueOf(selectedThemeName.toUpperCase());

        ThemeSchema themeSchema = new ThemeSchema();
        themeSchema.setSelectedTheme(selectedTheme);
        themeSchema.setSmoothCorners(smoothCornersCheckbox.isSelected());
        themeSchema.setSmoothButtons(smoothButtonsCheckbox.isSelected());
        themeSchema.setAnimations(animationsCheckbox.isSelected());
        themeSchema.setTransparency(transparencyCheckbox.isSelected());
        themeSchema.setHighlights(highlightsCheckbox.isSelected());
        themeSchema.setShadows(shadowsCheckbox.isSelected());
        themeSchema.setIntensity((String) intensityDropdown.getSelectedItem());
        themeSchema.setActive(true);

        if (osrsThemesManager.saveThemeConfig(themeSchema)) {
            if (osrsThemesManager.setActiveTheme(themeSchema)) {
                currentTheme = selectedTheme;
                updatePanelOpacity();
                loadEmptyPanelIfAppliedThemeMatches();
                Logger.info("OsrsThemesPanel: Theme applied successfully.");
            } else {
                Logger.warn("OsrsThemesPanel: Failed to set active theme.");
            }
        } else {
            Logger.warn("OsrsThemesPanel: Failed to save theme configuration.");
        }
    }

    private void previewTheme() {
        String selectedThemeName = (String) themeDropdown.getSelectedItem();
        if (selectedThemeName != null) {
            Themes selectedTheme = Themes.valueOf(selectedThemeName.toUpperCase());
            loadPanelBasedOnTheme(selectedTheme);
        }
    }

    private void loadPanelBasedOnTheme(Themes selectedTheme) {
        JPanel newPreviewPanel = selectedTheme == currentTheme ? new EmptyPanel() : selectedTheme.createPanel();
        if (newPreviewPanel != null) {
            if (themePreviewPanel != null) {
                remove(themePreviewPanel);
            }
            themePreviewPanel = newPreviewPanel;
            themePreviewPanel.setOpaque(false);
            add(themePreviewPanel, BorderLayout.CENTER);
            setComponentZOrder(themePreviewPanel, getComponentCount() - 1);
            repaint();
            revalidate();
        } else {
            Logger.warn("OsrsThemesPanel: Failed to load theme preview for '{}'", selectedTheme.name());
        }
    }

    private void loadEmptyPanelIfAppliedThemeMatches() {
        if (currentTheme.name().equals(themeDropdown.getSelectedItem())) {
            loadPanelBasedOnTheme(currentTheme);
        }
    }

    public void updatePanelOpacity() {
        panelOpacity = currentTheme == Themes.valueOf(((String) themeDropdown.getSelectedItem()).toUpperCase()) ? 1.0f : 0.0f;
        setOpaque(panelOpacity == 1.0f);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, panelOpacity));
        super.paintComponent(g2d);
        g2d.dispose();
    }
}
