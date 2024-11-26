package com.godscape.system.annotations;

import com.godscape.system.schemas.BaseSchema;

import java.lang.annotation.*;

/**
 * Annotation to specify schema details for fields, including schema type, SQL column name, and optional category.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SchemaField {
    /**
     * Specifies the schema associated with this field.
     * If schema is unspecified, set this to null during processing.
     */
    Class<? extends BaseSchema> schema();

    /**
     * Defines the corresponding SQL column name for this field.
     */
    String sqlColumn();

    /**
     * Optional category to further organize fields under this schema.
     */
    String category() default "";
}
