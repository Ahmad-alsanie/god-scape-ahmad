package com.godscape.system.utility.builders;

import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.enums.ConfigKeys;
import com.godscape.system.utility.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

/**
 * FileSystemBuilder is responsible for setting up the necessary file system directories
 * based on configurations defined in GodscapeConfig.
 */
public class FileSystemBuilder {

    private final GodscapeConfig config;

    /**
     * Constructor initializes the GodscapeConfig instance.
     */
    public FileSystemBuilder() {
        this.config = new GodscapeConfig();
    }

    /**
     * Creates the necessary system directories based on the configurations.
     * Files will be created individually as accessed.
     */
    private void createInitialDirectories() {
        Logger.info("FileSystemBuilder: Starting initial directory creation process.");

        // List of directories to create
        List<String> directories = Arrays.asList(
                config.getString(ConfigKeys.SAVE_DIRECTORY),
                config.getString(ConfigKeys.OSRS_SAVE_DIRECTORY),
                config.getString(ConfigKeys.OSRS_LOG_FILE_DIRECTORY),
                config.getString(ConfigKeys.RS3_SAVE_DIRECTORY),
                config.getString(ConfigKeys.RS3_LOG_FILE_DIRECTORY)
        );

        // Create directories if they don't already exist
        for (String dirPath : directories) {
            createDirectoryIfMissing(dirPath);
        }

        Logger.info("FileSystemBuilder: Initial directory creation process completed.");
    }

    /**
     * Creates a directory if it does not already exist.
     *
     * @param dirPath The path of the directory to create.
     */
    public void createDirectoryIfMissing(String dirPath) {
        if (dirPath == null || dirPath.isEmpty()) {
            Logger.warn("FileSystemBuilder: Received null or empty directory path. Skipping creation.");
            return;
        }

        Path path = Paths.get(dirPath);
        try {
            if (Files.exists(path)) {
                if (Files.isDirectory(path)) {
                    Logger.debug("FileSystemBuilder: Directory '{}' already exists.", dirPath);
                } else {
                    Logger.warn("FileSystemBuilder: Path '{}' exists but is not a directory.", dirPath);
                }
            } else {
                Files.createDirectories(path);
                Logger.info("FileSystemBuilder: Directory '{}' created successfully.", dirPath);
            }
        } catch (IOException e) {
            Logger.error("FileSystemBuilder: Failed to create directory '{}': {}", dirPath, e.getMessage(), e);
        }
    }

    /**
     * Creates a file if it does not already exist, and if 'CREATE_MISSING_FILES' is enabled in the configuration.
     *
     * @param filePath The path of the file to create.
     */
    public void createFileIfMissing(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            Logger.warn("FileSystemBuilder: Received null or empty file path. Skipping creation.");
            return;
        }

        boolean createMissingFiles = config.getBoolean(ConfigKeys.CREATE_MISSING_FILES, true);
        if (!createMissingFiles) {
            Logger.info("FileSystemBuilder: Skipping file creation for '{}' as per configuration.", filePath);
            return;
        }

        Path path = Paths.get(filePath);
        try {
            if (Files.exists(path)) {
                if (Files.isRegularFile(path)) {
                    Logger.debug("FileSystemBuilder: File '{}' already exists.", filePath);
                } else {
                    Logger.warn("FileSystemBuilder: Path '{}' exists but is not a regular file.", filePath);
                }
            } else {
                // Ensure parent directories exist
                Path parentDir = path.getParent();
                if (parentDir != null && !Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                    Logger.debug("FileSystemBuilder: Parent directories for file '{}' created.", filePath);
                }

                Files.createFile(path);
                Logger.info("FileSystemBuilder: File '{}' created successfully.", filePath);
            }
        } catch (IOException e) {
            Logger.error("FileSystemBuilder: Failed to create file '{}': {}", filePath, e.getMessage(), e);
        }
    }

    /**
     * Creates all required system directories and files if they don't exist.
     * Useful for initializing the complete file structure.
     */
    public void createFullFileSystem() {
        Logger.info("FileSystemBuilder: Starting full file system creation process.");

        List<String> directories = Arrays.asList(
                config.getString(ConfigKeys.SAVE_DIRECTORY),
                config.getString(ConfigKeys.OSRS_SAVE_DIRECTORY),
                config.getString(ConfigKeys.OSRS_LOG_FILE_DIRECTORY),
                config.getString(ConfigKeys.RS3_SAVE_DIRECTORY),
                config.getString(ConfigKeys.RS3_LOG_FILE_DIRECTORY)
        );

        List<String> files = Arrays.asList(
                config.getString(ConfigKeys.OSRS_DATABASE_FILENAME),
                config.getString(ConfigKeys.OSRS_PROFILES_JSON),
                config.getString(ConfigKeys.OSRS_PROFILES_XML),
                config.getString(ConfigKeys.RS3_DATABASE_FILENAME),
                config.getString(ConfigKeys.RS3_PROFILES_JSON),
                config.getString(ConfigKeys.RS3_PROFILES_XML)
        );

        for (String dirPath : directories) {
            createDirectoryIfMissing(dirPath);
        }

        for (String filePath : files) {
            createFileIfMissing(filePath);
        }

        Logger.info("FileSystemBuilder: Full file system creation process completed.");
    }
}
