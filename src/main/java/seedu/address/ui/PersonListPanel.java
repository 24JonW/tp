package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

    @FXML
    private GridPane columnHeader;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new PersonListViewCell());
        personListView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Person selectedPerson = personListView.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    event.consume();
                    showPersonDetails(selectedPerson);
                }
            }
        });

        columnHeader.prefWidthProperty().bind(personListView.widthProperty());
        personListView.skinProperty().addListener((observable, oldSkin, newSkin) ->
                Platform.runLater(this::bindHeaderWidth));
        Platform.runLater(this::bindHeaderWidth);
    }

    /**
     * Updates the placeholder heading for the displayed contact list.
     */
    public void setHeading(String heading) {
        listHeading.setText(heading);
    }

    /**
     * Aligns the header with the list viewport, excluding the vertical scrollbar.
     */
    private void bindHeaderWidth() {
        personListView.applyCss();

        for (Node node : personListView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar scrollBar
                    && scrollBar.getOrientation() == Orientation.VERTICAL) {
                DoubleBinding headerWidth = Bindings.createDoubleBinding(() -> Math.max(0,
                                personListView.getWidth()
                                    - (scrollBar.isVisible() ? scrollBar.getWidth() : 0)),
                            personListView.widthProperty(),
                            scrollBar.visibleProperty(),
                            scrollBar.widthProperty());

                columnHeader.prefWidthProperty().unbind();
                columnHeader.prefWidthProperty().bind(headerWidth);
                return;
            }
        }
    }

    /**
     * Displays a {@code PersonCard} constrained to the available cell width.
     */
    class PersonListViewCell extends ListCell<Person> {
        private final DoubleBinding contentWidth = Bindings.createDoubleBinding(() -> Math.max(0,
                        getWidth() - getInsets().getLeft() - getInsets().getRight()),
                        widthProperty(), insetsProperty());

        /**
         * Creates a cell whose contents do not determine the list's preferred width.
         */
        PersonListViewCell() {
            setMinWidth(0);
            setPrefWidth(0);

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

            if (getGraphic() instanceof Region previousCard) {
                previousCard.prefWidthProperty().unbind();
                previousCard.maxWidthProperty().unbind();
            }

            setText(null);

            if (empty || person == null) {
                setGraphic(null);
            } else {
                Region card = new PersonCard(person, getIndex() + 1).getRoot();
                card.prefWidthProperty().bind(contentWidth);
                card.maxWidthProperty().bind(contentWidth);
                setGraphic(card);
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
        detailsText.setPrefColumnCount(50);
        detailsText.setPrefRowCount(8);
        detailsText.getStyleClass().add("engagement-details-text");

        Label eventSummary = new Label(person.getEventType() + "  •  " + person.getEventDate());
        eventSummary.setMaxWidth(Double.MAX_VALUE);
        eventSummary.getStyleClass().add("engagement-details-summary");

        Label stageBadge = new Label(person.getStage().toString());
        stageBadge.getStyleClass().add("engagement-details-stage");

        HBox metadataBar = new HBox(12, eventSummary, stageBadge);
        metadataBar.getStyleClass().add("engagement-details-metadata");
        HBox.setHgrow(eventSummary, Priority.ALWAYS);

        VBox detailsContent = new VBox(12, metadataBar, detailsText);
        VBox.setVgrow(detailsText, Priority.ALWAYS);

        Alert detailsDialog = new Alert(AlertType.INFORMATION);
        detailsDialog.setTitle("Engagement details");
        detailsDialog.setHeaderText(person.getName().fullName);
        detailsDialog.getDialogPane().setContent(detailsContent);
        detailsDialog.getDialogPane().setGraphic(null);
        detailsDialog.getDialogPane().getStyleClass().add("engagement-details-dialog");
        detailsDialog.setResizable(true);

        ButtonType copyDetailsButtonType = new ButtonType("Copy details", ButtonBar.ButtonData.LEFT);
        ButtonType closeButtonType = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
        detailsDialog.getButtonTypes().setAll(copyDetailsButtonType, closeButtonType);

        Button copyDetailsButton = (Button) detailsDialog.getDialogPane().lookupButton(copyDetailsButtonType);
        copyDetailsButton.getStyleClass().add("copy-details-button");
        copyDetailsButton.setTooltip(new Tooltip("Copy all engagement details to the clipboard"));
        copyDetailsButton.setAccessibleText("Copy all engagement details");
        copyDetailsButton.addEventFilter(ActionEvent.ACTION, event -> {
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(detailsText.getText());
            Clipboard.getSystemClipboard().setContent(clipboardContent);
            copyDetailsButton.setText("Copied!");
            event.consume();
        });

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
