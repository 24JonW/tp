package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PersonCardTest {

    @BeforeAll
    public static void initialiseJavaFx() throws InterruptedException {
        CountDownLatch startupLatch = new CountDownLatch(1);

        try {
            Platform.startup(startupLatch::countDown);
            assertTrue(startupLatch.await(5, TimeUnit.SECONDS));
        } catch (IllegalStateException exception) {
            // JavaFX was already initialised.
        }
    }

    @AfterAll
    public static void shutDownJavaFx() {
        Platform.exit();
    }

    @Test
    public void constructor_displaysEngagementStage() throws Exception {
        Person person = new PersonBuilder()
                .withStage("Booked")
                .build();

        CountDownLatch testLatch = new CountDownLatch(1);
        AtomicReference<String> stageText = new AtomicReference<>();

        Platform.runLater(() -> {
            HBox root = (HBox) new PersonCard(person, 1).getRoot();
            GridPane grid = (GridPane) root.getChildren().get(0);

            Label stageLabel = (Label) grid.getChildren().stream()
                    .filter(child -> Integer.valueOf(4).equals(GridPane.getColumnIndex(child)))
                    .findFirst()
                    .orElseThrow();

            stageText.set(stageLabel.getText());
            testLatch.countDown();
        });

        assertTrue(testLatch.await(5, TimeUnit.SECONDS));
        assertEquals("Stage: Booked", stageText.get());
    }
}
