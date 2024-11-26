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
public class OsrsAdventuresTreasureTailsPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsAdventuresTreasureTailsPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsAdventuresTreasureTailsPanel...");

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
            // Create treasure tails list panel
            JPanel treasureTailsListPanel = createTreasureTailsListPanel();

            // Layout optionPanel and treasureTailsListPanel within mainPanel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.4;  // 40% width
            gbcOption.fill = GridBagConstraints.HORIZONTAL;
            gbcOption.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(optionPanel, gbcOption);

            GridBagConstraints gbcTreasureTailsList = new GridBagConstraints();
            gbcTreasureTailsList.gridx = 1;
            gbcTreasureTailsList.gridy = 0;
            gbcTreasureTailsList.weightx = 0.6;  // 60% width
            gbcTreasureTailsList.weighty = 1.0;
            gbcTreasureTailsList.fill = GridBagConstraints.BOTH;
            gbcTreasureTailsList.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(treasureTailsListPanel, gbcTreasureTailsList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsAdventuresTreasureTailsPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsAdventuresTreasureTailsPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> clueScrollDifficulty = Arrays.asList("All", "Beginner", "Easy", "Medium", "Hard", "Elite", "Master");
    List<String> clueScrollType = Arrays.asList("All", "Emote Clue", "Map Clue", "Puzzle Box", "Anagram");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Treasure Tails Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Treasure Tails Module:");
        gridBuilder.addCheckbox(2, "treasureTailsSettings", "showTreasureTails");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Type:");
        gridBuilder.addDropdown(2, "treasureTailsSettings", "clueScrollType", clueScrollType);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Difficulty:");
        gridBuilder.addDropdown(2, "treasureTailsSettings", "clueScrollDifficulty", clueScrollDifficulty);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createTreasureTailsListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Treasure Tails List:");
        gridBuilder.nextRow();

        // Create the treasure tails list with fixed size
        DefaultListModel<String> treasureTailsListModel = new DefaultListModel<>();
        treasureTailsListModel.addElement("Treasure Trail - Beginner");
        treasureTailsListModel.addElement("Treasure Trail - Easy");
        treasureTailsListModel.addElement("Treasure Trail - Medium");
        treasureTailsListModel.addElement("Treasure Trail - Hard");
        treasureTailsListModel.addElement("Treasure Trail - Elite");

        JList<String> treasureTailsList = new JList<>(treasureTailsListModel);
        treasureTailsList.setFixedCellWidth(300); // Set fixed width
        JScrollPane scrollPane = new JScrollPane(treasureTailsList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list

        // Add the treasure tails list to the gridBuilder with fixed size constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "treasureTailsList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel treasureTailsListPanel = new JPanel(new BorderLayout());
        treasureTailsListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return treasureTailsListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
