package com.godscape.system.enums;

import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.config.HazelcastConfig;
import com.godscape.system.config.SQLConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Configs {

    GODSCAPE_CONFIG(GodscapeConfig.class, () -> new GodscapeConfig()),   // Lambda for GodscapeConfig
    HAZELCAST_CONFIG(HazelcastConfig.class, () -> new HazelcastConfig()), // Lambda for HazelcastConfig
    SQL_CONFIG(SQLConfig.class, () -> new SQLConfig());                   // Lambda for SQLConfig

    private final Class<?> clazz;           // Class type of the configuration
    private final Supplier<?> supplier;     // Lambda supplier for configuration instance

    @Override
    public String toString() {
        return this.name();
    }

    @SuppressWarnings("unchecked")
    public <T> T getConfig() {
        return (T) supplier.get();
    }
}
