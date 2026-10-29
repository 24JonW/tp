package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

/**
 * Exercises list parsing, model ordering and storage boundaries together.
 */
public class ListCommandIntegrationTest {
    @TempDir
    public Path temporaryFolder;

    @Test
    public void execute_listVariants_doesNotSaveOrChangeStoredOrder() throws Exception {
        Model model = createModel();
        Path dataPath = temporaryFolder.resolve("contacts.json");
        JsonAddressBookStorage writableStorage = new JsonAddressBookStorage(dataPath);
        writableStorage.saveAddressBook(model.getAddressBook());
        String originalData = Files.readString(dataPath);
        JsonAddressBookStorage noWriteStorage = new JsonAddressBookStorage(dataPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook) {
                throw new AssertionError("A list command must not save data.");
            }
        };
        Logic logic = new LogicManager(model, new StorageManager(noWriteStorage,
                new JsonUserPrefsStorage(temporaryFolder.resolve("prefs.json"))));

        logic.execute("list sort/date");
        assertEquals("Earlier", logic.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("All engagements by event date (2)", logic.getPersonListHeading());
        assertEquals(originalData, Files.readString(dataPath));
        Model restartedModel = new ModelManager(writableStorage.readAddressBook().orElseThrow(), new UserPrefs());
        assertEquals("Later", restartedModel.getFilteredPersonList().get(0).getName().fullName);
        logic.execute("list");
        assertEquals("Later", logic.getFilteredPersonList().get(0).getName().fullName);
        assertEquals(originalData, Files.readString(dataPath));
    }

    @Test
    public void execute_invalidList_preservesSortedOrFilteredViewAndData() throws Exception {
        Model model = createModel();
        Logic logic = new LogicManager(model, new StorageManager(
                new JsonAddressBookStorage(temporaryFolder.resolve("contacts.json")),
                new JsonUserPrefsStorage(temporaryFolder.resolve("prefs.json"))));
        for (boolean isSortedView : new boolean[]{true, false}) {
            if (isSortedView) {
                logic.execute("list sort/date");
            } else {
                model.updateFilteredPersonList(person -> person.getName().fullName.equals("Later"));
            }
            List<Person> originalView = List.copyOf(logic.getFilteredPersonList());
            List<Person> originalData = List.copyOf(model.getAddressBook().getPersonList());
            String originalHeading = logic.getPersonListHeading();
            for (String input : new String[]{"list extra", "list sort/", "list sort/name",
                "list sort/date sort/date", "list stage/Booked", "list sort/date\n"}) {
                assertThrows(ParseException.class, () -> logic.execute(input));
                assertEquals(originalView, logic.getFilteredPersonList());
                assertEquals(originalHeading, logic.getPersonListHeading());
                assertEquals(originalData, model.getAddressBook().getPersonList());
            }
        }
    }

    private Model createModel() {
        Model model = new ModelManager();
        model.addPerson(new PersonBuilder().withName("Later").withEventDate("2030-12-31").build());
        model.addPerson(new PersonBuilder().withName("Earlier").withEventDate("2000-01-01").build());
        return model;
    }
}
