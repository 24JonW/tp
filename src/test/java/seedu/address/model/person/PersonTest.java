package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TYPE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // all fields same -> returns true
        assertTrue(ALICE.isSamePerson(new PersonBuilder(ALICE).build()));

        // name, email and event type differ only in case -> returns true
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase())
                .withEmail(VALID_EMAIL_BOB.toUpperCase()).withEventType(VALID_EVENT_TYPE_BOB.toUpperCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // same client, different event type and date (repeat client) -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withEventType(VALID_EVENT_TYPE_BOB)
                .withEventDate(VALID_EVENT_DATE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different phone, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different email, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different event type, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEventType(VALID_EVENT_TYPE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different event date, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEventDate(VALID_EVENT_DATE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
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
