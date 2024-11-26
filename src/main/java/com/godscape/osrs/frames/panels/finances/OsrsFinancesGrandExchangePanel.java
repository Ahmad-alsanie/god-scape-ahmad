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
public class OsrsFinancesGrandExchangePanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsFinancesGrandExchangePanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsFinancesGrandExchangePanel...");

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
            // Create Grand Exchange transactions list panel
            JPanel transactionsListPanel = createTransactionsListPanel();

            // Layout optionPanel and lootListPanel within mainPanel
            // Set up GridBagConstraints for optionPanel with consistent fill and weight
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.5;
            gbcOption.weighty = 1.0; // Ensuring both components expand vertically as well
            gbcOption.fill = GridBagConstraints.BOTH;
            mainPanel.add(optionPanel, gbcOption);

// Set up GridBagConstraints for transactionsListPanel with consistent settings
            GridBagConstraints gbcTransactionsList = new GridBagConstraints();
            gbcTransactionsList.gridx = 1;
            gbcTransactionsList.gridy = 0;
            gbcTransactionsList.weightx = 0.5;
            gbcTransactionsList.weighty = 1.0;
            gbcTransactionsList.fill = GridBagConstraints.BOTH;
            mainPanel.add(transactionsListPanel, gbcTransactionsList);


            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsFinancesGrandExchangePanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsFinancesGrandExchangePanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> itemTypeOptions = Arrays.asList("All", "Weapons", "Armor", "Resources", "Potions", "Others");
    List<String> priceRangeOptions = Arrays.asList("All", "0-1k", "1k-10k", "10k-100k", "100k+");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Grand Exchange Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Track Listings:");
        gridBuilder.addCheckbox(2, "geSettings", "trackListings");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Item Type:");
        gridBuilder.addDropdown(2, "geSettings", "itemType", itemTypeOptions);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Price Range:");
        gridBuilder.addDropdown(2, "geSettings", "priceRange", priceRangeOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createTransactionsListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Recent Transactions:");
        gridBuilder.nextRow();

        // Create the transactions list with padding
        DefaultListModel<String> transactionsListModel = new DefaultListModel<>();
        transactionsListModel.addElement("Bought: Abyssal Whip - 2,500,000 gp");
        transactionsListModel.addElement("Sold: Dragon Scimitar - 150,000 gp");
        transactionsListModel.addElement("Bought: Rune Platebody - 84,000 gp");
        transactionsListModel.addElement("Bought: Law Runes (x100) - 30,000 gp");
        transactionsListModel.addElement("Sold: Oak Logs (x1000) - 10,000 gp");

        JList<String> transactionsList = new JList<>(transactionsListModel);
        transactionsList.setPrototypeCellValue("Example Text"); // Ensures consistent cell width

        // Configure the scroll pane with fixed dimensions
        JScrollPane scrollPane = new JScrollPane(transactionsList);
          scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list

        // Add the transactions list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "transactionsList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel transactionsListPanel = new JPanel(new BorderLayout());
        transactionsListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return transactionsListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
