package com.godscape.system.controllers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Platforms;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.utility.Logger;

@Singleton
public class PlatformController {

    private Platforms currentPlatform;
    private GameVersion currentGameVersion;

    public PlatformController() {
        this.currentPlatform = Platforms.UNKNOWN;
        this.currentGameVersion = GameVersion.UNKNOWN;
    }

    public Platforms getCurrentPlatform() {
        return currentPlatform;
    }

    public void setCurrentPlatform(Platforms platform) {
        this.currentPlatform = platform;
        this.currentGameVersion = platform.getGameVersion();
        Logger.info("PlatformController: Current platform set to {}", platform);
    }

    public GameVersion getCurrentGameVersion() {
        return currentGameVersion;
    }

    public void detectPlatform(String platformName) {
        Platforms detectedPlatform = Platforms.fromPlatformName(platformName);
        if (detectedPlatform != null) {
            setCurrentPlatform(detectedPlatform);
            Logger.info("PlatformController: Platform detected as {}", detectedPlatform);
        } else {
            Logger.warn("PlatformController: Could not detect platform from name '{}'", platformName);
            setCurrentPlatform(Platforms.UNKNOWN);
        }
    }
}
