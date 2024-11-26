package com.godscape.osrs.managers.panels;

import com.godscape.osrs.controllers.OsrsCacheController;
import com.godscape.osrs.controllers.OsrsSettingsController;
import com.godscape.osrs.observations.OsrsProfileChangeObservation;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.interfaces.fuse.ObservationFuse;
import com.godscape.system.utility.Logger;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Singleton
public class OsrsHeaderManager implements ObservationFuse<OsrsProfileChangeObservation> {

    private final OsrsCacheController osrsCacheController;
    private final OsrsSettingsController osrsSettingsController;
    private String currentProfileName;
    private final List<OsrsProfileChangeObservation> observers = new CopyOnWriteArrayList<>();

    public OsrsHeaderManager() {
        DependencyFactory dependencyFactory = DependencyFactory.getInstance();
        this.osrsCacheController = dependencyFactory.getInjection(OsrsCacheController.class);
        this.osrsSettingsController = dependencyFactory.getInjection(OsrsSettingsController.class);
        Logger.info("OsrsHeaderManager initialized.");
    }

    public List<String> getAllProfileNames() {
        Collection<OsrsProfileSchema> profiles = osrsCacheController.getAllProfiles();
        Logger.info("OsrsHeaderManager: Retrieved {} profiles from cache.", profiles.size());
        return profiles.stream()
                .map(OsrsProfileSchema::getProfileName)
                .collect(Collectors.toList());
    }

    public UUID getProfileIdByName(String profileName) {
        OsrsProfileSchema profile = getProfileByName(profileName);
        return profile != null ? profile.getProfileId() : null;
    }

    public boolean createProfile(String profileName) {
        if (isInvalidProfileName(profileName)) {
            Logger.warn("Cannot create a profile with an empty or null name.");
            return false;
        }

        if (profileExists(profileName)) {
            Logger.info("Profile '{}' already exists. Skipping creation.", profileName);
            return false;
        }

        OsrsProfileSchema newProfile = new OsrsProfileSchema();
        newProfile.setProfileName(profileName);

        Map<String, Object> currentSettings = osrsSettingsController.getSettingsMap();
        newProfile.getSettingsMap().put("statSettings_skillGoals", currentSettings.get("statSettings_skillGoals"));

        boolean added = osrsCacheController.addProfile(newProfile);
        if (added) {
            setCurrentProfileName(profileName);
            notifyProfileAdded(newProfile);
            Logger.info("Successfully created profile '{}'.", profileName);
            return true;
        } else {
            Logger.warn("Failed to add profile '{}' to the cache.", profileName);
            return false;
        }
    }

    public boolean renameProfile(String currentName, String newName) {
        if (isInvalidProfileName(currentName) || isInvalidProfileName(newName)) {
            Logger.warn("Invalid profile names for renaming.");
            return false;
        }

        if (currentName.equals(newName)) {
            Logger.info("New name is the same as the current name. Skipping renaming.");
            return false;
        }

        if (profileExists(newName)) {
            Logger.warn("A profile with the name '{}' already exists. Renaming aborted.", newName);
            return false;
        }

        OsrsProfileSchema profile = getProfileByName(currentName);

        if (profile != null) {
            profile.setProfileName(newName);
            boolean updated = osrsCacheController.updateProfile(profile);
            if (updated) {
                setCurrentProfileName(newName);
                notifyProfileUpdated(profile);
                Logger.info("Renamed profile '{}' to '{}'.", currentName, newName);
                return true;
            } else {
                Logger.warn("Failed to update profile '{}' in the cache.", newName);
                return false;
            }
        } else {
            Logger.warn("Profile '{}' not found.", currentName);
            return false;
        }
    }

    public boolean deleteProfile(String profileName) {
        if (isInvalidProfileName(profileName)) {
            Logger.warn("Cannot delete a profile with an empty or null name.");
            return false;
        }

        OsrsProfileSchema profile = getProfileByName(profileName);

        if (profile != null) {
            boolean removed = osrsCacheController.deleteProfile(profile.getProfileId());
            if (removed) {
                if (profileName.equals(currentProfileName)) {
                    setCurrentProfileName(null);
                }
                notifyProfileRemoved(profile);
                Logger.info("Deleted profile '{}'.", profileName);
                return true;
            } else {
                Logger.warn("Failed to remove profile '{}' from the cache.", profileName);
                return false;
            }
        } else {
            Logger.warn("Profile '{}' not found.", profileName);
            return false;
        }
    }

    public boolean updateProfile(String profileName) {
        if (isInvalidProfileName(profileName)) {
            Logger.warn("Cannot update a profile with an empty or null name.");
            return false;
        }

        OsrsProfileSchema profile = getProfileByName(profileName);

        if (profile != null) {
            Map<String, Object> currentSettings = osrsSettingsController.getSettingsMap();
            profile.getSettingsMap().put("statSettings_skillGoals", currentSettings.get("statSettings_skillGoals"));

            boolean updated = osrsCacheController.updateProfile(profile);
            if (updated) {
                setCurrentProfileName(profileName);
                notifyProfileUpdated(profile);
                Logger.info("Updated profile '{}'.", profileName);
                return true;
            } else {
                Logger.warn("Failed to update profile '{}' in the cache.", profileName);
                return false;
            }
        } else {
            Logger.warn("Profile '{}' not found.", profileName);
            return false;
        }
    }

    public void loadProfileSettings(String profileName) {
        OsrsProfileSchema profile = getProfileByName(profileName);
        if (profile != null) {
            osrsSettingsController.setActiveProfileId(profile.getProfileId());
            osrsSettingsController.loadSettings();

            // Ensure UI updates are on Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                Logger.info("UI updated for profile '{}'.", profileName);
            });

            Logger.info("Loaded settings for profile '{}'.", profileName);
        } else {
            Logger.warn("Profile '{}' not found.", profileName);
        }
    }

    private OsrsProfileSchema getProfileByName(String profileName) {
        return osrsCacheController.getAllProfiles().stream()
                .filter(p -> p.getProfileName().equals(profileName))
                .findFirst()
                .orElse(null);
    }

    private boolean profileExists(String profileName) {
        return osrsCacheController.getAllProfiles().stream()
                .anyMatch(p -> p.getProfileName().equals(profileName));
    }

    private boolean isInvalidProfileName(String profileName) {
        return profileName == null || profileName.trim().isEmpty();
    }

    private void setCurrentProfileName(String profileName) {
        this.currentProfileName = profileName;
    }

    // Observer methods
    @Override
    public void addObserver(OsrsProfileChangeObservation observer) {
        observers.add(observer);
        Logger.info("OsrsHeaderManager: Registered OsrsProfileChangeObserver.");
    }

    @Override
    public void removeObserver(OsrsProfileChangeObservation observer) {
        observers.remove(observer);
        Logger.info("OsrsHeaderManager: Unregistered OsrsProfileChangeObserver.");
    }

    @Override
    public void notifyObservers(Object profile) {
        if (profile instanceof OsrsProfileSchema) {
            notifyProfileAdded((OsrsProfileSchema) profile);
        } else {
            Logger.warn("Unsupported profile type for notification.");
        }
    }

    private void notifyProfileAdded(OsrsProfileSchema profile) {
        observers.forEach(observer -> observer.onProfileAdded(profile));
    }

    private void notifyProfileUpdated(OsrsProfileSchema profile) {
        observers.forEach(observer -> observer.onProfileUpdated(profile));
    }

    private void notifyProfileRemoved(OsrsProfileSchema profile) {
        observers.forEach(observer -> observer.onProfileRemoved(profile));
    }
}
