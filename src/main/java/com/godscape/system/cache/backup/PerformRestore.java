package com.godscape.system.cache.backup;

import com.godscape.system.enums.Utilities;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

import java.util.Collections;
import java.util.List;

public class PerformRestore {

    private final RestoreFromJson restoreFromJson;
    private final RestoreFromXml restoreFromXml;

    private static volatile PerformRestore instance;

    public PerformRestore() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.restoreFromJson = dependencyFactory.getInjection(Utilities.RESTORE_FROM_JSON);
        this.restoreFromXml = dependencyFactory.getInjection(Utilities.RESTORE_FROM_XML);
        Logger.info("PerformRestore: Initialized.");
    }

    public static PerformRestore getInstance() {
        if (instance == null) {
            synchronized (PerformRestore.class) {
                if (instance == null) {
                    instance = new PerformRestore();
                }
            }
        }
        return instance;
    }

    public <T> List<T> restoreProfiles(String targetDirectory, String format) {
        try {
            if ("json".equalsIgnoreCase(format)) {
                return (List<T>) restoreFromJson.loadProfiles(targetDirectory);
            } else if ("xml".equalsIgnoreCase(format)) {
                return (List<T>) restoreFromXml.loadProfiles(targetDirectory);
            } else {
                Logger.error("PerformRestore: Unsupported format '{}'. Only 'json' and 'xml' are supported.", format);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            Logger.error("PerformRestore: Error occurred during restore - {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
