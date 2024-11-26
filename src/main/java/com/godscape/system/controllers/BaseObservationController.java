package com.godscape.system.controllers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Themes;
import com.godscape.osrs.managers.OsrsObservationManager;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.fuse.ObservationFuse;
import com.godscape.system.interfaces.mark.Observable;
import com.godscape.system.observers.BotStateObservation;
import com.godscape.system.observers.ThemeChangeObservation;

@Singleton
public abstract class BaseObservationController<T> implements Observable, ObservationFuse<T> {

    protected final OsrsObservationManager osrsObservationManager;

    public BaseObservationController() {
        this.osrsObservationManager = DependencyFactory.getInstance().getInjection(OsrsObservationManager.class);
    }

    // Register/unregister theme and bot state listeners
    public void registerThemeListener(ThemeChangeObservation listener) {
        osrsObservationManager.addThemeListener(listener);
    }

    public void unregisterThemeListener(ThemeChangeObservation listener) {
        osrsObservationManager.removeThemeListener(listener);
    }

    public void registerBotStateListener(BotStateObservation listener) {
        osrsObservationManager.addBotStateListener(listener);
    }

    public void unregisterBotStateListener(BotStateObservation listener) {
        osrsObservationManager.removeBotStateListener(listener);
    }

    public void notifyThemeChange(Themes newTheme) {
        osrsObservationManager.notifyThemeChange(newTheme);
    }

    public void notifyBotStateChanged(boolean isRunning) {
        osrsObservationManager.notifyBotStateChanged(isRunning);
    }

    @Override
    public abstract void addObserver(T observer);

    @Override
    public abstract void removeObserver(T observer);

    @Override
    public abstract void notifyObservers(Object profile);
}
