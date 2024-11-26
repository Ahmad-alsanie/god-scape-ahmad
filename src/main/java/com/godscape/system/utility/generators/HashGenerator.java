package com.godscape.system.utility.generators;

import com.godscape.system.annotations.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Singleton
public class HashGenerator {

    // Generate a UUID based on a hash of the entity name
    public static UUID generateId(String entityName) {
        if (entityName == null || entityName.isEmpty()) {
            throw new IllegalArgumentException("Entity name cannot be null or empty.");
        }

        try {
            // Create a SHA-256 hash of the entity name
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(entityName.toLowerCase().getBytes(StandardCharsets.UTF_8));

            // Use the first 16 bytes of the hash to create a UUID
            long mostSigBits = 0;
            long leastSigBits = 0;
            for (int i = 0; i < 8; i++) {
                mostSigBits = (mostSigBits << 8) | (hash[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                leastSigBits = (leastSigBits << 8) | (hash[i] & 0xff);
            }
            return new UUID(mostSigBits, leastSigBits);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate hash for entity name.", e);
        }
    }
}
