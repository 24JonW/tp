package seedu.address.model.person;

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