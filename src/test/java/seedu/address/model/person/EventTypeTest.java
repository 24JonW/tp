package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EventTypeTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EventType(null));
    }

    @Test
    public void constructor_invalidEventType_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new EventType(" "));
    }

    @Test
    public void isValidEventType() {
        // null event type
        assertThrows(NullPointerException.class, () -> EventType.isValidEventType(null));

        // invalid event types
        assertFalse(EventType.isValidEventType("")); // empty string
        assertFalse(EventType.isValidEventType(" ")); // spaces only
        assertFalse(EventType.isValidEventType("---")); // no letter or digit
        assertFalse(EventType.isValidEventType("Wedding!")); // unsupported symbol
        assertFalse(EventType.isValidEventType("Pre/Wedding")); // slash not allowed
        assertFalse(EventType.isValidEventType(" Wedding")); // leading space
        assertFalse(EventType.isValidEventType("Wedding ")); // trailing space
        assertFalse(EventType.isValidEventType("Corporate  Event")); // consecutive spaces
        assertFalse(EventType.isValidEventType("a".repeat(EventType.MAX_LENGTH + 1))); // too long

        // valid event types
        assertTrue(EventType.isValidEventType("Wedding")); // letters only
        assertTrue(EventType.isValidEventType("2026")); // digits only
        assertTrue(EventType.isValidEventType("Corporate 2026")); // letters, digits and a space
        assertTrue(EventType.isValidEventType("Pre-Wedding")); // hyphen
        assertTrue(EventType.isValidEventType("Hôn lễ")); // non-ASCII letters
        assertTrue(EventType.isValidEventType("a".repeat(EventType.MAX_LENGTH))); // maximum length
    }
}
