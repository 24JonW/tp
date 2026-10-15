package seedu.address.model.person;

/**
 * Represents the current engagement stage of a photography client.
 */
public enum Stage {
    ENQUIRY("Enquiry"),
    BOOKED("Booked"),
    SHOT_COMPLETED("Shot Completed"),
    AWAITING_PAYMENT("Awaiting Payment"),
    DELIVERED("Delivered");

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
        throw new IllegalArgumentException("Invalid engagement stage.");
    }

    @Override
    public String toString() {
        return value;
    }
}
