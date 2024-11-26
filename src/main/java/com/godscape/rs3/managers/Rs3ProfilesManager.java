package com.godscape.rs3.managers;

import com.godscape.rs3.cache.Rs3ProfileCache;
import com.godscape.rs3.schemas.Rs3ProfileSchema;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.utility.Logger;
import com.godscape.system.utility.validation.Normalization;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class Rs3ProfilesManager {

    private final Rs3ProfileCache profileCache;

    public Rs3ProfilesManager() {
        this.profileCache = DependencyFactory.getInstance().getInjection(Rs3ProfileCache.class);
        Logger.info("Rs3ProfilesManager initialized.");
    }

    public void addProfile(Rs3ProfileSchema profile) {
        if (validateProfile(profile)) {
            normalizeProfile(profile);
            profileCache.addProfile(profile);
            Logger.info("Profile added with ID: {}", profile.getProfileId());
        } else {
            Logger.warn("Failed to add profile: Invalid profile data.");
        }
    }

    public void updateProfile(Rs3ProfileSchema profile) {
        if (validateProfile(profile)) {
            normalizeProfile(profile);
            profileCache.updateProfile(profile);
            Logger.info("Profile updated with ID: {}", profile.getProfileId());
        } else {
            Logger.warn("Failed to update profile: Invalid profile data.");
        }
    }

    public boolean hasChanges(Rs3ProfileSchema profile) {
        Rs3ProfileSchema cachedProfile = profileCache.getProfile(profile.getProfileId());
        return cachedProfile != null && !cachedProfile.equals(profile);
    }

    public void removeProfile(UUID profileId) {
        if (profileId != null) {
            profileCache.removeProfile(profileId);
            Logger.info("Profile removed with ID: {}", profileId);
        } else {
            Logger.warn("Profile removal failed: Invalid UUID.");
        }
    }

    public Rs3ProfileSchema getProfile(UUID profileId) {
        Rs3ProfileSchema profile = profileCache.getProfile(profileId);
        if (profile == null) {
            Logger.warn("Profile with ID '{}' not found.", profileId);
        }
        return profile;
    }

    public Collection<Rs3ProfileSchema> getAllProfiles() {
        return profileCache.getAllProfiles();
    }

    public void clearProfiles() {
        profileCache.clearProfileCache();
        Logger.info("All profiles cleared from cache.");
    }

    private boolean validateProfile(Rs3ProfileSchema profile) {
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
        if (profile.getMembershipStatus() == null) {
            Logger.warn("Profile validation failed: Membership status is null.");
            return false;
        }
        if (profile.getMode() == null || profile.getMode().isEmpty()) {
            Logger.warn("Profile validation failed: Game mode is empty.");
            return false;
        }
        return true;
    }

    public void normalizeProfile(Rs3ProfileSchema profile) {
        // Normalize profile name
        profile.setProfileName(Normalization.normalizeStringSetting(
                "profileSettings",
                "profileName",
                profile.getProfileName()
        ));

        // Normalize membership status
        Boolean membershipStatus = profile.getMembershipStatus();
        if (membershipStatus != null) {
            profile.setMembershipStatus(Normalization.normalizeBooleanSetting(
                    "profileSettings",
                    "membershipStatus",
                    membershipStatus
            ));
        }

        // Normalize game mode
        String mode = profile.getMode();
        if (mode != null) {
            profile.setMode(Normalization.normalizeStringSetting(
                    "profileSettings",
                    "gameMode",
                    mode
            ));
        }

        // Normalize playstyle if available
        if (profile.getPlaystyle() != null) {
            profile.setPlaystyle(Normalization.normalizeStringSetting(
                    "profileSettings",
                    "playstyle",
                    profile.getPlaystyle()
            ));
        }

        // Normalize autoprofiler if available
        Boolean autoprofiler = profile.getAutoprofiler();
        if (autoprofiler != null) {
            profile.setAutoprofiler(Normalization.normalizeBooleanSetting(
                    "profileSettings",
                    "autoprofiler",
                    autoprofiler
            ));
        }

        // Normalize skill goals
        Map<String, Object> skillGoals = profile.getSetting("skillGoals", "levels", null);
        if (skillGoals != null) {
            for (Map.Entry<String, Object> entry : skillGoals.entrySet()) {
                String skillName = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Integer) {
                    int level = (Integer) value;
                    int normalizedLevel = Normalization.clampSkillLevel(level, skillName);
                    skillGoals.put(skillName, normalizedLevel);
                }
            }
            profile.setSetting("skillGoals", "levels", skillGoals);
        }
    }

    public boolean profileExists(UUID profileId) {
        return profileCache.getProfile(profileId) != null;
    }
}
