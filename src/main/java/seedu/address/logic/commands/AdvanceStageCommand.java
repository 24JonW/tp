package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Stage;

/**
 * Advances an engagement identified by its displayed index to the next stage.
 */
public class AdvanceStageCommand extends Command {

    public static final String COMMAND_WORD = "advance";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Advances the engagement identified by the index number used in the displayed list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Advanced engagement: %1$s";
    public static final String MESSAGE_ALREADY_AT_FINAL_STAGE =
            "This engagement is already Delivered and cannot be advanced.";

    private static final Logger logger = LogsCenter.getLogger(AdvanceStageCommand.class);

    private final Index targetIndex;

    /**
     * Creates an {@code AdvanceStageCommand} for the engagement at {@code targetIndex}.
     */
    public AdvanceStageCommand(Index targetIndex) {
        this.targetIndex = requireNonNull(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            logger.fine("Cannot advance engagement: displayed index " + targetIndex.getOneBased()
                    + " is outside the displayed list of size " + lastShownList.size());
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToAdvance = lastShownList.get(targetIndex.getZeroBased());
        Optional<Stage> nextStage = personToAdvance.getStage().getNextStage();
        if (nextStage.isEmpty()) {
            logger.fine("Cannot advance engagement at displayed index " + targetIndex.getOneBased()
                    + ": engagement is already at the final stage");
            throw new CommandException(MESSAGE_ALREADY_AT_FINAL_STAGE);
        }
        assert nextStage.isPresent();

        Person advancedPerson = new Person(personToAdvance.getName(), personToAdvance.getPhone(),
                personToAdvance.getEmail(), personToAdvance.getEventType(), personToAdvance.getEventDate(),
                nextStage.get());
        model.setPerson(personToAdvance, advancedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(advancedPerson)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other instanceof AdvanceStageCommand otherCommand
                && targetIndex.equals(otherCommand.targetIndex));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
