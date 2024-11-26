package com.godscape.rs3.managers;

import com.godscape.rs3.observations.Rs3ProfileChangeObservation;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.interfaces.fuse.ObservationFuse;
import com.godscape.system.interfaces.mark.Observable;
import com.godscape.system.managers.BaseObservationManager;
import com.godscape.system.utility.Logger;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class Rs3ObservationManager extends BaseObservationManager implements Observable, ObservationFuse<Object> {

    private final List<Rs3ProfileChangeObservation> profileObservers;

    private Rs3ObservationManager() {
        super();
        this.profileObservers = new ArrayList<>();
        Logger.info("Rs3ObservationManager: Initialized for RS3 profile observations.");
    }

    /**
     * Register a profile observer that implements Rs3ProfileChangeObservation.
     */
    public void addProfileObserver(Rs3ProfileChangeObservation observer) {
        if (!profileObservers.contains(observer)) {
            profileObservers.add(observer);
            Logger.info("Rs3ObservationManager: Registered Rs3ProfileChangeObservation.");
        }
    }

    /**
     * Remove a profile observer.
     */
    public void removeProfileObserver(Rs3ProfileChangeObservation observer) {
        if (profileObservers.remove(observer)) {
            Logger.info("Rs3ObservationManager: Unregistered Rs3ProfileChangeObservation.");
        }
    }

    /**
     * Notify observers of a profile addition.
     */
    @Override
    public void notifyProfileAdded(Object profile) {
        if (profile instanceof Rs3ProfileSchema) {
            for (Rs3ProfileChangeObservation observer : profileObservers) {
                observer.onProfileAdded((Rs3ProfileSchema) profile);
            }
        }
    }

    /**
     * Notify observers of a profile update.
     */
    @Override
    public void notifyProfileUpdated(Object profile) {
        if (profile instanceof Rs3ProfileSchema) {
            for (Rs3ProfileChangeObservation observer : profileObservers) {
                observer.onProfileUpdated((Rs3ProfileSchema) profile);
            }
        }
    }

    /**
     * Notify observers of a profile removal.
     */
    @Override
    public void notifyProfileRemoved(Object profile) {
        if (profile instanceof Rs3ProfileSchema) {
            for (Rs3ProfileChangeObservation observer : profileObservers) {
                observer.onProfileRemoved((Rs3ProfileSchema) profile);
            }
        }
    }

    /**
     * Trigger a notification to update observers on demand with a specific event type.
     */
    public void notifyChange(String eventType, Object data) {
        if (data instanceof Rs3ProfileSchema) {
            switch (eventType) {
                case "PROFILE_ADDED":
                    notifyProfileAdded(data);
                    break;
                case "PROFILE_UPDATED":
                    notifyProfileUpdated(data);
                    break;
                case "PROFILE_REMOVED":
                    notifyProfileRemoved(data);
                    break;
                default:
                    Logger.warn("Rs3ObservationManager: Unknown event type '{}'.", eventType);
                    break;
            }
        }
    }
}
