package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_DATE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_TYPE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.TypicalPersons.AMY;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddCommand;
import seedu.address.model.ModelManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

/**
 * Tests that command logging does not retain client personal data.
 */
public class LoggingPrivacyTest {
    @TempDir
    public Path temporaryFolder;

    @Test
    public void execute_commandWithPersonalData_logsCommandTypeOnly() throws Exception {
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + EVENT_TYPE_DESC_AMY + EVENT_DATE_DESC_AMY;
        Logic logic = new LogicManager(new ModelManager(), new StorageManager(
                new JsonAddressBookStorage(temporaryFolder.resolve("contacts.json")),
                new JsonUserPrefsStorage(temporaryFolder.resolve("preferences.json"))));
        Logger logicLogger = LogsCenter.getLogger(LogicManager.class);
        RecordingHandler recordingHandler = new RecordingHandler();
        logicLogger.addHandler(recordingHandler);

        try {
            logic.execute(addCommand);
        } finally {
            logicLogger.removeHandler(recordingHandler);
        }

        String loggedMessages = String.join("\n", recordingHandler.getMessages());
        assertTrue(loggedMessages.contains(AddCommand.class.getSimpleName()));
        assertFalse(loggedMessages.contains(AMY.getName().toString()));
        assertFalse(loggedMessages.contains(AMY.getPhone().toString()));
        assertFalse(loggedMessages.contains(AMY.getEmail().toString()));
    }

    /**
     * Records log messages emitted during a test.
     */
    private static class RecordingHandler extends Handler {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            messages.add(record.getMessage());
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        private List<String> getMessages() {
            return messages;
        }
    }
}
