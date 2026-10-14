package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the type of photography event for a person.
 */
public class EventType {
    public static final int MAX_LENGTH = 40;
    public static final String MESSAGE_CONSTRAINTS = "Invalid event type. Use 1-" + MAX_LENGTH
            + " characters containing letters, digits, spaces or hyphens, with at least one letter or digit.";

    /*
     * Words of letters, digits or hyphens separated by single spaces, with at least one letter or digit.
     */
    public static final String VALIDATION_REGEX = "(?=.*[\\p{L}0-9])[\\p{L}\\p{M}0-9-]+( [\\p{L}\\p{M}0-9-]+)*";
    public final String value;

    /**
     * Creates an event type.
     */
    public EventType(String eventType) {
        requireNonNull(eventType);
        checkArgument(isValidEventType(eventType), MESSAGE_CONSTRAINTS);
        value = eventType;
    }

    /**
     * Returns true if the given string is a valid event type.
     */
    public static boolean isValidEventType(String test) {
        return test.matches(VALIDATION_REGEX) && test.codePointCount(0, test.length()) <= MAX_LENGTH;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof EventType otherEventType && value.equals(otherEventType.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
