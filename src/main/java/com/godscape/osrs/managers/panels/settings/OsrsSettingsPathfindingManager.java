package com.godscape.osrs.managers.panels.settings;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.utility.Logger;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.osrs.controllers.OsrsSettingsController;

import java.util.UUID;

public class OsrsSettingsPathfindingManager {

    private final OsrsSettingsController settingsController;

    public OsrsSettingsPathfindingManager() {
        this.settingsController = DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);
        Logger.info("OsrsSettingsPathfindingManager initialized.");
    }
}
