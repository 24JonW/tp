package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.EventDate;
import seedu.address.model.person.EventType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_EVENT_TYPE = " ";
    private static final String INVALID_EVENT_DATE = "2026-02-30";
    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_EVENT_TYPE = "Wedding";
    private static final String VALID_EVENT_DATE = "2026-10-07";
    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, () ->
                ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_valuesHandled() throws Exception {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName(null));
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
        Name expected = new Name(VALID_NAME);
        assertEquals(expected, ParserUtil.parseName(VALID_NAME));
        assertEquals(expected, ParserUtil.parseName(WHITESPACE + VALID_NAME + WHITESPACE));
    }

    @Test
    public void parsePhone_valuesHandled() throws Exception {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone(null));
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
        Phone expected = new Phone(VALID_PHONE);
        assertEquals(expected, ParserUtil.parsePhone(VALID_PHONE));
        assertEquals(expected, ParserUtil.parsePhone(WHITESPACE + VALID_PHONE + WHITESPACE));
    }

    @Test
    public void parseEmail_valuesHandled() throws Exception {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail(null));
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
        Email expected = new Email(VALID_EMAIL);
        assertEquals(expected, ParserUtil.parseEmail(VALID_EMAIL));
        assertEquals(expected, ParserUtil.parseEmail(WHITESPACE + VALID_EMAIL + WHITESPACE));
    }

    @Test
    public void parseEventType_valuesHandled() throws Exception {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEventType(null));
        assertThrows(ParseException.class, () -> ParserUtil.parseEventType(INVALID_EVENT_TYPE));
        assertThrows(ParseException.class, () -> ParserUtil.parseEventType("Wedding!"));
        assertThrows(ParseException.class, () -> ParserUtil.parseEventType("a".repeat(EventType.MAX_LENGTH + 1)));
        EventType expected = new EventType(VALID_EVENT_TYPE);
        assertEquals(expected, ParserUtil.parseEventType(VALID_EVENT_TYPE));
        assertEquals(expected, ParserUtil.parseEventType(WHITESPACE + VALID_EVENT_TYPE + WHITESPACE));
        assertEquals(new EventType("Corporate 2026"), ParserUtil.parseEventType("Corporate \t  2026"));
    }

    @Test
    public void parseEventDate_valuesHandled() throws Exception {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEventDate(null));
        assertThrows(ParseException.class, () -> ParserUtil.parseEventDate(INVALID_EVENT_DATE));
        assertThrows(ParseException.class, () -> ParserUtil.parseEventDate("0000-01-01"));
        EventDate expected = new EventDate(VALID_EVENT_DATE);
        assertEquals(expected, ParserUtil.parseEventDate(VALID_EVENT_DATE));
        assertEquals(expected, ParserUtil.parseEventDate(WHITESPACE + VALID_EVENT_DATE + WHITESPACE));
    }
}
