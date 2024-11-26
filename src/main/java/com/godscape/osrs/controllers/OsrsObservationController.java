package com.godscape.osrs.controllers;

import com.godscape.osrs.managers.OsrsObservationManager;
import com.godscape.osrs.observations.OsrsProfileChangeObservation;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.controllers.BaseObservationController;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;

@Singleton
public class OsrsObservationController extends BaseObservationController<OsrsProfileChangeObservation> {

    private final OsrsObservationManager observationManager;

    public OsrsObservationController() {
        this.observationManager = DependencyFactory.getInstance().getInjection(OsrsObservationManager.class);
        if (this.observationManager == null) {
            throw new IllegalStateException("OsrsObservationController: Failed to inject OsrsObservationManager.");
        } else {
            Logger.info("OsrsObservationController initialized.");
        }
    }

    @Override
    public void addObserver(OsrsProfileChangeObservation observer) {
        observationManager.addProfileObserver(observer);
        Logger.info("OsrsObservationController: Observer added.");
    }

    @Override
    public void removeObserver(OsrsProfileChangeObservation observer) {
        observationManager.removeProfileObserver(observer);
        Logger.info("OsrsObservationController: Observer removed.");
    }

    @Override
    public void notifyObservers(Object profile) {
        observationManager.notifyProfileUpdated(profile);
        Logger.info("OsrsObservationController: Observers notified of profile update.");
    }
}
