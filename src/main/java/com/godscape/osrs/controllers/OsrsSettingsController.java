package com.godscape.osrs.controllers;

import com.godscape.osrs.enums.core.OsrsPanels;
import com.godscape.osrs.listeners.SettingsChangeListener;
import com.godscape.osrs.managers.OsrsSettingsManager;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.fuse.SettingsFuse;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.*;

@Singleton
public class OsrsSettingsController implements SettingsFuse {


    private final List<SettingsChangeListener> listeners = new ArrayList<>();
    private final OsrsSettingsManager settingsManager;

    public OsrsSettingsController() {
        this.settingsManager = DependencyFactory.getInstance().getInjection(OsrsSettingsManager.class);
        if (this.settingsManager == null) {
            throw new IllegalStateException("OsrsSettingsController: Failed to inject OsrsSettingsManager.");
        } else {
            Logger.info("OsrsSettingsController initialized.");
        }
    }

    public void addSettingsChangeListener(SettingsChangeListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void setActiveProfileId(UUID profileId) {
        settingsManager.setActiveProfileId(profileId);
        Logger.info("OsrsSettingsController: Active profile ID set to {}", profileId);
        notifyListeners(); // Notify all panels that a new profile is active
    }

    @Override
    public String registerComponent(Enum<?> panel, String componentId, JComponent component) {
        String key = settingsManager.registerComponent(panel, componentId, component);
        Logger.info("OsrsSettingsController: Component registered with key '{}'", key);
        return key;
    }

    @Override
    public void saveSetting(Enum<?> panel, String componentId, Object value) {
        OsrsProfileSchema currentProfile = getActiveProfile();

        if (currentProfile == null) {
            Logger.error("OsrsSettingsController: No active profile found to save setting.");
            return;
        }

        if (panel == OsrsPanels.OSRS_STATS_SKILL_GOALS_PANEL) {
            if (value instanceof Integer) {
                currentProfile.updateSkillGoal(componentId.toLowerCase(), (Integer) value);
                Logger.info("OsrsSettingsController: Updated skill goal for '{}'", componentId);
            } else {
                Logger.error("OsrsSettingsController: Invalid value type for skill goal '{}'", componentId);
            }
        } else {
            currentProfile.setSetting(componentId.toLowerCase(), value);
            Logger.info("OsrsSettingsController: Setting saved for component '{}'", componentId);
        }

        // Update the profile in cache after modification
        updateProfile(currentProfile);
        notifyListeners();
    }


    private void notifyListeners() {
        for (SettingsChangeListener listener : listeners) {
            listener.onSettingsChanged();
        }
    }

    @Override
    public Object loadSetting(Enum<?> panel, String componentId) {
        Object value = settingsManager.loadSetting(panel, componentId);
        notifyListeners();
        Logger.info("OsrsSettingsController: Setting loaded for component '{}'", componentId);
        return value;
    }

    @Override
    public void loadSettings() {
        settingsManager.loadSettings();
        notifyListeners();
        Logger.info("OsrsSettingsController: Settings loaded.");
    }

    @Override
    public Map<String, Object> getSettingsMap() {
        return settingsManager.getSettingsMap();
    }

    @Override
    public JComponent getComponentByKey(String key) {
        return settingsManager.getComponentByKey(key);
    }

    public OsrsProfileSchema getActiveProfile() {
        return settingsManager.getActiveProfile();
    }

    public void updateProfile(OsrsProfileSchema profile) {
        settingsManager.updateProfile(profile);
        Logger.info("OsrsSettingsController: Profile '{}' updated.", profile.getProfileId());
    }

    public Map<String, Integer> loadSkillGoals() {
        OsrsProfileSchema currentProfile = getActiveProfile();

        if (currentProfile == null) {
            Logger.error("OsrsSettingsController: No active profile found to load skill goals.");
            return new HashMap<>();
        }

        return currentProfile.getSkillMap();
    }
}
