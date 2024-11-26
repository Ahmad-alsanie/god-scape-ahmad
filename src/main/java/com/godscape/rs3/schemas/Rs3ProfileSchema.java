package com.godscape.rs3.schemas;

import com.godscape.rs3.enums.core.Rs3Schemas;
import com.godscape.system.annotations.SchemaField;
import com.godscape.system.interfaces.mark.Saveable;
import com.godscape.system.schemas.BaseSchema;
import com.godscape.system.factories.SerializableFactory;
import com.godscape.system.utility.validation.MapStructure;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Rs3ProfileSchema implements IdentifiedDataSerializable, BaseSchema, Saveable {

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "profile_id")
    private UUID profileId;

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "profile_name")
    private String profileName;

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "membership_status")
    private Boolean membershipStatus;

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "mode")
    private String mode;

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "playstyle")
    private String playstyle;

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "autoprofiler")
    private Boolean autoprofiler;

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "settings_map")
    private Map<String, Object> settingsMap = new HashMap<>();

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "profile_notes")
    private String profileNotes;

    @SchemaField(schema = Rs3ProfileSchema.class, sqlColumn = "last_updated")
    private long lastUpdated = System.currentTimeMillis();

    private final String cacheName = Rs3Schemas.RS3_PROFILE_SCHEMA.toString();

    public Rs3ProfileSchema() {}

    public Rs3ProfileSchema(UUID profileId, String profileName, Map<String, Object> settingsMap, String profileNotes, long lastUpdated) {
        this.profileId = profileId;
        this.profileName = profileName;
        this.settingsMap = settingsMap;
        this.profileNotes = profileNotes;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getCacheName() {
        return cacheName;
    }

    @Override
    public Rs3ProfileSchema getSchemaInstance() {
        return new Rs3ProfileSchema();
    }

    /**
     * Retrieves a setting with nested access, unflattening the settings map if needed.
     */
    public <T> T getSetting(String category, String key, T defaultValue) {
        Object categoryMap = settingsMap.get(category);
        if (categoryMap instanceof Map) {
            Object value = ((Map<String, Object>) categoryMap).getOrDefault(key, defaultValue);
            if (value != null && defaultValue.getClass().isInstance(value)) {
                return (T) value;
            }
        }
        return defaultValue;
    }

    /**
     * Sets a setting with nested access.
     */
    public void setSetting(String category, String key, Object value) {
        Map<String, Object> categoryMap = (Map<String, Object>) settingsMap.computeIfAbsent(category, k -> new HashMap<>());
        categoryMap.put(key, value);
    }

    /**
     * Column definitions for database schema.
     */
    public static Map<String, String> getColumnDefinitions() {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("profile_id", "UUID");
        columns.put("profile_name", "VARCHAR(255)");
        columns.put("membership_status", "BOOLEAN");
        columns.put("mode", "VARCHAR(255)");
        columns.put("playstyle", "VARCHAR(255)");
        columns.put("autoprofiler", "BOOLEAN");
        columns.put("settings_map", "TEXT");
        columns.put("profile_notes", "TEXT");
        columns.put("last_updated", "BIGINT");
        return columns;
    }

    @Override
    public Map<String, Object> extractFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("profileId", profileId != null ? profileId.toString() : null);
        fields.put("profileName", profileName);
        fields.put("membershipStatus", membershipStatus);
        fields.put("mode", mode);
        fields.put("playstyle", playstyle);
        fields.put("autoprofiler", autoprofiler);
        fields.put("profileNotes", profileNotes);
        fields.put("lastUpdated", lastUpdated);

        if (settingsMap != null) {
            fields.putAll(MapStructure.flattenMap(settingsMap, "_"));
        }
        return fields;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeObject(profileId);
        out.writeObject(profileName);
        out.writeObject(membershipStatus);
        out.writeObject(mode);
        out.writeObject(playstyle);
        out.writeObject(autoprofiler);
        out.writeObject(profileNotes);
        out.writeLong(lastUpdated);
        out.writeObject(settingsMap);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        profileId = in.readObject();
        profileName = in.readObject();
        membershipStatus = in.readObject();
        mode = in.readObject();
        playstyle = in.readObject();
        autoprofiler = in.readObject();
        profileNotes = in.readObject();
        lastUpdated = in.readLong();
        settingsMap = in.readObject();
    }

    @Override
    public int getFactoryId() {
        return SerializableFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return SerializableFactory.RS3_PROFILE_SCHEMA_ID;
    }
}
