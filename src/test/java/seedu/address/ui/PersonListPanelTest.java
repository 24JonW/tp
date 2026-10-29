package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

@ExtendWith(ApplicationExtension.class)
class PersonListPanelTest {

    private static final String LONG_EMAIL =
            "averylongphotographyclientemailaddressfortesting@international-photography.example.com";
    private Person person;

    @Start
    private void start(Stage stage) {
        person = new PersonBuilder()
                .withEmail(LONG_EMAIL)
                .build();
        PersonListPanel personListPanel = new PersonListPanel(FXCollections.observableArrayList(person));
        stage.setScene(new Scene(personListPanel.getRoot(), 1000, 600));
        stage.show();
    }

    @Test
    void formatPersonDetails_personWithLongEmail_containsCompleteEmail() {
        String details = PersonListPanel.formatPersonDetails(person);

        assertTrue(details.contains("Email: " + LONG_EMAIL));
    }

    @Test
    void constructor_personListProvided_displaysPersonCard(FxRobot robot) {
        ListView<?> personListView = robot.lookup("#personListView").queryAs(ListView.class);
        ListCell<?> personListCell = robot.lookup(".list-cell").queryAs(ListCell.class);

        assertEquals(1, personListView.getItems().size());
        assertEquals(person, personListCell.getItem());
        assertNotNull(personListCell.getGraphic());
    }

    @Test
    void doubleClick_personCard_displaysCompleteDetailsDialog(FxRobot robot) {
        ListCell<?> personListCell = robot.lookup(".list-cell").queryAs(ListCell.class);

        robot.doubleClickOn(personListCell);

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        TextArea detailsText = (TextArea) dialogPane.getContent();
        assertEquals(PersonListPanel.formatPersonDetails(person), detailsText.getText());
        assertTrue(detailsText.getText().contains(LONG_EMAIL));
        assertTrue(detailsText.isWrapText());
        assertFalse(detailsText.isEditable());
        assertFalse(detailsText.isFocusTraversable());
        assertTrue(detailsText.getStyleClass().contains("engagement-details-text"));
        assertTrue(dialogPane.getStyleClass().contains("engagement-details-dialog"));
        assertNull(dialogPane.getGraphic());

        robot.clickOn("OK");
    }
}
