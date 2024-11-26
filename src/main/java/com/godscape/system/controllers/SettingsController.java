package com.godscape.system.controllers;

import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.rs3.controllers.Rs3SettingController;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.interfaces.fuse.SettingsFuse;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;

@Singleton
public class SettingsController implements SettingsFuse {
    private final OsrsSettingsController osrsController;
    private final Rs3SettingController rs3Controller;
    private final PlatformFactory platformFactory;

    public SettingsController() {
        this.osrsController = DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);
        this.rs3Controller = DependencyFactory.getInstance().getInjection(Rs3SettingController.class);
        this.platformFactory = DependencyFactory.getInstance().getInjection(PlatformFactory.class);

        if (this.osrsController == null || this.rs3Controller == null || this.platformFactory == null) {
            throw new IllegalStateException("SettingsController: Failed to inject necessary controllers.");
        } else {
            Logger.info("SettingsController initialized with OSRS and RS3 controllers.");
        }
    }

    @Override
    public void setActiveProfileId(UUID profileId) {
        osrsController.setActiveProfileId(profileId);
        rs3Controller.setActiveProfileId(profileId);
        Logger.info("SettingsController: Active profile ID set to {}", profileId);
    }

    @Override
    public String registerComponent(Enum<?> panel, String componentId, JComponent component) {
        SettingsFuse controller = getController();
        return controller != null ? controller.registerComponent(panel, componentId, component) : null;
    }

    @Override
    public Object loadSetting(Enum<?> panel, String componentId) {
        SettingsFuse controller = getController();
        return controller != null ? controller.loadSetting(panel, componentId) : null;
    }

    @Override
    public void saveSetting(Enum<?> panel, String componentId, Object value) {
        SettingsFuse controller = getController();
        if (controller != null) controller.saveSetting(panel, componentId, value);
    }

    @Override
    public void loadSettings() {
        SettingsFuse controller = getController();
        if (controller != null) controller.loadSettings();
    }

    @Override
    public Map<String, Object> getSettingsMap() {
        SettingsFuse controller = getController();
        return controller != null ? controller.getSettingsMap() : null;
    }

    @Override
    public JComponent getComponentByKey(String key) {
        SettingsFuse controller = getController();
        return controller != null ? controller.getComponentByKey(key) : null; // Added ': null' here
    }

    private SettingsFuse getController() {
        GameVersion gameVersion = platformFactory.getCurrentGameVersion();
        switch (gameVersion) {
            case OSRS:
                return osrsController;
            case RS3:
                return rs3Controller;
            default:
                Logger.warn("SettingsController: Unsupported GameVersion: {}", gameVersion);
                return null;
        }
    }
}
