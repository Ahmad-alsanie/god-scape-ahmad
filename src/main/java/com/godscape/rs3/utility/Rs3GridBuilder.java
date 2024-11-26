package com.godscape.rs3.utility;

import com.godscape.rs3.controllers.Rs3SettingController;
import com.godscape.system.enums.GameVersion;
import com.godscape.rs3.enums.core.Rs3Schemas;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.builders.BaseGridBuilder;

public class Rs3GridBuilder extends BaseGridBuilder {

    public Rs3GridBuilder(Enum<?> panel) {
        super(panel);
    }

    @Override
    public GameVersion getGameVersion() {
        return GameVersion.RS3;
    }

    @Override
    protected Enum<?> getSchema() {
        return Rs3Schemas.RS3_PROFILE_SCHEMA;
    }

    @Override
    protected Rs3SettingController getController() {
        return DependencyFactory.getInstance().getInjection(Rs3SettingController.class);
    }
}
