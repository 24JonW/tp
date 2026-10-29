package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

class PersonListPanelTest {

    private static final String LONG_EMAIL =
            "averylongphotographyclientemailaddressfortesting@international-photography.example.com";

    @Test
    void formatPersonDetails_personWithLongEmail_containsCompleteEmail() {
        Person person = new PersonBuilder()
                .withEmail(LONG_EMAIL)
                .build();

        String details = PersonListPanel.formatPersonDetails(person);

        assertTrue(details.contains("Email: " + LONG_EMAIL));
    }
}
