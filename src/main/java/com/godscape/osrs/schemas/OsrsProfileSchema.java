package com.godscape.osrs.schemas;

import com.godscape.osrs.enums.core.OsrsSchemas;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.validation.MapStructure;
import com.godscape.system.utility.validation.Normalization;
import com.godscape.system.factories.SerializableFactory;
import com.godscape.system.interfaces.mark.Cacheable;
import com.godscape.system.interfaces.mark.Saveable;
import com.godscape.system.interfaces.mark.Schemable;
import com.godscape.system.schemas.BaseSchema;
import com.godscape.system.utility.generators.BaseKeyGenerator;
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
public class OsrsProfileSchema implements IdentifiedDataSerializable, BaseSchema, Schemable, Saveable, Cacheable {

    private UUID profileId;
    private String profileName;
    private Map<String, Object> settingsMap = new HashMap<>();  // Always initialized
    private long lastUpdated = System.currentTimeMillis();

    private final BaseKeyGenerator baseKeyGenerator = DependencyFactory.getInstance().getInjection(BaseKeyGenerator.class);

    public OsrsProfileSchema() {}

    public OsrsProfileSchema(UUID profileId, String profileName, Map<String, Object> settingsMap, long lastUpdated) {
        this.profileId = profileId;
        this.profileName = profileName;
        this.settingsMap = settingsMap != null ? filterReservedFields(settingsMap) : new HashMap<>();
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getCacheName() {
        return OsrsSchemas.OSRS_PROFILE_SCHEMA.getCacheName();
    }

    @Override
    public BaseSchema getSchema() {
        return OsrsSchemas.OSRS_PROFILE_SCHEMA.getSchema();
    }

    @Override
    public OsrsProfileSchema getSchemaInstance() {
        return new OsrsProfileSchema();
    }

    public Map<String, Integer> getSkillMap() {
        return getSetting("osrs_stats_skill_goals_panel", new HashMap<>());
    }

    public void setSkillMap(Map<String, Integer> skillMap) {
        if (skillMap != null) {
            Map<String, Integer> normalizedSkillMap = new HashMap<>();
            skillMap.forEach((skillName, level) ->
                    normalizedSkillMap.put(skillName, Normalization.clampSkillLevel(level, skillName))
            );
            setSetting("osrs_stats_skill_goals_panel", normalizedSkillMap);
        } else {
            setSetting("osrs_stats_skill_goals_panel", new HashMap<>());
        }
    }

    private Map<String, Object> filterReservedFields(Map<String, Object> originalMap) {
        Map<String, Object> filteredMap = new HashMap<>(originalMap);
        filteredMap.remove("profileId");
        filteredMap.remove("profileName");
        filteredMap.remove("lastUpdated");
        return filteredMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T getSetting(String panelPath, String key, T defaultValue) {
        Map<String, Object> nestedMap = MapStructure.unflattenMap(settingsMap);
        Map<String, Object> categoryMap = (Map<String, Object>) nestedMap.get(panelPath);
        if (categoryMap != null) {
            Object value = categoryMap.get(key);
            if (defaultValue != null && defaultValue.getClass().isInstance(value)) {
                return (T) value;
            }
        }
        return defaultValue;
    }

    @SuppressWarnings("unchecked")
    public <T> void setSetting(String panelPath, String key, T value) {
        Map<String, Object> nestedMap = MapStructure.unflattenMap(settingsMap);
        Map<String, Object> categoryMap = (Map<String, Object>) nestedMap.computeIfAbsent(panelPath, k -> new LinkedHashMap<>());

        if (!isReservedField(key)) {
            if (value instanceof Integer && "skill_goals".equalsIgnoreCase(key)) {
                value = (T) Integer.valueOf(Normalization.clampSkillLevel((Integer) value, key));
            }
            categoryMap.put(key, value);
        }

        // After modifying the nested map, flatten it back to settingsMap
        settingsMap = filterReservedFields(MapStructure.flattenMap(nestedMap, ""));
    }

    public <T> void setSetting(String key, T value) {
        if (!isReservedField(key)) {
            settingsMap.put(key, value);
        }
    }

    public <T> T getSetting(String key, T defaultValue) {
        Object value = settingsMap.get(key);
        if (defaultValue != null && defaultValue.getClass().isInstance(value)) {
            return (T) value;
        }
        return defaultValue;
    }

    private boolean isReservedField(String key) {
        return "profileId".equalsIgnoreCase(key) || "profileName".equalsIgnoreCase(key) || "lastUpdated".equalsIgnoreCase(key);
    }

    @Override
    public Map<String, Object> extractFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("profileId", profileId != null ? profileId.toString() : null);
        fields.put("profileName", profileName);
        fields.put("lastUpdated", lastUpdated);
        return fields;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeString(profileId != null ? profileId.toString() : "");
        out.writeString(profileName != null ? profileName : "");
        out.writeLong(lastUpdated);
        out.writeObject(filterReservedFields(settingsMap));  // Filter reserved fields on write
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        String profileIdStr = in.readString();
        this.profileId = !profileIdStr.isEmpty() ? UUID.fromString(profileIdStr) : null;
        this.profileName = in.readString();
        this.lastUpdated = in.readLong();
        this.settingsMap = filterReservedFields(in.readObject());
        if (this.settingsMap == null) {  // Ensure settingsMap is initialized on read
            this.settingsMap = new HashMap<>();
        }
    }

    @Override
    public int getFactoryId() {
        return SerializableFactory.FACTORY_ID;
    }

    @Override
    public int getClassId() {
        return SerializableFactory.OSRS_PROFILE_SCHEMA_ID;
    }

    public static Map<String, String> getColumnDefinitions() {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("profile_id", "UUID PRIMARY KEY"); // Ensure profile_id is a UUID type, or text if needed.
        columns.put("profile_name", "VARCHAR(255)");
        columns.put("last_updated", "BIGINT");
        columns.put("settings_map", "TEXT"); // Assuming settings_map is serialized as JSON.
        return columns;
    }

    public void updateSkillGoal(String skillName, int value) {
        Map<String, Integer> skillGoals = getSkillMap();
        skillGoals.put(skillName, Normalization.clampSkillLevel(value, skillName));
        setSkillMap(skillGoals);  // This will re-flatten the map properly
    }




}
