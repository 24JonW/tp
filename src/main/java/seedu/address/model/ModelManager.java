package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final SortedList<Person> displayedPersons;
    private LocalDate upcomingStartDate;
    private LocalDate upcomingEndDate;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        displayedPersons = new SortedList<>(filteredPersons);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        if (displayedPersons.getComparator() != null) {
            updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        }
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return displayedPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        upcomingStartDate = null;
        upcomingEndDate = null;
        displayedPersons.setComparator(null);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void showAllPersons(boolean isSortedByDate) {
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        if (isSortedByDate) {
            sortPersonsByEventDate();
        }
        logger.fine("Showing all contacts; sorted by event date: " + isSortedByDate);
    }

    @Override
    public void showUpcomingPersons(LocalDate startDate, LocalDate endDate) {
        requireAllNonNull(startDate, endDate);
        checkArgument(!endDate.isBefore(startDate), "End date must not precede start date");
        updateFilteredPersonList(person -> !person.getEventDate().value.isBefore(startDate)
                && !person.getEventDate().value.isAfter(endDate));
        sortPersonsByEventDate();
        upcomingStartDate = startDate;
        upcomingEndDate = endDate;
        logger.fine("Showing upcoming events from " + startDate + " to " + endDate);
    }

    /**
     * Sorts only the display, using saved insertion positions to break equal-date ties.
     */
    private void sortPersonsByEventDate() {
        Comparator<Person> byDate = Comparator.comparing(person -> person.getEventDate().value);
        displayedPersons.setComparator(byDate.thenComparingInt(addressBook.getPersonList()::indexOf));
    }

    @Override
    public String getPersonListHeading() {
        String heading;
        if (upcomingStartDate != null) {
            heading = "Upcoming: " + upcomingStartDate + " to " + upcomingEndDate;
        } else if (displayedPersons.getComparator() != null) {
            heading = "All engagements by event date";
        } else if (filteredPersons.getPredicate() == null
                || filteredPersons.getPredicate() == PREDICATE_SHOW_ALL_PERSONS) {
            heading = "All engagements";
        } else {
            heading = "Engagements";
        }
        return heading + " (" + displayedPersons.size() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && displayedPersons.equals(otherModelManager.displayedPersons)
                && Objects.equals(upcomingStartDate, otherModelManager.upcomingStartDate)
                && Objects.equals(upcomingEndDate, otherModelManager.upcomingEndDate)
                && ((displayedPersons.getComparator() != null)
                == (otherModelManager.displayedPersons.getComparator() != null));
    }

}
