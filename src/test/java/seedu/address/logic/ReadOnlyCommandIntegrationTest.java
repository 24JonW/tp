package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

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
        JsonAddressBookStorage noWriteStorage = new JsonAddressBookStorage(
                temporaryFolder.resolve("contacts.json")) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) {
                throw new AssertionError("A non-mutating command must not save data.");
            }
        };
        Logic logic = new LogicManager(model, new StorageManager(noWriteStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("preferences.json"))));

        assertEquals(HelpCommand.SHOWING_HELP_MESSAGE,
                logic.execute(HelpCommand.COMMAND_WORD).getFeedbackToUser());
        assertEquals(ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT,
                logic.execute(ExitCommand.COMMAND_WORD).getFeedbackToUser());
    }
}
