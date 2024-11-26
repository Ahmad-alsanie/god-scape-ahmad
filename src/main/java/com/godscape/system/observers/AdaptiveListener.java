package com.godscape.system.observers;

import com.godscape.osrs.managers.OsrsObservationManager;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.managers.Rs3ObservationManager;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;

public class AdaptiveListener implements ActionListener, ItemListener, ChangeListener, FocusListener, DocumentListener {

    private final String category;
    private final String key;
    private final Object profile;
    private final GameVersion gameVersion;
    private final OsrsObservationManager osrsManager;
    private final Rs3ObservationManager rs3Manager;

    public AdaptiveListener(String category, String key, Object profile) {
        this.category = category;
        this.key = key;
        this.profile = profile;
        this.gameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();

        if (profile != null) {
            Logger.info("AdaptiveListener: Initialized with profile of type '{}'", profile.getClass().getName());
        } else {
            Logger.warn("AdaptiveListener: Profile is null for category '{}' and key '{}'.", category, key);
        }

        if (gameVersion == GameVersion.OSRS) {
            this.osrsManager = DependencyFactory.getInstance().getInjection(OsrsObservationManager.class);
            this.rs3Manager = null;
        } else {
            this.rs3Manager = DependencyFactory.getInstance().getInjection(Rs3ObservationManager.class);
            this.osrsManager = null;
        }
    }

    private void setSetting(Object value) {
        if (profile instanceof OsrsProfileSchema && osrsManager != null) {
            ((OsrsProfileSchema) profile).setSetting(category, key, value);
            osrsManager.notifyChange("SETTING_UPDATED", profile);
        } else if (profile instanceof Rs3ProfileSchema && rs3Manager != null) {
            ((Rs3ProfileSchema) profile).setSetting(category, key, value);
            rs3Manager.notifyChange("SETTING_UPDATED", profile);
        } else {
            Logger.warn("AdaptiveListener: Failed to update setting - profile or manager is null.");
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        Logger.info("AdaptiveListener: Focus gained for component key '{}'", key);
    }

    @Override
    public void focusLost(FocusEvent e) {
        Object source = e.getSource();
        if (source instanceof JTextField) {
            JTextField textField = (JTextField) source;
            Logger.info("AdaptiveListener: Focus lost for JTextField key '{}', triggering update", key);
            setSetting(textField.getText());
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        handleDocumentEvent(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        handleDocumentEvent(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        handleDocumentEvent(e);
    }

    private void handleDocumentEvent(DocumentEvent e) {
        Object source = e.getDocument().getProperty("owner");
        if (source instanceof JTextField) {
            JTextField textField = (JTextField) source;
            Logger.info("AdaptiveListener: Document update for JTextField key '{}'", key);
            setSetting(textField.getText());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof JComboBox) {
            JComboBox<?> dropdown = (JComboBox<?>) source;
            Logger.info("AdaptiveListener: Action performed for JComboBox key '{}'", key);
            setSetting(dropdown.getSelectedItem());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getSource();
        if (source instanceof JCheckBox) {
            JCheckBox checkbox = (JCheckBox) source;
            Logger.info("AdaptiveListener: Item state changed for JCheckBox key '{}'", key);
            setSetting(checkbox.isSelected());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source instanceof JSpinner) {
            JSpinner spinner = (JSpinner) source;
            Logger.info("AdaptiveListener: State changed for JSpinner key '{}'", key);
            setSetting(spinner.getValue());
        }
    }
}
