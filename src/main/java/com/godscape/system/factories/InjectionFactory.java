package com.godscape.system.factories;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InjectionFactory {

    private final Map<Class<?>, Object> singletonInstances = new HashMap<>();
    private final Map<Class<?>, Supplier<?>> componentSuppliers = new HashMap<>();

    public InjectionFactory(Map<Class<?>, Supplier<?>> utilitySuppliers) {
        // Directly populate componentSuppliers with utility suppliers
        componentSuppliers.putAll(utilitySuppliers);
    }

    public void registerSingleton(Class<?> clazz) {
        if (!singletonInstances.containsKey(clazz)) {
            singletonInstances.put(clazz, createInstance(clazz));
            Logger.info("InjectionFactory: Registered singleton: {}", clazz.getSimpleName());
        }
    }

    public void registerComponent(Class<?> clazz) {
        componentSuppliers.put(clazz, () -> createInstance(clazz));
        Logger.info("InjectionFactory: Registered component: {}", clazz.getSimpleName());
    }

    @SuppressWarnings("unchecked")
    public <T> T getDependency(Object type) {
        if (type instanceof Class<?>) {
            Class<T> clazz = (Class<T>) type;
            if (clazz.isAnnotationPresent(Singleton.class)) {
                return (T) singletonInstances.computeIfAbsent(clazz, this::createInstance);
            }
            return (T) componentSuppliers.getOrDefault(clazz, () -> createInstance(clazz)).get();
        } else {
            Logger.error("InjectionFactory: Unsupported dependency type {}", type.getClass().getSimpleName());
            throw new IllegalArgumentException("Unsupported dependency type: " + type);
        }
    }

    private <T> T createInstance(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Logger.info("InjectionFactory: Successfully created instance of {}", clazz.getSimpleName());
            return instance;
        } catch (Exception e) {
            Logger.error("InjectionFactory: Failed to instantiate class {}: {}", clazz.getSimpleName(), e.getMessage());
            throw new IllegalArgumentException("Could not instantiate class: " + clazz, e);
        }
    }
}
