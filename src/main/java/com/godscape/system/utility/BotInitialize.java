package com.godscape.system.utility;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Factories;
import com.godscape.system.enums.Managers;
import com.godscape.system.enums.Utilities;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.FrameFactory;
import com.godscape.system.managers.PreloadManager;
import lombok.Data;

@Singleton
public class BotInitialize {

    private static volatile BotInitialize instance;
    private final DependencyFactory dependencyFactory = DependencyFactory.getInstance();

    public BotInitialize() {
        Logger.info("BotInitialize: Initializing bot system.");
    }

    public void start() {
        PreloadManager preloadManager = dependencyFactory.getInjection(Managers.PRELOAD_MANAGER);
        FrameFactory frameFactory = dependencyFactory.getInjection(Factories.FRAME_FACTORY);

        // Use the generic loadPreloader method that auto-detects the game type
        preloadManager.loadPreloader();

        // Initialize the main frame
        frameFactory.getFrame();
        Logger.info("BotInitialize: Initialization process complete.");
    }
}
