package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UpcomingCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tests upcoming dispatch and rejection of arguments or multiline input.
 */
public class UpcomingCommandParserTest {
    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parse_validCommand_acceptsCaseAndHorizontalWhitespace() throws Exception {
        for (String input : new String[]{"upcoming", "UPCOMING", " \tUpcoming \t"}) {
            assertInstanceOf(UpcomingCommand.class, parser.parseCommand(input));
        }
    }

    @Test
    public void parse_invalidArguments_rejected() {
        for (String input : new String[]{"upcoming 7", "upcoming sort/date", "upcoming stage/Booked",
            "upcoming\n", "upcoming\u2028", "upcoming \u0000", "upcoming\rlist", "upcoming\u2029"}) {
            ParseException exception = assertThrows(ParseException.class, () -> parser.parseCommand(input));
            assertEquals(UpcomingCommandParser.MESSAGE_INVALID_FORMAT, exception.getMessage(), input);
        }
        assertThrows(ParseException.class, () -> parser.parseCommand("upcomingExtra"));
    }

    @Test
    public void parse_nullArguments_rejected() {
        assertThrows(NullPointerException.class, () -> new UpcomingCommandParser().parse(null));
    }
}
