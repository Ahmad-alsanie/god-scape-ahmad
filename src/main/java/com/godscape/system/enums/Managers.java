package com.godscape.system.enums;

import com.godscape.osrs.enums.core.OsrsManagers;
import com.godscape.rs3.enums.core.Rs3Managers;
import com.godscape.system.managers.*;
import com.godscape.system.factories.DependencyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum Managers {

    CHARACTER_MANAGER(CharacterManager.class, () -> DependencyFactory.getInstance().getInjection(CharacterManager.class)),
    PROFILE_MANAGER(ProfileManager.class, () -> DependencyFactory.getInstance().getInjection(ProfileManager.class)),
    PRELOAD_MANAGER(PreloadManager.class, () -> DependencyFactory.getInstance().getInjection(PreloadManager.class)),
    PLATFORM_MANAGER(PlatformManager.class, () -> DependencyFactory.getInstance().getInjection(PlatformManager.class)),
    CACHE_MANAGER(CacheManager.class, () -> DependencyFactory.getInstance().getInjection(CacheManager.class)),
    OBSERVATION_MANAGER(BaseObservationManager.class, () -> DependencyFactory.getInstance().getInjection(BaseObservationManager.class)),

    OSRS_MANAGERS(OsrsManagers.class, () -> null),
    RS3_MANAGERS(Rs3Managers.class, () -> null);

    private final Class<?> clazz;
    private final Supplier<?> supplier;

    @Override
    public String toString() {
        return this.name();
    }

    @SuppressWarnings("unchecked")
    public <T> T getManager() {
        return (T) supplier.get();
    }
}
