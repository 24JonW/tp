package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;

/**
 * Shows all contacts in insertion order or ascending event-date order.
 */
public class ListCommand extends Command {
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = "list [sort/date]";
    public static final String MESSAGE_SUCCESS = "Showing %d engagement(s).";
    public static final String MESSAGE_EMPTY = "No engagements to display.";

    private final boolean isSortedByDate;

    /**
     * Creates a command that restores insertion order.
     */
    public ListCommand() {
        this(false);
    }

    /**
     * Creates a command that optionally sorts contacts by event date.
     */
    public ListCommand(boolean isSortedByDate) {
        this.isSortedByDate = isSortedByDate;
    }

    @Override
    public boolean modifiesAddressBook() {
        return false;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.showAllPersons(isSortedByDate);
        int count = model.getFilteredPersonList().size();
        return new CommandResult(count == 0 ? MESSAGE_EMPTY : String.format(MESSAGE_SUCCESS, count));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof ListCommand otherCommand
                && isSortedByDate == otherCommand.isSortedByDate);
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(isSortedByDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("isSortedByDate", isSortedByDate).toString();
    }
}
