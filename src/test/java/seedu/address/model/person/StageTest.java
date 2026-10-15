package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    public void getNextStage_nonFinalStages_returnsFollowingStage() {
        assertEquals(Stage.BOOKED, Stage.ENQUIRY.getNextStage().get());
        assertEquals(Stage.SHOT_COMPLETED, Stage.BOOKED.getNextStage().get());
        assertEquals(Stage.AWAITING_PAYMENT, Stage.SHOT_COMPLETED.getNextStage().get());
        assertEquals(Stage.DELIVERED, Stage.AWAITING_PAYMENT.getNextStage().get());
    }

    @Test
    public void getNextStage_delivered_returnsEmpty() {
        assertTrue(Stage.DELIVERED.getNextStage().isEmpty());
    }
}
