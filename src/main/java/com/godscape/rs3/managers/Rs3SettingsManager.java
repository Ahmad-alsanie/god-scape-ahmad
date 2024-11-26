package com.godscape.rs3.managers;

import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.rs3.utility.Rs3KeyGenerator;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import com.godscape.rs3.enums.core.Rs3Panels;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
public class Rs3SettingsManager {

    private final Rs3KeyGenerator keyGenerator = new Rs3KeyGenerator();
    private final Map<String, JComponent> componentMap = new HashMap<>();
    private UUID activeProfileId;

    private Rs3SettingsManager() {
        Logger.info("Rs3SettingsManager initialized.");
    }

    public void setActiveProfileId(UUID profileId) {
        this.activeProfileId = profileId;
        Logger.info("Active profile ID set to {}", profileId);
    }

    public String registerComponent(Rs3Panels panel, String componentId, JComponent component) {
        String key = keyGenerator.generateKey(panel, componentId);
        if (key == null || component == null) {
            Logger.warn("Cannot register a null componentId or component.");
            return null;
        }

        componentMap.put(key, component);
        attachListenerToComponent(component, panel, componentId);
        Logger.debug("Component registered with generated key: {}", key);
        return key;
    }

    private void attachListenerToComponent(JComponent component, Rs3Panels panel, String componentId) {
        if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    String value = textField.getText();
                    saveSetting(panel, componentId, value);
                    updateCacheWithoutUI(panel, componentId, value);
                }
            });
        } else if (component instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) component;
            checkBox.addActionListener(e -> {
                Boolean value = checkBox.isSelected();
                saveSetting(panel, componentId, value);
                updateCacheWithoutUI(panel, componentId, value);
            });
        } else if (component instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) component;
            comboBox.addActionListener(e -> {
                Object value = comboBox.getSelectedItem();
                saveSetting(panel, componentId, value);
                updateCacheWithoutUI(panel, componentId, value);
            });
        } else if (component instanceof JSpinner) {
            JSpinner spinner = (JSpinner) component;
            spinner.addChangeListener(e -> {
                Object value = spinner.getValue();
                saveSetting(panel, componentId, value);
                updateCacheWithoutUI(panel, componentId, value);
            });
        } else {
           // Logger.warn("Unsupported component type for key: {}", componentId);
        }
    }

    private void updateCacheWithoutUI(Rs3Panels panel, String componentId, Object value) {
        if (activeProfileId != null) {
            Rs3ProfileSchema profile = getProfileFromCache(activeProfileId);
            if (profile != null) {
                profile.setSetting(panel.name().toLowerCase(), componentId, value);
                updateProfileInCache(profile);
                Logger.info("Cache updated without UI for profile ID: {}", activeProfileId);
            } else {
                Logger.warn("No profile found in cache with ID: {}", activeProfileId);
            }
        } else {
            Logger.warn("Active profile ID is null; cannot update cache without UI.");
        }
    }

    public void saveSetting(Rs3Panels panel, String componentId, Object value) {
        String key = keyGenerator.generateKey(panel, componentId);
        if (key == null) {
            Logger.warn("Key generation skipped for saving componentId: {}", componentId);
            return;
        }

        if (activeProfileId != null) {
            Rs3ProfileSchema profile = getProfileFromCache(activeProfileId);
            if (profile != null) {
                profile.setSetting(panel.name().toLowerCase(), componentId, value);
                updateProfileInCache(profile);
            }
        }
    }

    public Object loadSetting(Rs3Panels panel, String componentId) {
        if (activeProfileId == null) {
            Logger.warn("Active profile ID is null; cannot load settings.");
            return null;
        }
        Rs3ProfileSchema profile = getProfileFromCache(activeProfileId);
        return profile != null ? profile.getSetting(panel.name().toLowerCase(), componentId, null) : null;
    }

    public Map<String, JComponent> getComponentsForPanel(Rs3Panels panel) {
        Map<String, JComponent> panelComponents = new HashMap<>();
        for (Map.Entry<String, JComponent> entry : componentMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(panel.name().toLowerCase())) {
                panelComponents.put(key, entry.getValue());
            }
        }
        return panelComponents;
    }

    public void loadSettings(Map<String, Object> newSettings) {
        Logger.debug("Loading provided settings into profile.");
        if (activeProfileId != null) {
            Rs3ProfileSchema profile = getProfileFromCache(activeProfileId);
            if (profile != null) {
                profile.getSettingsMap().putAll(newSettings);
                updateProfileInCache(profile);
                Logger.info("Settings loaded successfully for profile ID: {}", activeProfileId);
            } else {
                Logger.warn("No profile found in cache with ID: {}", activeProfileId);
            }
        } else {
            Logger.warn("Active profile ID is null; cannot load settings.");
        }
    }

    public Map<String, Object> getSettingsMap() {
        if (activeProfileId != null) {
            Rs3ProfileSchema profile = getProfileFromCache(activeProfileId);
            if (profile != null) {
                return profile.getSettingsMap();
            } else {
                Logger.warn("No profile found in cache with ID: {}", activeProfileId);
            }
        } else {
            Logger.warn("Active profile ID is null; cannot retrieve settings map.");
        }
        return new HashMap<>();
    }

    private Rs3ProfileSchema getProfileFromCache(UUID profileId) {
        Rs3ProfilesManager profilesManager = DependencyFactory.getInstance().getInjection(Rs3ProfilesManager.class);
        return profilesManager.getProfile(profileId);
    }

    private void updateProfileInCache(Rs3ProfileSchema profile) {
        Rs3ProfilesManager profilesManager = DependencyFactory.getInstance().getInjection(Rs3ProfilesManager.class);
        profilesManager.updateProfile(profile);
    }

    public JComponent getComponentByKey(String key) {
        return componentMap.get(key);
    }

    public void loadSkillGoals(Rs3Panels panel, Map<String, Object> skillGoals) {
        Logger.debug("Loading skill goals into registered components.");
        if (skillGoals == null) {
            Logger.warn("Skill goals map is null.");
            return;
        }

        for (Map.Entry<String, Object> entry : skillGoals.entrySet()) {
            String skillName = entry.getKey();
            Object levelObj = entry.getValue();

            if (!(levelObj instanceof Number)) {
                Logger.warn("Invalid level for skill '{}': {}", skillName, levelObj);
                continue;
            }

            int level = ((Number) levelObj).intValue();
            String componentKey = keyGenerator.generateKey(panel, skillName.toLowerCase());

            if (componentKey == null) {
                Logger.warn("Key generation skipped for skill '{}'", skillName);
                continue;
            }

            JComponent component = componentMap.get(componentKey);
            if (component != null) {
                applyValueToComponent(component, level);
            } else {
                Logger.warn("No component registered for key: {}", componentKey);
            }

            if (activeProfileId != null) {
                Rs3ProfileSchema profile = getProfileFromCache(activeProfileId);
                if (profile != null) {
                    profile.setSetting(panel.name().toLowerCase(), skillName.toLowerCase(), level);
                    updateProfileInCache(profile);
                }
            }
            Logger.debug("Loaded skill goal for {}: {}", skillName, level);
        }

        Logger.debug("Skill goals loaded successfully.");
    }

    private void applyValueToComponent(JComponent component, Object value) {
        if (component instanceof JTextField) {
            ((JTextField) component).setText(value.toString());
        } else if (component instanceof JCheckBox) {
            ((JCheckBox) component).setSelected(Boolean.parseBoolean(value.toString()));
        } else if (component instanceof JComboBox) {
            ((JComboBox<?>) component).setSelectedItem(value);
        } else if (component instanceof JSpinner) {
            ((JSpinner) component).setValue(value);
        } else {
            //Logger.warn("Unsupported component type: {}", component.getClass().getName());
        }
    }
}
