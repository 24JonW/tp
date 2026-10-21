package seedu.address.model.person;


import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/**
 * Represents the current engagement stage of a photography client.
 */
public enum Stage {
    ENQUIRY("Enquiry"),
    BOOKED("Booked"),
    SHOT_COMPLETED("Shot Completed"),
    AWAITING_PAYMENT("Awaiting Payment"),
    DELIVERED("Delivered");

    public static final String MESSAGE_CONSTRAINTS = "Stage must be one of: Enquiry, Booked, Shot Completed, "
            + "Awaiting Payment, Delivered.";

    private static final Logger logger = LogsCenter.getLogger(Stage.class);

    private final String value;

    Stage(String value) {
        this.value = value;
    }

    /**
     * Converts text into the corresponding engagement stage.
     *
     * @param input stage text to convert
     * @return the matching engagement stage
     * @throws IllegalArgumentException if the input is not a supported stage
     */
    public static Stage fromString(String input) {
        for (Stage stage : values()) {
            if (stage.value.equalsIgnoreCase(input.trim())) {
                return stage;
            }
        }
        logger.warning("Rejected unsupported engagement stage input.");
        throw new IllegalArgumentException("Invalid engagement stage.");
    }

    /**
     * Returns the next stage in the engagement workflow, or an empty value when this is the final stage.
     */
    public Optional<Stage> getNextStage() {
        return switch (this) {
            case ENQUIRY -> Optional.of(BOOKED);
            case BOOKED -> Optional.of(SHOT_COMPLETED);
            case SHOT_COMPLETED -> Optional.of(AWAITING_PAYMENT);
            case AWAITING_PAYMENT -> Optional.of(DELIVERED);
            case DELIVERED -> Optional.empty();
        };
    }

    @Override
    public String toString() {
        return value;
    }
}
