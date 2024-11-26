package com.godscape.rs3.schemas;

import com.godscape.rs3.enums.core.Rs3Schemas;
import com.godscape.system.schemas.BaseSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Rs3CharacterSchema implements Serializable, BaseSchema {

    private static final long serialVersionUID = 1L;

    // Basic Information
    private String characterName;
    private boolean isMember;
    private int combatLevel;

    // Inventory example
    private final Map<String, Integer> inventoryItems = new ConcurrentHashMap<>();

    // Implementing extractFields method as required by BaseSchema
    @Override
    public Map<String, Object> extractFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("characterName", characterName);
        fields.put("isMember", isMember);
        fields.put("combatLevel", combatLevel);
        fields.put("inventoryItems", new LinkedHashMap<>(inventoryItems)); // Create a copy to prevent external modifications
        return fields;
    }

    @Override
    public String getCacheName() {
        return Rs3Schemas.RS3_CHARACTER_SCHEMA.toString(); // Fetch from an enum or other source if needed
    }


    // Implementing getSchemaInstance method as required by BaseSchema
    @Override
    public Rs3CharacterSchema getSchemaInstance() {
        return new Rs3CharacterSchema();
    }
}
