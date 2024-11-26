package com.godscape.system.factories;

import com.godscape.system.observers.BotStateObservation;
import com.godscape.system.observers.ThemeChangeObservation;
import com.godscape.system.utility.Logger;
import com.godscape.system.interfaces.fuse.ObservationFuse;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ObservationFactory<T> implements ObservationFuse<T> {

    private static volatile ObservationFactory instance;

    // Lists to hold different types of observers
    private final List<Object> profileObservers; // Replace Object with actual observer type for profile changes
    private final List<ThemeChangeObservation> themeListeners;
    private final List<BotStateObservation> botStateObservations;

    private ObservationFactory() {
        profileObservers = new CopyOnWriteArrayList<>();
        themeListeners = new CopyOnWriteArrayList<>();
        botStateObservations = new CopyOnWriteArrayList<>();
        Logger.info("ObservationFactory: Initialized observer lists.");
    }

    public static ObservationFactory getInstance() {
        if (instance == null) {
            synchronized (ObservationFactory.class) {
                if (instance == null) {
                    instance = new ObservationFactory();
                }
            }
        }
        return instance;
    }

    @Override
    public void addObserver(T observer) {
        if (observer instanceof Object) { // Replace Object with profile observer type, if known
            profileObservers.add(observer);
            Logger.info("ObservationFactory: Added ProfileChangeObserver.");
        } else if (observer instanceof ThemeChangeObservation) {
            themeListeners.add((ThemeChangeObservation) observer);
            Logger.info("ObservationFactory: Added ThemeChangeObserver.");
        } else if (observer instanceof BotStateObservation) {
            botStateObservations.add((BotStateObservation) observer);
            Logger.info("ObservationFactory: Added BotStateObserver.");
        } else {
            Logger.warn("ObservationFactory: Unsupported observer type.");
        }
    }

    @Override
    public void removeObserver(T observer) {
        if (observer instanceof Object) { // Replace Object with profile observer type, if known
            profileObservers.remove(observer);
            Logger.info("ObservationFactory: Removed ProfileChangeObserver.");
        } else if (observer instanceof ThemeChangeObservation) {
            themeListeners.remove(observer);
            Logger.info("ObservationFactory: Removed ThemeChangeObserver.");
        } else if (observer instanceof BotStateObservation) {
            botStateObservations.remove(observer);
            Logger.info("ObservationFactory: Removed BotStateObserver.");
        } else {
            Logger.warn("ObservationFactory: Unsupported observer type.");
        }
    }

    @Override
    public void notifyObservers(Object event) {
        // Implement notification logic based on the actual type of event
        Logger.warn("ObservationFactory: notifyObservers method needs an actual implementation based on the event type.");
    }

    // Getter methods for accessing observer lists
    public List<Object> getProfileObservers() { // Replace Object with actual profile observer type if known
        return profileObservers;
    }

    public List<ThemeChangeObservation> getThemeListeners() {
        return themeListeners;
    }

    public List<BotStateObservation> getBotStateListeners() {
        return botStateObservations;
    }
}
