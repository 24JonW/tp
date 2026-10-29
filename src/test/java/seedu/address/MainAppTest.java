package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonUserPrefsStorage;

/**
 * Tests recovery from invalid application preferences.
 */
public class MainAppTest {
    @TempDir
    public Path testFolder;

    @Test
    public void initPrefs_nullGuiSettings_restoresAndSavesDefaults() throws Exception {
        Path prefsPath = testFolder.resolve("preferences.json");
        Files.writeString(prefsPath, "{\"guiSettings\":null}");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(prefsPath);

        UserPrefs prefs = new MainApp().initPrefs(storage);

        assertEquals(new UserPrefs(), prefs);
        assertEquals(prefs, storage.readUserPrefs().orElseThrow());
    }
}
