package com.godscape.rs3.controllers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;

@Singleton
public class Rs3CharacterController {

    public Rs3CharacterController() {
        Logger.info("Rs3CharacterController: Initialization complete.");
    }

    public void loadCharacter() {
        Logger.info("Rs3CharacterController: Loading character...");
        // Logic for loading character
    }

    public void saveCharacter() {
        Logger.info("Rs3CharacterController: Saving character...");
        // Logic for saving character
    }
}
