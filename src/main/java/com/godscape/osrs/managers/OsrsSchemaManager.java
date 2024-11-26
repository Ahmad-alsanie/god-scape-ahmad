package com.godscape.osrs.managers;

import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.interfaces.mark.Schemable;
import com.godscape.system.schemas.BaseSchema;

public class OsrsSchemaManager implements Schemable {
    private final OsrsProfileSchema schema;

    public OsrsSchemaManager() {
        this.schema = new OsrsProfileSchema();
    }

    @Override
    public BaseSchema getSchema() {
        return schema;
    }

    // Additional manager-specific methods can be added here
}
