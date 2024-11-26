package com.godscape.system.controllers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.BotState;
import com.godscape.system.utility.BotMainLoop;
import com.godscape.system.utility.Logger;
import lombok.Data;

/**
 * BotController manages bot-specific logic and actions, including start, pause, resume, and stop.
 */
@Data
@Singleton
public class BotController {

    private final BotMainLoop botMainLoop;
    private BotState botState = BotState.STOPPED;
    private boolean stopRequested = false;

    // Constructor with dependency injection
    public BotController(BotMainLoop botMainLoop) {
        this.botMainLoop = botMainLoop;
        Logger.info("BotController initialized for botState: {}", botState);
    }

    public void startBot() {
        if (botState == BotState.STOPPED || botState == BotState.PAUSED) {
            Logger.info(botState == BotState.STOPPED ? "Bot started!" : "Bot resumed!");
            botState = BotState.RUNNING;
            stopRequested = false;
        } else {
            Logger.warn("Bot is already running.");
        }
    }

    public int mainLoop() {
        if (botState == BotState.STOPPED) {
            Logger.error("Cannot execute main loop. Bot is stopped.");
            return -1;
        } else if (botState == BotState.PAUSED) {
            Logger.info("Bot is paused.");
            return 1000;
        } else {
            return botMainLoop.loop();
        }
    }

    public void pauseBot() {
        if (botState == BotState.RUNNING) {
            Logger.info("Bot paused.");
            botState = BotState.PAUSED;
        } else {
            Logger.warn("Bot is not running or already paused.");
        }
    }

    public void stopBot() {
        if (stopRequested) {
            Logger.warn("Stop request already in progress. Ignoring additional stop request.");
            return;
        }
        stopRequested = true;

        if (botState != BotState.STOPPED) {
            Logger.info("Bot stopped.");
            botState = BotState.STOPPED;
        } else {
            Logger.warn("Bot is already stopped.");
        }
    }

    public BotState getBotState() {
        return botState;
    }
}
