package com.godscape.osrs.platforms.dreambot;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.enums.Platforms;
import com.godscape.system.enums.Utilities;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.FrameFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.BotInitialize;
import com.godscape.system.utility.BotMainLoop;
import com.godscape.system.utility.BotTerminate;
import com.godscape.system.utility.Logger;
import lombok.extern.java.Log;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import javax.swing.SwingUtilities;

@ScriptManifest(
        author = "Shujin",
        name = "Godscape: DreamBot Edition",
        version = 1.0,
        description = "-= DreamBot Platform Adapter =-",
        category = Category.MISC
)
@Singleton
public class DreamBotAdapter extends AbstractScript {

    private BotInitialize botInitialize;
    private BotMainLoop botMainLoop;
    private BotTerminate botTerminate;

    public DreamBotAdapter() {
        Logger.info("DreamBotAdapter: Initializing DreamBotAdapter...");
        DependencyFactory.getInstance().getInjection(PlatformFactory.class).setCurrentPlatform(Platforms.DREAMBOT_OSRS);
        Logger.info("DreamBotAdapter: Initialized PlatformFactory with" + " game version.");
    }

    @Override
    public void onStart() {
          Logger.info("DreamBotAdapter: onStart called.");
        initializeBot();
    }

    @Override
    public int onLoop() {
        if (botMainLoop == null) {
            botMainLoop = DependencyFactory.getInstance().getInjection(Utilities.BOT_MAIN_LOOP);
        }
        return botMainLoop.loop();
    }

    @Override
    public void onExit() {
        terminateBot();

        SwingUtilities.invokeLater(() -> {
            FrameFactory.getInstance().disposeFrame();
            Logger.info("DreamBotAdapter: Closed UI when the script stopped.");
        });
    }

    private void initializeBot() {
        if (botInitialize == null) {
            botInitialize = DependencyFactory.getInstance().getInjection(BotInitialize.class);
        }
        botInitialize.start();
    }

    private void terminateBot() {
        if (botTerminate == null) {
            botTerminate = DependencyFactory.getInstance().getInjection(Utilities.BOT_TERMINATE);
        }
        botTerminate.exit();
    }

    public static void main(String[] args) {
        System.out.println("This is a DreamBot script. Launch it via the DreamBot client.");
    }
}
