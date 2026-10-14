package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.UpcomingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the upcoming command, which accepts no arguments.
 */
public class UpcomingCommandParser implements Parser<UpcomingCommand> {
    public static final String MESSAGE_INVALID_FORMAT = "Invalid command format. Usage: upcoming";

    @Override
    public UpcomingCommand parse(String args) throws ParseException {
        requireNonNull(args);
        if (!args.matches("[ \\t]*")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }
        return new UpcomingCommand();
    }
}
