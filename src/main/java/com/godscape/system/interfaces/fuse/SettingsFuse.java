package com.godscape.system.interfaces.fuse;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;

public interface SettingsFuse {
    void setActiveProfileId(UUID profileId);

    String registerComponent(Enum<?> panel, String componentId, JComponent component);

    Object loadSetting(Enum<?> panel, String componentId);

    void saveSetting(Enum<?> panel, String componentId, Object value);

    void loadSettings();

    Map<String, Object> getSettingsMap();

    JComponent getComponentByKey(String key);
}
