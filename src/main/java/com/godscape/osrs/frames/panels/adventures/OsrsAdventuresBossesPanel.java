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
public class OsrsAdventuresBossesPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsAdventuresBossesPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsAdventuresBossesPanel...");

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
            // Create boss list panel
            JPanel bossListPanel = createBossListPanel();

            // Layout optionPanel and bossListPanel within mainPanel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.4;  // 40% width
            gbcOption.fill = GridBagConstraints.HORIZONTAL;
            gbcOption.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(optionPanel, gbcOption);

            GridBagConstraints gbcBossList = new GridBagConstraints();
            gbcBossList.gridx = 1;
            gbcBossList.gridy = 0;
            gbcBossList.weightx = 0.6;  // 60% width
            gbcBossList.weighty = 1.0;
            gbcBossList.fill = GridBagConstraints.BOTH;
            gbcBossList.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(bossListPanel, gbcBossList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsAdventuresBossesPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsAdventuresBossesPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> bossDifficulty = Arrays.asList("All", "Easy", "Medium", "Hard", "Elite", "Master");
    List<String> bossType = Arrays.asList("All", "Solo", "Group");
    List<String> bossRegion = Arrays.asList("All", "Wilderness", "God Wars Dungeon", "Kalphite Lair", "Dagannoth Kings", "Others");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Boss Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Boss Module:");
        gridBuilder.addCheckbox(2, "bossSettings", "showBosses");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Type:");
        gridBuilder.addDropdown(2, "bossSettings", "bossType", bossType);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Difficulty:");
        gridBuilder.addDropdown(2, "bossSettings", "bossDifficulty", bossDifficulty);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Region:");
        gridBuilder.addDropdown(2, "bossSettings", "bossRegion", bossRegion, OsrsSchemas.OSRS_PROFILE_SCHEMA);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createBossListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Boss List:");
        gridBuilder.nextRow();

        // Create the boss list with padding
        DefaultListModel<String> bossListModel = new DefaultListModel<>();
        bossListModel.addElement("K'ril Tsutsaroth");
        bossListModel.addElement("General Graardor");
        bossListModel.addElement("Commander Zilyana");
        bossListModel.addElement("Kalphite Queen");
        bossListModel.addElement("Dagannoth Supreme");

        JList<String> bossList = new JList<>(bossListModel);
        bossList.setFixedCellWidth(300); // Set fixed width for list cells

        JScrollPane scrollPane = new JScrollPane(bossList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrollbar

        // Add the boss list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "bossList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel bossListPanel = new JPanel(new BorderLayout());
        bossListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return bossListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
