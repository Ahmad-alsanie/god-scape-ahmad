package com.godscape.system.utility.grid;

import com.godscape.system.utility.generators.BaseKeyGenerator;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.PanelSeparate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.*;
import java.util.List;

public class ComponentBuilder {

    private final JPanel panel;
    private final JScrollPane scrollPane;
    private final Map<String, JComponent> componentMap = new HashMap<>();
    private final GridBagConstraints baseConstraints = new GridBagConstraints();
    private final BaseKeyGenerator keyGenerator = new BaseKeyGenerator();
    private final Set<String> listenerKeys = new HashSet<>();
    private int currentRow = 0;
    private int maxColumnsInRow = 0;

    public ComponentBuilder(JPanel parentPanel) {
        this.panel = parentPanel;
        this.panel.setLayout(new GridBagLayout());
        baseConstraints.insets = new Insets(5, 5, 5, 5);
        baseConstraints.fill = GridBagConstraints.BOTH;
        baseConstraints.weightx = 1.0;
        baseConstraints.anchor = GridBagConstraints.NORTH;

        this.panel.setOpaque(false);
        this.scrollPane = new JScrollPane(panel);
        this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.scrollPane.setOpaque(false);
        this.scrollPane.getViewport().setOpaque(false);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void addSeparator(String title) {
        PanelSeparate separator = new PanelSeparate(title);

        GridBagConstraints gbc = (GridBagConstraints) baseConstraints.clone();
        gbc.gridx = 0;
        gbc.gridy = currentRow;
        gbc.gridwidth = maxColumnsInRow;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.weighty = 0.0;

        panel.add(separator, gbc);
        currentRow++;
    }

    public void addLabel(int column, String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setOpaque(false);

        GridBagConstraints gbc = (GridBagConstraints) baseConstraints.clone();
        gbc.gridx = column - 1;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        label.setPreferredSize(new Dimension(100, 30));
        panel.add(label, gbc);
        revalidatePanel();
    }

    public String addDropdown(int column, String category, String key, List<String> options, Enum<?> schema) {
        JComboBox<String> dropdown = new JComboBox<>(options.toArray(new String[0]));
        dropdown.setOpaque(false);

        String fullKey = registerComponent(column, category, key, dropdown, schema);
        if (fullKey != null) {
            attachFocusListener(dropdown, fullKey);
        }
        return fullKey;
    }

    public void addCheckbox(int column, String category, String key, Enum<?> schema) {
        JCheckBox checkbox = new JCheckBox();
        checkbox.setOpaque(false);

        String fullKey = registerComponent(column, category, key, checkbox, schema);
        if (fullKey != null) {
            attachFocusListener(checkbox, fullKey);
        }
    }

    public String addTextField(int column, String category, String key, Enum<?> schema) {
        JTextField textField = new JTextField();
        textField.setOpaque(false);
        textField.setForeground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        String fullKey = registerComponent(column, category, key, textField, schema);
        if (fullKey != null) {
            attachFocusListener(textField, fullKey);
        }
        return fullKey;
    }

    public void nextRow() {
        GridBagConstraints fillerConstraints = new GridBagConstraints();
        fillerConstraints.gridx = 0;
        fillerConstraints.gridy = currentRow;
        fillerConstraints.gridwidth = maxColumnsInRow;
        fillerConstraints.weightx = 1.0;
        fillerConstraints.weighty = 0.0;
        fillerConstraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel filler = new JPanel();
        filler.setOpaque(false);
        panel.add(filler, fillerConstraints);

        currentRow++;
        updateGridColumnWeights();
    }

    public Set<String> getComponentKeys() {
        return componentMap.keySet();
    }

    private void attachFocusListener(JComponent component, String fullKey) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Logger.info("Component with key '{}' lost focus.", fullKey);
                saveSetting(fullKey, getValueFromComponent(component));
            }
        });
    }

    private String registerComponent(int column, String category, String key, JComponent component, Enum<?> schema) {
        maxColumnsInRow = Math.max(maxColumnsInRow, column);
        String fullKey = keyGenerator.generateKey(schema, key);

        if (fullKey == null) {
            Logger.warn("ComponentBuilder: Failed to generate key for component '{}'.", key);
            return null;
        }

        componentMap.put(fullKey, component);
        GridBagConstraints gbc = (GridBagConstraints) baseConstraints.clone();
        gbc.gridx = column - 1;
        gbc.gridy = currentRow;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        component.setPreferredSize(new Dimension(100, 30));
        panel.add(component, gbc);
        revalidatePanel();
        return fullKey;
    }

    private void updateGridColumnWeights() {
        int columnCount = Math.max(maxColumnsInRow, 1);
        double[] columnWeights = new double[columnCount];
        Arrays.fill(columnWeights, 1.0);

        GridBagLayout layout = (GridBagLayout) panel.getLayout();
        layout.columnWeights = columnWeights;
    }

    private void revalidatePanel() {
        panel.revalidate();
        panel.repaint();
    }

    public Object loadSetting(String fullKey, Enum<?> schema) {
        // Load the setting based on schema and key
        Logger.info("Loading setting for key '{}'", fullKey);
        // Your specific logic for loading
        return null;
    }

    public void saveSetting(String fullKey, Object value) {
        Logger.info("Saving setting for key '{}'", fullKey);
        // Your specific logic for saving
    }

    private Object getValueFromComponent(JComponent component) {
        if (component instanceof JTextField) {
            return ((JTextField) component).getText().trim();
        } else if (component instanceof JCheckBox) {
            return ((JCheckBox) component).isSelected();
        } else if (component instanceof JComboBox) {
            return ((JComboBox<?>) component).getSelectedItem();
        } else if (component instanceof JSpinner) {
            return ((JSpinner) component).getValue();
        }
        return null;
    }
}
