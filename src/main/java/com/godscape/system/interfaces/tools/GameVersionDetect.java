package com.godscape.system.interfaces.tools;

import com.godscape.system.enums.GameVersion;

/**
 * Interface for classes that need to be aware of the current GameVersion.
 */
public interface GameVersionDetect {

    /**
     * Sets the current game version for the implementing class.
     *
     * @param gameVersion the GameVersion to set
     */
    void setGameVersion(GameVersion gameVersion);

    /**
     * Gets the current game version for the implementing class.
     *
     * @return the current GameVersion
     */
    GameVersion getGameVersion();
}
