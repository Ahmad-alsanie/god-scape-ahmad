package com.godscape.system.managers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Platforms;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.interfaces.mark.Shutdownable;
import com.godscape.system.utility.BotTerminate;
import com.godscape.system.utility.Logger;
import com.godscape.osrs.utility.DreamBotShutdown;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class PlatformManager {

    private final Platforms currentPlatform;
    private final List<Shutdownable> shutdownTasks = new ArrayList<>();

    public PlatformManager(PlatformFactory platformFactory, BotTerminate botTerminate) {
        this.currentPlatform = detectPlatform(platformFactory);
        initializeShutdownTasks(botTerminate);
    }

    private Platforms detectPlatform(PlatformFactory platformFactory) {
        Logger.info("PlatformManager: Detecting platform...");
        Platforms detectedPlatform = platformFactory.detectPlatform("DreamBot");
        Logger.info("PlatformManager: Platform detected and set to {}", detectedPlatform);
        return detectedPlatform;
    }

    private void initializeShutdownTasks(BotTerminate botTerminate) {
        shutdownTasks.add(botTerminate);

        if (currentPlatform == Platforms.DREAMBOT_OSRS) {
            shutdownTasks.add(new DreamBotShutdown());
        }
    }

    public void shutdownPlatform() {
        Logger.info("PlatformManager: Starting shutdown for platform {}", currentPlatform);

        for (Shutdownable task : shutdownTasks) {
            if (task instanceof BotTerminate) {
                ((BotTerminate) task).exit();
                Logger.info("PlatformManager: BotTerminate cleanup executed.");
            } else if (task instanceof DreamBotShutdown) {
                ((DreamBotShutdown) task).shutdown();
            }
        }

        Logger.info("PlatformManager: Shutdown completed for {}", currentPlatform);
    }
}
