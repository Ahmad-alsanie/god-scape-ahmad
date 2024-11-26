package com.godscape.system.observers;

import com.godscape.system.enums.Themes;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.managers.BaseObservationManager;
import com.godscape.system.utility.Logger;

public class ThemeChangeListener implements ThemeChangeObservation {

    private final BaseObservationManager baseObservationManager;

    public ThemeChangeListener() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.baseObservationManager = dependencyFactory.getInjection(BaseObservationManager.class);
    }

    @Override
    public void onThemeChange(Themes newTheme) {
        Logger.info("Theme changed to: {}", newTheme);
        baseObservationManager.notifyThemeChange(newTheme);
    }
}
