package com.godscape.system.modules.database.queries;

import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.mark.Schemable;
import com.godscape.system.modules.database.DatabaseConnectionManager;
import com.godscape.system.utility.validation.MapStructure;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.builders.FileSystemBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

public abstract class BaseProfileDAO<T, S extends Enum<S> & Schemable> {

    protected final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    protected final Supplier<S> schemaSupplier;
    protected final Supplier<Map<String, String>> columnDefinitionsSupplier;
    private final FileSystemBuilder fileSystemBuilder;

    protected BaseProfileDAO(Supplier<S> schemaSupplier, Supplier<Map<String, String>> columnDefinitionsSupplier) {
        this.schemaSupplier = schemaSupplier;
        this.columnDefinitionsSupplier = columnDefinitionsSupplier;
        this.fileSystemBuilder = DependencyFactory.getInstance().getInjection(FileSystemBuilder.class);
    }

    public void initializeSchema(Connection connection) {
        S schemaEnum = schemaSupplier.get();
        String tableName = schemaEnum.name().toLowerCase();
        Map<String, String> columns = columnDefinitionsSupplier.get();

        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" (profileId TEXT PRIMARY KEY");

        columns.forEach((name, type) -> createTableSQL.append(", ").append(name).append(" ").append(type));
        createTableSQL.append(");");

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL.toString());
            Logger.info("BaseProfileDAO: Initialized table '{}'.", tableName);
        } catch (SQLException e) {
            Logger.error("BaseProfileDAO: Failed to create table '{}' - {}", tableName, e.getMessage());
        }
    }

    public void updateProfilesInDatabase(List<T> profiles) {
        if (profiles == null || profiles.isEmpty()) {
            Logger.info("BaseProfileDAO: No profiles to update in the database.");
            return;
        }

        Set<String> fieldNames = collectFieldNames(profiles);
        String upsertQuery = generateDynamicUpsertQuery(fieldNames);

        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement upsertStmt = conn.prepareStatement(upsertQuery)) {
                for (T profile : profiles) {
                    prepareStatementForUpsert(upsertStmt, profile, fieldNames);
                    upsertStmt.addBatch();
                }
                upsertStmt.executeBatch();
                conn.commit();
                Logger.info("BaseProfileDAO: Successfully updated profiles in the database for table '{}'.", schemaSupplier.get().name().toLowerCase());
            } catch (SQLException e) {
                conn.rollback();
                Logger.error("BaseProfileDAO: Error executing upsert for table '{}'. Rolling back transaction.", schemaSupplier.get().name().toLowerCase(), e);
            }
        } catch (SQLException e) {
            Logger.error("BaseProfileDAO: Error updating profiles in the database for table '{}': {}", schemaSupplier.get().name().toLowerCase(), e.getMessage());
        }
    }

    public List<T> loadAllProfilesFromDatabase(Class<T> schemaClass) {
        List<T> profiles = new ArrayList<>();
        String query = "SELECT * FROM " + schemaSupplier.get().name().toLowerCase();

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                T profile = schemaClass.getDeclaredConstructor().newInstance();
                populateProfileFromResultSet(rs, profile);
                profiles.add(profile);
            }
            Logger.info("BaseProfileDAO: Loaded {} profiles from '{}' table.", profiles.size(), schemaSupplier.get().name().toLowerCase());
        } catch (Exception e) {
            Logger.error("BaseProfileDAO: Error loading profiles from '{}' database: {}", schemaSupplier.get().name().toLowerCase(), e.getMessage());
        }
        return profiles;
    }

    public void deleteProfileFromDatabase(UUID profileId) {
        String tableName = schemaSupplier.get().name().toLowerCase();
        String deleteQuery = "DELETE FROM " + tableName + " WHERE profileId = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

            deleteStmt.setString(1, profileId.toString());
            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                Logger.info("BaseProfileDAO: Successfully deleted profile '{}' from '{}' database.", profileId, tableName);
            } else {
                Logger.warn("BaseProfileDAO: Profile '{}' not found in '{}' database for deletion.", profileId, tableName);
            }
        } catch (SQLException e) {
            Logger.error("BaseProfileDAO: Error deleting profile '{}' from '{}' database: {}", profileId, schemaSupplier.get().name().toLowerCase(), e.getMessage());
        }
    }

    public boolean profileExists(UUID profileId) {
        String tableName = schemaSupplier.get().name().toLowerCase();
        String query = "SELECT 1 FROM " + tableName + " WHERE profileId = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, profileId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            Logger.error("BaseProfileDAO: Error checking existence of profile '{}' in '{}': {}", profileId, tableName, e.getMessage());
            return false;
        }
    }

    public T getProfileById(UUID profileId, Class<T> schemaClass) {
        String tableName = schemaSupplier.get().name().toLowerCase();
        String query = "SELECT * FROM " + tableName + " WHERE profileId = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, profileId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T profile = schemaClass.getDeclaredConstructor().newInstance();
                    populateProfileFromResultSet(rs, profile);
                    Logger.info("BaseProfileDAO: Retrieved profile '{}' from '{}'.", profileId, tableName);
                    return profile;
                }
            }
        } catch (Exception e) {
            Logger.error("BaseProfileDAO: Error retrieving profile '{}' from '{}': {}", profileId, tableName, e.getMessage());
        }
        return null;
    }

    private Set<String> collectFieldNames(List<T> profiles) {
        Set<String> fieldNames = new LinkedHashSet<>();
        for (T profile : profiles) {
            fieldNames.addAll(MapStructure.extractFields(profile, false).keySet());
        }
        return fieldNames;
    }

    private String generateDynamicUpsertQuery(Set<String> fieldNames) {
        String tableName = schemaSupplier.get().name().toLowerCase();
        StringBuilder query = new StringBuilder("INSERT INTO ")
                .append(tableName)
                .append(" (profileId");

        for (String fieldName : fieldNames) {
            if (!fieldName.equals("profileId")) {
                query.append(", ").append(fieldName);
            }
        }
        query.append(") VALUES (?");

        for (int i = 1; i < fieldNames.size(); i++) {
            query.append(", ?");
        }
        query.append(") ON CONFLICT(profileId) DO UPDATE SET ");

        Iterator<String> iterator = fieldNames.iterator();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            if (!fieldName.equals("profileId")) {
                query.append(fieldName).append("=excluded.").append(fieldName);
                if (iterator.hasNext()) {
                    query.append(", ");
                }
            }
        }
        query.append(";");

        return query.toString();
    }

    private void prepareStatementForUpsert(PreparedStatement stmt, T profile, Set<String> fieldNames) throws SQLException {
        int index = 1;
        Map<String, Object> fields = MapStructure.extractFields(profile, false);

        Object profileId = fields.get("profileId");
        stmt.setObject(index++, profileId != null ? profileId.toString() : null);

        for (String fieldName : fieldNames) {
            if (fieldName.equals("profileId")) {
                continue;
            }
            Object value = fields.getOrDefault(fieldName, null);
            if (value instanceof Boolean) {
                value = (Boolean) value ? 1 : 0;
            } else if (value instanceof Map || value instanceof List) {
                value = gson.toJson(value);
            } else if (value instanceof Enum) {
                value = value.toString();
            }
            stmt.setObject(index++, value);
        }
    }

    private void populateProfileFromResultSet(ResultSet rs, T profile) throws SQLException {
        Map<String, Object> fields = MapStructure.extractFields(profile, false);
        for (String fieldName : fields.keySet()) {
            try {
                Field field = profile.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = rs.getObject(fieldName);

                if (value != null) {
                    if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        field.set(profile, ((Integer) value) != 0);
                    } else if (field.getType() == Map.class || field.getType() == List.class) {
                        String json = rs.getString(fieldName);
                        if (json != null) {
                            field.set(profile, gson.fromJson(json, field.getType()));
                        }
                    } else if (Enum.class.isAssignableFrom(field.getType())) {
                        @SuppressWarnings("unchecked")
                        Class<? extends Enum> enumType = (Class<? extends Enum>) field.getType();
                        field.set(profile, Enum.valueOf(enumType, value.toString()));
                    } else if (field.getType() == UUID.class) {
                        field.set(profile, UUID.fromString(value.toString()));
                    } else {
                        field.set(profile, value);
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                Logger.warn("BaseProfileDAO: Unable to set field '{}' in profile: {}", fieldName, e.getMessage());
            }
        }
    }
}
