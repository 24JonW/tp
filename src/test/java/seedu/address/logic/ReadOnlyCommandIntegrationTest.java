package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

/**
 * Tests commands that must remain available when address book storage is not writable.
 */
public class ReadOnlyCommandIntegrationTest {
    @TempDir
    public Path temporaryFolder;

    @Test
    public void execute_helpAndExit_doesNotSaveAddressBook() throws Exception {
        Model model = new ModelManager();
        Logic logic = createLogicWithNoWriteStorage(model);

        assertEquals(HelpCommand.SHOWING_HELP_MESSAGE,
                logic.execute(HelpCommand.COMMAND_WORD).getFeedbackToUser());
        assertEquals(ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT,
                logic.execute(ExitCommand.COMMAND_WORD).getFeedbackToUser());
    }

    @Test
    public void execute_find_doesNotSaveAddressBook() throws Exception {
        Model model = new ModelManager();
        model.addPerson(AMY);
        Logic logic = createLogicWithNoWriteStorage(model);

        assertEquals(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 1),
                logic.execute("find n/Amy").getFeedbackToUser());
        assertEquals(List.of(AMY), logic.getFilteredPersonList());
    }

    private Logic createLogicWithNoWriteStorage(Model model) {
        JsonAddressBookStorage noWriteStorage = new JsonAddressBookStorage(
                temporaryFolder.resolve("contacts.json")) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) {
                throw new AssertionError("A non-mutating command must not save data.");
            }
        };
        return new LogicManager(model, new StorageManager(noWriteStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("preferences.json"))));
    }
}
