package com.godscape;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HazelcastTest {

    private HazelcastInstance hazelcastInstance;

    @BeforeEach
    public void setUp() {
        // Initialize a Hazelcast instance before each test
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    @AfterEach
    public void tearDown() {
        // Shutdown the Hazelcast instance after each test
        if (hazelcastInstance != null) {
            hazelcastInstance.shutdown();
        }
    }

    //@Test
    public void testHazelcastMapPutAndGet() {
        // Create or retrieve a distributed map
        IMap<String, String> map = hazelcastInstance.getMap("my-distributed-map");

        // Put some data in the map
        map.put("key", "value");

        // Retrieve the value by key
        String value = map.get("key");

        // Verify the value using an assertion
        assertEquals("value", value, "Value for 'key' should be 'value'");
    }
}
