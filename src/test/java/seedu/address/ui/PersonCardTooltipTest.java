package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import javafx.application.Platform;
import javafx.scene.control.Label;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Checks real tooltip controls. Set RUN_UI_TESTS=true on a machine with a display to run this test.
 */
@EnabledIfEnvironmentVariable(named = "RUN_UI_TESTS", matches = "true")
class PersonCardTooltipTest {
    @Test
    void tooltips_containCompleteStoredValuesAcrossDifferentCards() throws Exception {
        FutureTask<Void> checks = new FutureTask<>(() -> {
            Platform.setImplicitExit(false);
            Person first = new PersonBuilder().withName("A long client name that can be clipped")
                    .withEmail("averylongemailaddress@international-photography.example.com")
                    .withStage("Awaiting Payment").build();
            Person second = new PersonBuilder().withName("Different Client").withStage("Delivered").build();
            for (Person person : List.of(first, second)) {
                PersonCard card = new PersonCard(person, 1);
                Map<String, String> expectedFields = Map.of(
                        "name", person.getName().fullName,
                        "phone", person.getPhone().value,
                        "email", person.getEmail().value,
                        "eventType", person.getEventType().value,
                        "stage", "Stage: " + person.getStage(),
                        "eventDate", person.getEventDate().toString());
                for (Map.Entry<String, String> field : expectedFields.entrySet()) {
                    Label label = (Label) card.getRoot().lookup("#" + field.getKey());
                    assertNotNull(label.getTooltip(), field.getKey());
                    assertEquals(field.getValue(), label.getTooltip().getText(), field.getKey());
                    assertTrue(label.getTooltip().isWrapText());
                }
            }
            return null;
        });
        try {
            Platform.startup(checks);
        } catch (IllegalStateException e) {
            // TestFX may already have started the shared JavaFX toolkit.
            Platform.runLater(checks);
        }
        checks.get(15, TimeUnit.SECONDS);
    }
}
