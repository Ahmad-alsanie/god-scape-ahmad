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
public class OsrsFinancesBankPanel extends JPanel {

    private OsrsProfileSchema profile;
    private final OsrsCacheController cacheController;

    public OsrsFinancesBankPanel() {
        setLayout(new BorderLayout());
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);

        try {
            Logger.info("Initializing OsrsFinancesBankPanel...");

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
            // Create bank items list panel
            JPanel bankListPanel = createBankListPanel();

            // Set up GridBagConstraints for optionPanel with consistent fill and weight
            GridBagConstraints gbcOption = new GridBagConstraints();
            gbcOption.gridx = 0;
            gbcOption.gridy = 0;
            gbcOption.weightx = 0.5;
            gbcOption.weighty = 1.0; // Ensuring vertical expansion
            gbcOption.fill = GridBagConstraints.BOTH;
            mainPanel.add(optionPanel, gbcOption);

// Set up GridBagConstraints for bankListPanel with consistent settings
            GridBagConstraints gbcBankList = new GridBagConstraints();
            gbcBankList.gridx = 1;
            gbcBankList.gridy = 0;
            gbcBankList.weightx = 0.5;
            gbcBankList.weighty = 1.0;
            gbcBankList.fill = GridBagConstraints.BOTH;
            mainPanel.add(bankListPanel, gbcBankList);

            // Add mainPanel to scroll pane
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(mainScrollPane, BorderLayout.CENTER);

            Logger.info("PanelGrid added to OsrsFinancesBankPanel layout successfully.");
        } catch (Exception e) {
            Logger.error("Error initializing OsrsFinancesBankPanel: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    List<String> bankViewOptions = Arrays.asList("All", "Favorites", "Recent Items", "High-Value Items");
    List<String> bankSortOptions = Arrays.asList("Alphabetical", "Value Descending", "Value Ascending");

    private JPanel createOptionPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Use addDropdown and addCheckbox methods to add components
        gridBuilder.addSeparator("Bank Settings:");
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "View Mode:");
        gridBuilder.addDropdown(2, "bankSettings", "viewMode", bankViewOptions);
        gridBuilder.nextRow();

        gridBuilder.addLabel(1, "Sort By:");
        gridBuilder.addDropdown(2, "bankSettings", "sortBy", bankSortOptions, OsrsSchemas.OSRS_PROFILE_SCHEMA);
        gridBuilder.nextRow();

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return optionPanel;
    }

    private JPanel createBankListPanel() {
        OsrsGridBuilder gridBuilder = new OsrsGridBuilder(null);

        // Add the separator
        gridBuilder.addSeparator("Bank Inventory:");
        gridBuilder.nextRow();

        // Create the bank item list with padding
        DefaultListModel<String> bankListModel = new DefaultListModel<>();
        bankListModel.addElement("Rune Scimitar");
        bankListModel.addElement("Dragon Axe");
        bankListModel.addElement("Super Restore (4)");
        bankListModel.addElement("Noted Lobster");
        bankListModel.addElement("Coins (1,000,000)");

        JList<String> bankList = new JList<>(bankListModel);
        JScrollPane scrollPane = new JScrollPane(bankList);
         scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // Add 5px padding around the list

        // Add the bank list to the gridBuilder
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // First column
        gbc.gridy = gridBuilder.getCurrentRow();
        gbc.gridwidth = 1;
        gbc.weightx = 0.0; // Do not let it expand horizontally
        gbc.weighty = 1.0; // Allow vertical expansion
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gridBuilder.addComponent(null, "bankList", scrollPane, OsrsSchemas.OSRS_PROFILE_SCHEMA, gbc);

        JPanel bankListPanel = new JPanel(new BorderLayout());
        bankListPanel.add(gridBuilder, BorderLayout.NORTH); // Align to top
        return bankListPanel;
    }

    private UUID profileIdOrDefault() {
        return profile != null ? profile.getProfileId() : UUID.randomUUID();
    }
}
