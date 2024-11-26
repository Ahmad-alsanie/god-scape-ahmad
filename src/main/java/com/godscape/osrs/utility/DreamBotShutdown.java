package com.godscape.osrs.utility;

import com.godscape.system.interfaces.mark.Shutdownable;
import com.godscape.system.utility.Logger;
import org.dreambot.api.script.ScriptManager;

public class DreamBotShutdown implements Shutdownable {

    /**
     * Executes the DreamBot-specific shutdown process.
     */
    public void shutdown() {
        Logger.info("DreamBotShutdown: Stopping all scripts with ScriptManager.");
        ScriptManager.getScriptManager().stop();  // Terminate all running scripts
        Logger.info("DreamBotShutdown: DreamBot-specific shutdown complete.");
    }
}
