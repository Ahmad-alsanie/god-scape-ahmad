package com.godscape.system.factories;

import com.godscape.system.enums.Managers;
import com.godscape.system.utility.Logger;

public class ManagerFactory {

    // Singleton instance of ManagerFactory
    private static volatile ManagerFactory instance;
    private final DependencyFactory dependencyFactory;

    // Private constructor to prevent external instantiation
    private ManagerFactory() {
        Logger.info("ManagerFactory: Initializing ManagerFactory...");
        this.dependencyFactory = DependencyFactory.getInstance();
        Logger.info("ManagerFactory: Initialization complete.");
    }

    // Public method to retrieve the singleton instance
    public static ManagerFactory getInstance() {
        if (instance == null) {
            synchronized (ManagerFactory.class) {
                if (instance == null) {
                    instance = new ManagerFactory();
                }
            }
        }
        return instance;
    }

    // Retrieve a manager instance based on the provided enum
    @SuppressWarnings("unchecked")
    public <T> T getManager(Managers managerEnum) {
        Logger.info("Retrieving manager: {}", managerEnum.name());
        return (T) dependencyFactory.getInjection(managerEnum);
    }
}
