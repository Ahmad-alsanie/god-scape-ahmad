package com.godscape.osrs.managers;

import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.fuse.SettingsFuse;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsSettingsManager implements SettingsFuse {

    private final OsrsCacheController cacheController;
    private final Map<String, JComponent> componentMap = new HashMap<>();
    private UUID activeProfileId;

    public OsrsSettingsManager() {
        this.cacheController = DependencyFactory.getInstance().getInjection(OsrsCacheController.class);
        Logger.info("OsrsSettingsManager initialized.");
    }

    @Override
    public void setActiveProfileId(UUID profileId) {
        this.activeProfileId = profileId;
        Logger.info("OsrsSettingsManager: Active profile ID set to {}", profileId);
    }

    @Override
    public String registerComponent(Enum<?> panel, String componentId, JComponent component) {
        if (!(panel instanceof OsrsSchemas)) {
            Logger.error("OsrsSettingsManager: Panel '{}' must be an instance of OsrsSchemas.", panel);
            return null;
        }

        String key = (panel.name() + "_" + componentId).toLowerCase();
        if (component == null) {
            Logger.warn("OsrsSettingsManager: Cannot register a null component.");
            return null;
        }
        componentMap.put(key, component);
        Logger.info("OsrsSettingsManager: Registered component '{}' with key '{}'", componentId, key);
        return key;
    }

    @Override
    public void saveSetting(Enum<?> schema, String componentId, Object value) {
        if (activeProfileId == null) {
            Logger.warn("OsrsSettingsManager: Active profile ID is null; cannot save setting.");
            return;
        }

        if (schema instanceof OsrsSchemas) {
            saveToCorrectCache((OsrsSchemas) schema, componentId, value);
        } else {
            Logger.error("OsrsSettingsManager: Unsupported schema type '{}'", schema.getClass().getSimpleName());
        }
    }

    private void saveToCorrectCache(OsrsSchemas schema, String componentId, Object value) {
        switch (schema) {
            case OSRS_PROFILE_SCHEMA:
                cacheController.saveToProfileCache(activeProfileId, componentId, value);
                break;
            case OSRS_CHARACTER_SCHEMA:
                cacheController.saveToCharacterCache(activeProfileId, componentId, value);
                break;
            default:
                Logger.error("OsrsSettingsManager: Unsupported schema '{}'", schema.name());
        }
    }

    @Override
    public Object loadSetting(Enum<?> schema, String componentId) {
        if (activeProfileId == null) {
            Logger.warn("OsrsSettingsManager: Active profile ID is null; cannot load setting.");
            return null;
        }

        if (schema instanceof OsrsSchemas) {
            return loadFromCorrectCache((OsrsSchemas) schema, componentId);
        }

        Logger.error("OsrsSettingsManager: Unsupported schema type '{}'", schema.getClass().getSimpleName());
        return null;
    }

    private Object loadFromCorrectCache(OsrsSchemas schema, String componentId) {
        switch (schema) {
            case OSRS_PROFILE_SCHEMA:
                return cacheController.loadFromProfileCache(activeProfileId, componentId);
            case OSRS_CHARACTER_SCHEMA:
                return cacheController.loadFromCharacterCache(activeProfileId, componentId);
            default:
                Logger.error("OsrsSettingsManager: Unsupported schema '{}'", schema.name());
                return null;
        }
    }

    @Override
    public void loadSettings() {
        if (activeProfileId == null) {
            Logger.warn("OsrsSettingsManager: Active profile ID is null; cannot load settings.");
            return;
        }

        componentMap.forEach((key, component) -> {
            String[] parts = key.split("_", 2);
            if (parts.length < 2) return;

            String schemaPrefix = parts[0].toUpperCase();
            OsrsSchemas schema;

            try {
                schema = OsrsSchemas.valueOf(schemaPrefix);
            } catch (IllegalArgumentException e) {
                Logger.error("OsrsSettingsManager: Invalid schema prefix '{}' in key '{}'.", schemaPrefix, key);
                return; // Skip invalid keys
            }

            String componentId = parts[1];
            Object value = loadSetting(schema, componentId);

            if (value != null) {
                updateComponentValue(component, value);
                Logger.info("OsrsSettingsManager: Loaded value for '{}'.", key);
            }
        });
    }

    @Override
    public Map<String, Object> getSettingsMap() {
        if (activeProfileId == null) {
            Logger.warn("OsrsSettingsManager: Active profile ID is null; cannot retrieve settings map.");
            return Collections.emptyMap();
        }

        return Collections.emptyMap(); // Simplified logic as schema is passed for individual settings
    }

    @Override
    public JComponent getComponentByKey(String key) {
        return componentMap.get(key.toLowerCase());
    }

    private void updateComponentValue(JComponent component, Object value) {
        if (component instanceof JTextField) {
            ((JTextField) component).setText(value.toString());
        } else if (component instanceof JCheckBox) {
            ((JCheckBox) component).setSelected(Boolean.parseBoolean(value.toString()));
        } else if (component instanceof JComboBox) {
            ((JComboBox<Object>) component).setSelectedItem(value);
        } else {
            Logger.warn("OsrsSettingsManager: Unsupported component type '{}'", component.getClass());
        }
    }
}
