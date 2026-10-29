package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private Label listHeading;

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
    }

    /**
     * Updates the placeholder heading for the displayed contact list.
     */
    public void setHeading(String heading) {
        listHeading.setText(heading);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class PersonListViewCell extends ListCell<Person> {
        PersonListViewCell() {
            setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2
                        && !isEmpty()
                        && getItem() != null) {
                    showPersonDetails(getItem());
                }
            });
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new PersonCard(person, getIndex() + 1).getRoot());
            }
        }
    }

    /**
     * Displays every stored field of the selected engagement in a resizable dialog.
     */
    private void showPersonDetails(Person person) {
        TextArea detailsText = new TextArea(formatPersonDetails(person));
        detailsText.setEditable(false);
        detailsText.setWrapText(true);
        detailsText.setFocusTraversable(false);
        detailsText.setPrefColumnCount(50);
        detailsText.setPrefRowCount(8);
        detailsText.getStyleClass().add("engagement-details-text");

        Alert detailsDialog = new Alert(AlertType.INFORMATION);
        detailsDialog.setTitle("Engagement details");
        detailsDialog.setHeaderText(person.getName().fullName);
        detailsDialog.getDialogPane().setContent(detailsText);
        detailsDialog.getDialogPane().setGraphic(null);
        detailsDialog.getDialogPane().getStyleClass().add("engagement-details-dialog");
        detailsDialog.setResizable(true);

        if (personListView.getScene() != null) {
            detailsDialog.initOwner(personListView.getScene().getWindow());
        }

        detailsDialog.showAndWait();
    }

    static String formatPersonDetails(Person person) {
        requireNonNull(person);
        return String.format(
                "Name: %s%n"
                        + "Phone: %s%n"
                        + "Email: %s%n"
                        + "Event type: %s%n"
                        + "Event stage: %s%n"
                        + "Event date: %s",
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getEventType(),
                person.getStage(),
                person.getEventDate());
    }
}
