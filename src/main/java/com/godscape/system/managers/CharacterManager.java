package com.godscape.system.managers;

import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Data
public class CharacterManager {

    // Singleton instance
    private static volatile CharacterManager instance;

    // Public method to retrieve the Singleton instance
    @Synchronized
    public static CharacterManager getInstance() {
        if (instance == null) {
            instance = new CharacterManager();
            Logger.info("CharacterManager: Singleton instance created.");
        }
        return instance;
    }

}
