package com.godscape.system.utility;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.controllers.BotController;
import com.godscape.system.enums.BotState;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.FrameFactory;
import com.godscape.system.interfaces.mark.Shutdownable;
import com.godscape.system.utility.Logger;
import org.dreambot.api.script.ScriptManager;
import lombok.Data;

@Singleton
public class BotTerminate implements Shutdownable {

    private static volatile BotTerminate instance;

    // Private constructor enforces singleton pattern
    private BotTerminate() {
        Logger.info("BotTerminate initialized for the script termination process.");
    }

    /**
     * Returns the singleton instance of BotTerminate.
     *
     * @return The singleton instance of BotTerminate.
     */
    public static BotTerminate getInstance() {
        if (instance == null) {
            synchronized (BotTerminate.class) {
                if (instance == null) {
                    instance = new BotTerminate();
                    Logger.info("BotTerminate singleton initialized.");
                }
            }
        }
        return instance;
    }

    /**
     * Main termination method that stops the bot session, logs character details, stops
     * any running scripts, and closes the UI.
     */
    public void execute() {
        stop();    // Stops the bot and any ongoing tasks
        closeUI(); // Closes the main UI
        Logger.info("Bot termination process executed.");
    }

    /**
     * Public method to exit the bot session by invoking execute, intended to be used as the
     * main entry point for shutdown tasks.
     */
    public void exit() {
        execute();
    }

    /**
     * Stops the bot using the BotController and performs necessary cleanup tasks.
     */
    private void stop() {
        Logger.info("Stopping the bot through BotController.");

        // Use BotController to stop the bot
        BotController botController = DependencyFactory.getInstance().getInjection(BotController.class);
        if (botController != null && botController.getBotState() != BotState.STOPPED) {
            botController.stopBot();
            Logger.info("BotController successfully stopped the bot.");
        } else {
            Logger.warn("BotController is null or bot is already stopped.");
        }

        // Additional cleanup tasks
        ScriptManager.getScriptManager().stop();
        Logger.info("All scripts stopped through ScriptManager.");
    }

    /**
     * Closes the main UI using FrameFactory.
     */
    private void closeUI() {
        Logger.info("Attempting to close the platform-specific UI frame.");

        // Call FrameFactory to dispose of the current frame
        FrameFactory.getInstance().disposeFrame();
        Logger.info("Platform-specific UI frame closed.");
    }
}
