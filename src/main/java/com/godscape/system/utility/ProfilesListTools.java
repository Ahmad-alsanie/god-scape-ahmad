package com.godscape.system.utility;

import com.godscape.osrs.schemas.OsrsProfileSchema;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for handling profile-related operations.
 */
public class ProfilesListTools {

    /**
     * Sorts a list of OsrsProfileSchema objects alphabetically by profile name, with numeric sorting for numbers
     * and ensuring "Default" appears at the top.
     *
     * @param profiles  The list of profiles to sort.
     * @param ascending If true, sorts in ascending order; otherwise, sorts in descending order.
     */
    public static void sortProfiles(List<OsrsProfileSchema> profiles, boolean ascending) {
        if (profiles == null || profiles.isEmpty()) {
            return;
        }

        // Custom comparator to handle numeric sorting and place "Default" at the top
        Comparator<OsrsProfileSchema> comparator = (profile1, profile2) -> {
            String name1 = profile1.getProfileName();
            String name2 = profile2.getProfileName();

            // "Default" should always come first
            if ("Default".equalsIgnoreCase(name1)) {
                return -1;
            }
            if ("Default".equalsIgnoreCase(name2)) {
                return 1;
            }

            // Compare the profile names using the enhanced numeric comparison
            int result = compareLevelNames(name1, name2);
            if (result != 0) {
                return result;
            }

            // Fallback to case-insensitive string comparison
            return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
        };

        // Sort in ascending or descending order based on the parameter
        if (!ascending) {
            comparator = comparator.reversed();
        }
        Collections.sort(profiles, comparator);
    }

    /**
     * Compares two profile names, extracting numeric levels if possible and sorting accordingly.
     *
     * @param s1 The first profile name.
     * @param s2 The second profile name.
     * @return A negative number if s1 < s2, zero if s1 == s2, or a positive number if s1 > s2.
     */
    private static int compareLevelNames(String s1, String s2) {
        Integer level1 = extractLevelNumber(s1);
        Integer level2 = extractLevelNumber(s2);

        if (level1 != null && level2 != null) {
            // Compare numerically if both have numbers
            return Integer.compare(level1, level2);
        } else if (level1 != null) {
            // If only the first has a number, it comes first
            return -1;
        } else if (level2 != null) {
            // If only the second has a number, it comes first
            return 1;
        } else {
            // Fallback to alphanumeric comparison if no numbers were found
            return s1.compareToIgnoreCase(s2);
        }
    }

    /**
     * Extracts the first number found in a profile name.
     *
     * @param name The profile name.
     * @return The extracted number, or null if not found.
     */
    private static Integer extractLevelNumber(String name) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }
}
