package com.godscape.system.annotations;

import com.godscape.system.annotations.SchemaField;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHelper {

    // Cache to improve performance by avoiding reflection every time
    private static final Map<Class<?>, Map<String, String>> columnCache = new HashMap<>();

    public static String getSqlColumnName(Class<?> clazz, String fieldName) {
        Map<String, String> columnMap = columnCache.computeIfAbsent(clazz, AnnotationHelper::mapSqlColumns);
        return columnMap.getOrDefault(fieldName, fieldName); // Return field name if no annotation found
    }

    private static Map<String, String> mapSqlColumns(Class<?> clazz) {
        Map<String, String> columnMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            SchemaField annotation = field.getAnnotation(SchemaField.class);
            if (annotation != null) {
                columnMap.put(field.getName(), annotation.sqlColumn());
            }
        }
        return columnMap;
    }
}
