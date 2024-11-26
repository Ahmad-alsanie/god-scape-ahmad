package com.godscape.system.utility;

import com.godscape.osrs.enums.core.OsrsPanels;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the tab hierarchy, holding a panel and its child tabs.
 */
public class TabNode {
    private final OsrsPanels panel;
    private final List<TabNode> children = new ArrayList<>();

    public TabNode(OsrsPanels panel) {
        this.panel = panel;
    }

    public OsrsPanels getPanel() {
        return panel;
    }

    public List<TabNode> getChildren() {
        return children;
    }

    public void addChild(TabNode child) {
        children.add(child);
    }
}
