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
public class OsrsAdventuresSlayerTasksPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsAdventuresSlayerTasksPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsAdventuresSlayerTasksPanel...");

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
            // Create Slayer tasks list panel
            JPanel slayerTasksListPanel = createSlayerTasksListPanel();

            // Layout optionPanel and slayerTasksListPanel within mainPanel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.4;  // 40% width
            gbcOption.fill = GridBagConstraints.HORIZONTAL;
            gbcOption.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(optionPanel, gbcOption);

            GridBagConstraints gbcSlayerTasksList = new GridBagConstraints();
            gbcSlayerTasksList.gridx = 1;
            gbcSlayerTasksList.gridy = 0;
            gbcSlayerTasksList.weightx = 0.6;  // 60% width
            gbcSlayerTasksList.weighty = 1.0;
            gbcSlayerTasksList.fill = GridBagConstraints.BOTH;
            gbcSlayerTasksList.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(slayerTasksListPanel, gbcSlayerTasksList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsAdventuresSlayerTasksPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsAdventuresSlayerTasksPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> slayerTaskDifficulty = Arrays.asList("All", "Easy", "Medium", "Hard", "Elite");
    List<String> slayerTaskRegion = Arrays.asList("All", "Wilderness", "Slayer Tower", "Fremennik Slayer Dungeon", "Catacombs of Kourend");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Slayer Task Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Slayer Task Module:");
        gridBuilder.addCheckbox(2, "slayerTaskSettings", "showSlayerTasks");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Difficulty:");
        gridBuilder.addDropdown(2, "slayerTaskSettings", "slayerTaskDifficulty", slayerTaskDifficulty);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Region:");
        gridBuilder.addDropdown(2, "slayerTaskSettings", "slayerTaskRegion", slayerTaskRegion, OsrsSchemas.OSRS_PROFILE_SCHEMA);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createSlayerTasksListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Slayer Tasks List:");
        gridBuilder.nextRow();

        // Create the Slayer tasks list with padding
        DefaultListModel<String> slayerTasksListModel = new DefaultListModel<>();
        slayerTasksListModel.addElement("Nechryael");
        slayerTasksListModel.addElement("Abyssal Demon");
        slayerTasksListModel.addElement("Gargoyle");
        slayerTasksListModel.addElement("Greater Demon");
        slayerTasksListModel.addElement("Black Dragon");

        JList<String> slayerTasksList = new JList<>(slayerTasksListModel);
        slayerTasksList.setFixedCellWidth(300);  // Set a fixed width for each cell
        JScrollPane scrollPane = new JScrollPane(slayerTasksList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrollbar

        // Add the Slayer tasks list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "slayerTasksList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel slayerTasksListPanel = new JPanel(new BorderLayout());
        slayerTasksListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return slayerTasksListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
