package com.godscape.system.annotations;

import java.lang.annotation.*;

/**
 * Marks a class as a component to be managed by the DI system.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}
