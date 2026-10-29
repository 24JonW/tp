package seedu.address.logic.parser;

import java.util.regex.Pattern;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_EVENT_TYPE = new Prefix("t/");
    public static final Prefix PREFIX_EVENT_DATE = new Prefix("d/");
    public static final Prefix PREFIX_STAGE = new Prefix("s/");

    /**
     * Relationship markers used in legal names, such as {@code s/o} (son of) in {@code Raj s/o Kumar}.
     * When followed by whitespace they are part of a name, not a prefix.
     */
    public static final Pattern NAME_RELATION_MARKER = Pattern.compile("(?i)(?:s/o|d/o|a/l|a/p|w/o)(?=\\s)");

    /**
     * Returns true if a name relation marker starts at {@code index} of {@code text}.
     */
    public static boolean isNameRelationMarkerAt(String text, int index) {
        return NAME_RELATION_MARKER.matcher(text).region(index, text.length()).lookingAt();
    }

}
