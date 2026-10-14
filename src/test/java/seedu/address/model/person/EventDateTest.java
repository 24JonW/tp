package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EventDateTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EventDate(null));
    }

    @Test
    public void constructor_invalidEventDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new EventDate("2026-02-30"));
    }

    @Test
    public void isValidEventDate() {
        // null event date
        assertThrows(NullPointerException.class, () -> EventDate.isValidEventDate(null));

        // invalid event dates
        assertFalse(EventDate.isValidEventDate("")); // empty string
        assertFalse(EventDate.isValidEventDate("2026-02-30")); // day does not exist
        assertFalse(EventDate.isValidEventDate("2026-13-01")); // month out of range
        assertFalse(EventDate.isValidEventDate("2027-02-29")); // not a leap year
        assertFalse(EventDate.isValidEventDate("07-10-2026")); // wrong order
        assertFalse(EventDate.isValidEventDate("2026-1-7")); // missing leading zeroes
        assertFalse(EventDate.isValidEventDate("2026/10/07")); // wrong separator
        assertFalse(EventDate.isValidEventDate("0000-01-01")); // year 0000
        assertFalse(EventDate.isValidEventDate("+10000-01-01")); // year above 9999

        // valid event dates
        assertTrue(EventDate.isValidEventDate("2026-10-07"));
        assertTrue(EventDate.isValidEventDate("2028-02-29")); // leap year
        assertTrue(EventDate.isValidEventDate("0001-01-01")); // earliest year
        assertTrue(EventDate.isValidEventDate("9999-12-31")); // latest year
        assertTrue(EventDate.isValidEventDate("2020-01-01")); // past date
    }
}
