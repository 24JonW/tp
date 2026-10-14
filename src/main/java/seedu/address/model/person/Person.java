package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a photography engagement with a client.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    private final Name name;
    private final Phone phone;
    private final Email email;
    private final EventType eventType;
    private final EventDate eventDate;
    private final Stage stage;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, EventType eventType, EventDate eventDate) {
        requireAllNonNull(name, phone, email, eventType, eventDate, Stage.ENQUIRY);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.stage = Stage.ENQUIRY;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Stage getStage() {
        return stage;
    }

    /** Returns the date of the photography event. */
    public EventDate getEventDate() {
        return eventDate;
    }

    /**
     * Returns true if both persons represent the same engagement, i.e., they have the same name, phone, email,
     * event type and event date. Name, email and event type are compared case-insensitively.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && name.fullName.equalsIgnoreCase(otherPerson.name.fullName)
                && phone.equals(otherPerson.phone)
                && email.value.equalsIgnoreCase(otherPerson.email.value)
                && eventType.value.equalsIgnoreCase(otherPerson.eventType.value)
                && eventDate.equals(otherPerson.eventDate);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person otherPerson)) {
            return false;
        }

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && eventType.equals(otherPerson.eventType)
                && eventDate.equals(otherPerson.eventDate)
                && stage.equals(otherPerson.stage);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, eventType, eventDate, stage);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("eventType", eventType)
                .add("eventDate", eventDate)
                .add("stage", stage)
                .toString();
    }

}
