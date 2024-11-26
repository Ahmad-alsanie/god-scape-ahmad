package com.godscape.osrs.controllers;

import com.godscape.osrs.managers.OsrsSettingsManager;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.fuse.SettingsFuse;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsSettingsController implements SettingsFuse {

    private final OsrsSettingsManager settingsManager;

    public OsrsSettingsController() {
        this.settingsManager = DependencyFactory.getInstance().getInjection(OsrsSettingsManager.class);
        if (this.settingsManager == null) {
            throw new IllegalStateException("OsrsSettingsController: Failed to inject OsrsSettingsManager.");
        } else {
            Logger.info("OsrsSettingsController initialized.");
        }
    }

    @Override
    public void setActiveProfileId(UUID profileId) {
        settingsManager.setActiveProfileId(profileId);
        Logger.info("OsrsSettingsController: Active profile ID set to {}", profileId);
    }

    @Override
    public String registerComponent(Enum<?> panel, String componentId, JComponent component) {
        String key = settingsManager.registerComponent(panel, componentId, component);
        Logger.info("OsrsSettingsController: Component registered with key '{}'", key);
        return key;
    }

    @Override
    public void saveSetting(Enum<?> panel, String componentId, Object value) {
        settingsManager.saveSetting(panel, componentId, value);
        Logger.info("OsrsSettingsController: Setting saved for component '{}'", componentId);
    }

    @Override
    public Object loadSetting(Enum<?> panel, String componentId) {
        Object value = settingsManager.loadSetting(panel, componentId);
        Logger.info("OsrsSettingsController: Setting loaded for component '{}'", componentId);
        return value;
    }

    @Override
    public void loadSettings() {
        settingsManager.loadSettings();
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
}
