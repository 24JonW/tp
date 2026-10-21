package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AdvanceStageCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToAdvance = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person advancedPerson = new PersonBuilder(personToAdvance).withStage("Booked").build();
        AdvanceStageCommand command = new AdvanceStageCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(AdvanceStageCommand.MESSAGE_SUCCESS,
                Messages.format(advancedPerson));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToAdvance, advancedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person personToAdvance = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person advancedPerson = new PersonBuilder(personToAdvance).withStage("Booked").build();
        AdvanceStageCommand command = new AdvanceStageCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(AdvanceStageCommand.MESSAGE_SUCCESS,
                Messages.format(advancedPerson));
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToAdvance, advancedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deliveredStage_throwsCommandException() {
        Person original = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person delivered = new PersonBuilder(original).withStage("Delivered").build();
        model.setPerson(original, delivered);

        assertCommandFailure(new AdvanceStageCommand(INDEX_FIRST_PERSON), model,
                AdvanceStageCommand.MESSAGE_ALREADY_AT_FINAL_STAGE);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        assertCommandFailure(new AdvanceStageCommand(outOfBoundIndex), model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        AdvanceStageCommand firstCommand = new AdvanceStageCommand(INDEX_FIRST_PERSON);
        AdvanceStageCommand firstCommandCopy = new AdvanceStageCommand(INDEX_FIRST_PERSON);
        AdvanceStageCommand secondCommand = new AdvanceStageCommand(INDEX_SECOND_PERSON);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(firstCommandCopy));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(1));
    }

    @Test
    public void toStringMethod() {
        AdvanceStageCommand command = new AdvanceStageCommand(INDEX_FIRST_PERSON);
        String expected = AdvanceStageCommand.class.getCanonicalName()
                + "{targetIndex=" + INDEX_FIRST_PERSON + "}";
        assertEquals(expected, command.toString());
    }
}
