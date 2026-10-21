package seedu.address.model.util;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Email;
import seedu.address.model.person.EventDate;
import seedu.address.model.person.EventType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Stage;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new EventType("Wedding"), new EventDate("2026-12-20"), Stage.ENQUIRY),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new EventType("Graduation"), new EventDate("2026-11-15"), Stage.ENQUIRY),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new EventType("Birthday"), new EventDate("2027-01-08"), Stage.ENQUIRY),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new EventType("Corporate"), new EventDate("2026-10-30"), Stage.ENQUIRY),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new EventType("Wedding"), new EventDate("2027-02-14"), Stage.ENQUIRY),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new EventType("Birthday"), new EventDate("2026-12-01"), Stage.ENQUIRY)
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

}
