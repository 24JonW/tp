package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AdvanceStageCommand;

public class AdvanceStageCommandParserTest {

    private final AdvanceStageCommandParser parser = new AdvanceStageCommandParser();

    @Test
    public void parse_validArgs_returnsAdvanceStageCommand() {
        assertParseSuccess(parser, "1", new AdvanceStageCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AdvanceStageCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "a", expectedMessage);
        assertParseFailure(parser, "1 extra", expectedMessage);
    }
}
