package com.godscape.system.utility;

import com.godscape.system.utility.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ManualIOBalancer {

    private final Map<String, IOTask> ioTaskMap = new ConcurrentHashMap<>(); // Active I/O tasks stored in map
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Thread pool for I/O tasks

    // Log initialization on class load
    static {
        Logger.info("ManualIOBalancer: Initialization complete.");
    }

    // Adds and starts a new I/O task with the specified taskId
    public void addIOTask(String taskId, IOTask ioTask) {
        if (ioTaskMap.containsKey(taskId)) {
            Logger.warn("ManualIOBalancer: Task with ID '{}' already exists.", taskId);
            return;
        }

        ioTaskMap.put(taskId, ioTask); // Add task to map
        executorService.submit(ioTask); // Start task in separate thread
        Logger.info("ManualIOBalancer: Task with ID '{}' added and started.", taskId);
    }

    // Removes an I/O task from the balancer and stops it
    public void removeIOTask(String taskId) {
        IOTask ioTask = ioTaskMap.remove(taskId); // Remove task from map

        if (ioTask == null) {
            Logger.error("ManualIOBalancer: No task found with ID '{}'.", taskId);
            return;
        }

        ioTask.stopTask(); // Gracefully stop the task
        Logger.info("ManualIOBalancer: Task with ID '{}' removed and stopped.", taskId);
    }

    // Shuts down the balancer and stops all tasks
    public void shutdown() {
        Logger.info("ManualIOBalancer: Shutting down balancer, waiting for active tasks to finish...");

        try {
            for (IOTask ioTask : ioTaskMap.values()) {
                ioTask.stopTask(); // Stop each active task
            }

            executorService.shutdown(); // Shut down executor service
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                Logger.warn("ManualIOBalancer: Forcing shutdown of tasks.");
                executorService.shutdownNow(); // Force shutdown if tasks don't stop
            }
            Logger.info("ManualIOBalancer: Balancer shutdown complete.");
        } catch (InterruptedException e) {
            Logger.error("ManualIOBalancer: Error during shutdown - {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    // Task for manually removing a pipeline (I/O-related logic placeholder)
    public static class RemovePipelineTask implements Runnable {
        @Override
        public void run() {
            Logger.info("ManualIOBalancer: RemovePipelineTask invoked.");
            // Add manual I/O balancing logic here
        }
    }

    // Represents an I/O task, which runs in a loop until stopped
    public static class IOTask implements Runnable {

        private volatile boolean running = true; // Controls task's running state

        @Override
        public void run() {
            while (running) {
                try {
                    Logger.info("IOTask: Running task..."); // Simulate I/O work
                    Thread.sleep(1000); // Sleep to simulate work delay
                } catch (InterruptedException e) {
                    Logger.error("IOTask: Task interrupted - {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
            Logger.info("IOTask: Task completed."); // Task completes when stopped
        }

        // Gracefully stop the I/O task
        public void stopTask() {
            Logger.info("IOTask: Stopping task...");
            this.running = false;
        }
    }
}
