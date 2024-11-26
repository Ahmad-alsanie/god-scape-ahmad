package com.godscape.system.utility.validation;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.utility.Logger;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class MapStructure {

    private MapStructure() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    public static Map<String, Object> extractFields(Object obj, boolean flatten) {
        Map<String, Object> fields = new LinkedHashMap<>();
        if (obj == null) {
            Logger.warn("MapStructure: Provided object is null, returning empty field map.");
            return fields;
        }

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                fields.put(field.getName().toLowerCase(), value); // Ensure keys are lowercase
            } catch (IllegalAccessException e) {
                Logger.error("MapStructure: Failed to access field '{}': {}", field.getName(), e.getMessage());
            }
        }
        return flatten ? flattenMap(fields, "") : fields;
    }

    public static Map<String, Object> flattenMap(Map<String, Object> map, String parentKey) {
        Map<String, Object> flatMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = parentKey.isEmpty() ? entry.getKey().toLowerCase() : parentKey + "_" + entry.getKey().toLowerCase();
            Object value = entry.getValue();
            if (value instanceof Map) {
                flatMap.putAll(flattenMap((Map<String, Object>) value, key));
            } else {
                flatMap.put(key, value);
            }
        }
        return flatMap;
    }

    public static Map<String, Object> unflattenMap(Map<String, Object> flatMap) {
        Map<String, Object> nestedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : flatMap.entrySet()) {
            String[] keys = entry.getKey().toLowerCase().split("_"); // Normalize to lowercase
            Map<String, Object> currentMap = nestedMap;
            for (int i = 0; i < keys.length - 1; i++) {
                currentMap = (Map<String, Object>) currentMap.computeIfAbsent(keys[i], k -> new LinkedHashMap<>());
            }
            currentMap.put(keys[keys.length - 1], entry.getValue());
        }
        return nestedMap;
    }

    public static boolean isEmptyValue(Object value) {
        if (value == null) return true;
        if (value instanceof String && ((String) value).isEmpty()) return true;
        if (value instanceof Map && ((Map<?, ?>) value).isEmpty()) return true;
        if (value instanceof List && ((List<?>) value).isEmpty()) return true;
        return false;
    }
}
