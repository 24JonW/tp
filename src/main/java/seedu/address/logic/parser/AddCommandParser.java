package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STAGE;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.EventDate;
import seedu.address.model.person.EventType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Stage;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_EVENT_TYPE, PREFIX_EVENT_DATE, PREFIX_STAGE);

        ParserUtil.verifyNoUnsupportedPrefixes(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_EVENT_TYPE, PREFIX_EVENT_DATE, PREFIX_STAGE);
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_EVENT_TYPE, PREFIX_EVENT_DATE, PREFIX_STAGE);
        try {
            ParserUtil.verifyRequiredPrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                    PREFIX_EVENT_TYPE, PREFIX_EVENT_DATE);
        } catch (ParseException pe) {
            throw new ParseException(pe.getMessage() + "\n" + AddCommand.MESSAGE_USAGE, pe);
        }
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        EventType eventType = ParserUtil.parseEventType(argMultimap.getValue(PREFIX_EVENT_TYPE).get());
        EventDate eventDate = ParserUtil.parseEventDate(argMultimap.getValue(PREFIX_EVENT_DATE).get());
        Stage stage = Stage.ENQUIRY;
        if (argMultimap.getValue(PREFIX_STAGE).isPresent()) {
            stage = ParserUtil.parseStage(argMultimap.getValue(PREFIX_STAGE).get());
        }

        Person person = new Person(name, phone, email, eventType, eventDate, stage);

        return new AddCommand(person);
    }

}
