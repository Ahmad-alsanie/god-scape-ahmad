package com.godscape.osrs.frames.panels.finances;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.osrs.enums.game.OsrsCurrencyType;
import com.godscape.osrs.utility.OsrsGridBuilder;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OsrsFinancesWalletPanel extends JPanel {

    private final OsrsGridBuilder gridBuilder;

    public OsrsFinancesWalletPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(600, 400));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize grid builder for the panel layout
        gridBuilder = new OsrsGridBuilder(OsrsPanels.OSRS_FINANCES_WALLET_PANEL);

        // Populate the grid with currency options
        populateCurrencyOptions();

        // Add the grid to the panel
        add(gridBuilder.getScrollPane(), BorderLayout.CENTER);
    }

    private void populateCurrencyOptions() {
        gridBuilder.addSeparator("Wallet Settings");
        gridBuilder.nextRow();

        for (OsrsCurrencyType currency : OsrsCurrencyType.values()) {
            // Add checkbox to enable currency
            gridBuilder.addCheckbox(1, "currencySettings", currency.name().toLowerCase() + "_enabled", OsrsSchemas.OSRS_PROFILE_SCHEMA);

            // Add currency name label
            gridBuilder.addLabel(2, currency.getDisplayName() + ":");

            // Add "Min" label
            gridBuilder.addLabel(3, "Min:");

            // Add minimum amount text field
            gridBuilder.addTextField(4, "currencySettings", currency.name().toLowerCase() + "_min", OsrsSchemas.OSRS_PROFILE_SCHEMA);

            // Add "Max" label
            gridBuilder.addLabel(5, "Max:");

            // Add maximum amount text field
            gridBuilder.addTextField(6, "currencySettings", currency.name().toLowerCase() + "_max", OsrsSchemas.OSRS_PROFILE_SCHEMA);

            gridBuilder.nextRow(); // Move to the next row for each currency
        }
    }
}
