package com.godscape.system.config;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Factories;
import com.godscape.system.factories.SerializableFactory;
import com.godscape.system.utility.Logger;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.networking.nio.iobalancer.CustomIOBalancer;
import com.hazelcast.logging.LoggingService;

import java.util.Random;

@Singleton
public class HazelcastConfig {

    private final HazelcastInstance hazelcastInstance;

    // Constructor for initializing the Hazelcast instance
    public HazelcastConfig() {
        this.hazelcastInstance = initializeHazelcastInstance();
    }

    // Initialize Hazelcast instance with configuration
    private HazelcastInstance initializeHazelcastInstance() {
        try {
            Config config = new Config();
            config.setInstanceName("godscape-hazelcast-instance");

            // Configure network settings
            configureNetwork(config);

            // Get SerializableFactory from Factories enum
            SerializableFactory serializableFactory = (SerializableFactory) Factories.SERIALIZABLE_FACTORY.getSupplier().get();
            config.getSerializationConfig().addDataSerializableFactory(SerializableFactory.FACTORY_ID, serializableFactory);

            // Initialize Hazelcast instance
            HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
            Logger.info("HazelcastConfig: Hazelcast instance '{}' initialized successfully on port {}.", config.getInstanceName(), config.getNetworkConfig().getPort());

            // Set up custom IOBalancer
            setupCustomIOBalancer(instance);

            return instance;
        } catch (Throwable t) {
            Logger.error("HazelcastConfig: Error initializing Hazelcast instance - {}", t.getMessage(), t);
            throw t;
        }
    }

    // Configure network settings
    private void configureNetwork(Config config) {
        NetworkConfig networkConfig = config.getNetworkConfig();
        int randomPort = new Random().nextInt(5001) + 5000; // Random port between 5000-10000
        networkConfig.setPort(randomPort)
                .setPortAutoIncrement(true)
                .setPortCount(100);
        networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        networkConfig.getJoin().getTcpIpConfig().setEnabled(true);
    }

    // Set up custom IOBalancer
    private void setupCustomIOBalancer(HazelcastInstance instance) {
        try {
            LoggingService loggingService = instance.getLoggingService();
            CustomIOBalancer customIOBalancer = new CustomIOBalancer(null, null, "custom-balancer", 5, loggingService);
            customIOBalancer.start();
            Logger.info("HazelcastConfig: Custom IOBalancer started successfully.");
        } catch (Exception e) {
            Logger.error("HazelcastConfig: Error setting up custom IOBalancer - {}", e.getMessage(), e);
        }
    }

    // Retrieve the HazelcastInstance
    public HazelcastInstance getHazelcastInstance() {
        if (hazelcastInstance != null) {
            Logger.info("HazelcastConfig: Returning Hazelcast instance '{}'.", hazelcastInstance.getName());
        } else {
            Logger.warn("HazelcastConfig: Hazelcast instance is null.");
        }
        return hazelcastInstance;
    }

    // Shut down the Hazelcast instance
    public void shutdown() {
        try {
            if (hazelcastInstance != null) {
                hazelcastInstance.shutdown();
                Logger.info("HazelcastConfig: Hazelcast instance '{}' shut down successfully.", hazelcastInstance.getName());
            }
        } catch (Exception e) {
            Logger.error("HazelcastConfig: Error shutting down Hazelcast instance - {}", e.getMessage(), e);
        }
    }
}
