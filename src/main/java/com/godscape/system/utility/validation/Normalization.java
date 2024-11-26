package com.godscape.system.utility.validation;

import com.godscape.osrs.preloaders.OsrsDefaultProfilePreloader;
import com.godscape.rs3.preloaders.Rs3DefaultProfilePreloader;
import com.godscape.system.annotations.Singleton;
import com.godscape.system.enums.GameVersion;
import com.godscape.system.factories.DependencyFactory;
import com.godscape.system.factories.PlatformFactory;
import com.godscape.system.utility.Logger;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class Normalization {

    // Constants for OSRS and RS3 level caps and experience limit
    private static final int OSRS_MIN_LEVEL = 1;
    private static final int OSRS_MAX_LEVEL = 99;
    private static final int RS3_MAX_LEVEL = 120;
    private static final int RS3_ELITE_CAP = 150;
    private static final int EXP_CLAMP = 200_000_000;

    private static final String SKILL_GOALS_CATEGORY = "skillGoals";

    private static final DependencyFactory dependencyFactory = DependencyFactory.getInstance();
    private static final OsrsDefaultProfilePreloader osrsPreloader = dependencyFactory.getInjection(OsrsDefaultProfilePreloader.class);
    private static final Rs3DefaultProfilePreloader rs3Preloader = dependencyFactory.getInjection(Rs3DefaultProfilePreloader.class);
    private static final PlatformFactory platformFactory = dependencyFactory.getInjection(PlatformFactory.class);

    private Normalization() {
        throw new UnsupportedOperationException("Utility class should not be instantiated.");
    }

    /**
     * Clamps skill levels based on the game version and specific skill requirements.
     *
     * @param level The skill level to clamp.
     * @param skillName The skill name.
     * @return The clamped skill level.
     */
    public static int clampSkillLevel(int level, String skillName) {
        GameVersion gameVersion = platformFactory.getCurrentGameVersion();
        int maxLevel = (gameVersion == GameVersion.RS3 && isEliteSkill(skillName)) ? RS3_ELITE_CAP : OSRS_MAX_LEVEL;

        return Math.max(OSRS_MIN_LEVEL, Math.min(level, maxLevel));
    }

    /**
     * Clamps experience points to a maximum allowed value.
     *
     * @param exp Experience points to clamp.
     * @return Clamped experience points.
     */
    public static int clampExperience(int exp) {
        return Math.min(exp, EXP_CLAMP);
    }

    /**
     * General setting normalization with custom logic.
     *
     * @param value The initial value.
     * @param normalizationFunction Custom normalization function.
     * @return Normalized value.
     */
    public static <T> T normalizeSetting(T value, Function<T, T> normalizationFunction) {
        return normalizationFunction.apply(value);
    }

    /**
     * Normalizes profile settings based on category and game version.
     *
     * @param category Setting category.
     * @param settingName Setting name.
     * @param defaultValue Default setting value.
     * @param normalizationFunction Normalization function.
     * @return Normalized setting.
     */
    public static <T> T normalizeProfileSetting(String category, String settingName, T defaultValue, Function<T, T> normalizationFunction) {
        GameVersion gameVersion = platformFactory.getCurrentGameVersion();
        T value = defaultValue;

        if (gameVersion == GameVersion.OSRS) {
            value = (T) osrsPreloader.createDefaultProfileSchema().getSetting(category, settingName, defaultValue);
        } else if (gameVersion == GameVersion.RS3) {
            value = (T) rs3Preloader.createDefaultProfile().getSetting(category, settingName, defaultValue);
        }

        return normalizeSetting(value, normalizationFunction);
    }

    /**
     * Validates and clamps skill level in a text field.
     *
     * @param skillField Text field for skill input.
     * @param skillName Name of the skill.
     */
    public static void validateSkillLevel(JTextField skillField, String skillName) {
        String inputText = skillField.getText().trim();
        int clampedValue = OSRS_MIN_LEVEL;

        try {
            int skillLevel = Integer.parseInt(inputText);
            clampedValue = clampSkillLevel(skillLevel, skillName);
        } catch (NumberFormatException e) {
            clampedValue = normalizeProfileSetting(SKILL_GOALS_CATEGORY, skillName, OSRS_MIN_LEVEL, level -> clampSkillLevel(level, skillName));
        }

        skillField.setText(String.valueOf(clampedValue));
    }

    /**
     * Adds a validation listener to a text field for clamping skill levels.
     *
     * @param skillField Skill level text field.
     * @param skillName Skill name.
     */
    public static void addSkillLevelValidationListener(JTextField skillField, String skillName) {
        skillField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateSkillLevel(skillField, skillName);
            }
        });
    }

    /**
     * Resets multiple skill fields to their default values.
     *
     * @param skillFields Map of skill names to text fields.
     */
    public static void resetFieldsToDefault(Map<String, JTextField> skillFields) {
        skillFields.forEach((skillName, field) -> {
            int defaultLevel = normalizeProfileSetting(SKILL_GOALS_CATEGORY, skillName, OSRS_MIN_LEVEL, level -> clampSkillLevel(level, skillName));
            field.setText(String.valueOf(defaultLevel));
        });
    }

    /**
     * Normalizes a boolean setting.
     *
     * @param category Setting category.
     * @param settingName Setting name.
     * @param defaultValue Default boolean value.
     * @return Normalized boolean setting.
     */
    public static boolean normalizeBooleanSetting(String category, String settingName, boolean defaultValue) {
        return normalizeProfileSetting(category, settingName, defaultValue, value -> value);
    }

    /**
     * Normalizes a string setting by trimming whitespace.
     *
     * @param category Setting category.
     * @param settingName Setting name.
     * @param defaultValue Default string value.
     * @return Normalized string setting.
     */
    public static String normalizeStringSetting(String category, String settingName, String defaultValue) {
        return normalizeProfileSetting(category, settingName, defaultValue, value -> value != null ? value.trim() : "");
    }

    private static boolean isEliteSkill(String skillName) {
        return skillName.equalsIgnoreCase("Invention") || skillName.equalsIgnoreCase("Archaeology");
    }
}
