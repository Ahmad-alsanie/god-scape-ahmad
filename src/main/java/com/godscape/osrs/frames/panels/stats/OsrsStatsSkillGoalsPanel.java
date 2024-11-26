package com.godscape.osrs.frames.panels.stats;

import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.osrs.enums.game.OsrsSkillNames;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;

public class OsrsStatsSkillGoalsPanel extends JPanel {

    private static final int MAX_LEVEL = 126;
    private static final int SKILLS_PER_ROW = 3;
    private final OsrsGridBuilder gridBuilder;
    private final Map<OsrsSkillNames, JTextField> skillFields = new HashMap<>();
    private final OsrsSettingsController settingsController;

    public OsrsStatsSkillGoalsPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(600, 400));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        settingsController = DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);

        gridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_STATS_SKILL_GOALS_PANEL);
        gridBuilder.addSeparator("Skill Goal Settings");
        gridBuilder.nextRow();

        populateSkillGoals();
        addFillerComponent();
        add(gridBuilder.getScrollPane(), BorderLayout.CENTER);
    }

    private void populateSkillGoals() {
        int skillCount = 0;
        for (OsrsSkillNames skill : OsrsSkillNames.values()) {
            gridBuilder.addLabel((skillCount % SKILLS_PER_ROW) * 2 + 1, skill.name() + ":");

            String skillKey = skill.name().toLowerCase();
            String fullKey = gridBuilder.addTextField((skillCount % SKILLS_PER_ROW) * 2 + 2, "stats", skillKey, OsrsSchemas.OSRS_PROFILE_SCHEMA);

            if (fullKey != null) {
                JTextField goalField = (JTextField) gridBuilder.getComponentByKey(fullKey);
                skillFields.put(skill, goalField);

                ((AbstractDocument) goalField.getDocument()).setDocumentFilter(new ClampingDocumentFilter(skill));

                addCacheUpdateListeners(goalField, skill);
                Logger.info("OsrsStatsSkillGoalsPanel: Registered JTextField for '{}'", skill.name());
            }

            skillCount++;
            if (skillCount % SKILLS_PER_ROW == 0) {
                gridBuilder.nextRow();
            }
        }
    }

    private void addFillerComponent() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = gridBuilder.maxColumnsInRow;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        gridBuilder.add(filler, gbc);
    }

    private void addCacheUpdateListeners(JTextField goalField, OsrsSkillNames skill) {
        goalField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String input = goalField.getText().trim();
                int minValue = skill == OsrsSkillNames.HITPOINTS ? 10 : 1;
                try {
                    int value = Integer.parseInt(input);
                    if (value < minValue) {
                        goalField.setText(String.valueOf(minValue));
                    }
                    Logger.debug("Updating skill '{}' to '{}'", skill.name(), value);

                    // Confirming the panel and componentId values in debug logs
                    Logger.debug("Saving setting with panel '{}', skill '{}'", OsrsPanels.OSRS_STATS_SKILL_GOALS_PANEL, skill.name());
                    settingsController.saveSetting(OsrsPanels.OSRS_STATS_SKILL_GOALS_PANEL, skill.name(), value); // Save setting via settings controller
                } catch (NumberFormatException ex) {
                    goalField.setText(String.valueOf(minValue));
                    Logger.warn("Invalid input for '{}', resetting to '{}'", skill.name(), minValue);
                }
            }
        });
    }

    /**
     * Restricts input to digits and clamps values within the allowed range.
     */
    private static class ClampingDocumentFilter extends DocumentFilter {
        private final OsrsSkillNames skill;

        public ClampingDocumentFilter(OsrsSkillNames skill) {
            this.skill = skill;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
                clampIfNeeded(fb);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
                clampIfNeeded(fb);
            }
        }

        private void clampIfNeeded(FilterBypass fb) throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength()).trim();
            if (!currentText.isEmpty()) {
                try {
                    int value = Integer.parseInt(currentText);
                    if (value > MAX_LEVEL) {
                        fb.getDocument().remove(0, fb.getDocument().getLength());
                        fb.getDocument().insertString(0, String.valueOf(MAX_LEVEL), null);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    public void updateGoals(Map<OsrsSkillNames, Integer> skillGoals) {
        if (skillGoals == null || skillGoals.isEmpty()) {
            skillFields.values().forEach(field -> field.setText(""));  // Clear all fields if no goals are provided
            Logger.info("OsrsStatsSkillGoalsPanel: All skill goal fields reset.");
            return;
        }

        skillGoals.forEach((skill, level) -> {
            JTextField goalField = skillFields.get(skill);
            if (goalField != null) {
                goalField.setText(String.valueOf(level));
            }
        });

        Logger.info("OsrsStatsSkillGoalsPanel: Skill goal fields updated.");
    }

    public Map<OsrsSkillNames, Integer> getCurrentSkillGoals() {
        Map<OsrsSkillNames, Integer> currentGoals = new HashMap<>();
        for (Map.Entry<OsrsSkillNames, JTextField> entry : skillFields.entrySet()) {
            OsrsSkillNames skill = entry.getKey();
            JTextField goalField = entry.getValue();
            try {
                String text = goalField.getText().trim();
                if (!text.isEmpty()) {
                    int goalValue = Integer.parseInt(text);
                    currentGoals.put(skill, goalValue);
                }
            } catch (NumberFormatException e) {
                Logger.warn("Invalid input for {}: '{}'", skill.name(), goalField.getText());
            }
        }
        return currentGoals;
    }
}
