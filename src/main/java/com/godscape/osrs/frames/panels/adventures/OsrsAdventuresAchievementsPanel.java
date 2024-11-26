package com.godscape.osrs.frames.panels.adventures;

import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.system.factories.DependencyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Singleton
public class OsrsAdventuresAchievementsPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsAdventuresAchievementsPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsAdventuresAchievementsPanel...");

            // Fetch or create a new profile from OsrsCacheController
            UUID profileId = profileIdOrDefault();
            profile = cacheController.getProfile(profileId);
            if (profile == null) {
                profile = new OsrsProfileSchema();
                profile.setProfileId(profileId);
                cacheController.updateProfile(profile);
                Logger.info("Created new profile with ID: {}", profileId);
            }

            // Main panel setup
            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Create option panel using OsrsGridBuilder
            JPanel optionPanel = createOptionPanel();
            // Create achievements list panel
            JPanel achievementsListPanel = createAchievementsListPanel();

            // Layout optionPanel and achievementsListPanel within mainPanel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.4;  // 40% width
            gbcOption.fill = GridBagConstraints.HORIZONTAL;
            gbcOption.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(optionPanel, gbcOption);

            GridBagConstraints gbcAchievementsList = new GridBagConstraints();
            gbcAchievementsList.gridx = 1;
            gbcAchievementsList.gridy = 0;
            gbcAchievementsList.weightx = 0.6;  // 60% width
            gbcAchievementsList.weighty = 1.0;
            gbcAchievementsList.fill = GridBagConstraints.BOTH;
            gbcAchievementsList.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(achievementsListPanel, gbcAchievementsList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsAdventuresAchievementsPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsAdventuresAchievementsPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> achievementDifficulty = Arrays.asList("All", "Easy", "Medium", "Hard", "Elite", "Master");
    List<String> achievementType = Arrays.asList("All", "Combat", "Skilling", "Miscellaneous");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Achievement Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Achievement Module:");
        gridBuilder.addCheckbox(2, "achievementSettings", "showAchievements");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Type:");
        gridBuilder.addDropdown(2, "achievementSettings", "achievementType", achievementType);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Difficulty:");
        gridBuilder.addDropdown(2, "achievementSettings", "achievementDifficulty", achievementDifficulty);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createAchievementsListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Achievements List:");
        gridBuilder.nextRow();

        // Create the achievements list with padding
        DefaultListModel<String> achievementsListModel = new DefaultListModel<>();
        achievementsListModel.addElement("Champion's Challenge");
        achievementsListModel.addElement("Fire Cape");
        achievementsListModel.addElement("Music Cape");
        achievementsListModel.addElement("Achievement Diary Cape");
        achievementsListModel.addElement("Master Quest Cape");

        JList<String> achievementsList = new JList<>(achievementsListModel);
        achievementsList.setFixedCellWidth(300); // Set fixed width for list cells
        JScrollPane scrollPane = new JScrollPane(achievementsList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list

        // Add the achievements list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "achievementsList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel achievementsListPanel = new JPanel(new BorderLayout());
        achievementsListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return achievementsListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
