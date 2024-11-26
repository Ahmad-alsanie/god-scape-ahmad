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
public class OsrsAdventuresDiariesPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsAdventuresDiariesPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsAdventuresDiariesPanel...");

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
            // Create diaries list panel
            JPanel diariesListPanel = createDiariesListPanel();

            // Layout optionPanel and diariesListPanel within mainPanel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.4;  // 40% width
            gbcOption.fill = GridBagConstraints.HORIZONTAL;
            gbcOption.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(optionPanel, gbcOption);

            GridBagConstraints gbcDiariesList = new GridBagConstraints();
            gbcDiariesList.gridx = 1;
            gbcDiariesList.gridy = 0;
            gbcDiariesList.weightx = 0.6;  // 60% width
            gbcDiariesList.weighty = 1.0;
            gbcDiariesList.fill = GridBagConstraints.BOTH;
            gbcDiariesList.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(diariesListPanel, gbcDiariesList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsAdventuresDiariesPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsAdventuresDiariesPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> diaryDifficulty = Arrays.asList("All", "Easy", "Medium", "Hard", "Elite");
    List<String> diaryRegion = Arrays.asList("All", "Varrock", "Lumbridge & Draynor", "Falador", "Ardougne", "Karamja");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Diary Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Diary Module:");
        gridBuilder.addCheckbox(2, "diarySettings", "showDiaries");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Difficulty:");
        gridBuilder.addDropdown(2, "diarySettings", "diaryDifficulty", diaryDifficulty);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Region:");
        gridBuilder.addDropdown(2, "diarySettings", "diaryRegion", diaryRegion, OsrsSchemas.OSRS_PROFILE_SCHEMA);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createDiariesListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Diaries List:");
        gridBuilder.nextRow();

        // Create the diaries list with padding
        DefaultListModel<String> diariesListModel = new DefaultListModel<>();
        diariesListModel.addElement("Varrock Diary - Easy");
        diariesListModel.addElement("Ardougne Diary - Medium");
        diariesListModel.addElement("Karamja Diary - Hard");
        diariesListModel.addElement("Lumbridge & Draynor Diary - Elite");
        diariesListModel.addElement("Falador Diary - Easy");

        JList<String> diariesList = new JList<>(diariesListModel);
        diariesList.setFixedCellWidth(300); // Ensure fixed cell width for consistency

        JScrollPane scrollPane = new JScrollPane(diariesList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Prevent horizontal resizing

        // Add the diaries list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "diariesList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel diariesListPanel = new JPanel(new BorderLayout());
        diariesListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return diariesListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
