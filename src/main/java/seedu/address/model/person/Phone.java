package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {


    public static final int MIN_DIGITS = 8;
    public static final int MAX_DIGITS = 15;
    public static final String MESSAGE_CONSTRAINTS = "Invalid phone number. Enter " + MIN_DIGITS + "-"
            + MAX_DIGITS + " digits, optionally starting with +; spaces and hyphens are allowed.";

    /*
     * An optional leading +, followed by digits, spaces or hyphens.
     */
    public static final String VALIDATION_REGEX = "\\+?[0-9 -]+";
    public final String value;

    /**
     * Constructs a {@code Phone}, removing any spaces and hyphens.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        value = removeSeparators(phone);
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }
        int digitCount = removeSeparators(test).replace("+", "").length();
        return digitCount >= MIN_DIGITS && digitCount <= MAX_DIGITS;
    }

    /**
     * Returns {@code phone} without spaces and hyphens.
     */
    public static String removeSeparators(String phone) {
        requireNonNull(phone);
        return phone.replaceAll("[ -]", "");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone otherPhone)) {
            return false;
        }

        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
