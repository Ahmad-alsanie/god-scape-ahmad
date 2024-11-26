package com.godscape.osrs.managers;

import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.fuse.ObservationFuse;
import com.godscape.system.interfaces.fuse.SettingsFuse;
import com.godscape.system.interfaces.mark.Observable;
import com.godscape.system.managers.BaseObservationManager;
import com.godscape.system.observers.ProfileChangeObserver;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Singleton
public class OsrsObservationManager extends BaseObservationManager implements Observable, ObservationFuse<Object> {

    private final List<ProfileChangeObserver> profileObservers;

    public OsrsObservationManager() {
        super();
        this.profileObservers = new CopyOnWriteArrayList<>();

        // Inject OsrsSettingsController which implements SettingsFuse
        SettingsFuse settingsController = DependencyFactory.getInstance().getInjection(OsrsSettingsController.class);
        if (settingsController == null) {
            throw new IllegalStateException("OsrsObservationManager: Failed to inject OsrsSettingsController.");
        }

        Logger.info("OsrsObservationManager: Initialized for OSRS profile observations.");
    }

    /**
     * Register a profile observer that implements ProfileChangeObserver.
     */
    public void addProfileObserver(ProfileChangeObserver observer) {
        if (!profileObservers.contains(observer)) {
            profileObservers.add(observer);
            Logger.info("OsrsObservationManager: Registered ProfileChangeObserver.");
        }
    }

    /**
     * Remove a profile observer.
     */
    public void removeProfileObserver(ProfileChangeObserver observer) {
        if (profileObservers.remove(observer)) {
            Logger.info("OsrsObservationManager: Unregistered ProfileChangeObserver.");
        }
    }

    /**
     * Notify observers of a profile addition.
     */
    @Override
    public void notifyProfileAdded(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            for (ProfileChangeObserver observer : profileObservers) {
                try {
                    observer.updateSettings("PROFILE_ADDED", profile);
                } catch (Exception e) {
                    Logger.error("OsrsObservationManager: Failed to notify observer: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * Notify observers of a profile update.
     */
    @Override
    public void notifyProfileUpdated(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            for (ProfileChangeObserver observer : profileObservers) {
                try {
                    observer.updateSettings("PROFILE_UPDATED", profile);
                } catch (Exception e) {
                    Logger.error("OsrsObservationManager: Failed to notify observer: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * Notify observers of a profile removal.
     */
    @Override
    public void notifyProfileRemoved(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            for (ProfileChangeObserver observer : profileObservers) {
                try {
                    observer.updateSettings("PROFILE_REMOVED", profile);
                } catch (Exception e) {
                    Logger.error("OsrsObservationManager: Failed to notify observer: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * Trigger a notification to update observers on demand with a specific event type.
     */
    public void notifyChange(String eventType, Object data) {
        for (ProfileChangeObserver observer : profileObservers) {
            try {
                observer.updateSettings(eventType, data);
            } catch (Exception e) {
                Logger.error("OsrsObservationManager: Failed to notify observer: {}", e.getMessage());
            }
        }
    }
}
