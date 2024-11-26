package com.godscape.system.utility;

public class Separate {

    /**
     * Repeats a specified character a given number of times.
     *
     * @param c     The character to repeat.
     * @param count The number of times to repeat the character.
     * @return A string containing the repeated characters.
     */
    public static String repeatChar(char c, int count) {
        if (count <= 0) return "";
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Repeats a specified string separator a given number of times.
     *
     * @param separator The string separator to repeat (e.g., "-", "#").
     * @param count     The number of times to repeat the separator.
     * @return A string with the separator repeated the specified number of times.
     */
    public static String repeatSeparator(String separator, int count) {
        if (separator == null || separator.isEmpty() || count <= 0) return "";
        StringBuilder sb = new StringBuilder(separator.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(separator);
        }
        return sb.toString();
    }

    /**
     * Repeats a specified separator character recursively a given number of times.
     * Each recursive level adds a nested repetition pattern for a layered appearance.
     *
     * @param separatorChar The separator character to repeat (e.g., '#', '-').
     * @param levels        The depth or levels of recursion.
     * @param count         The number of times to repeat the character at each level.
     * @return A recursively repeated pattern as a string.
     */
    public static String recursiveSeparator(char separatorChar, int levels, int count) {
        if (levels <= 0 || count <= 0) return "";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < levels; i++) {
            result.append(repeatChar(separatorChar, count)).append("\n");
        }
        return result.toString();
    }
}
