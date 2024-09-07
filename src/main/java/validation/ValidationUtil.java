package validation;

public class ValidationUtil {

    /**
     * Validates that the provided price is non-negative.
     *
     * @param price The price to validate.
     * @return true if the price is non-negative, false if the price is negative.
     */
    public static boolean isValidPrice(double price) {
        return price >= 0;
    }
}
