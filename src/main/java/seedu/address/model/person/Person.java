package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
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
    public Person(Name name, Phone phone, Email email, EventType eventType, EventDate eventDate, Stage stage) {
        requireAllNonNull(name, phone, email, eventType, eventDate, stage);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.stage = stage;
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
    public boolean isSameEngagement(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && isSameClient(otherPerson)
                && eventType.value.equalsIgnoreCase(otherPerson.eventType.value)
                && eventDate.equals(otherPerson.eventDate);
    }

    /**
     * Returns true if both persons share a phone number or email, but are not the same client
     * (i.e., their names, phones and emails are not all the same). Such records may contain a typo.
     */
    public boolean hasSimilarContactDetails(Person otherPerson) {
        requireNonNull(otherPerson);
        boolean hasSharedContact = hasSamePhone(otherPerson) || hasSameEmail(otherPerson);
        return hasSharedContact && !isSameClient(otherPerson);
    }

    /**
     * Returns true if both persons have the same phone number.
     */
    public boolean hasSamePhone(Person otherPerson) {
        return phone.equals(otherPerson.phone);
    }

    /**
     * Returns true if both persons have the same email, ignoring case.
     */
    public boolean hasSameEmail(Person otherPerson) {
        return email.value.equalsIgnoreCase(otherPerson.email.value);
    }

    /**
     * Returns true if both persons have the same name (ignoring case), phone and email,
     * i.e., they are records of the same client.
     */
    public boolean isSameClient(Person otherPerson) {
        return name.fullName.equalsIgnoreCase(otherPerson.name.fullName)
                && hasSamePhone(otherPerson)
                && hasSameEmail(otherPerson);
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
