package com.godscape.system.enums;

import javax.swing.*;

public abstract class PanelComponents extends JPanel {
    // Common functionalities or abstract methods for all setting panels

    /**
     * Initialize components.
     */
    protected abstract void initializeComponents();

    /**
     * Load settings into the panel.
     */
    public abstract void loadSettings();

    /**
     * Save settings from the panel.
     */
    public abstract void saveSettings();
}
