package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_DATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TYPE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TYPE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withEventType("Wedding").withEventDate("2026-01-01").withEmail("alice@example.com")
            .withPhone("94351253").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withEventType("Portrait").withEventDate("2026-02-02")
            .withEmail("johnd@example.com").withPhone("98765432")
            .build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withEventType("Corporate").withEventDate("2026-03-03").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withEventType("Wedding").withEventDate("2026-04-04").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("94822240")
            .withEmail("werner@example.com").withEventType("Portrait").withEventDate("2026-05-05").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("94824270")
            .withEmail("lydia@example.com").withEventType("Corporate").withEventDate("2026-06-06").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("94824420")
            .withEmail("anna@example.com").withEventType("Wedding").withEventDate("2026-07-07").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("84824240")
            .withEmail("stefan@example.com").withEventType("Portrait").withEventDate("2026-08-08").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("84821310")
            .withEmail("hans@example.com").withEventType("Corporate").withEventDate("2026-09-09").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withEventType(VALID_EVENT_TYPE_AMY).withEventDate(VALID_EVENT_DATE_AMY).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withEventType(VALID_EVENT_TYPE_BOB).withEventDate(VALID_EVENT_DATE_BOB).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
