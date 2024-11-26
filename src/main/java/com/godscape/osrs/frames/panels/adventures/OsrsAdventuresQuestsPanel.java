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
public class OsrsAdventuresQuestsPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsAdventuresQuestsPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsAdventuresQuestsPanel...");

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
            // Create quest list panel
            JPanel questListPanel = createQuestListPanel();

            // Layout optionPanel and questListPanel within mainPanel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.5;  // 40% width
            gbcOption.fill = GridBagConstraints.HORIZONTAL;
            gbcOption.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(optionPanel, gbcOption);

            GridBagConstraints gbcQuestList = new GridBagConstraints();
            gbcQuestList.gridx = 1;
            gbcQuestList.gridy = 0;
            gbcQuestList.weightx = 0.5;  // 60% width
            gbcQuestList.weighty = 1.0;
            gbcQuestList.fill = GridBagConstraints.BOTH;
            gbcQuestList.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(questListPanel, gbcQuestList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsAdventuresQuestsPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsAdventuresQuestsPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> questDifficulty = Arrays.asList("All", "Easy", "Medium", "Hard", "Elite", "Master");
    List<String> questLength = Arrays.asList("All", "Short", "Medium", "Long", "Very Long");
    List<String> questType = Arrays.asList("All", "Free-to-Play", "Members");
    List<String> questCategory = Arrays.asList("All", "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Quest Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Quest Module:");
        gridBuilder.addCheckbox(2, "questSettings", "showQuests");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Type:");
        gridBuilder.addDropdown(2, "questSettings", "questType", questType);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Difficulty:");
        gridBuilder.addDropdown(2, "questSettings", "questDifficulty", questDifficulty);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Length:");
        gridBuilder.addDropdown(2, "questSettings", "questLength", questLength, OsrsSchemas.OSRS_PROFILE_SCHEMA);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Category:");
        gridBuilder.addDropdown(2, "questSettings", "questCategory", questCategory, OsrsSchemas.OSRS_PROFILE_SCHEMA);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createQuestListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Quest List:");
        gridBuilder.nextRow();

        // Create the quest list with padding
        DefaultListModel<String> questListModel = new DefaultListModel<>();
        questListModel.addElement("Cook's Assistant");
        questListModel.addElement("Demon Slayer");
        questListModel.addElement("Dragon Slayer");
        questListModel.addElement("The Restless Ghost");
        questListModel.addElement("Vampire Slayer");

        JList<String> questList = new JList<>(questListModel);
        questList.setFixedCellWidth(300); // Set fixed width for the list
        JScrollPane scrollPane = new JScrollPane(questList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll bar

        // Add the quest list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "questList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel questListPanel = new JPanel(new BorderLayout());
        questListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return questListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
