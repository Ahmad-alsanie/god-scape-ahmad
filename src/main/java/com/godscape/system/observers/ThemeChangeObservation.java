package com.godscape.system.observers;

import com.godscape.system.enums.Themes;

@FunctionalInterface
public interface ThemeChangeObservation {
    void onThemeChange(Themes newTheme);
}
