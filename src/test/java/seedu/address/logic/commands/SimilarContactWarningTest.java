package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SimilarContactWarningTest {

    private static final String ALICE_PHONE = ALICE.getPhone().value;
    private static final String ALICE_EMAIL = ALICE.getEmail().value;

    @Test
    public void generate_nullInputs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> SimilarContactWarning.generate(null, ALICE));
        assertThrows(NullPointerException.class, () -> SimilarContactWarning.generate(List.of(ALICE), null));
    }

    @Test
    public void generate_noSimilarContacts_returnsEmptyString() {
        assertEquals("", SimilarContactWarning.generate(List.of(), ALICE));

        // the target itself is not reported
        assertEquals("", SimilarContactWarning.generate(List.of(ALICE, BENSON), ALICE));
    }

    @Test
    public void generate_similarContacts_listsSharedDetailsWithIndexes() {
        Person samePhone = new PersonBuilder(BENSON).withPhone(ALICE_PHONE).build();
        Person sameEmail = new PersonBuilder(CARL).withEmail(ALICE_EMAIL).build();
        Person samePhoneAndEmail = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        List<Person> displayedPersons = List.of(ALICE, samePhone, sameEmail, samePhoneAndEmail);

        String expectedContacts = "#2 " + BENSON.getName() + " (same phone), "
                + "#3 " + CARL.getName() + " (same email), "
                + "#4 " + VALID_NAME_BOB + " (same phone and email)";
        assertEquals(String.format(SimilarContactWarning.MESSAGE_WARNING, expectedContacts),
                SimilarContactWarning.generate(displayedPersons, ALICE));
    }

    @Test
    public void generate_clientWithSeveralRecords_listedOnce() {
        Person aliceWedding = new PersonBuilder(ALICE).withEventType("Wedding")
                .withEventDate("2026-12-20").build();
        Person aliceBirthday = new PersonBuilder(ALICE).withEventType("Birthday")
                .withEventDate("2027-03-01").build();
        Person target = new PersonBuilder(BENSON).withPhone(ALICE_PHONE).build();
        List<Person> displayedPersons = List.of(CARL, aliceWedding, aliceBirthday);

        String expectedContacts = "#2 " + ALICE.getName() + " (same phone)";
        assertEquals(String.format(SimilarContactWarning.MESSAGE_WARNING, expectedContacts),
                SimilarContactWarning.generate(displayedPersons, target));
    }

    @Test
    public void generate_moreThanMaxSimilarContacts_summarizesRemainder() {
        Person first = new PersonBuilder().withName("Amy Bee").withPhone(ALICE_PHONE).build();
        Person second = new PersonBuilder().withName("Ben Cee").withPhone(ALICE_PHONE).build();
        Person third = new PersonBuilder().withName("Cal Dee").withPhone(ALICE_PHONE).build();
        Person fourth = new PersonBuilder().withName("Dan Eff").withPhone(ALICE_PHONE).build();
        Person fifth = new PersonBuilder().withName("Eve Gee").withPhone(ALICE_PHONE).build();
        List<Person> displayedPersons = List.of(first, second, third, fourth, fifth);

        String expectedContacts = "#1 Amy Bee (same phone), #2 Ben Cee (same phone), #3 Cal Dee (same phone)"
                + String.format(SimilarContactWarning.MESSAGE_MORE_CONTACTS, 2);
        assertEquals(String.format(SimilarContactWarning.MESSAGE_WARNING, expectedContacts),
                SimilarContactWarning.generate(displayedPersons, ALICE));
    }
}
