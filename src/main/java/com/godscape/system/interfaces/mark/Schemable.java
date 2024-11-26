package com.godscape.system.interfaces.mark;

import com.godscape.system.schemas.BaseSchema;

public interface Schemable {

    /**
     * Retrieves the associated schema.
     *
     * @return The schema instance, which can be an OSRS or RS3 schema.
     */
    BaseSchema getSchema();
}
