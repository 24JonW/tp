package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.PersonSearchPredicate;

/**
 * Finds and lists contacts whose selected field contains the search value.
 * Matching is case-insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds contacts by exactly one field.\n"
            + "Parameters: n/NAME, p/PHONE, e/EMAIL, or t/EVENT_TYPE\n"
            + "Examples:\n"
            + "find n/Alice Pauline\n"
            + "find p/91234567\n"
            + "find e/alice@example.com\n"
            + "find t/Wedding";

    public static final String MESSAGE_NO_MATCHING_CONTACTS = "No matching contacts found.";

    private static final Logger logger = LogsCenter.getLogger(FindCommand.class);

    private final PersonSearchPredicate predicate;


    /**
     * Creates a command that filters contacts using the given predicate.
     *
     * @param predicate the predicate used to select matching contacts
     */
    public FindCommand(PersonSearchPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        logger.fine("Executing find command.");
        model.updateFilteredPersonList(predicate);

        int matchCount = model.getFilteredPersonList().size();
        logger.info("Find command completed with " + matchCount + " matching contact(s).");

        if (matchCount == 0) {
            return new CommandResult(MESSAGE_NO_MATCHING_CONTACTS);
        }

        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, matchCount));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand otherFindCommand)) {
            return false;
        }

        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
