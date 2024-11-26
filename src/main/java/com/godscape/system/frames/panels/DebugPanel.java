package com.godscape.system.frames.panels;

import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Schemas;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.schemas.ThemeSchema;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.ProfilesListTools;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public class DebugPanel extends JPanel {

    // Singleton instance
    private static volatile DebugPanel instance;

    // UI Components
    private JTree cacheTree;
    private JTextArea detailsArea;
    private JButton refreshButton;
    private JTextField searchField;
    private JButton searchButton;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    public DebugPanel() {
        // Set layout for the panel
        setLayout(new BorderLayout());

        // Initialize UI Components
        initUI();

        // Populate the tree with Hazelcast data
        populateTree();
    }

    /**
     * Initializes the UI components and layout.
     */
    private void initUI() {
        // Split pane to divide tree and details area
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(300);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

        // Initialize the tree
        cacheTree = new JTree();
        cacheTree.setModel(null); // Start with no model
        cacheTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        cacheTree.setShowsRootHandles(true);
        cacheTree.setRootVisible(true);

        // Set custom tree cell renderer for icons
        cacheTree.setCellRenderer(new CustomTreeCellRenderer());

        // Add a tree selection listener to handle node selection
        cacheTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                if (path == null) {
                    return;
                }
                Object selectedNode = path.getLastPathComponent();
                if (selectedNode instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectedNode;
                    Object userObject = node.getUserObject();
                    displayDetails(userObject);
                }
            }
        });

        JScrollPane treeScrollPane = new JScrollPane(cacheTree);
        treeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Ensure vertical scrollbar is always visible
        splitPane.setLeftComponent(treeScrollPane);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Vertical scrollbar appears as needed
        splitPane.setRightComponent(detailsScrollPane);

        add(splitPane, BorderLayout.CENTER);

        // Add the top panel with Refresh and Search buttons
        JPanel topPanel = new JPanel(new BorderLayout());

        // Refresh Button
        refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.addActionListener(e -> {
            populateTree();
            detailsArea.setText("");
        });

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                searchTree(query);
            }
        });

        searchField.addActionListener(e -> searchButton.doClick()); // Trigger search on Enter key

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Populates the tree with data from Hazelcast using OsrsSchemas enum.
     */
    private void populateTree() {
        try {
            // Retrieve Hazelcast instance
            HazelcastInstance hazelcastInstance = DependencyFactory.getInstance().getInjection(HazelcastConfig.class).getHazelcastInstance();

            if (hazelcastInstance == null) {
                Logger.error("DebugPanel: HazelcastInstance is not initialized.");
                JOptionPane.showMessageDialog(this, "HazelcastInstance is not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Root node
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Hazelcast");

            // Adding OSRS Profile Cache branch and confirming retrieval
            Logger.info("DebugPanel: Retrieving OSRS profiles from cache.");
            IMap<UUID, OsrsProfileSchema> profileCache = hazelcastInstance.getMap(OsrsSchemas.OSRS_PROFILE_SCHEMA.getCacheName());

            if (profileCache.isEmpty()) {
                Logger.warn("DebugPanel: No profiles found in OSRS profile cache.");
            } else {
                Logger.info("DebugPanel: Found {} profiles in OSRS profile cache.", profileCache.size());
            }

            // Add the schema branch for OSRS profiles
            addSchemaBranch(root, OsrsSchemas.OSRS_PROFILE_SCHEMA, hazelcastInstance);
            addSchemaBranch(root, OsrsSchemas.OSRS_CHARACTER_SCHEMA, hazelcastInstance);

            // Set the model for the tree
            DefaultTreeModel model = new DefaultTreeModel(root);
            cacheTree.setModel(model);

            // Expand all nodes
            expandAllNodes(cacheTree, 0, cacheTree.getRowCount());

        } catch (Exception e) {
            Logger.error("DebugPanel: Error populating tree.", e);
            JOptionPane.showMessageDialog(this, "Error populating tree: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Adds a branch to the tree for a specific schema.
     *
     * @param parent           The parent node.
     * @param schema           The OsrsSchemas enum value.
     * @param hazelcastInstance The HazelcastInstance to retrieve maps from.
     */
    private void addSchemaBranch(DefaultMutableTreeNode parent, OsrsSchemas schema, HazelcastInstance hazelcastInstance) {   // Create a schema node with a readable map name
        DefaultMutableTreeNode schemaNode = new DefaultMutableTreeNode(schema.toString());
        parent.add(schemaNode);

        // Retrieve the IMap from Hazelcast using the cache name from enum
        IMap<?, ?> map = hazelcastInstance.getMap(schema.getCacheName());

        if (map == null) {
            Logger.warn("DebugPanel: IMap '{}' not found in Hazelcast.", schema.getCacheName());
            schemaNode.add(new DefaultMutableTreeNode("Error: Map not found"));
            return;
        }

        if (map.isEmpty()) {
            Logger.info("DebugPanel: IMap '{}' is empty.", schema.getCacheName());
            schemaNode.add(new DefaultMutableTreeNode("No entries found"));
            return;
        }

        Logger.info("DebugPanel: IMap '{}' contains {} entries.", schema.getCacheName(), map.size());

        // Depending on the schema, handle different types
        if (schema == OsrsSchemas.OSRS_PROFILE_SCHEMA) {
            List<OsrsProfileSchema> profiles = new ArrayList<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof OsrsProfileSchema) {
                    profiles.add((OsrsProfileSchema) value);
                }
            }

            // Sort the profiles using ProfilesListTools
            ProfilesListTools.sortProfiles(profiles, true); // Change to false for descending order

            // Add sorted profiles to the tree
            for (OsrsProfileSchema profile : profiles) {
                String displayName = profile.getProfileName();
                if (displayName == null || displayName.trim().isEmpty()) {
                    displayName = "Unnamed Profile (" + profile.getProfileId() + ")";
                }
                DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode(new DisplayNode(displayName, profile));
                schemaNode.add(entryNode);
            }
        } else if (schema == OsrsSchemas.OSRS_CHARACTER_SCHEMA) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof ThemeSchema) {
                    ThemeSchema theme = (ThemeSchema) value;
                    String displayName = theme.getThemeName();
                    if (displayName == null || displayName.trim().isEmpty()) {
                        displayName = "Unnamed Theme (" + theme.getThemeId() + ")";
                    }
                    DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode(new DisplayNode(displayName, theme));
                    schemaNode.add(entryNode);
                }
            }
        }
    }

    /**
     * Displays details of a selected node in the details area.
     *
     * @param userObject The selected node's user object.
     */
    private void displayDetails(Object userObject) {
        if (userObject == null) {
            detailsArea.setText("");
            return;
        }

        Object actualObject = userObject instanceof DisplayNode ? ((DisplayNode) userObject).getActualObject() : userObject;

        StringBuilder details = new StringBuilder();

        if (actualObject instanceof ThemeSchema) {
            ThemeSchema theme = (ThemeSchema) actualObject;
            details.append("ThemeSchema Details:\n");
            details.append("----------------------\n");
            details.append("Theme Name: ").append(theme.getThemeName()).append("\n");
            details.append("Is Active: ").append(theme.isActive()).append("\n");
            details.append("Selected Theme: ").append(theme.getSelectedTheme()).append("\n");
            details.append("Intensity: ").append(theme.getIntensity()).append("\n");
            details.append("Smooth Corners: ").append(theme.isSmoothCorners()).append("\n");
            details.append("Smooth Buttons: ").append(theme.isSmoothButtons()).append("\n");
            details.append("Animations: ").append(theme.isAnimations()).append("\n");
            details.append("Transparency: ").append(theme.isTransparency()).append("\n");
            details.append("Highlights: ").append(theme.isHighlights()).append("\n");
            details.append("Shadows: ").append(theme.isShadows()).append("\n");
        } else if (actualObject instanceof OsrsProfileSchema) {
            OsrsProfileSchema profile = (OsrsProfileSchema) actualObject;
            details.append("OsrsProfileSchema Details:\n");
            details.append("--------------------------\n");
            details.append("Profile ID: ").append(profile.getProfileId()).append("\n");
            details.append("Profile Name: ").append(profile.getProfileName()).append("\n");
            // Uncomment if profile notes are available
            // details.append("Profile Notes: ").append(profile.getProfileNotes()).append("\n");
            details.append("Last Updated: ").append(profile.getLastUpdated()).append("\n");

            details.append("Settings Map:\n");
            Map<String, Object> settingsMap = profile.getSettingsMap();

            for (Map.Entry<String, Object> categoryEntry : settingsMap.entrySet()) {
                String category = categoryEntry.getKey();
                Object categoryContent = categoryEntry.getValue();

                if (categoryContent instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> subMap = (Map<String, Object>) categoryContent;
                    details.append("  ").append(category).append(":\n");

                    for (Map.Entry<String, Object> entry : subMap.entrySet()) {
                        details.append("    ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                } else {
                    details.append("  ").append(category).append(": ").append(categoryContent).append("\n");
                }
            }
        } else {
            details.append("Unknown Object Type:\n");
            details.append(actualObject.toString());
        }

        detailsArea.setText(details.toString());
    }

    /**
     * Expands all nodes in the tree.
     *
     * @param tree         The JTree to expand.
     * @param startingIndex The starting row index.
     * @param rowCount     The total number of rows.
     */
    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            tree.expandRow(i);
        }

        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    /**
     * Searches the tree for nodes matching the query and selects the first match.
     *
     * @param query The search query.
     */
    private void searchTree(String query) {
        TreeModel model = cacheTree.getModel();
        if (model == null) {
            return;
        }

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        TreePath path = searchNode(root, query.toLowerCase());
        if (path != null) {
            cacheTree.setSelectionPath(path);
            cacheTree.scrollPathToVisible(path);
        } else {
            JOptionPane.showMessageDialog(this, "No matching entries found for \"" + query + "\".", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Recursively searches for a node containing the query string.
     *
     * @param node  The current node.
     * @param query The search query in lowercase.
     * @return The TreePath to the matching node, or null if not found.
     */
    private TreePath searchNode(DefaultMutableTreeNode node, String query) {
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            Object userObject = child.getUserObject();
            String nodeName = "";

            if (userObject instanceof DisplayNode) {
                nodeName = ((DisplayNode) userObject).toString();
            } else {
                nodeName = child.toString();
            }

            if (nodeName.toLowerCase().contains(query)) {
                return new TreePath(child.getPath());
            }

            TreePath path = searchNode(child, query);
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    /**
     * Custom TreeCellRenderer to display different icons based on node type.
     */
    private class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        private Icon profileIcon;
        private Icon themeIcon;
        private Icon defaultIcon;

        public CustomTreeCellRenderer() {
            profileIcon = UIManager.getIcon("FileView.fileIcon");
            themeIcon = UIManager.getIcon("FileView.directoryIcon");
            defaultIcon = UIManager.getIcon("FileView.computerIcon");
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObject = node.getUserObject();

                if (userObject instanceof DisplayNode) {
                    Object actualObject = ((DisplayNode) userObject).getActualObject();
                    if (actualObject instanceof OsrsProfileSchema) {
                        setIcon(profileIcon);
                    } else if (actualObject instanceof ThemeSchema) {
                        setIcon(themeIcon);
                    }
                } else {
                    String nodeName = node.toString();
                    if (nodeName.equals("OSRS Profile Cache")) {
                        setIcon(profileIcon);
                    } else if (nodeName.equals("Theme Cache")) {
                        setIcon(themeIcon);
                    } else if (nodeName.equals("Hazelcast")) {
                        setIcon(defaultIcon);
                    }
                }
            }

            return this;
        }
    }

    /**
     * Inner class to represent display nodes with actual objects.
     */
    private static class DisplayNode {
        private final String displayName;
        private final Object actualObject;

        public DisplayNode(String displayName, Object actualObject) {
            this.displayName = displayName;
            this.actualObject = actualObject;
        }

        @Override
        public String toString() {
            return displayName;
        }

        public Object getActualObject() {
            return actualObject;
        }
    }
}
