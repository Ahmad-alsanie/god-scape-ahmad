package com.godscape.system.registries;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Supplier;

public class DependencyRegistry {

    private static volatile DependencyRegistry instance;
    private final Map<Class<?>, Object> singletonInstances = new HashMap<Class<?>, Object>();
    private final Map<Enum<?>, Supplier<?>> enumToSupplierMapping = new HashMap<Enum<?>, Supplier<?>>();
    private final Map<Enum<?>, Class<?>> enumToClassMapping = new HashMap<Enum<?>, Class<?>>();
    private final Map<String, Supplier<?>> entries = new HashMap<String, Supplier<?>>();
    private boolean initialized = false;

    private DependencyRegistry() {}

    public static DependencyRegistry getInstance() {
        if (instance == null) {
            synchronized (DependencyRegistry.class) {
                if (instance == null) {
                    instance = new DependencyRegistry();
                }
            }
        }
        return instance;
    }

    public void initializeRegistry(String... packageNames) {
        if (!initialized) {
            initializeEnumMappings();
            initialized = true;
            Logger.info("DependencyRegistry: Initialization complete.");
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void registerEntry(String key, Supplier<?> supplier) {
        entries.put(key, supplier);
    }

    public void registerEnumMapping(Enum<?> enumValue, Supplier<?> supplier, Class<?> clazz) {
        if (!enumToSupplierMapping.containsKey(enumValue)) {
            enumToSupplierMapping.put(enumValue, supplier);
            enumToClassMapping.put(enumValue, clazz);
            Logger.info("DependencyRegistry: Registered enum mapping {} -> {}",
                    enumValue.name(), clazz.getName());
        }
    }

    private void initializeEnumMappings() {
        // Initialize other enums if necessary
        // Example:
        // for (Utilities utility : Utilities.values()) {
        //     registerEnumMapping(utility, utility.getSupplier(), utility.getClazz());
        // }
    }

    @SuppressWarnings("unchecked")
    public <T> T getInjection(Class<T> clazz) {
        Logger.debug("DependencyRegistry: Attempting to retrieve injection for {}",
                clazz.getName());

        if (clazz.isEnum()) {
            Logger.warn("DependencyRegistry: Cannot inject enum class {}. Returning null.",
                    clazz.getName());
            return null;
        }

        if (singletonInstances.containsKey(clazz)) {
            return (T) singletonInstances.get(clazz);
        }

        T instance = (T) createInstance(clazz);
        if (clazz.isAnnotationPresent(Singleton.class)) {
            singletonInstances.put(clazz, instance);
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T getInjection(Enum<?> enumValue) {
        Supplier<?> supplier = enumToSupplierMapping.get(enumValue);
        if (supplier != null) {
            Logger.debug("DependencyRegistry: Successfully retrieved supplier for enum {}",
                    enumValue.name());
            return (T) supplier.get();
        }
        Logger.error("DependencyRegistry: No mapping found for enum {}",
                enumValue.name());
        throw new IllegalArgumentException("No mapping found for enum: " + enumValue);
    }

    public <T> Class<T> getClassForEnum(Enum<?> enumValue) {
        return (Class<T>) enumToClassMapping.get(enumValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T getInstance(String key) {
        Supplier<?> supplier = entries.get(key);
        if (supplier == null) {
            Logger.error("DependencyRegistry: No entry found for key: {}", key);
            throw new IllegalArgumentException("No entry found for key: " + key);
        }
        return (T) supplier.get();
    }

    private Object createInstance(Class<?> clazz) {
        Logger.debug("DependencyRegistry: Creating instance of class {}", clazz.getName());
        try {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();

            // Convert to a list
            List<Constructor<?>> constructorList = Arrays.asList(constructors);

            // Sort constructors by parameter count in descending order
            Collections.sort(constructorList, new Comparator<Constructor<?>>() {
                @Override
                public int compare(Constructor<?> c1, Constructor<?> c2) {
                    int p1 = c1.getParameterTypes().length;
                    int p2 = c2.getParameterTypes().length;
                    return p2 - p1; // Descending order
                }
            });

            for (Constructor<?> constructor : constructorList) {
                Logger.debug("DependencyRegistry: Trying constructor with {} parameters for class {}",
                        constructor.getParameterTypes().length, clazz.getName());
                try {
                    Class<?>[] paramTypes = constructor.getParameterTypes();
                    Object[] parameters = new Object[paramTypes.length];
                    boolean canInstantiate = true;

                    for (int i = 0; i < paramTypes.length; i++) {
                        parameters[i] = resolveParameter(paramTypes[i]);
                        if (parameters[i] == null) {
                            Logger.warn("DependencyRegistry: Cannot resolve parameter of type {} for constructor of {}",
                                    paramTypes[i].getName(), clazz.getName());
                            canInstantiate = false;
                            break;
                        }
                    }

                    if (!canInstantiate) {
                        Logger.debug("DependencyRegistry: Skipping constructor due to unresolvable dependencies.");
                        continue;
                    }

                    constructor.setAccessible(true);
                    Object instance = constructor.newInstance(parameters);
                    Logger.info("DependencyRegistry: Successfully created instance of {}",
                            clazz.getName());
                    return instance;
                } catch (Exception e) {
                    Logger.warn("DependencyRegistry: Failed to instantiate {} with constructor {}: {}",
                            clazz.getName(), constructor.toString(), e.getMessage());
                    // Continue to next constructor
                }
            }

            throw new NoSuchMethodException("No suitable constructor found for " + clazz.getName());
        } catch (Exception e) {
            Logger.error("DependencyRegistry: Failed to create instance of {}: {}",
                    clazz.getName(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private Object resolveParameter(Class<?> paramClass) {
        Logger.debug("DependencyRegistry: Resolving parameter of type {}",
                paramClass.getName());
        try {
            Object dependency = getInjection(paramClass);
            if (dependency == null) {
                Logger.error("DependencyRegistry: Cannot resolve dependency for type {}", paramClass.getName());
            }
            return dependency;
        } catch (Exception e) {
            Logger.warn("DependencyRegistry: Unable to resolve parameter of type {}: {}",
                    paramClass.getName(), e.getMessage());
            return null;
        }
    }
}
