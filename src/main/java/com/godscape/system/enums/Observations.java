package com.godscape.system.enums;

import com.godscape.system.observers.*;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Enum for managing observer classes with lazy loading and compile-time safety.
 */
@Getter
public enum Observations {

    // Observers with no arguments
    PROFILE_CHANGE_LISTENER(ProfileChangeListener.class, () -> DependencyFactory.getInstance().getInjection(ProfileChangeListener.class)),
    THEME_CHANGE_LISTENER(ThemeChangeListener.class, () -> DependencyFactory.getInstance().getInjection(ThemeChangeListener.class)),
    BOT_STATE_LISTENER(BotStateListener.class, () -> DependencyFactory.getInstance().getInjection(BotStateListener.class));

    // Special supplier for MultiListener with parameters
    private static final Function<MultiListenerParams, AdaptiveListener> MULTI_LISTENER_SUPPLIER =
            params -> new AdaptiveListener(params.category, params.key, params.profile);

    // Class type for each observer
    private final Class<?> clazz;
    private final Supplier<?> supplier;

    // Constructor for no-argument suppliers
    Observations(Class<?> clazz, Supplier<?> supplier) {
        this.clazz = clazz;
        this.supplier = supplier;
    }

    // Method to get an instance of MultiListener with specific parameters
    public static AdaptiveListener getMultiListener(String category, String key, OsrsProfileSchema profile) {
        return MULTI_LISTENER_SUPPLIER.apply(new MultiListenerParams(category, key, profile));
    }

    @Override
    public String toString() {
        return this.name();
    }

    // Inner class to hold parameters for MultiListener
    private static class MultiListenerParams {
        private final String category;
        private final String key;
        private final OsrsProfileSchema profile;

        public MultiListenerParams(String category, String key, OsrsProfileSchema profile) {
            this.category = category;
            this.key = key;
            this.profile = profile;
        }
    }
}
