package com.godscape.system.annotations;

import java.lang.annotation.*;

/**
 * Indicates that a class should be treated as a singleton.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Singleton {
}
