package com.godscape.osrs.utility;

import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.system.enums.GameVersion;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.builders.BaseGridBuilder;

import javax.swing.*;
import java.awt.*;

public class OsrsGridBuilder extends BaseGridBuilder {

    public OsrsGridBuilder(Enum<?> panel) {
        super(panel);
    }

    @Override
    public GameVersion getGameVersion() {
        return GameVersion.OSRS;
    }

    @Override
    protected Enum<?> getSchema() {
        return OsrsSchemas.OSRS_PROFILE_SCHEMA;
    }

    @Override
    protected OsrsSettingsController getController() {
        return DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);
    }

    @Override
    public String addComponent(int column, String category, String key, JComponent component, Enum<?> schema) {
        return super.addComponent(column, category, key, component, schema);
    }

    public void addTallList(String category, String key, JList<String> list, int startColumn, int columnSpan, int startingRow, int rowsToSpan) {
        JScrollPane listScrollPane = new JScrollPane(list);
        list.setVisibleRowCount(15);

        GridBagConstraints listConstraints = new GridBagConstraints();
        listConstraints.gridx = startColumn - 1;
        listConstraints.gridy = startingRow;
        listConstraints.gridwidth = columnSpan;
        listConstraints.gridheight = rowsToSpan;
        listConstraints.weightx = 1.0;
        listConstraints.weighty = 0.0; // Prevent vertical stretching
        listConstraints.fill = GridBagConstraints.BOTH;
        listConstraints.insets = new Insets(5, 5, 5, 5);
        listConstraints.anchor = GridBagConstraints.NORTH; // Align to top

        addComponent(category, key, listScrollPane, getSchema(), listConstraints);

        maxColumnsInRow = Math.max(maxColumnsInRow, startColumn + columnSpan - 1);

        // Update currentRow if necessary
        currentRow = startingRow + rowsToSpan;
    }

}
