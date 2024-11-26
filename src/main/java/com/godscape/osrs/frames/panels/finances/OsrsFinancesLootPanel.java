package com.godscape.osrs.frames.panels.finances;

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
public class OsrsFinancesLootPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsFinancesLootPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsFinancesLootPanel...");

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
            // Create loot list panel
            JPanel lootListPanel = createLootListPanel();

            // Adjusted GridBagConstraints for Option Panel
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.5;
            gbcOption.weighty = 1.0;
            gbcOption.fill = GridBagConstraints.BOTH;
            mainPanel.add(optionPanel, gbcOption);

// Adjusted GridBagConstraints for List Panel (e.g., transactionsListPanel, questListPanel, lootListPanel)
            GridBagConstraints gbcList = new GridBagConstraints();
            gbcList.gridx = 1;
            gbcList.gridy = 0;
            gbcList.weightx = 0.5;
            gbcList.weighty = 1.0;
            gbcList.fill = GridBagConstraints.BOTH;
            mainPanel.add(lootListPanel, gbcList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsFinancesLootPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsFinancesLootPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> lootType = Arrays.asList("All", "Rare Drops", "Common Drops", "Resources", "Equipment");
    List<String> lootValueRange = Arrays.asList("All", "0-10k", "10k-100k", "100k-1M", "1M+");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Loot Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Loot Module:");
        gridBuilder.addCheckbox(2, "lootSettings", "showLoot");
        gridBuilder.nextRow();
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Type:");
        gridBuilder.addDropdown(2, "lootSettings", "lootType", lootType);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Value Range:");
        gridBuilder.addDropdown(2, "lootSettings", "lootValueRange", lootValueRange, OsrsSchemas.OSRS_PROFILE_SCHEMA);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createLootListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Loot List:");
        gridBuilder.nextRow();

        // Create the loot list with padding
        DefaultListModel<String> lootListModel = new DefaultListModel<>();
        lootListModel.addElement("Dragon Claws");
        lootListModel.addElement("Bandos Chestplate");
        lootListModel.addElement("Zenyte Shard");
        lootListModel.addElement("Abyssal Whip");
        lootListModel.addElement("Magic Logs");

        JList<String> lootList = new JList<>(lootListModel);
        lootList.setFixedCellWidth(300);  // Set a fixed width for each cell in the list
        JScrollPane scrollPane = new JScrollPane(lootList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list

        // Add the loot list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "lootList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel lootListPanel = new JPanel(new BorderLayout());
        lootListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return lootListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
