package com.godscape.system.registries;

import com.godscape.system.enums.GameVersion;
import com.godscape.system.enums.Platforms;
import com.godscape.system.utility.Logger;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class PlatformRegistry {

    private static volatile PlatformRegistry instance;
    private final Map<Platforms, Platforms> platformInstances = new EnumMap<>(Platforms.class);

    private PlatformRegistry() {
        initializePlatforms();
    }

    public static PlatformRegistry getInstance() {
        if (instance == null) {
            synchronized (PlatformRegistry.class) {
                if (instance == null) {
                    instance = new PlatformRegistry();
                }
            }
        }
        return instance;
    }

    private void initializePlatforms() {
        for (Platforms platform : Platforms.values()) {
            platformInstances.put(platform, platform);
            Logger.info("PlatformRegistry: Registered platform - {}",
                    platform.getPlatformName());
        }
    }

    public Optional<Platforms> getPlatformByName(String platformName) {
        return platformInstances.values().stream()
                .filter(platform -> platform.getPlatformName()
                        .equalsIgnoreCase(platformName))
                .findFirst();
    }

    public List<Platforms> getPlatformsByGameVersion(GameVersion gameVersion) {
        return platformInstances.values().stream()
                .filter(platform -> platform.getGameVersion() == gameVersion)
                .collect(Collectors.toList());
    }

    public List<Platforms> getAllPlatforms() {
        return new ArrayList<>(platformInstances.values());
    }

    public Platforms getUnknownPlatform() {
        return Platforms.UNKNOWN;
    }
}
