package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * A UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label eventType;
    @FXML
    private Label email;
    @FXML
    private Label eventDate;
    @FXML
    private Label stage;

    /**
     * Creates a {@code PersonCard} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        eventType.setText(person.getEventType().value);
        email.setText(person.getEmail().value);
        eventDate.setText(person.getEventDate().toString());
        stage.setText("Stage: " + person.getStage());
        addFullValueTooltips(name, phone, email, eventType, stage, eventDate);
    }

    /**
     * Adds wrapped tooltips so clipped fields can be read without opening the details dialog.
     */
    private void addFullValueTooltips(Label... labels) {
        for (Label label : labels) {
            Tooltip tooltip = new Tooltip(label.getText());
            tooltip.setWrapText(true);
            tooltip.setMaxWidth(480);
            label.setTooltip(tooltip);
        }
    }
}
