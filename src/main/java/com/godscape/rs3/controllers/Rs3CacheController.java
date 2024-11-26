package com.godscape.rs3.controllers;

import com.godscape.rs3.enums.core.Rs3Panels;
import com.godscape.rs3.managers.Rs3CacheManager;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class Rs3CacheController {

    private final Rs3CacheManager cacheManager;
    private UUID activeProfileId;
    private final Map<String, JComponent> componentMap = new HashMap<>();

    public Rs3CacheController() {
        this.cacheManager = DependencyFactory.getInstance().getInjection(Rs3CacheManager.class);
        Logger.info("Rs3CacheController initialized.");
    }

    // Profile Methods

    public Collection<Rs3ProfileSchema> getAllProfiles() {
        Logger.info("Rs3CacheController: Retrieving all profiles from cache.");
        return cacheManager.getCachedProfiles().values();
    }

    public Rs3ProfileSchema getProfile(UUID profileId) {
        Logger.info("Rs3CacheController: Retrieving profile with ID '{}'.", profileId);
        return cacheManager.getProfile(profileId);
    }

    public void addProfile(Rs3ProfileSchema profile) {
        cacheManager.addProfileToCache(profile);
    }

    public void updateProfile(Rs3ProfileSchema profile) {
        cacheManager.updateProfileInCache(profile);
    }

    public void deleteProfile(UUID profileId) {
        cacheManager.removeProfile(profileId);
    }

    // Character Methods

    public void loadCharacter() {
        Logger.info("Rs3CacheController: Loading character.");
        cacheManager.loadCharacter();
    }

    public void saveCharacter() {
        Logger.info("Rs3CacheController: Saving character.");
        cacheManager.saveCharacter();
    }

    // Cache Maintenance Methods

    public void clearCache() {
        cacheManager.clearCache();
        Logger.info("Rs3CacheController: All profiles cleared from cache.");
    }

    public void shutdownCache() {
        clearCache();
        Logger.info("Rs3CacheController: Cache shutdown complete.");
    }

    // Settings Methods for Rs3SettingController

    public void setActiveProfileId(UUID profileId) {
        this.activeProfileId = profileId;
        cacheManager.setActiveProfileId(profileId);
        Logger.info("Rs3CacheController: Active profile ID set to {}", profileId);
    }

    public String registerComponent(Rs3Panels panel, String componentId, JComponent component) {
        String key = panel.name() + "_" + componentId;
        componentMap.put(key, component);
        Logger.info("Rs3CacheController: Component '{}' registered under panel '{}'.", componentId, panel);
        return key;
    }

    public Object loadSetting(Rs3Panels panel, String componentId) {
        String key = panel.name() + "_" + componentId;
        Logger.info("Rs3CacheController: Loading setting for key '{}'.", key);
        return componentMap.getOrDefault(key, null);
    }

    public void saveSetting(Rs3Panels panel, String componentId, Object value) {
        String key = panel.name() + "_" + componentId;
        componentMap.put(key, (JComponent) value);
        Logger.info("Rs3CacheController: Saved setting for key '{}'.", key);
    }

    public void loadSettings() {
        Logger.info("Rs3CacheController: Loading all settings.");
        // Additional logic if needed
    }

    public void loadSkillGoals(Rs3Panels panel, Map<String, Object> skillGoals) {
        Logger.info("Rs3CacheController: Loading skill goals for panel '{}'.", panel);
        // Logic to handle skill goals as needed
    }

    public Map<String, Object> getSettingsMap() {
        Map<String, Object> settingsMap = new HashMap<>();
        componentMap.forEach((key, component) -> settingsMap.put(key, component));
        Logger.info("Rs3CacheController: Retrieved settings map.");
        return settingsMap;
    }

    public JComponent getComponentByKey(String key) {
        Logger.info("Rs3CacheController: Retrieving component by key '{}'.", key);
        return componentMap.get(key);
    }
}
