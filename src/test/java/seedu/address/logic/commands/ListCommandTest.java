package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests list ordering, storage isolation and displayed-index integration with existing commands.
 */
public class ListCommandTest {
    private final Person later = new PersonBuilder().withName("Later").withEventDate("2030-01-02").build();
    private final Person earlier = new PersonBuilder().withName("Earlier").withEventDate("2000-02-29").build();
    private final Person tied = new PersonBuilder().withName("Tied").withEventDate("2000-02-29").build();
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        model.addPerson(later);
        model.addPerson(earlier);
        model.addPerson(tied);
    }

    @Test
    public void execute_plainList_restoresAllRecordsInInsertionOrder() {
        model.updateFilteredPersonList(person -> person.equals(earlier));
        assertEquals("Showing 3 engagement(s).", new ListCommand().execute(model).getFeedbackToUser());
        assertEquals(List.of(later, earlier, tied), model.getFilteredPersonList());
        assertEquals("All engagements (3)", model.getPersonListHeading());
    }

    @Test
    public void execute_sortFromEmptyFilter_ordersAllDatesAndPreservesTies() {
        model.updateFilteredPersonList(person -> false);
        assertEquals("Showing 3 engagement(s).", new ListCommand(true).execute(model).getFeedbackToUser());
        assertEquals(List.of(earlier, tied, later), model.getFilteredPersonList());
        assertEquals("All engagements by event date (3)", model.getPersonListHeading());
        assertEquals(List.of(later, earlier, tied), model.getAddressBook().getPersonList());
        new ListCommand(true).execute(model);
        assertEquals(List.of(earlier, tied, later), model.getFilteredPersonList());
        new ListCommand().execute(model);
        assertEquals(List.of(later, earlier, tied), model.getFilteredPersonList());
    }

    @Test
    public void execute_emptyAndSingleRecord_returnsCorrectFeedback() {
        Model emptyModel = new ModelManager();
        for (boolean isSortedByDate : new boolean[]{false, true}) {
            assertEquals("No engagements to display.",
                    new ListCommand(isSortedByDate).execute(emptyModel).getFeedbackToUser());
            assertEquals(isSortedByDate ? "All engagements by event date (0)" : "All engagements (0)",
                    emptyModel.getPersonListHeading());
        }
        emptyModel.addPerson(later);
        assertEquals("Showing 1 engagement(s).", new ListCommand(true).execute(emptyModel).getFeedbackToUser());
        assertEquals(List.of(later), emptyModel.getFilteredPersonList());
    }

    @Test
    public void execute_afterSort_filterRestoresInsertionOrder() {
        new ListCommand(true).execute(model);
        model.updateFilteredPersonList(person -> !person.equals(tied));
        assertEquals(List.of(later, earlier), model.getFilteredPersonList());
        assertEquals("Engagements (2)", model.getPersonListHeading());
    }

    @Test
    public void execute_afterSort_deleteUsesDisplayedIndexAndRestoresOrder() throws Exception {
        new ListCommand(true).execute(model);
        new DeleteCommand(INDEX_FIRST_PERSON).execute(model);
        assertEquals(List.of(later, tied), model.getAddressBook().getPersonList());
        assertEquals(List.of(later, tied), model.getFilteredPersonList());
        assertEquals("All engagements (2)", model.getPersonListHeading());
    }

    @Test
    public void execute_afterSort_editUsesDisplayedIndexAndRetainsInsertionPosition() throws Exception {
        new ListCommand(true).execute(model);
        EditCommand.EditPersonDescriptor descriptor = new EditCommand.EditPersonDescriptor();
        descriptor.setEventDate(later.getEventDate());
        new EditCommand(INDEX_FIRST_PERSON, descriptor).execute(model);
        Person edited = new PersonBuilder(earlier).withEventDate("2030-01-02").build();
        assertEquals(List.of(later, edited, tied), model.getFilteredPersonList());
        new ListCommand(true).execute(model);
        assertEquals(List.of(tied, later, edited), model.getFilteredPersonList());
    }

    @Test
    public void execute_afterSort_addRestoresOrderAndAppendsRecord() {
        new ListCommand(true).execute(model);
        Person added = new PersonBuilder().withName("Added").withEventDate("2000-02-29").build();
        model.addPerson(added);
        assertEquals(List.of(later, earlier, tied, added), model.getFilteredPersonList());
        new ListCommand(true).execute(model);
        assertEquals(List.of(earlier, tied, added, later), model.getFilteredPersonList());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ListCommand().execute(null));
    }

    @Test
    public void equals_comparesSortChoice() {
        assertEquals(new ListCommand(), new ListCommand(false));
        assertEquals(new ListCommand(true), new ListCommand(true));
        assertNotEquals(new ListCommand(), new ListCommand(true));
        assertNotEquals(null, new ListCommand());
    }
}
