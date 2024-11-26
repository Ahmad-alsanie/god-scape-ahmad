package com.godscape.system.managers;

import com.godscape.osrs.managers.OsrsCacheManager;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.rs3.managers.Rs3CacheManager;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import lombok.Data;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Data
public class CacheManager {

    private static volatile CacheManager instance;
    private final BaseBackupManager baseBackupManager;
    private final OsrsCacheManager osrsCacheManager;
    private final Rs3CacheManager rs3CacheManager;

    private CacheManager() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.baseBackupManager = dependencyFactory.getInjection(BaseBackupManager.class);
        this.osrsCacheManager = dependencyFactory.getInjection(OsrsCacheManager.class);
        this.rs3CacheManager = dependencyFactory.getInjection(Rs3CacheManager.class);
        Logger.info("CacheManager: Initialization complete with injected dependencies.");
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    Logger.info("CacheManager: Creating Singleton instance...");
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    public void updateProfileInCache(Object profile) {
        GameVersion gameVersion = getGameVersion(profile);
        if (gameVersion == GameVersion.OSRS) {
            osrsCacheManager.updateProfileInCache((OsrsProfileSchema) profile);
        } else {
            rs3CacheManager.updateProfileInCache((Rs3ProfileSchema) profile);
        }
    }

    public Map<UUID, ?> getCachedProfiles(GameVersion gameVersion) {
        return gameVersion == GameVersion.OSRS
                ? osrsCacheManager.getCachedProfiles()
                : rs3CacheManager.getCachedProfiles();
    }

    public Object getProfile(GameVersion gameVersion, UUID profileId) {
        return gameVersion == GameVersion.OSRS
                ? osrsCacheManager.getProfile(profileId)
                : rs3CacheManager.getProfile(profileId);
    }

    public void addProfilesToCache(GameVersion gameVersion, Collection<?> profiles) {
        if (gameVersion == GameVersion.OSRS) {
            osrsCacheManager.addProfilesToCache((Collection<OsrsProfileSchema>) profiles);
        } else {
            rs3CacheManager.addProfilesToCache((Collection<Rs3ProfileSchema>) profiles);
        }
    }

    public void removeProfile(GameVersion gameVersion, UUID profileId) {
        if (gameVersion == GameVersion.OSRS) {
            osrsCacheManager.removeProfile(profileId);
        } else {
            rs3CacheManager.removeProfile(profileId);
        }
    }

    public void clearCache(GameVersion gameVersion) {
        if (gameVersion == GameVersion.OSRS) {
            osrsCacheManager.clearCache();
        } else {
            rs3CacheManager.clearCache();
        }
    }

    public void shutdownCacheSystem() {
        osrsCacheManager.shutdownCacheManager();
        rs3CacheManager.shutdownCacheManager();
        Logger.info("CacheManager: Cache system shutdown complete.");
    }

    private GameVersion getGameVersion(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            return GameVersion.OSRS;
        } else if (profile instanceof Rs3ProfileSchema) {
            return GameVersion.RS3;
        }
        throw new IllegalArgumentException("Unsupported profile type: " + profile.getClass().getSimpleName());
    }
}
