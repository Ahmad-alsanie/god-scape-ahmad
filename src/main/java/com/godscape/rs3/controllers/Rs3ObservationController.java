package com.godscape.rs3.controllers;

import com.godscape.rs3.managers.Rs3ObservationManager;
import com.godscape.rs3.observations.Rs3ProfileChangeObservation;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.controllers.BaseObservationController;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

public class Rs3ObservationController extends BaseObservationController<Rs3ProfileChangeObservation> {

    private final Rs3ObservationManager rs3ObservationManager;

    public Rs3ObservationController() {
        this.rs3ObservationManager = DependencyFactory.getInstance().getInjection(Rs3ObservationManager.class);
    }

    @Override
    public void addObserver(Rs3ProfileChangeObservation observer) {
        rs3ObservationManager.addProfileObserver(observer);
        Logger.info("Rs3ObservationController: Registered Rs3ProfileChangeObservation.");
    }

    @Override
    public void removeObserver(Rs3ProfileChangeObservation observer) {
        rs3ObservationManager.removeProfileObserver(observer);
        Logger.info("Rs3ObservationController: Unregistered Rs3ProfileChangeObservation.");
    }

    /**
     * Notifies all observers of a specific type of profile event.
     *
     * @param eventType The type of event (e.g., "PROFILE_ADDED", "PROFILE_UPDATED", "PROFILE_REMOVED").
     * @param profile   The Rs3ProfileSchema profile object related to the event.
     */
    public void notifyObservers(String eventType, Rs3ProfileSchema profile) {
        switch (eventType) {
            case "PROFILE_ADDED":
                rs3ObservationManager.notifyProfileAdded(profile);
                Logger.info("Rs3ObservationController: Notified observers about profile addition.");
                break;
            case "PROFILE_UPDATED":
                rs3ObservationManager.notifyProfileUpdated(profile);
                Logger.info("Rs3ObservationController: Notified observers about profile update.");
                break;
            case "PROFILE_REMOVED":
                rs3ObservationManager.notifyProfileRemoved(profile);
                Logger.info("Rs3ObservationController: Notified observers about profile removal.");
                break;
            default:
                Logger.warn("Rs3ObservationController: Unsupported event type '{}'.", eventType);
                break;
        }
    }

    /**
     * Implemented to satisfy BaseObservationController's abstract method, checking for Rs3ProfileSchema.
     */
    @Override
    public void notifyObservers(Object profile) {
        if (profile instanceof Rs3ProfileSchema) {
            notifyObservers("PROFILE_UPDATED", (Rs3ProfileSchema) profile);  // Default event type used here
        } else {
            Logger.warn("Rs3ObservationController: Unsupported profile type for notification.");
        }
    }
}
