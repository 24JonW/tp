package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests upcoming execution through the parser and logic without storage writes.
 */
public class UpcomingCommandIntegrationTest {
    @TempDir
    public Path temporaryFolder;

    @Test
    public void execute_upcoming_doesNotSaveOrChangeStoredData() throws Exception {
        Model model = new ModelManager();
        // Use an interior date so crossing midnight during this integration test does not affect inclusion.
        Person included = new PersonBuilder().withName("Included")
                .withEventDate(LocalDate.now().plusDays(2).toString()).build();
        Person outside = new PersonBuilder().withName("Outside").withEventDate("2000-01-01").build();
        model.addPerson(outside);
        model.addPerson(included);
        Path dataPath = temporaryFolder.resolve("contacts.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(dataPath);
        storage.saveAddressBook(model.getAddressBook());
        String originalData = Files.readString(dataPath);
        Logic logic = createReadOnlyLogic(model, dataPath);

        assertEquals("Showing 1 upcoming engagement(s).", logic.execute("upcoming").getFeedbackToUser());
        assertEquals(List.of(included), logic.getFilteredPersonList());
        assertEquals(originalData, Files.readString(dataPath));
        assertEquals(List.of(outside, included), storage.readAddressBook().orElseThrow().getPersonList());

        String heading = logic.getPersonListHeading();
        assertThrows(ParseException.class, () -> logic.execute("upcoming 7"));
        assertEquals(heading, logic.getPersonListHeading());
        assertEquals(List.of(included), logic.getFilteredPersonList());
        logic.execute("list sort/date");
        assertEquals(List.of(outside, included), logic.getFilteredPersonList());
        assertEquals("All engagements by event date (2)", logic.getPersonListHeading());
    }

    @Test
    public void execute_emptyUpcoming_doesNotCreateDataFile() throws Exception {
        Path dataPath = temporaryFolder.resolve("missing.json");
        Model model = new ModelManager();
        createReadOnlyLogic(model, dataPath).execute("upcoming");
        assertFalse(Files.exists(dataPath));
        assertEquals(List.of(), model.getFilteredPersonList());
    }

    private Logic createReadOnlyLogic(Model model, Path dataPath) {
        JsonAddressBookStorage noWriteStorage = new JsonAddressBookStorage(dataPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) {
                throw new AssertionError("Upcoming must not write to storage.");
            }
        };
        return new LogicManager(model, new StorageManager(noWriteStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("prefs.json"))));
    }
}
