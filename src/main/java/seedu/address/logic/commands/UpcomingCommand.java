package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Shows engagements taking place during the next seven calendar dates, including today.
 */
public class UpcomingCommand extends Command {
    public static final String COMMAND_WORD = "upcoming";
    public static final String MESSAGE_SUCCESS = "Showing %d upcoming contacts.";
    public static final String MESSAGE_EMPTY = "No events scheduled from %s to %s.";
    public static final String MESSAGE_DATE_ERROR =
            "Unable to determine today's date. Check the system date and try again.";

    private final Clock clock;

    /**
     * Creates a command using the computer's local timezone.
     */
    public UpcomingCommand() {
        this(Clock.systemDefaultZone());
    }

    /**
     * Creates a command with a supplied clock for deterministic date-boundary testing.
     */
    public UpcomingCommand(Clock clock) {
        this.clock = requireNonNull(clock);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.now(clock);
            endDate = startDate.plusDays(6);
        } catch (DateTimeException e) {
            // Calculate both boundaries before changing the existing view.
            throw new CommandException(MESSAGE_DATE_ERROR, e);
        }

        model.showUpcomingPersons(startDate, endDate);
        int count = model.getFilteredPersonList().size();
        String feedback = count == 0 ? String.format(MESSAGE_EMPTY, startDate, endDate)
                : String.format(MESSAGE_SUCCESS, count);
        return new CommandResult(feedback);
    }
}
