package com.godscape.system.observers;

import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.managers.BaseObservationManager;
import com.godscape.system.utility.Logger;

public class BotStateListener implements BotStateObservation {

    private final BaseObservationManager baseObservationManager;

    public BotStateListener() {
        this.baseObservationManager = DependencyFactory.getInstance().getInjection(BaseObservationManager.class);
    }

    @Override
    public void onBotStatusChanged(boolean isRunning) {
        Logger.info("Bot state changed: {}", isRunning);
        baseObservationManager.notifyBotStateChanged(isRunning);
    }
}
