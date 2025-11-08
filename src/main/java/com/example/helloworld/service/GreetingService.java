package com.example.helloworld.service;

import org.springframework.stereotype.Service;

/**
 * Service class responsible for:
 * - Validating the "name" input.
 * - Deciding whether the first letter is in A–M (inclusive).
 * - Formatting the name for the response message.
 *
 * Keeping this logic here (instead of the controller) makes
 * the code easier to test and maintain.
 */
@Service
public class GreetingService {

    /**
     * Checks if the first letter of the given name is in the first half
     * of the English alphabet: A–M (case insensitive).
     *
     * Rules:
     * - The input is trimmed.
     * - If the name is null, empty, or starts with a non-letter,
     *   this method returns false (treated as invalid).
     *
     * @param name the raw name passed from the request
     * @return true if first letter is A–M, false otherwise
     */
    public boolean isFirstLetterInFirstHalf(String name) {
        if (name == null) {
            return false;
        }

        // Remove leading and trailing spaces
        String trimmed = name.trim();

        // Empty string after trimming is invalid
        if (trimmed.isEmpty()) {
            return false;
        }

        // Get the first character of the trimmed string
        char firstChar = trimmed.charAt(0);

        // If the first character is not a letter, treat as invalid
        if (!Character.isLetter(firstChar)) {
            return false;
        }

        // Convert to upper-case so we don't care about case
        char upper = Character.toUpperCase(firstChar);

        // Check if it falls in A–M (inclusive)
        return upper >= 'A' && upper <= 'M';
    }

    /**
     * Formats the given name to be used in the "Hello X" message.
     *
     * Assumptions (also noted in README):
     * - We trim leading and trailing spaces.
     * - We convert the first character to upper-case.
     * - We convert the rest of the characters to lower-case.
     *
     * Examples:
     * - "alice"     -> "Alice"
     * - "  ALICE  " -> "Alice"
     *
     * @param name the raw name passed from the request
     * @return a formatted version of the name suitable for display
     */
    public String formatNameForMessage(String name) {
        if (name == null) {
            return "";
        }

        String trimmed = name.trim();

        if (trimmed.isEmpty()) {
            return "";
        }

        if (trimmed.length() == 1) {
            // Single-character names are just upper-cased
            return trimmed.toUpperCase();
        }

        // First character upper-case, the rest lower-case
        char firstChar = Character.toUpperCase(trimmed.charAt(0));
        String remaining = trimmed.substring(1).toLowerCase();

        return firstChar + remaining;
    }
}
