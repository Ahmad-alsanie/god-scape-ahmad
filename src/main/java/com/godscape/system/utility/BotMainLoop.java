package com.godscape.system.utility;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.BotState;
import com.godscape.system.controllers.BotController;
import com.godscape.system.enums.Controllers;
import com.godscape.system.factories.DependencyFactory;

@Singleton
public class BotMainLoop {

    // Singleton instance
    private static volatile BotMainLoop instance;

    // Private constructor to enforce singleton pattern
    public BotMainLoop() {
        // Empty constructor to avoid direct dependencies
    }

    /**
     * Retrieves the singleton instance of BotMainLoop.
     *
     * @return The singleton instance.
     */
    public static BotMainLoop getInstance() {
        if (instance == null) {
            synchronized (BotMainLoop.class) {
                if (instance == null) {
                    instance = new BotMainLoop();
                    Logger.info("BotMainLoop: Singleton instance created.");
                }
            }
        }
        return instance;
    }

    public void execute() {
        Logger.info("BotMainLoop executed.");

        // Retrieve BotController via DependencyFactory
        BotController botController = DependencyFactory.getInstance().getInjection(Controllers.BOT_CONTROLLER);
        BotState botState = botController.getBotState();

        switch (botState) {
            case RUNNING:
                Logger.info("Bot is running.");
                // Add main bot logic here
                break;
            case PAUSED:
                Logger.info("Bot is paused. Waiting...");
                break;
            case STOPPED:
                Logger.info("Bot has stopped. Exiting loop.");
                break;
            default:
                Logger.warn("Unknown bot state.");
        }
    }

    public int loop() {
        execute();
        // Retrieve BotController and check state
        BotController botController = DependencyFactory.getInstance().getInjection(Controllers.BOT_CONTROLLER);
        BotState botState = botController.getBotState();
        return botState == BotState.RUNNING ? 5000 : 10000;
    }
}
