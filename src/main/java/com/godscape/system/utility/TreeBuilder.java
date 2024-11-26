package com.godscape.system.utility;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * TreeBuilder is a generalized utility for building JTree structures from various data sources.
 */
public class TreeBuilder {

    /**
     * Builds a tree from the provided schema configurations.
     *
     * @param rootName      The name of the root node.
     * @param schemaConfigs List of SchemaConfig objects defining how each schema should be added to the tree.
     * @return The root node of the built tree.
     */
    public DefaultMutableTreeNode buildTree(String rootName, List<SchemaConfig<?>> schemaConfigs) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootName);
        for (SchemaConfig<?> schemaConfig : schemaConfigs) {
            addSchemaBranch(root, schemaConfig);
        }
        return root;
    }

    /**
     * Adds a schema branch to the tree.
     *
     * @param parent       The parent tree node to which the branch should be added.
     * @param schemaConfig The configuration for this schema branch.
     */
    private <T> void addSchemaBranch(DefaultMutableTreeNode parent, SchemaConfig<T> schemaConfig) {
        DefaultMutableTreeNode schemaNode = new DefaultMutableTreeNode(schemaConfig.readableName);
        parent.add(schemaNode);

        Map<?, T> dataMap = schemaConfig.dataSupplier.get();
        if (dataMap == null || dataMap.isEmpty()) {
            schemaNode.add(new DefaultMutableTreeNode("No entries found"));
            return;
        }

        for (T entry : dataMap.values()) {
            String nodeName = schemaConfig.nodeLabelProvider.apply(entry);
            schemaNode.add(new DefaultMutableTreeNode(new DisplayNode(nodeName, entry)));
        }
    }

    /**
     * A configuration class representing the settings for each schema type in the tree.
     *
     * @param <T> The type of the data entries.
     */
    public static class SchemaConfig<T> {
        public final String readableName;
        public final Supplier<Map<?, T>> dataSupplier;
        public final Function<T, String> nodeLabelProvider;

        /**
         * Creates a SchemaConfig instance.
         *
         * @param readableName     The human-readable name of the schema branch.
         * @param dataSupplier     Supplies the data map for this schema.
         * @param nodeLabelProvider Provides a label for each node in this schema.
         */
        public SchemaConfig(String readableName, Supplier<Map<?, T>> dataSupplier, Function<T, String> nodeLabelProvider) {
            this.readableName = readableName;
            this.dataSupplier = dataSupplier;
            this.nodeLabelProvider = nodeLabelProvider;
        }
    }

    /**
     * DisplayNode encapsulates a display name and the underlying object.
     */
    public static class DisplayNode {
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

        /**
         * Provides detailed information about the actual object.
         *
         * @return A string representation of the actual object's details.
         */
        public String getDetails() {
            return actualObject.toString(); // Customize this as needed for detailed information
        }
    }
}
