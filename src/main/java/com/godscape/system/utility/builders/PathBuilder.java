package com.godscape.system.utility.builders;

import com.godscape.system.config.GodscapeConfig;
import com.godscape.system.enums.ConfigKeys;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathBuilder {

    private static volatile PathBuilder instance;
    private final GodscapeConfig config;

    private PathBuilder(GodscapeConfig config) {
        this.config = config;
    }

    public static PathBuilder getInjection() {
        if (instance == null) {
            synchronized (PathBuilder.class) {
                if (instance == null) {
                    DependencyFactory dependencyFactory = DependencyFactory.getInstance();
                    GodscapeConfig config = dependencyFactory.getInjection(GodscapeConfig.class);
                    instance = new PathBuilder(config);
                }
            }
        }
        return instance;
    }

    public Path buildPath(ConfigKeys key) {
        String baseDirectory = config.getString(ConfigKeys.SAVE_DIRECTORY);
        return sanitizePath(createPath(baseDirectory, key.getSubpath()));
    }

    public Path buildPathForGameSpecificConfig(ConfigKeys key) {
        GameVersion currentGameVersion = DependencyFactory.getInstance().getInjection(PlatformFactory.class).getCurrentGameVersion();
        String gameSpecificPath = currentGameVersion == GameVersion.OSRS
                ? config.getString(ConfigKeys.OSRS_DATABASE_FILENAME)
                : config.getString(ConfigKeys.RS3_DATABASE_FILENAME);

        return sanitizePath(createPath(config.getString(ConfigKeys.SAVE_DIRECTORY), gameSpecificPath));
    }

    private Path createPath(String basePath, String... paths) {
        return FileSystems.getDefault().getPath(basePath, paths).toAbsolutePath().normalize();
    }

    private Path sanitizePath(Path path) {
        String sanitized = path.toString().replaceAll("[\\r\\n]+", "").trim();
        return Paths.get(sanitized).toAbsolutePath().normalize();
    }
}
