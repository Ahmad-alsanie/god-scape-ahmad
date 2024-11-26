package com.godscape.system.utility;

/**
 * TextConvert is a utility class that provides methods for text manipulation,
 * such as capitalizing words.
 */
public class StringConvert {

    /**
     * Capitalizes the first letter of each word in the input string.
     * Words are assumed to be separated by whitespace characters.
     *
     * @param input The input string to be capitalized.
     * @return A new string with the first letter of each word capitalized.
     *         If the input is null or empty, returns the input as-is.
     */
    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Split the input string into words based on whitespace
        String[] words = input.split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.length() > 0) {
                // Capitalize the first character and append the rest of the word in lowercase
                capitalized.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    capitalized.append(word.substring(1).toLowerCase());
                }
            }

            // Append a space after each word except the last one
            if (i < words.length - 1) {
                capitalized.append(" ");
            }
        }

        return capitalized.toString();
    }

    /**
     * Converts the entire input string to uppercase.
     *
     * @param input The input string to be converted to uppercase.
     * @return A new string with all characters in uppercase.
     *         If the input is null or empty, returns the input as-is.
     */
    public static String toUpperCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.toUpperCase();
    }

    /**
     * Converts the entire input string to lowercase.
     *
     * @param input The input string to be converted to lowercase.
     * @return A new string with all characters in lowercase.
     *         If the input is null or empty, returns the input as-is.
     */
    public static String toLowerCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.toLowerCase();
    }

    // You can add more text conversion methods here as needed
}
