package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNSUPPORTED_PARAMETER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.PersonSearchPredicate;

public class FindCommandParserTest {

    private static final String INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
    private final FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_unsupportedPrefix_throwsParseException() {
        assertParseFailure(parser, " n/Alice s/Booked", String.format(MESSAGE_UNSUPPORTED_PARAMETER, "s/"));
        assertParseFailure(parser, " n/Alice d/2026-12-20", String.format(MESSAGE_UNSUPPORTED_PARAMETER, "d/"));
        assertParseFailure(parser, " n/Alice x/oops", String.format(MESSAGE_UNSUPPORTED_PARAMETER, "x/"));
        assertParseFailure(parser, " x/oops n/Alice", String.format(MESSAGE_UNSUPPORTED_PARAMETER, "x/"));
        assertParseFailure(parser, " n/Alice\tx/oops", String.format(MESSAGE_UNSUPPORTED_PARAMETER, "x/"));
    }

    @Test
    public void parse_name_success() throws Exception {
        FindCommand expected = new FindCommand(
                new PersonSearchPredicate(
                        PersonSearchPredicate.Field.NAME,
                        "Alice Pauline"));

        assertParseSuccess(parser, " n/Alice Pauline", expected);
    }

    @Test
    public void parse_nameWithRelationMarker_success() throws Exception {
        assertParseSuccess(parser, " n/Raj s/o Kumar", new FindCommand(
                new PersonSearchPredicate(PersonSearchPredicate.Field.NAME, "Raj s/o Kumar")));
        // a partial search may end with the marker
        assertParseSuccess(parser, " n/Raj s/o", new FindCommand(
                new PersonSearchPredicate(PersonSearchPredicate.Field.NAME, "Raj s/o")));
        assertParseSuccess(parser, " n/Muthu A/L ", new FindCommand(
                new PersonSearchPredicate(PersonSearchPredicate.Field.NAME, "Muthu A/L")));
    }

    @Test
    public void parse_phone_success() throws Exception {
        FindCommand expected = new FindCommand(
                new PersonSearchPredicate(
                        PersonSearchPredicate.Field.PHONE,
                        "+6591234567"));

        assertParseSuccess(parser, " p/+65 9123-4567", expected);

    }

    @Test
    public void parse_email_success() throws Exception {
        FindCommand expected = new FindCommand(
                new PersonSearchPredicate(
                        PersonSearchPredicate.Field.EMAIL,
                        "alice@example.com"));

        assertParseSuccess(parser, " e/alice@example.com", expected);
    }

    @Test
    public void parse_eventType_success() throws Exception {
        FindCommand expected = new FindCommand(
                new PersonSearchPredicate(
                        PersonSearchPredicate.Field.EVENT_TYPE,
                        "Wedding"));

        assertParseSuccess(parser, " t/Wedding", expected);
    }

    @Test
    public void parse_multipleFields_throwsParseException() {
        assertParseFailure(parser, " n/Alice p/91234567", INVALID_FORMAT);
    }

    @Test
    public void parse_emptyValue_throwsParseException() {
        assertParseFailure(parser, "n/", INVALID_FORMAT);
        assertParseFailure(parser, "p/ - ", INVALID_FORMAT);
    }

    @Test
    public void parse_duplicateField_throwsParseException() {
        assertParseFailure(parser, "n/Alice n/Bob", INVALID_FORMAT);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, "Alice", INVALID_FORMAT);
    }

}
