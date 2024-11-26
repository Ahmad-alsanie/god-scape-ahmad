package com.godscape.osrs.frames.panels;

import com.godscape.osrs.managers.panels.OsrsFooterManager;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.system.factories.PanelFactory;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.TabNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main panel for the OSRS interface that integrates all sub-panels.
 */
public class OsrsMainPanel extends JPanel {
    private final JTabbedPane tabbedPane;
    private final PanelFactory panelFactory;
    private final OsrsFooterManager footerManager;

    public OsrsMainPanel(OsrsFooterManager footerManager) {
        this.footerManager = footerManager;
        setLayout(new BorderLayout(2, 2));
        setOpaque(false); // Ensure the main panel is transparent to allow theme to show.

        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setOpaque(false); // Make the inner panel transparent too.
        innerPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add 10px padding on all sides

        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        this.tabbedPane.setOpaque(false); // Make the tabbed pane transparent.
        this.tabbedPane.setBackground(new Color(0, 0, 0, 0)); // Transparent background for tabbed pane.
        this.tabbedPane.setForeground(Color.WHITE); // Make sure text remains visible if needed.

        this.panelFactory = PanelFactory.getInstance();
        innerPanel.add(tabbedPane, BorderLayout.CENTER);

        add(innerPanel, BorderLayout.CENTER);

        initializeTabs();
    }

    private void initializeTabs() {
        Map<OsrsPanels, TabNode> tabNodeMap = new HashMap<>();
        for (OsrsPanels panel : OsrsPanels.values()) {
            if (isExcludedPanel(panel)) {
                continue;
            }
            tabNodeMap.put(panel, new TabNode(panel));
        }

        TabNode root = new TabNode(null); // Root node
        for (OsrsPanels panel : OsrsPanels.values()) {
            if (isExcludedPanel(panel)) {
                continue;
            }

            TabNode node = tabNodeMap.get(panel);
            OsrsPanels parentPanel = panel.getParentPanel();
            if (parentPanel == null || isExcludedPanel(parentPanel)) {
                root.addChild(node);
            } else {
                TabNode parentNode = tabNodeMap.get(parentPanel);
                if (parentNode != null) {
                    parentNode.addChild(node);
                } else {
                    Logger.error("Parent panel not found for: {}", panel.getTabTitle());
                }
            }
        }

        createTabs(tabbedPane, root.getChildren());
    }

    private void createTabs(JTabbedPane parentTabbedPane, List<TabNode> nodes) {
        for (TabNode node : nodes) {
            OsrsPanels panelEnum = node.getPanel();

            if (isExcludedPanel(panelEnum)) {
                continue;
            }

            String tabTitle = panelEnum.getTabTitle();

            if (!node.getChildren().isEmpty()) {
                JTabbedPane nestedTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
                nestedTabbedPane.setOpaque(false); // Ensure nested tabs are transparent.
                nestedTabbedPane.setBackground(new Color(0, 0, 0, 0)); // Transparent background.
                nestedTabbedPane.setForeground(Color.WHITE); // Visible text in the tabs.
                parentTabbedPane.addTab(tabTitle, nestedTabbedPane);
                createTabs(nestedTabbedPane, node.getChildren());
            } else {
                try {
                    JComponent panel = panelFactory.createPanel(panelEnum, footerManager);
                    if (panel != null) {
                        panel.setOpaque(false); // Make panel transparent.

                        // Wrap the panel to ensure components start at the top
                        JPanel wrapperPanel = new JPanel(new BorderLayout());
                        wrapperPanel.setOpaque(false);
                        wrapperPanel.add(panel, BorderLayout.NORTH); // Align panel to the top
                        parentTabbedPane.addTab(tabTitle, wrapperPanel);
                    } else {
                        Logger.warn("Panel not created for: {} - proceeding without it.", panelEnum.getTabTitle());
                    }
                } catch (Exception e) {
                    Logger.error("Error creating panel for {}: {}. Skipping this panel.", panelEnum.getTabTitle(), e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Checks if a panel is either OSRS_MAIN_PANEL or a child of it.
     *
     * @param panel The panel to check.
     * @return True if the panel is excluded, false otherwise.
     */
    private boolean isExcludedPanel(OsrsPanels panel) {
        if (panel == OsrsPanels.OSRS_MAIN_PANEL) {
            return true; // Exclude main panels
        }

        OsrsPanels parent = panel.getParentPanel();
        while (parent != null) {
            if (parent == OsrsPanels.OSRS_MAIN_PANEL) {
                return true; // Exclude children of main panels
            }
            parent = parent.getParentPanel();
        }

        return false;
    }
}
