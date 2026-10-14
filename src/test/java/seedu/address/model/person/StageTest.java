package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StageTest {

    @Test
    public void fromString_validStage_returnsStage() {
        assertEquals(Stage.BOOKED, Stage.fromString("booked"));
    }

    @Test
    public void fromString_invalidStage_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> Stage.fromString("Unknown Stage"));
    }
}
