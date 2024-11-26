package com.godscape.system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum Platforms {

    DREAMBOT_OSRS("DreamBot", GameVersion.OSRS),
    RUNELITE_OSRS("RuneLite", GameVersion.OSRS),
    OSBOT_OSRS("OSBot", GameVersion.OSRS),
    TRIBOT_OSRS("TRiBot", GameVersion.OSRS),
    TOPBOT_OSRS("TopBot", GameVersion.OSRS),
    POWERBOT_OSRS("Powerbot", GameVersion.OSRS),
    OPENOSRS("OpenOSRS", GameVersion.OSRS),
    RUNEDREAM_OSRS("RuneDream", GameVersion.OSRS),
    SILENTOSRS("SilentOSRS", GameVersion.OSRS),
    NEXUS_OSRS("Nexus", GameVersion.OSRS),
    ZAROS_OSRS("Zaros", GameVersion.OSRS),
    PARABOT_OSRS("Parabot", GameVersion.OSRS),
    BOTOMATION_OSRS("Botomation", GameVersion.OSRS),
    RUNEMATE_OSRS("RuneMate", GameVersion.OSRS),

    RUNEMATE_RS3("RuneMate", GameVersion.RS3),
    POWERBOT_RS3("Powerbot", GameVersion.RS3),
    TRIBOT_RS3("TRiBot", GameVersion.RS3),
    DREAMBOT_RS3("DreamBot", GameVersion.RS3),
    SILENT_RS3("Silent", GameVersion.RS3),
    RUNEDREAM_RS3("RuneDream", GameVersion.RS3),
    BOTOMATION_RS3("Botomation", GameVersion.RS3),
    SPECTRE_RS3("Spectre", GameVersion.RS3),
    RSPROBOT_RS3("RsProBot", GameVersion.RS3),

    UNKNOWN("Unknown", GameVersion.UNKNOWN);

    private final String platformName;
    private final GameVersion gameVersion;

    private static final Map<String, Platforms> lookupMap = new HashMap<>();

    static {
        for (Platforms platform : Platforms.values()) {
            lookupMap.put(platform.getPlatformName().toLowerCase(), platform);
        }
    }

    public static Platforms fromPlatformName(String platformName) {
        return lookupMap.getOrDefault(platformName.toLowerCase(), UNKNOWN);
    }

    @Override
    public String toString() {
        return this.platformName;
    }
}
