package com.godscape.osrs.managers.panels;

import com.godscape.system.enums.BotState;
import com.godscape.system.enums.Controllers;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.controllers.BotController;

public class OsrsFooterManager {

    private final BotController botController;

    public OsrsFooterManager() {
        this.botController = DependencyFactory.getInstance().getInjection(Controllers.BOT_CONTROLLER);

        if (botController == null) {
            throw new IllegalStateException("Failed to initialize BotController in OsrsFooterManager.");
        }
    }

    public void startScript() {
        BotState botState = botController.getBotState();
        if (botState == BotState.PAUSED) {
            resumeScript();
        } else if (botState != BotState.RUNNING) {
            botController.startBot();
        }
    }

    public void resumeScript() {
        if (botController.getBotState() == BotState.PAUSED) {
            botController.startBot();
        }
    }

    public void pauseScript() {
        if (botController.getBotState() == BotState.RUNNING) {
            botController.pauseBot();
        }
    }

    public void stopScript() {
        if (botController.getBotState() != BotState.STOPPED) {
            botController.stopBot();
        }
    }

    public BotState getBotState() {
        return botController.getBotState();
    }
}
