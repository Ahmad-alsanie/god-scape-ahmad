package com.hazelcast.internal.networking.nio.iobalancer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Advanced implementation of RemovePipelineTask that simulates the removal
 * of a networking pipeline for load balancing.
 */
public class RemovePipelineTask implements Runnable {

    private static final Logger logger = Logger.getLogger(RemovePipelineTask.class.getName());

    private final String pipelineId;
    private final PipelineManager pipelineManager;
    private final int maxRetryAttempts;
    private final long retryIntervalMillis;

    /**
     * Constructs a new RemovePipelineTask.
     *
     * @param pipelineId         The ID of the pipeline to be removed.
     * @param pipelineManager    The manager responsible for handling the pipeline operations.
     * @param maxRetryAttempts   The maximum number of retry attempts if the pipeline cannot be removed immediately.
     * @param retryIntervalMillis The interval in milliseconds between retry attempts.
     */
    public RemovePipelineTask(String pipelineId, PipelineManager pipelineManager, int maxRetryAttempts, long retryIntervalMillis) {
        if (pipelineId == null || pipelineId.isEmpty()) {
            throw new IllegalArgumentException("pipelineId cannot be null or empty.");
        }
        if (pipelineManager == null) {
            throw new IllegalArgumentException("pipelineManager cannot be null.");
        }
        if (maxRetryAttempts < 1) {
            throw new IllegalArgumentException("maxRetryAttempts must be at least 1.");
        }
        if (retryIntervalMillis < 0) {
            throw new IllegalArgumentException("retryIntervalMillis cannot be negative.");
        }

        this.pipelineId = pipelineId;
        this.pipelineManager = pipelineManager;
        this.maxRetryAttempts = maxRetryAttempts;
        this.retryIntervalMillis = retryIntervalMillis;
    }

    /**
     * Executes the task to remove the specified pipeline with retry logic.
     */
    @Override
    public void run() {
        logger.log(Level.INFO, "Starting removal of pipeline: {0}", pipelineId);
        int attempt = 0;
        boolean isRemoved = false;

        while (attempt < maxRetryAttempts && !isRemoved) {
            attempt++;
            try {
                logger.log(Level.FINE, "Attempt {0} to remove pipeline {1}", new Object[]{attempt, pipelineId});
                isRemoved = pipelineManager.removePipeline(pipelineId);
                if (isRemoved) {
                    logger.log(Level.INFO, "Pipeline {0} removed successfully on attempt {1}", new Object[]{pipelineId, attempt});
                } else {
                    if (attempt < maxRetryAttempts) {
                        logger.log(Level.WARNING, "Pipeline {0} could not be removed on attempt {1}. Retrying after {2} ms...",
                                new Object[]{pipelineId, attempt, retryIntervalMillis});
                        Thread.sleep(retryIntervalMillis);
                    }
                }
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "RemovePipelineTask for pipeline {0} was interrupted during retry interval.", pipelineId);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "An unexpected error occurred while removing pipeline {0}: {1}", new Object[]{pipelineId, e.getMessage()});
                break;
            }
        }

        if (!isRemoved) {
            logger.log(Level.SEVERE, "Failed to remove pipeline {0} after {1} attempts.", new Object[]{pipelineId, maxRetryAttempts});
        }

        // Perform cleanup, whether the pipeline was successfully removed or not
        cleanupResources();
    }

    /**
     * Performs any necessary cleanup after attempting to remove the pipeline.
     */
    private void cleanupResources() {
        try {
            logger.log(Level.INFO, "Performing cleanup operations for pipeline {0}.", pipelineId);
            pipelineManager.cleanupPipelineResources(pipelineId);
            logger.log(Level.INFO, "Cleanup completed for pipeline {0}.", pipelineId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to cleanup resources for pipeline {0}: {1}", new Object[]{pipelineId, e.getMessage()});
        }
    }
}
