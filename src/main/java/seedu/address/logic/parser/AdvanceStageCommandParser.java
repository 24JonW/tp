package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AdvanceStageCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code AdvanceStageCommand}.
 */
public class AdvanceStageCommandParser implements Parser<AdvanceStageCommand> {

    @Override
    public AdvanceStageCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new AdvanceStageCommand(index);
        } catch (ParseException exception) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AdvanceStageCommand.MESSAGE_USAGE), exception);
        }
    }
}
