package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://se-education.org/addressbook-level3/UserGuide.html";

    public static final String HELP_MESSAGE = """
            All parameters shown below are required.

            add n/NAME p/PHONE e/EMAIL t/EVENT_TYPE d/EVENT_DATE
                Add a contact with event details.

            edit INDEX n/NAME p/PHONE e/EMAIL t/EVENT_TYPE d/EVENT_DATE
                Update all details of the contact at INDEX.

            delete INDEX
                Immediately delete the contact at INDEX.

            find p/PHONE
                Show contacts whose phone number exactly matches PHONE.

            find e/EMAIL
                Show contacts whose email address exactly matches EMAIL.

            find t/EVENT_TYPE
                Show contacts whose event type exactly matches EVENT_TYPE.

            list
                Show all contacts.

            list sort/date
                Show all contacts sorted by event date.

            upcoming
                Show events taking place today or within the next six dates.

            help
                Open this Help panel.
            """
            + "\nRefer to the user guide: " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    private final Runnable onHidden;

    @FXML
    private Button closeButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root, Runnable onHidden) {
        super(FXML, root);
        this.onHidden = onHidden;
        helpMessage.setText(HELP_MESSAGE);
        getRoot().getScene().addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        getRoot().setOnHidden(event -> this.onHidden.run());
    }

    /**
     * Creates a new HelpWindow owned by the supplied stage.
     */
    public HelpWindow(Stage root, Stage owner, Runnable onHidden) {
        this(root, onHidden);
        root.initOwner(owner);
        root.initModality(Modality.WINDOW_MODAL);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(() -> { });
    }

    /**
     * Creates a new HelpWindow that invokes {@code onHidden} whenever it closes.
     */
    public HelpWindow(Runnable onHidden) {
        this(new Stage(), onHidden);
    }

    /**
     * Creates a new HelpWindow using the supplied stage.
     */
    public HelpWindow(Stage root) {
        this(root, () -> { });
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().setIconified(false);
        getRoot().toFront();
        getRoot().requestFocus();
    }

    /**
     * Closes the Help panel.
     */
    @FXML
    private void close() {
        hide();
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            close();
            event.consume();
        }
    }
}
