package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Represents the date of a photography event in ISO-8601 format.
 */
public class EventDate {
    public static final String MESSAGE_CONSTRAINTS =
            "Invalid date. Enter a valid calendar date in YYYY-MM-DD format.";
    public static final String VALIDATION_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    public final LocalDate value;

    /**
     * Creates an event date from an ISO-8601 date string.
     */
    public EventDate(String eventDate) {
        requireNonNull(eventDate);
        checkArgument(isValidEventDate(eventDate), MESSAGE_CONSTRAINTS);
        value = LocalDate.parse(eventDate);
    }

    /**
     * Returns true if the given string is a valid calendar date in YYYY-MM-DD format, with a year from 0001 to 9999.
     */
    public static boolean isValidEventDate(String test) {
        if (!test.matches(VALIDATION_REGEX)) {
            return false;
        }
        try {
            return LocalDate.parse(test).getYear() >= 1;
        } catch (DateTimeParseException exception) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof EventDate otherEventDate && value.equals(otherEventDate.value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
