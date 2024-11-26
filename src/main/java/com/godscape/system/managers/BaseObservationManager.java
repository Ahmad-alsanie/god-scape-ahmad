package com.godscape.system.managers;

import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.Themes;
import com.godscape.system.interfaces.fuse.ObservationFuse;
import com.godscape.system.interfaces.mark.Observable;
import com.godscape.system.observers.BotStateObservation;
import com.godscape.system.observers.ThemeChangeObservation;
import com.godscape.osrs.observations.OsrsProfileChangeObservation;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.utility.Logger;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class BaseObservationManager implements Observable, ObservationFuse<Object> {

    protected final List<ThemeChangeObservation> themeListeners;
    protected final List<BotStateObservation> botStateObservations;
    protected final List<OsrsProfileChangeObservation> profileListeners;

    public BaseObservationManager() {
        this.themeListeners = new ArrayList<>();
        this.botStateObservations = new ArrayList<>();
        this.profileListeners = new ArrayList<>();
        Logger.info("BaseObservationManager: Initialized.");
    }

    // Add an observer based on its type
    @Override
    public void addObserver(Object observer) {
        if (observer instanceof ThemeChangeObservation) {
            addThemeListener((ThemeChangeObservation) observer);
        } else if (observer instanceof BotStateObservation) {
            addBotStateListener((BotStateObservation) observer);
        } else if (observer instanceof OsrsProfileChangeObservation) {
            addProfileObserver((OsrsProfileChangeObservation) observer);
        } else {
            Logger.warn("BaseObservationManager: Unsupported observer type.");
        }
    }

    // Remove an observer based on its type
    @Override
    public void removeObserver(Object observer) {
        if (observer instanceof ThemeChangeObservation) {
            removeThemeListener((ThemeChangeObservation) observer);
        } else if (observer instanceof BotStateObservation) {
            removeBotStateListener((BotStateObservation) observer);
        } else if (observer instanceof OsrsProfileChangeObservation) {
            removeProfileObserver((OsrsProfileChangeObservation) observer);
        } else {
            Logger.warn("BaseObservationManager: Unsupported observer type.");
        }
    }

    // Notify observers based on the update type
    @Override
    public void notifyObservers(Object update) {
        if (update instanceof Themes) {
            notifyThemeChange((Themes) update);
        } else if (update instanceof Boolean) {
            notifyBotStateChanged((Boolean) update);
        } else {
            Logger.warn("BaseObservationManager: Unsupported update type.");
        }
    }

    // Methods for managing ThemeChangeObservation listeners
    public void addThemeListener(ThemeChangeObservation listener) {
        if (listener != null && !themeListeners.contains(listener)) {
            themeListeners.add(listener);
            Logger.info("BaseObservationManager: Registered ThemeChangeObserver.");
        }
    }

    public void removeThemeListener(ThemeChangeObservation listener) {
        if (listener != null && themeListeners.remove(listener)) {
            Logger.info("BaseObservationManager: Unregistered ThemeChangeObserver.");
        }
    }

    public void notifyThemeChange(Themes newTheme) {
        for (ThemeChangeObservation listener : themeListeners) {
            try {
                listener.onThemeChange(newTheme);
            } catch (Exception e) {
                Logger.error("Error notifying ThemeChangeObserver: {}", e.getMessage());
            }
        }
    }

    // Methods for managing BotStateObservation listeners
    public void addBotStateListener(BotStateObservation listener) {
        if (listener != null && !botStateObservations.contains(listener)) {
            botStateObservations.add(listener);
            Logger.info("BaseObservationManager: Registered BotStateObserver.");
        }
    }

    public void removeBotStateListener(BotStateObservation listener) {
        if (listener != null && botStateObservations.remove(listener)) {
            Logger.info("BaseObservationManager: Unregistered BotStateObserver.");
        }
    }

    public void notifyBotStateChanged(boolean isRunning) {
        for (BotStateObservation listener : botStateObservations) {
            try {
                listener.onBotStatusChanged(isRunning);
            } catch (Exception e) {
                Logger.error("Error notifying BotStateObserver: {}", e.getMessage());
            }
        }
    }

    // Methods for managing OsrsProfileChangeObservation listeners
    public void addProfileObserver(OsrsProfileChangeObservation observer) {
        if (observer != null && !profileListeners.contains(observer)) {
            profileListeners.add(observer);
            Logger.info("BaseObservationManager: Registered OsrsProfileChangeObserver.");
        }
    }

    public void removeProfileObserver(OsrsProfileChangeObservation observer) {
        if (observer != null && profileListeners.remove(observer)) {
            Logger.info("BaseObservationManager: Unregistered OsrsProfileChangeObserver.");
        }
    }

    public void notifyProfileAdded(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            OsrsProfileSchema osrsProfile = (OsrsProfileSchema) profile;
            for (OsrsProfileChangeObservation listener : profileListeners) {
                try {
                    listener.onProfileAdded(osrsProfile);
                } catch (Exception e) {
                    Logger.error("Error notifying OsrsProfileChangeObserver for profile added: {}", e.getMessage());
                }
            }
        } else {
            Logger.warn("BaseObservationManager: notifyProfileAdded received unsupported profile type.");
        }
    }

    public void notifyProfileUpdated(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            OsrsProfileSchema osrsProfile = (OsrsProfileSchema) profile;
            for (OsrsProfileChangeObservation listener : profileListeners) {
                try {
                    listener.onProfileUpdated(osrsProfile);
                } catch (Exception e) {
                    Logger.error("Error notifying OsrsProfileChangeObserver for profile updated: {}", e.getMessage());
                }
            }
        } else {
            Logger.warn("BaseObservationManager: notifyProfileUpdated received unsupported profile type.");
        }
    }

    public void notifyProfileRemoved(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            OsrsProfileSchema osrsProfile = (OsrsProfileSchema) profile;
            for (OsrsProfileChangeObservation listener : profileListeners) {
                try {
                    listener.onProfileRemoved(osrsProfile);
                } catch (Exception e) {
                    Logger.error("Error notifying OsrsProfileChangeObserver for profile removed: {}", e.getMessage());
                }
            }
        } else {
            Logger.warn("BaseObservationManager: notifyProfileRemoved received unsupported profile type.");
        }
    }
}
