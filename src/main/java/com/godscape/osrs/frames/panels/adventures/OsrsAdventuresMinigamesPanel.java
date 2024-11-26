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
public class OsrsAdventuresMinigamesPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsAdventuresMinigamesPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsAdventuresMinigamesPanel...");

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
            // Create Minigames list panel
            JPanel minigamesListPanel = createMinigamesListPanel();

            // Layout optionPanel and minigamesListPanel within mainPanel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.4;  // 40% width
            gbcOption.fill = GridBagConstraints.HORIZONTAL;
            gbcOption.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(optionPanel, gbcOption);

            GridBagConstraints gbcMinigamesList = new GridBagConstraints();
            gbcMinigamesList.gridx = 1;
            gbcMinigamesList.gridy = 0;
            gbcMinigamesList.weightx = 0.6;  // 60% width
            gbcMinigamesList.weighty = 1.0;
            gbcMinigamesList.fill = GridBagConstraints.BOTH;
            gbcMinigamesList.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(minigamesListPanel, gbcMinigamesList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsAdventuresMinigamesPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsAdventuresMinigamesPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> minigameType = Arrays.asList("All", "Combat", "Skilling", "Cooperative", "Solo");
    List<String> minigameDifficulty = Arrays.asList("All", "Beginner", "Intermediate", "Advanced");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Minigame Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Minigame Module:");
        gridBuilder.addCheckbox(2, "minigameSettings", "showMinigames");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Type:");
        gridBuilder.addDropdown(2, "minigameSettings", "minigameType", minigameType);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Difficulty:");
        gridBuilder.addDropdown(2, "minigameSettings", "minigameDifficulty", minigameDifficulty);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createMinigamesListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Minigames List:");
        gridBuilder.nextRow();

        // Create the minigames list with padding
        DefaultListModel<String> minigamesListModel = new DefaultListModel<>();
        minigamesListModel.addElement("Barrows");
        minigamesListModel.addElement("Pest Control");
        minigamesListModel.addElement("Tears of Guthix");
        minigamesListModel.addElement("Castle Wars");
        minigamesListModel.addElement("Trouble Brewing");

        JList<String> minigamesList = new JList<>(minigamesListModel);
        minigamesList.setFixedCellWidth(300); // Set fixed width for list items
        JScrollPane scrollPane = new JScrollPane(minigamesList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll

        // Add the minigames list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "minigamesList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel minigamesListPanel = new JPanel(new BorderLayout());
        minigamesListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return minigamesListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
