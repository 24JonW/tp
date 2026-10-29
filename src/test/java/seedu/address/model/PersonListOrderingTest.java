package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests displayed ordering against an independent stable-sort reference.
 */
public class PersonListOrderingTest {
    @Test
    public void showAllPersons_dateBoundaries_ordersChronologically() {
        Model model = new ModelManager();
        Person latest = createPerson(0, "9999-12-31");
        Person leapDay = createPerson(1, "2000-02-29");
        Person earliest = createPerson(2, "0001-01-01");
        model.addPerson(latest);
        model.addPerson(leapDay);
        model.addPerson(earliest);
        model.showAllPersons(true);
        assertEquals(List.of(earliest, leapDay, latest), model.getFilteredPersonList());
        assertEquals(List.of(latest, leapDay, earliest), model.getAddressBook().getPersonList());
    }

    @Test
    public void showAllPersons_manyTiesAndMutations_matchesStableReferenceSort() {
        Random random = new Random(2103);
        Model model = new ModelManager();
        List<Person> insertionOrder = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            Person added = createPerson(i, nextDate(random));
            insertionOrder.add(added);
            model.addPerson(added);
        }
        for (int i = 0; i < 300; i++) {
            model.showAllPersons(true);
            assertOrder(model, insertionOrder);
            int position = random.nextInt(insertionOrder.size());
            Person original = insertionOrder.get(position);
            switch (i % 3) {
                case 0:
                    Person edited = new PersonBuilder(original).withEventDate(nextDate(random)).build();
                    model.setPerson(original, edited);
                    insertionOrder.set(position, edited);
                    // Direct replacements must also maintain a valid observable sorted list.
                    assertOrder(model, insertionOrder);
                    break;
                case 1:
                    model.deletePerson(original);
                    insertionOrder.remove(position);
                    break;
                default:
                    Person added = createPerson(150 + i, nextDate(random));
                    model.addPerson(added);
                    insertionOrder.add(added);
                    break;
            }
            model.showAllPersons(true);
            assertOrder(model, insertionOrder);
            model.showAllPersons(false);
            assertEquals(insertionOrder, model.getFilteredPersonList());
        }
    }

    @Test
    public void setAddressBook_afterSorting_resetsViewAndHandlesEmptyData() {
        Model model = new ModelManager();
        model.addPerson(createPerson(0, "2030-01-01"));
        model.showAllPersons(true);
        AddressBook replacement = new AddressBook();
        replacement.addPerson(createPerson(1, "2031-01-01"));
        replacement.addPerson(createPerson(2, "2020-01-01"));
        model.setAddressBook(replacement);
        assertEquals(replacement.getPersonList(), model.getFilteredPersonList());
        assertEquals("All engagements (2)", model.getPersonListHeading());
        model.showAllPersons(true);
        model.setAddressBook(new AddressBook());
        assertEquals(List.of(), model.getFilteredPersonList());
        assertEquals("All engagements (0)", model.getPersonListHeading());
    }

    @Test
    public void getFilteredPersonList_sortedView_remainsUnmodifiable() {
        Model model = new ModelManager();
        Person original = createPerson(0, "2030-01-01");
        model.addPerson(original);
        model.showAllPersons(true);
        assertThrows(UnsupportedOperationException.class, () -> model.getFilteredPersonList().clear());
        assertThrows(UnsupportedOperationException.class, () -> model.getFilteredPersonList().remove(original));
        assertThrows(UnsupportedOperationException.class, () -> model.getFilteredPersonList().add(original));
        assertThrows(UnsupportedOperationException.class, () -> model.getFilteredPersonList().set(0, original));
        assertEquals(List.of(original), model.getAddressBook().getPersonList());
    }

    @Test
    public void updateFilteredPersonList_nullPredicate_preservesSortedView() {
        Model model = new ModelManager();
        model.addPerson(createPerson(0, "2030-01-01"));
        model.showAllPersons(true);
        assertThrows(NullPointerException.class, () -> model.updateFilteredPersonList(null));
        assertEquals("All engagements by event date (1)", model.getPersonListHeading());
    }

    private void assertOrder(Model model, List<Person> insertionOrder) {
        List<Person> expected = new ArrayList<>(insertionOrder);
        expected.sort(Comparator.comparing(person -> person.getEventDate().value));
        assertEquals(expected, model.getFilteredPersonList());
        assertEquals(insertionOrder, model.getAddressBook().getPersonList());
    }

    private Person createPerson(int identifier, String date) {
        return new PersonBuilder().withName("Contact " + identifier).withEventDate(date).build();
    }

    private String nextDate(Random random) {
        return LocalDate.of(2026, 1, 1).plusDays(random.nextInt(5)).toString();
    }
}
