package com.godscape.osrs.enums.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LocationName {

    @Getter
    @RequiredArgsConstructor
    public enum MajorLocation {
        VARROCK(() -> "Varrock"),
        LUMBRIDGE(() -> "Lumbridge"),
        FALADOR(() -> "Falador"),
        ARDOUGNE(() -> "Ardougne"),
        KARAMJA(() -> "Karamja"),
        DRAYNOR(() -> "Draynor"),
        EDGEVILLE(() -> "Edgeville"),
        AL_KHARID(() -> "Al Kharid"),
        TAVERLEY(() -> "Taverley"),
        CATHERBY(() -> "Catherby"),
        RELLEKKA(() -> "Rellekka"),
        YANILLE(() -> "Yanille"),
        CANIFIS(() -> "Canifis"),
        MORYTANIA(() -> "Morytania"),
        LUNAR_ISLE(() -> "Lunar Isle"),
        ZANARIS(() -> "Zanaris"),
        PRIFDDINAS(() -> "Prifddinas"),
        SHILO_VILLAGE(() -> "Shilo Village"),
        POLLNIVNEACH(() -> "Pollnivneach"),
        MENAPHOS(() -> "Menaphos"),
        SOPHANEM(() -> "Sophanem"),
        BURTHORPE(() -> "Burthorpe"),
        KELDAGRIM(() -> "Keldagrim"),
        PISCATORIS(() -> "Piscatoris"),
        APE_ATOLL(() -> "Ape Atoll"),
        WILDERNESS(() -> "Wilderness"),
        MISCELLANIA(() -> "Miscellania"),
        FREMENNIK_PROVINCE(() -> "Fremennik Province"),
        GREAT_KOUREND(() -> "Great Kourend"),
        PORT_PHASMATYS(() -> "Port Phasmatys");

        private final Supplier<String> locationNameSupplier;

        // Reverse lookup map
        private static final Map<String, MajorLocation> lookup = new HashMap<>();

        static {
            for (MajorLocation location : MajorLocation.values()) {
                lookup.put(location.getLocationName(), location);
            }
        }

        public String getLocationName() {
            return locationNameSupplier.get();
        }

        public static MajorLocation get(String locationName) {
            return lookup.get(locationName);
        }
    }
}
