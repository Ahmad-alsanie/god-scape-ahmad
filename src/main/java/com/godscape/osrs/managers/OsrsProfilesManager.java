package com.godscape.osrs.managers;

import com.godscape.osrs.cache.OsrsProfileCache;
import com.godscape.osrs.schemas.OsrsProfileSchema;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.validation.Normalization;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Singleton
public class OsrsProfilesManager {

    private final OsrsProfileCache profileCache;

    // Constructor to inject OsrsProfileCache dependency (singleton)
    public OsrsProfilesManager() {
        this.profileCache = DependencyFactory.getInstance().getInjection(OsrsProfileCache.class);
        Logger.info("OsrsProfilesManager initialized.");
    }

    public void addProfile(OsrsProfileSchema profile) {
        if (validateProfile(profile)) {
            normalizeProfile(profile);
            profileCache.addProfile(profile);
            Logger.info("Profile added with ID: {}", profile.getProfileId());
        } else {
            Logger.warn("Failed to add profile: Invalid profile data.");
        }
    }

    /**
     * Updates an existing profile in the cache.
     *
     * @param profile The profile to update.
     */
    public void updateProfile(OsrsProfileSchema profile) {
        if (validateProfile(profile)) {
            normalizeProfile(profile);
            profileCache.updateProfile(profile);
            Logger.info("Profile updated with ID: {}", profile.getProfileId());
        } else {
            Logger.warn("Failed to update profile: Invalid profile data.");
        }
    }

    /**
     * Checks if a profile has changes compared to the cached version.
     *
     * @param profile The profile to check.
     * @return true if there are changes, false otherwise.
     */
    public boolean hasChanges(OsrsProfileSchema profile) {
        OsrsProfileSchema cachedProfile = profileCache.getProfile(profile.getProfileId());
        return cachedProfile != null && !cachedProfile.equals(profile);
    }

    /**
     * Removes a profile from the cache by its UUID.
     *
     * @param profileId The UUID of the profile to remove.
     */
    public void removeProfile(UUID profileId) {
        if (profileId != null) {
            profileCache.removeProfile(profileId);
            Logger.info("Profile removed with ID: {}", profileId);
        } else {
            Logger.warn("Profile removal failed: Invalid UUID.");
        }
    }

    /**
     * Retrieves a profile by its UUID from the cache.
     *
     * @param profileId The UUID of the profile to retrieve.
     * @return The profile if found, null otherwise.
     */
    public OsrsProfileSchema getProfile(UUID profileId) {
        OsrsProfileSchema profile = profileCache.getProfile(profileId);
        if (profile == null) {
            Logger.warn("Profile with ID '{}' not found.", profileId);
        }
        return profile;
    }

    /**
     * Retrieves all profiles from the cache.
     *
     * @return A collection of all profiles.
     */
    public Collection<OsrsProfileSchema> getAllProfiles() {
        return profileCache.getAllProfiles();
    }

    /**
     * Clears all profiles from the cache.
     */
    public void clearProfiles() {
        profileCache.clearProfileCache();
        Logger.info("All profiles cleared from cache.");
    }

    /**
     * Validates if the profile contains necessary data.
     * This ensures that profile data is not null and meets basic criteria.
     *
     * @param profile The profile to validate.
     * @return True if the profile is valid, false otherwise.
     */
    private boolean validateProfile(OsrsProfileSchema profile) {
        if (profile == null) {
            Logger.warn("Profile validation failed: Profile is null.");
            return false;
        }
        if (profile.getProfileId() == null) {
            Logger.warn("Profile validation failed: Profile ID is null.");
            return false;
        }
        if (profile.getProfileName() == null || profile.getProfileName().isEmpty()) {
            Logger.warn("Profile validation failed: Profile name is empty.");
            return false;
        }
        return true;
    }

    /**
     * Normalizes the profile data using the Normalization utility.
     * This method will normalize the profile settings for any specific needs.
     *
     * @param profile The profile to normalize.
     */
    public void normalizeProfile(OsrsProfileSchema profile) {
        // Normalize profile name
        profile.setProfileName(Normalization.normalizeStringSetting(
                "profileSettings",
                "profileName",
                profile.getProfileName()
        ));

        // Normalize skill goals
        Map<String, Integer> skillMap = profile.getSkillMap();
        skillMap.forEach((skill, level) -> {
            int normalizedLevel = Normalization.clampSkillLevel(level, skill);
            skillMap.put(skill, normalizedLevel);
        });
        profile.setSkillMap(skillMap);
    }

    /**
     * Checks if a profile exists in the cache by its UUID.
     *
     * @param profileId The UUID of the profile to check.
     * @return True if the profile exists, false otherwise.
     */
    public boolean profileExists(UUID profileId) {
        return profileCache.getProfile(profileId) != null;
    }
}
