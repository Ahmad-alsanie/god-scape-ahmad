package com.godscape.rs3.controllers;

import com.godscape.rs3.enums.core.Rs3Panels;
import com.godscape.rs3.managers.Rs3SettingsManager;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.fuse.SettingsFuse;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class Rs3SettingController implements SettingsFuse {

    private final Rs3SettingsManager settingsManager;

    public Rs3SettingController() {
        this.settingsManager = DependencyFactory.getInstance().getInjection(Rs3SettingsManager.class);
        Logger.info("Rs3SettingController initialized.");
    }

    @Override
    public void setActiveProfileId(UUID profileId) {
        settingsManager.setActiveProfileId(profileId);
    }

    @Override
    public String registerComponent(Enum<?> panel, String componentId, JComponent component) {
        return settingsManager.registerComponent((Rs3Panels) panel, componentId, component);
    }

    @Override
    public Object loadSetting(Enum<?> panel, String componentId) {
        return settingsManager.loadSetting((Rs3Panels) panel, componentId);
    }

    @Override
    public void saveSetting(Enum<?> panel, String componentId, Object value) {
        settingsManager.saveSetting((Rs3Panels) panel, componentId, value);
    }

    @Override
    public void loadSettings() {
        settingsManager.loadSettings(new HashMap<>());
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
