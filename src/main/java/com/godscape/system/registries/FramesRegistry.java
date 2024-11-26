package com.godscape.system.registries;

import com.godscape.system.enums.Frames;
import com.godscape.system.utility.Logger;

import java.util.function.Supplier;

/**
 * FramesRegistry synchronizes with the Frames enum to provide a registry of frame instances
 * using the DependencyRegistry for dependency injection.
 */
public class FramesRegistry {

    private static volatile FramesRegistry instance;
    private final DependencyRegistry dependencyRegistry;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes frames by registering them with the DependencyRegistry.
     */
    private FramesRegistry() {
        dependencyRegistry = DependencyRegistry.getInstance();
        initializeFrames();
        Logger.info("FramesRegistry: Initialized with frames from the Frames enum.");
    }

    /**
     * Retrieves the singleton instance of FramesRegistry.
     *
     * @return the singleton instance
     */
    public static FramesRegistry getInstance() {
        if (instance == null) {
            synchronized (FramesRegistry.class) {
                if (instance == null) {
                    instance = new FramesRegistry();
                    Logger.info("FramesRegistry: Instance created.");
                }
            }
        }
        return instance;
    }

    /**
     * Initializes frames by registering each frame enum with the DependencyRegistry.
     * Ensures that frames are managed via dependency injection.
     */
    private void initializeFrames() {
        Logger.info("FramesRegistry: Initializing frames...");
        for (Frames frame : Frames.values()) {
            try {
                Supplier<?> supplier = frame.getSupplier(); // Assumes Frames enum has getSupplier()
                Class<?> clazz = frame.getClazz();           // Assumes Frames enum has getClazz()
                if (supplier != null && clazz != null) {
                    dependencyRegistry.registerEnumMapping(frame, supplier, clazz);
                    Logger.debug("FramesRegistry: Registered frame '{}'", frame.name());
                } else {
                    Logger.warn("FramesRegistry: Supplier or class for frame '{}' is null.", frame.name());
                }
            } catch (Exception e) {
                Logger.error("FramesRegistry: Exception while registering frame '{}': {}", frame.name(), e.getMessage());
            }
        }
        Logger.info("FramesRegistry: Frames initialization complete.");
    }

    /**
     * Retrieves the frame instance corresponding to the specified frame enum.
     *
     * @param frame the frame enum
     * @param <T>   the type of the frame
     * @return the frame instance
     */
    @SuppressWarnings("unchecked")
    public <T> T getFrame(Frames frame) {
        return dependencyRegistry.getInjection(frame);
    }

    /**
     * Reloads (re-initializes) the specified frame.
     * This forces the DependencyRegistry to recreate the frame instance.
     *
     * @param frame the frame enum to reload
     */
    public void reloadFrame(Frames frame) {
        dependencyRegistry.registerEnumMapping(frame, frame.getSupplier(), frame.getClazz());
        Logger.info("FramesRegistry: Reloaded frame '{}'", frame.name());
    }

    /**
     * Reloads (re-initializes) all frames managed by the FramesRegistry.
     */
    public void reloadAllFrames() {
        for (Frames frame : Frames.values()) {
            reloadFrame(frame);
        }
        Logger.info("FramesRegistry: All frames reloaded.");
    }
}
