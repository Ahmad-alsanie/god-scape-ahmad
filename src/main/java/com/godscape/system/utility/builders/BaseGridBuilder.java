package com.godscape.system.utility.builders;

import com.godscape.system.controllers.PlatformController;
import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.rs3.controllers.Rs3SettingController;
import com.godscape.rs3.enums.core.Rs3Panels;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.generators.BaseKeyGenerator;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.PanelSeparate;
import com.godscape.system.observers.ProfileUpdateScanner;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Optional;

@Singleton
public abstract class BaseGridBuilder extends JPanel {

    private final JScrollPane scrollPane;
    private final ProfileUpdateScanner profileUpdateScanner;  // Scanner to attach listeners
    public int currentRow = 0;
    public final GridBagConstraints baseConstraints = new GridBagConstraints();
    public final GridBagLayout gridBagLayout;
    private final Enum<?> panel;
    private final Set<String> listenerKeys = new HashSet<>();
    public final Map<String, JComponent> componentMap = new HashMap<>();  // Stores components by key
    public int maxColumnsInRow = 0;

    // Instantiate BaseKeyGenerator
    private final BaseKeyGenerator keyGenerator = new BaseKeyGenerator();

    // Inject the PlatformController
    private final PlatformController platformController = new PlatformController();

    /**
     * Abstract method to retrieve the schema enum.
     * Must be implemented by concrete subclasses to return the appropriate schema.
     *
     * @return The schema enum (OsrsPanels or Rs3Panels).
     */
    protected abstract Enum<?> getSchema();

    /**
     * Abstract method to retrieve the settings controller.
     * Must be implemented by concrete subclasses to return the appropriate controller.
     *
     * @return The settings controller (OsrsSettingsController or Rs3SettingController).
     */
    protected abstract Object getController();

    public BaseGridBuilder(Enum<?> panel) {
        this.panel = panel;
        this.profileUpdateScanner = DependencyFactory.getInstance().getInjection(ProfileUpdateScanner.class);  // Initialize the scanner
        this.gridBagLayout = new GridBagLayout();
        setLayout(this.gridBagLayout);

        baseConstraints.insets = new Insets(5, 5, 5, 5);
        baseConstraints.fill = GridBagConstraints.BOTH;
        baseConstraints.weightx = 1.0;
        baseConstraints.anchor = GridBagConstraints.NORTH; // Align components to the top

        setOpaque(false);

        this.scrollPane = new JScrollPane(this);
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.scrollPane.setOpaque(false);
        this.scrollPane.getViewport().setOpaque(false);
    }

    // Retrieve the game version from PlatformController
    public GameVersion getGameVersion() {
        return platformController.getCurrentGameVersion();
    }

    // Method to get the current row index
    public int getCurrentRow() {
        return currentRow;
    }

    private void updateGridColumnWeights() {
        int columnCount = Math.max(maxColumnsInRow, 1);
        double[] columnWeights = new double[columnCount];
        Arrays.fill(columnWeights, 1.0);
        gridBagLayout.columnWeights = columnWeights;

        int[] columnWidths = new int[columnCount];
        Arrays.fill(columnWidths, 100);
        gridBagLayout.columnWidths = columnWidths;
    }

    /**
     * Adds a separator to the grid layout.
     *
     * @param title The title of the separator.
     */
    public void addSeparator(String title) {
        PanelSeparate separator = new PanelSeparate(title);

        GridBagConstraints gbc = (GridBagConstraints) baseConstraints.clone();
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = maxColumnsInRow;  // Span the full row
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);  // Additional spacing for clarity
        gbc.weighty = 0.0; // Prevent vertical stretching

        add(separator, gbc);
        currentRow++;  // Move to the next row after adding the separator
    }

    /**
     * Adds a label to the grid layout.
     *
     * @param column The column position in the grid.
     * @param text   The text of the label.
     */
    public void addLabel(int column, String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setOpaque(false);

        GridBagConstraints gbc = (GridBagConstraints) baseConstraints.clone();
        gbc.gridx = column - 1;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // Prevent vertical stretching

        label.setPreferredSize(new Dimension(100, 30));
        add(label, gbc);
        revalidate();
        repaint();
    }

    /**
     * Adds a dropdown (JComboBox) to the grid layout and attaches a listener using ProfileUpdateScanner.
     *
     * @param column  The column position in the grid.
     * @param category The category under which the component is categorized.
     * @param key     The identifier for the component.
     * @param options The list of options for the dropdown.
     * @return The generated full key for the component.
     */
    public String addDropdown(int column, String category, String key, List<String> options) {
        return addDropdown(column, category, key, options, getSchema());
    }

    /**
     * Adds a dropdown (JComboBox) to the grid layout with a specific schema and attaches a listener.
     *
     * @param column  The column position in the grid.
     * @param category The category under which the component is categorized.
     * @param key     The identifier for the component.
     * @param options The list of options for the dropdown.
     * @param schema  The schema enum to identify the panel type.
     * @return The generated full key for the component.
     */
    public String addDropdown(int column, String category, String key, List<String> options, Enum<?> schema) {
        JComboBox<String> dropdown = new JComboBox<>(options.toArray(new String[0]));
        dropdown.setOpaque(false);

        Object controller = getController();
        String selectedValue = Optional.ofNullable(loadSetting(controller, key, schema))
                .map(val -> val instanceof String ? (String) val : options.get(0))
                .orElse(options.get(0));
        dropdown.setSelectedItem(selectedValue);

        String fullKey = addComponent(column, category, key, dropdown, schema);

        if (fullKey != null) {
            // Attach listener using ProfileUpdateScanner
            profileUpdateScanner.attachListener(dropdown, fullKey, schema);
            Logger.debug("Listener attached to dropdown with key: {}", fullKey);
        }
        return fullKey;
    }

    /**
     * Adds a checkbox to the grid layout and attaches a listener using ProfileUpdateScanner.
     *
     * @param column   The column position in the grid.
     * @param category The category under which the component is categorized.
     * @param key      The identifier for the component.
     */
    public void addCheckbox(int column, String category, String key) {
        addCheckbox(column, category, key, getSchema());
    }

    /**
     * Adds a checkbox to the grid layout with a specific schema and attaches a listener.
     *
     * @param column  The column position in the grid.
     * @param category The category under which the component is categorized.
     * @param key     The identifier for the component.
     * @param schema  The schema enum to identify the panel type.
     */
    public void addCheckbox(int column, String category, String key, Enum<?> schema) {
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        checkboxPanel.setOpaque(false);

        JCheckBox checkbox = new JCheckBox();
        checkbox.setOpaque(false);

        Object controller = getController();
        Boolean initialValue = Optional.ofNullable(loadSetting(controller, key, schema))
                .map(val -> val instanceof Boolean ? (Boolean) val : false)
                .orElse(false);
        checkbox.setSelected(initialValue);

        JLabel statusLabel = new JLabel(checkbox.isSelected() ? "Enabled" : "Disabled");
        checkboxPanel.add(checkbox);
        checkboxPanel.add(statusLabel);

        String fullKey = addComponent(column, category, key, checkboxPanel, schema);

        if (fullKey != null) {
            // Attach listener using ProfileUpdateScanner
            profileUpdateScanner.attachListener(checkbox, fullKey, schema);
            Logger.debug("Listener attached to checkbox with key: {}", fullKey);
        }

        // Additional listener to update status label based on checkbox state
        checkbox.addItemListener(e -> {
            boolean isSelected = checkbox.isSelected();
            statusLabel.setText(isSelected ? "Enabled" : "Disabled");
        });
    }

    /**
     * Adds a text field to the grid layout and attaches a listener using ProfileUpdateScanner.
     *
     * @param column  The column position in the grid.
     * @param category The category under which the component is categorized.
     * @param key     The identifier for the component.
     * @return The generated full key for the component.
     */
    public String addTextField(int column, String category, String key) {
        return addTextField(column, category, key, getSchema());
    }

    /**
     * Adds a text field to the grid layout with a specific schema and attaches a listener.
     *
     * @param column  The column position in the grid.
     * @param category The category under which the component is categorized.
     * @param key     The identifier for the component.
     * @param schema  The schema enum to identify the panel type.
     * @return The generated full key for the component.
     */
    public String addTextField(int column, String category, String key, Enum<?> schema) {
        JTextField textField = new JTextField();
        textField.setOpaque(false);
        textField.setForeground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        Object controller = getController();
        String initialValue = Optional.ofNullable(loadSetting(controller, key, schema))
                .map(val -> val instanceof Integer ? val.toString() : (String) val)
                .orElse("");
        textField.setText(initialValue);

        String fullKey = addComponent(column, category, key, textField, schema);

        if (fullKey != null) {
            // Attach listener using ProfileUpdateScanner
            profileUpdateScanner.attachListener(textField, fullKey, schema);
            Logger.debug("Listener attached to text field with key: {}", fullKey);
        }
        return fullKey;
    }

    /**
     * Adds a component to the grid and registers it with the Settings Manager using the new BaseKeyGenerator.
     *
     * @param column    The column position in the grid.
     * @param category  The category under which the component is categorized.
     * @param key       The identifier for the component.
     * @param component The Swing component to be added.
     * @param schema    The schema enum (OsrsPanels or Rs3Panels).
     * @return The generated full key for the component.
     */
    public String addComponent(int column, String category, String key, JComponent component, Enum<?> schema) {
        maxColumnsInRow = Math.max(maxColumnsInRow, column);

        // Generate the structured full key using BaseKeyGenerator
        String fullKey = keyGenerator.generateKey(panel, key);

        if (fullKey == null) {
            Logger.warn("BaseGridBuilder: Failed to generate key for component '{}'.", key);
            return null;
        }

        // Register the component with the Settings Manager using the fullKey
        registerComponent(fullKey, component, schema);

        // Set the component's name and store it in the componentMap
        component.setName(fullKey);
        componentMap.put(fullKey, component);  // Store component by key for retrieval

        // Define GridBagConstraints for the component
        GridBagConstraints gbc = (GridBagConstraints) baseConstraints.clone();
        gbc.gridx = column - 1;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; // Prevent vertical stretching

        // Set preferred size and add the component to the panel
        component.setPreferredSize(new Dimension(100, 30));
        add(component, gbc);
        revalidate();
        repaint();

        return fullKey;
    }

    /**
     * Registers the component with the Settings Manager using the generated fullKey.
     *
     * @param fullKey   The generated full key for the component.
     * @param component The Swing component to be registered.
     * @param schema    The schema enum (OsrsPanels or Rs3Panels).
     * @return The full key if registration is successful, null otherwise.
     */
    private String registerComponent(String fullKey, JComponent component, Enum<?> schema) {
        Object controller = getController();
        if (schema instanceof OsrsPanels && controller instanceof OsrsSettingsController) {
            return ((OsrsSettingsController) controller).registerComponent((OsrsPanels) panel, fullKey, component);
        } else if (schema instanceof Rs3Panels && controller instanceof Rs3SettingController) {
            return ((Rs3SettingController) controller).registerComponent((Rs3Panels) panel, fullKey, component);
        }
        Logger.warn("BaseGridBuilder: Unsupported controller or schema type for component '{}'.", fullKey);
        return null;
    }

    /**
     * Adds a component with custom constraints using the new BaseKeyGenerator and attaches a listener.
     *
     * @param category          The category of the component.
     * @param key               The key of the component.
     * @param component         The Swing component.
     * @param schema            The schema enum (OsrsPanels or Rs3Panels).
     * @param customConstraints The custom GridBagConstraints.
     * @return The generated full key for the component.
     */
    public String addComponent(String category, String key, JComponent component, Enum<?> schema, GridBagConstraints customConstraints) {
        // Generate the structured full key using BaseKeyGenerator
        String fullKey = keyGenerator.generateKey(panel, key);

        if (fullKey == null) {
            Logger.warn("BaseGridBuilder: Failed to generate key for component '{}'.", key);
            return null;
        }

        // Register the component with the Settings Manager using the fullKey
        registerComponent(fullKey, component, schema);

        // Set the component's name and store it in the componentMap
        component.setName(fullKey);
        componentMap.put(fullKey, component);

        // Clone the custom constraints or use baseConstraints
        GridBagConstraints gbc = (customConstraints != null) ? (GridBagConstraints) customConstraints.clone() : (GridBagConstraints) baseConstraints.clone();

        // Only set gbc.gridy if it hasn't been set in customConstraints
        if (customConstraints == null || customConstraints.gridy == GridBagConstraints.RELATIVE || customConstraints.gridy < 0) {
            gbc.gridy = getCurrentRow();
        }

        // Add the component to the panel
        add(component, gbc);
        revalidate();
        repaint();

        // Attach listener using ProfileUpdateScanner
        profileUpdateScanner.attachListener(component, fullKey, schema);
        Logger.debug("Listener attached to component with key: {}", fullKey);

        return fullKey;
    }

    /**
     * Retrieves a component by its key.
     *
     * @param key The unique key of the component.
     * @return The Swing component associated with the key.
     */
    public JComponent getComponentByKey(String key) {
        return componentMap.get(key);
    }

    /**
     * Moves to the next row in the grid layout.
     */
    public void nextRow() {
        addRowFiller();
        currentRow++;
        updateGridColumnWeights();
    }

    /**
     * Adds a filler component to occupy remaining space in the current row.
     */
    private void addRowFiller() {
        GridBagConstraints fillerConstraints = new GridBagConstraints();
        fillerConstraints.gridx = 0;
        fillerConstraints.gridy = currentRow;
        fillerConstraints.gridwidth = maxColumnsInRow;
        fillerConstraints.weightx = 1.0;
        fillerConstraints.weighty = 0.0; // Prevent vertical stretching
        fillerConstraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        add(filler, fillerConstraints);
    }

    /**
     * Retrieves the scroll pane containing the grid.
     *
     * @return The JScrollPane instance.
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Loads a setting value from the controller based on the fullKey.
     *
     * @param controller The settings controller (OsrsSettingsController or Rs3SettingController).
     * @param fullKey    The full key of the component.
     * @param schema     The schema enum (OsrsPanels or Rs3Panels).
     * @return The loaded setting value, or null if not found.
     */
    private Object loadSetting(Object controller, String fullKey, Enum<?> schema) {
        if (schema instanceof OsrsPanels && controller instanceof OsrsSettingsController) {
            return ((OsrsSettingsController) controller).loadSetting((OsrsPanels) panel, fullKey);
        } else if (schema instanceof Rs3Panels && controller instanceof Rs3SettingController) {
            return ((Rs3SettingController) controller).loadSetting((Rs3Panels) panel, fullKey);
        }
        return null;
    }

    /**
     * Saves a setting value to the controller based on the fullKey.
     *
     * @param controller The settings controller (OsrsSettingsController or Rs3SettingController).
     * @param fullKey    The full key of the component.
     * @param value      The value to be saved.
     * @param schema     The schema enum (OsrsPanels or Rs3Panels).
     */
    private void saveSetting(Object controller, String fullKey, Object value, Enum<?> schema) {
        if (schema instanceof OsrsPanels && controller instanceof OsrsSettingsController) {
            ((OsrsSettingsController) controller).saveSetting((OsrsPanels) panel, fullKey, value);
        } else if (schema instanceof Rs3Panels && controller instanceof Rs3SettingController) {
            ((Rs3SettingController) controller).saveSetting((Rs3Panels) panel, fullKey, value);
        }
    }

    /**
     * Adds an "obtainbox" component (custom checkbox with dynamic label) to the grid layout and attaches a listener.
     *
     * @param column   The column position in the grid.
     * @param category The category under which the component is categorized.
     * @param key      The identifier for the component.
     */
    public void addObtainbox(int column, String category, String key) {
        addObtainbox(column, category, key, getSchema());
    }

    /**
     * Adds an "obtainbox" component (custom checkbox with dynamic label) to the grid layout with a specific schema and attaches a listener.
     *
     * @param column  The column position in the grid.
     * @param category The category under which the component is categorized.
     * @param key     The identifier for the component.
     * @param schema  The schema enum to identify the panel type.
     */
    public void addObtainbox(int column, String category, String key, Enum<?> schema) {
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        checkboxPanel.setOpaque(false);

        JCheckBox checkbox = new JCheckBox();
        checkbox.setOpaque(false);

        Object controller = getController();
        Boolean initialValue = Optional.ofNullable(loadSetting(controller, key, schema))
                .map(val -> val instanceof Boolean ? (Boolean) val : false)
                .orElse(false);
        checkbox.setSelected(initialValue);

        JLabel statusLabel = new JLabel(checkbox.isSelected() ? "Obtain" : "Forsake");
        checkboxPanel.add(checkbox);
        checkboxPanel.add(statusLabel);

        String fullKey = addComponent(column, category, key, checkboxPanel, schema);

        if (fullKey != null) {
            // Attach listener using ProfileUpdateScanner
            profileUpdateScanner.attachListener(checkbox, fullKey, schema);
            Logger.debug("Listener attached to obtainbox with key: {}", fullKey);
        }

        // Additional listener to update status label based on checkbox state
        checkbox.addItemListener(e -> {
            boolean isSelected = checkbox.isSelected();
            statusLabel.setText(isSelected ? "Obtain" : "Forsake");
        });
    }

    /**
     * Optional: Implement a ClampingDocumentFilter if needed for specific fields.
     * This example assumes that only certain text fields require clamping.
     */
    private static class ClampingDocumentFilter extends DocumentFilter {
        private static final int MAX_LEVEL = 126;

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
                clampIfNeeded(fb);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
                clampIfNeeded(fb);
            }
        }

        private void clampIfNeeded(FilterBypass fb) throws BadLocationException {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength()).trim();
            if (!currentText.isEmpty()) {
                try {
                    int value = Integer.parseInt(currentText);
                    if (value > MAX_LEVEL) {
                        fb.getDocument().remove(0, fb.getDocument().getLength());
                        fb.getDocument().insertString(0, String.valueOf(MAX_LEVEL), null);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    public Map<String, JComponent> getComponentMap() {
        return componentMap;
    }

}
