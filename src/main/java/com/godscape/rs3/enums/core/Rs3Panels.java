package com.godscape.rs3.enums.core;

import com.godscape.system.enums.GameVersion;
import com.godscape.system.templates.EmptyPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public enum Rs3Panels {

    RS3_MAIN_PANEL("Main", null, GameVersion.RS3, EmptyPanel::new),
    RS3_HEADER_PANEL("Header", RS3_MAIN_PANEL, GameVersion.RS3, EmptyPanel::new),
    RS3_FOOTER_PANEL("Footer", RS3_MAIN_PANEL, GameVersion.RS3, EmptyPanel::new),

    RS3_STATS_PANEL("Stats", RS3_MAIN_PANEL, GameVersion.RS3, EmptyPanel::new),
    RS3_STATS_SKILL_GOALS_PANEL("Stats & Skill Goals", RS3_STATS_PANEL, GameVersion.RS3, EmptyPanel::new);

    private final String tabTitle;
    private final Rs3Panels parentPanel;
    private final GameVersion gameVersion;
    private final Supplier<? extends JPanel> supplier;

    Rs3Panels(String tabTitle, Rs3Panels parentPanel, GameVersion gameVersion, Supplier<? extends JPanel> supplier) {
        this.tabTitle = tabTitle;
        this.parentPanel = parentPanel;
        this.gameVersion = gameVersion;
        this.supplier = supplier;
    }

    public String getTabTitle() {
        return tabTitle;
    }

    public Rs3Panels getParentPanel() {
        return parentPanel;
    }

    public GameVersion getGameVersion() {
        return gameVersion;
    }

    public JPanel createPanel() {
        return supplier.get();
    }

    // Changed getSupplier to an instance method
    public Supplier<? extends JPanel> getSupplier() {
        return supplier;
    }

    // Get the full path in lowercase and snake_case format for consistency
    public String getFullPath() {
        List<String> path = getFullTabTitlePath();
        return String.join("_", path).toLowerCase().replaceAll("\\s+", "_");
    }

    // Generates the path list from the root to this panel
    public List<String> getFullTabTitlePath() {
        List<String> path = new ArrayList<>();
        Rs3Panels current = this;
        while (current != null) {
            path.add(0, current.getTabTitle());
            current = current.getParentPanel();
        }
        return path;
    }

    public static Rs3Panels fromName(String name) {
        for (Rs3Panels panel : Rs3Panels.values()) {
            if (panel.name().equalsIgnoreCase(name)) {
                return panel;
            }
        }
        return null;
    }
}
