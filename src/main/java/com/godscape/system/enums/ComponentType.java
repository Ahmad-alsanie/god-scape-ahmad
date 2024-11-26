package com.godscape.system.enums;

import javax.swing.*;

public enum ComponentType {
    BUTTON(JButton.class),
    LABEL(JLabel.class),
    TEXT_FIELD(JTextField.class),
    PASSWORD_FIELD(JPasswordField.class),
    TEXT_AREA(JTextArea.class),
    CHECK_BOX(JCheckBox.class),
    RADIO_BUTTON(JRadioButton.class),
    COMBO_BOX(JComboBox.class),
    LIST(JList.class),
    TABLE(JTable.class),
    PANEL(JPanel.class),
    SCROLL_PANE(JScrollPane.class),
    TOOLBAR(JToolBar.class),
    TABBED_PANE(JTabbedPane.class),
    SLIDER(JSlider.class),
    PROGRESS_BAR(JProgressBar.class),
    SPINNER(JSpinner.class),
    SPLIT_PANE(JSplitPane.class),
    TREE(JTree.class),
    MENU_BAR(JMenuBar.class),
    MENU_ITEM(JMenuItem.class),
    MENU(JMenu.class),
    POPUP_MENU(JPopupMenu.class),
    TOGGLE_BUTTON(JToggleButton.class),
    FILE_CHOOSER(JFileChooser.class),
    COLOR_CHOOSER(JColorChooser.class),
    DESKTOP_PANE(JDesktopPane.class),
    INTERNAL_FRAME(JInternalFrame.class),
    OPTION_PANE(JOptionPane.class),
    ROOT_PANE(JRootPane.class),
    SEPARATOR(JSeparator.class),
    TOOL_TIP(JToolTip.class);

    private final Class<? extends JComponent> componentClass;

    ComponentType(Class<? extends JComponent> componentClass) {
        this.componentClass = componentClass;
    }

    public Class<? extends JComponent> getComponentClass() {
        return componentClass;
    }
}
