package com.godscape.rs3.managers;

import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.interfaces.mark.Schemable;
import com.godscape.system.schemas.BaseSchema;

public class Rs3SchemaManager implements Schemable {
    private final Rs3ProfileSchema schema;

    public Rs3SchemaManager() {
        this.schema = new Rs3ProfileSchema();
    }

    @Override
    public BaseSchema getSchema() {
        return schema;
    }

    // Additional manager-specific methods can be added here
}
