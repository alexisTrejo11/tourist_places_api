package at.backend.tourist.places.core.Utils;

public class StringHandler {

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        input = input.toLowerCase();
        input = input.strip();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
