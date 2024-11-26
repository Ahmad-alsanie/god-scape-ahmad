package com.godscape.system.factories;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Platforms;
import com.godscape.system.enums.GameVersion;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class PlatformFactory {
    private final AtomicReference<Platforms> currentPlatform;
    private final Map<String, Platforms> platformLookupMap;

    private PlatformFactory() {
        this.currentPlatform = new AtomicReference<>(Platforms.DREAMBOT_OSRS);
        this.platformLookupMap = new ConcurrentHashMap<>();

        for (Platforms platform : Platforms.values()) {
            platformLookupMap.put(platform.getPlatformName().toLowerCase(), platform);
        }
    }

    public void setCurrentPlatform(Platforms platform) {
        currentPlatform.set(platform);
    }

    public Platforms getCurrentPlatform() {
        return currentPlatform.get();
    }

    public Platforms detectPlatform(String platformName) {
        Platforms detectedPlatform = platformLookupMap.getOrDefault(
                platformName.toLowerCase(), Platforms.DREAMBOT_OSRS);
        setCurrentPlatform(detectedPlatform);
        return detectedPlatform;
    }

    public GameVersion getCurrentGameVersion() {
        return currentPlatform.get().getGameVersion();
    }
}
