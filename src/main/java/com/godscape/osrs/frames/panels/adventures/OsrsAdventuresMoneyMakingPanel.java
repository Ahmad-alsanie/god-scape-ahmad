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
public class OsrsAdventuresMoneyMakingPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsAdventuresMoneyMakingPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsAdventuresMoneyMakingPanel...");

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
            // Create Money-Making methods list panel
            JPanel moneyMakingListPanel = createMoneyMakingListPanel();

            // Layout optionPanel and moneyMakingListPanel within mainPanel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.4;  // 40% width
            gbcOption.fill = GridBagConstraints.HORIZONTAL;
            gbcOption.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(optionPanel, gbcOption);

            GridBagConstraints gbcMoneyMakingList = new GridBagConstraints();
            gbcMoneyMakingList.gridx = 1;
            gbcMoneyMakingList.gridy = 0;
            gbcMoneyMakingList.weightx = 0.6;  // 60% width
            gbcMoneyMakingList.weighty = 1.0;
            gbcMoneyMakingList.fill = GridBagConstraints.BOTH;
            gbcMoneyMakingList.anchor = GridBagConstraints.NORTHWEST;
            mainPanel.add(moneyMakingListPanel, gbcMoneyMakingList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsAdventuresMoneyMakingPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsAdventuresMoneyMakingPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> moneyMakingMethodType = Arrays.asList("All", "Skilling", "PvM", "Merchanting", "Other");
    List<String> moneyMakingMethodDifficulty = Arrays.asList("All", "Beginner", "Intermediate", "Advanced");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Money-Making Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Money-Making Module:");
        gridBuilder.addCheckbox(2, "moneyMakingSettings", "showMoneyMakingMethods");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Type:");
        gridBuilder.addDropdown(2, "moneyMakingSettings", "moneyMakingType", moneyMakingMethodType);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Difficulty:");
        gridBuilder.addDropdown(2, "moneyMakingSettings", "moneyMakingDifficulty", moneyMakingMethodDifficulty);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createMoneyMakingListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Money-Making Methods List:");
        gridBuilder.nextRow();

        // Create the money-making methods list with fixed width
        DefaultListModel<String> moneyMakingListModel = new DefaultListModel<>();
        moneyMakingListModel.addElement("Flipping Items");
        moneyMakingListModel.addElement("Killing Green Dragons");
        moneyMakingListModel.addElement("Woodcutting Magic Trees");
        moneyMakingListModel.addElement("Crafting Blood Runes");
        moneyMakingListModel.addElement("Barrows Runs");

        JList<String> moneyMakingList = new JList<>(moneyMakingListModel);
        moneyMakingList.setFixedCellWidth(300); // Set fixed width for the list
        JScrollPane scrollPane = new JScrollPane(moneyMakingList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list

        // Add the money-making methods list to the gridBuilder with fixed size constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "moneyMakingList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel moneyMakingListPanel = new JPanel(new BorderLayout());
        moneyMakingListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return moneyMakingListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
