package com.hazelcast.internal.networking.nio.iobalancer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Interface representing a manager responsible for handling networking pipelines.
 * Implementations should provide the logic for removing pipelines and cleaning up resources.
 */
public interface PipelineManager {

    /**
     * Attempts to remove the specified pipeline.
     *
     * @param pipelineId The ID of the pipeline to be removed.
     * @return {@code true} if the pipeline was removed successfully; {@code false} otherwise.
     */
    boolean removePipeline(String pipelineId);

    /**
     * Cleans up resources associated with the specified pipeline.
     *
     * @param pipelineId The ID of the pipeline for which resources are to be cleaned up.
     */
    void cleanupPipelineResources(String pipelineId);
}
