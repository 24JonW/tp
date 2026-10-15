package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TYPE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void isSameEngagement() {
        // same object -> returns true
        assertTrue(ALICE.isSameEngagement(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameEngagement(null));

        // all fields same -> returns true
        assertTrue(ALICE.isSameEngagement(new PersonBuilder(ALICE).build()));

        // name, email and event type differ only in case -> returns true
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase())
                .withEmail(VALID_EMAIL_BOB.toUpperCase()).withEventType(VALID_EVENT_TYPE_BOB.toUpperCase()).build();
        assertTrue(BOB.isSameEngagement(editedBob));

        // same client, different event type and date (repeat client) -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withEventType(VALID_EVENT_TYPE_BOB)
                .withEventDate(VALID_EVENT_DATE_BOB).build();
        assertFalse(ALICE.isSameEngagement(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameEngagement(editedAlice));

        // different phone, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSameEngagement(editedAlice));

        // different email, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSameEngagement(editedAlice));

        // different event type, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEventType(VALID_EVENT_TYPE_BOB).build();
        assertFalse(ALICE.isSameEngagement(editedAlice));

        // different event date, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEventDate(VALID_EVENT_DATE_BOB).build();
        assertFalse(ALICE.isSameEngagement(editedAlice));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSameEngagement(editedBob));
    }

    @Test
    public void hasSimilarContactDetails() {
        // null -> throws NullPointerException
        assertThrows(NullPointerException.class, () -> ALICE.hasSimilarContactDetails(null));

        // same object -> returns false
        assertFalse(ALICE.hasSimilarContactDetails(ALICE));

        // same name, phone and email, different event (repeat client) -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withEventType(VALID_EVENT_TYPE_BOB)
                .withEventDate(VALID_EVENT_DATE_BOB).build();
        assertFalse(ALICE.hasSimilarContactDetails(editedAlice));

        // same name, different phone and email (different client) -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.hasSimilarContactDetails(editedAlice));

        // no shared details -> returns false
        assertFalse(ALICE.hasSimilarContactDetails(BOB));

        // same name and phone, different email -> returns true
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertTrue(ALICE.hasSimilarContactDetails(editedAlice));

        // same name and email, different phone -> returns true
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertTrue(ALICE.hasSimilarContactDetails(editedAlice));

        // same phone and email, different name -> returns true
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertTrue(ALICE.hasSimilarContactDetails(editedAlice));

        // only phone shared -> returns true
        Person editedBob = new PersonBuilder(BOB).withPhone(ALICE.getPhone().value).build();
        assertTrue(ALICE.hasSimilarContactDetails(editedBob));

        // only email shared, differing in case -> returns true
        editedBob = new PersonBuilder(BOB).withEmail(ALICE.getEmail().value.toUpperCase()).build();
        assertTrue(ALICE.hasSimilarContactDetails(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different event type -> returns false
        editedAlice = new PersonBuilder(ALICE).withEventType(VALID_EVENT_TYPE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different event date -> returns false
        editedAlice = new PersonBuilder(ALICE).withEventDate(VALID_EVENT_DATE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void constructor_nullStage_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Person(
                new Name("Test Person"),
                new Phone("91234567"),
                new Email("test@example.com"),
                new EventType("Wedding"),
                new EventDate("2026-12-01"),
                null));
    }

    @Test
    public void equals_differentStage_returnsFalse() {
        Person bookedPerson = new PersonBuilder(ALICE)
                .withStage("Booked")
                .build();

        assertFalse(ALICE.equals(bookedPerson));
    }

    @Test
    public void hashCode_samePerson_returnsSameHashCode() {
        Person aliceCopy = new PersonBuilder(ALICE).build();

        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail()
                + ", eventType=" + ALICE.getEventType()
                + ", eventDate=" + ALICE.getEventDate()
                + ", stage=" + ALICE.getStage()
                + "}";
        assertEquals(expected, ALICE.toString());
    }
}
