package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Tests the inclusive calendar window, local clock and display ordering for upcoming events.
 */
public class UpcomingCommandTest {
    private static final LocalDate TODAY = LocalDate.of(2026, 10, 14);
    private final Model model = new ModelManager();

    @Test
    public void execute_boundariesAndTies_showsExactlySevenDatesFromAllRecords() throws Exception {
        Person last = addPerson("Last", TODAY.plusDays(6));
        Person past = addPerson("Past", TODAY.minusDays(1));
        Person first = addPerson("First", TODAY);
        Person tied = addPerson("Tied", TODAY);
        Person middle = addPerson("Middle", TODAY.plusDays(3));
        Person future = addPerson("Future", TODAY.plusDays(7));
        model.updateFilteredPersonList(person -> person.equals(past));

        CommandResult result = new UpcomingCommand(clockAt(TODAY)).execute(model);
        assertEquals("Showing 4 upcoming engagement(s).", result.getFeedbackToUser());
        assertEquals(List.of(first, tied, middle, last), model.getFilteredPersonList());
        assertEquals("Upcoming: 2026-10-14 to 2026-10-20 (4)", model.getPersonListHeading());
        assertEquals(List.of(last, past, first, tied, middle, future), model.getAddressBook().getPersonList());
        new ListCommand().execute(model);
        assertEquals(model.getAddressBook().getPersonList(), model.getFilteredPersonList());
        assertEquals("All engagements (6)", model.getPersonListHeading());
    }

    @Test
    public void execute_emptyAndNoMatches_showsWindowInFeedback() throws Exception {
        UpcomingCommand command = new UpcomingCommand(clockAt(TODAY));
        for (int i = 0; i < 2; i++) {
            assertEquals("No events scheduled from 2026-10-14 to 2026-10-20.",
                    command.execute(model).getFeedbackToUser());
            assertEquals(List.of(), model.getFilteredPersonList());
            assertEquals("Upcoming: 2026-10-14 to 2026-10-20 (0)", model.getPersonListHeading());
            if (i == 0) {
                addPerson("Outside", TODAY.minusDays(1));
            }
        }
    }

    @Test
    public void execute_calendarTransitions_usesCalendarDays() throws Exception {
        String[][] windows = {
            {"2026-12-29", "2027-01-04"},
            {"2028-02-27", "2028-03-04"},
            {"2026-03-07", "2026-03-13"}
        };
        for (String[] window : windows) {
            Model emptyModel = new ModelManager();
            ZoneId zone = ZoneId.of("America/New_York");
            LocalDate start = LocalDate.parse(window[0]);
            Clock clock = Clock.fixed(start.atStartOfDay(zone).toInstant(), zone);
            new UpcomingCommand(clock).execute(emptyModel);
            assertEquals("Upcoming: " + window[0] + " to " + window[1] + " (0)",
                    emptyModel.getPersonListHeading());
        }
    }

    @Test
    public void execute_sameInstantDifferentZones_usesLocalDate() throws Exception {
        Person today = addPerson("Today", TODAY);
        Person nextWeek = addPerson("Next week", TODAY.plusDays(7));
        Instant instant = Instant.parse("2026-10-14T12:00:00Z");
        new UpcomingCommand(Clock.fixed(instant, ZoneId.of("Pacific/Honolulu"))).execute(model);
        assertEquals(List.of(today), model.getFilteredPersonList());
        new UpcomingCommand(Clock.fixed(instant, ZoneId.of("Pacific/Kiritimati"))).execute(model);
        assertEquals(List.of(nextWeek), model.getFilteredPersonList());
        assertEquals("Upcoming: 2026-10-15 to 2026-10-21 (1)", model.getPersonListHeading());
    }

    @Test
    public void execute_afterMidnight_refreshesOnlyWhenExecutedAgain() throws Exception {
        Person today = addPerson("Today", TODAY);
        Person nextWeek = addPerson("Next week", TODAY.plusDays(7));
        MutableClock clock = new MutableClock(clockAt(TODAY).instant());
        UpcomingCommand command = new UpcomingCommand(clock);
        command.execute(model);
        assertEquals(List.of(today), model.getFilteredPersonList());
        clock.instant = clockAt(TODAY.plusDays(1)).instant();
        assertEquals(List.of(today), model.getFilteredPersonList());
        command.execute(model);
        assertEquals(List.of(nextWeek), model.getFilteredPersonList());
    }

    @Test
    public void execute_dateFailureOrOverflow_preservesExistingView() throws Exception {
        addPerson("Today", TODAY);
        new UpcomingCommand(clockAt(TODAY)).execute(model);
        List<Person> originalView = List.copyOf(model.getFilteredPersonList());
        String originalHeading = model.getPersonListHeading();
        MutableClock brokenClock = new MutableClock(null);
        for (Clock clock : List.of(brokenClock, clockAt(LocalDate.MAX))) {
            CommandException exception = assertThrows(CommandException.class, () ->
                    new UpcomingCommand(clock).execute(model));
            assertEquals(UpcomingCommand.MESSAGE_DATE_ERROR, exception.getMessage());
            assertEquals(originalView, model.getFilteredPersonList());
            assertEquals(originalHeading, model.getPersonListHeading());
        }
    }

    @Test
    public void execute_deleteUsesDisplayedIndex_thenResetsView() throws Exception {
        Person later = addPerson("Later", TODAY.plusDays(3));
        addPerson("First", TODAY);
        Person outside = addPerson("Outside", TODAY.plusDays(7));
        new UpcomingCommand(clockAt(TODAY)).execute(model);
        new DeleteCommand(Index.fromOneBased(1)).execute(model);
        assertEquals(List.of(later, outside), model.getFilteredPersonList());
        assertEquals("All engagements (2)", model.getPersonListHeading());
    }

    @Test
    public void showUpcomingPersons_invalidRange_preservesView() throws Exception {
        addPerson("Today", TODAY);
        new UpcomingCommand(clockAt(TODAY)).execute(model);
        String heading = model.getPersonListHeading();
        assertThrows(NullPointerException.class, () -> model.showUpcomingPersons(null, TODAY));
        assertThrows(NullPointerException.class, () -> model.showUpcomingPersons(TODAY, null));
        assertThrows(IllegalArgumentException.class, () -> model.showUpcomingPersons(TODAY, TODAY.minusDays(1)));
        assertEquals(heading, model.getPersonListHeading());
        assertEquals(1, model.getFilteredPersonList().size());
    }

    @Test
    public void execute_nullDependencies_rejected() {
        assertThrows(NullPointerException.class, () -> new UpcomingCommand(null));
        assertThrows(NullPointerException.class, () -> new UpcomingCommand(clockAt(TODAY)).execute(null));
    }

    private Person addPerson(String name, LocalDate date) {
        Person person = new PersonBuilder().withName(name).withEventDate(date.toString()).build();
        model.addPerson(person);
        return person;
    }

    private Clock clockAt(LocalDate date) {
        return Clock.fixed(date.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    }

    /**
     * Provides a controllable date source; a null instant simulates a failed system clock.
     */
    private static class MutableClock extends Clock {
        private Instant instant;

        MutableClock(Instant instant) {
            this.instant = instant;
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return Clock.fixed(instant(), zone);
        }

        @Override
        public Instant instant() {
            if (instant == null) {
                throw new DateTimeException("Clock unavailable");
            }
            return instant;
        }
    }
}
