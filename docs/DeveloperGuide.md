---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

ShutterLink is based on [AddressBook-Level3](https://github.com/se-edu/addressbook-level3) by the SE-EDU initiative.
It uses JavaFX for the UI, Jackson for JSON serialization, JUnit 5 for testing, and Gradle for builds.

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in `docs/diagrams`. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="500" />

The ***Architecture Diagram*** above explains the high-level design of ShutterLink. Solid arrows show runtime
collaboration, while dotted arrows show dependencies on shared types or external actors and files.

The following provides a quick overview of the main components and their interactions.

**Main components of the architecture**

**`Main`** (consisting of classes
[`Main`](https://github.com/AY2627S1-CS2103T-T08-3/tp/blob/master/src/main/java/seedu/address/Main.java) and
[`MainApp`](https://github.com/AY2627S1-CS2103T-T08-3/tp/blob/master/src/main/java/seedu/address/MainApp.java)) is in
charge of app launch and shutdown.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact when the user issues `delete 1`. It includes the
successful JSON save and the subsequent refresh of the list heading. The UI observes the model's displayed list, so
individual list-change notifications are omitted.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* provides its functionality through a concrete `{Component Name}Manager` class that implements the corresponding API interface.

For example, the `Logic` component defines its API in `Logic.java` and implements it in `LogicManager.java`. Other components interact with a component through its interface rather than its concrete class, preventing them from coupling to that component's implementation, as illustrated in the following partial class diagram.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in
[`Ui.java`](https://github.com/AY2627S1-CS2103T-T08-3/tp/blob/master/src/main/java/seedu/address/ui/Ui.java).

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` and its parts, such as `CommandBox`, `ResultDisplay`, `PersonListPanel`, and `StatusBarFooter`. All of these, including `MainWindow`, inherit from the abstract `UiPart` class, which captures common behavior among classes that represent visible GUI parts.

The `UI` component uses the JavaFX UI framework. The layouts of these UI parts are defined in matching `.fxml` files
in `src/main/resources/view`. For example,
[`MainWindow.fxml`](https://github.com/AY2627S1-CS2103T-T08-3/tp/blob/master/src/main/resources/view/MainWindow.fxml)
specifies the layout of
[`MainWindow`](https://github.com/AY2627S1-CS2103T-T08-3/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java).

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component because it displays `Person` objects from the model.

### Logic component

**API**:
[`Logic.java`](https://github.com/AY2627S1-CS2103T-T08-3/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the parsing and command-execution portion of the `Logic` component, taking
the `execute("delete 1")` API call as an example. The subsequent storage write is shown in the architecture sequence
diagram above.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, the command is passed to an `AddressBookParser` object, which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete an engagement).<br>
   Note that although this is shown as a single step in the diagram above for simplicity, the code can require several interactions between the command object and the `Model` to complete the operation.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name, e.g., `AddCommandParser`). The parser uses the other classes shown above to parse the user command and create an `XYZCommand` object (e.g., `AddCommand`). The `AddressBookParser` returns that object as a `Command` object.
* All `XYZCommandParser` classes, such as `AddCommandParser` and `DeleteCommandParser`, implement the `Parser` interface so they can be treated similarly where appropriate, for example during testing.

### Model component
**API**:
[`Model.java`](https://github.com/AY2627S1-CS2103T-T08-3/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* represents each `Person` as one photography engagement containing a client's `Name`, `Phone`, `Email`,
  `EventType`, `EventDate`, and `Stage`.
* stores the `Person` objects selected by the current filter, such as search results, in a separate _filtered_ list. It exposes this list as an unmodifiable `ObservableList<Person>` that the UI can observe and bind to, so the UI updates when the list changes.
* stores a `UserPrefs` object that represents the user’s preferences (currently, just the GUI settings). This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

### Storage component

**API**:
[`Storage.java`](https://github.com/AY2627S1-CS2103T-T08-3/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* is implemented by `StorageManager`, which delegates the actual JSON file access to `JsonAddressBookStorage` and `JsonUserPrefsStorage` (one class per data file).
* converts each `Person` to and from a `JsonAdaptedPerson`, including its event type, ISO-8601 event date,
  and engagement stage.
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Viewing upcoming events

`UpcomingCommandParser` accepts the case-insensitive, argument-free `upcoming` command. Invalid arguments,
including line breaks, are rejected before execution. `UpcomingCommand` obtains the current local date from a
`Clock` at execution time and calculates the inclusive end date with `plusDays(6)`. Tests supply fixed or mutable
clocks to cover timezone differences, daylight-saving transitions, leap years and rerunning after midnight.
Both boundaries are calculated before changing the model, so a date failure leaves the previous view intact.

`Model#showUpcomingPersons` replaces the existing filter with an inclusive date-range predicate over all saved
engagements, then reuses the list feature's date sorter with insertion-order ties. The model retains the date window
for the `Upcoming: START_DATE to END_DATE (N)` heading. Other view updates clear that window. The existing list
panel displays the heading and renumbers cards; index-based commands therefore use the upcoming view.
No stage restriction is applied. The selected window stays fixed until the command is executed again.

`LogicManager` skips storage writes for `UpcomingCommand`, just as it does for `ListCommand`. Tests verify that
the command works without writable storage, neither changes existing files nor creates missing files, and
preserves the previous view on invalid input. The persistent insertion order is unchanged.

The sequence diagram below summarizes the successful command path. It focuses on parsing, calculating the inclusive
date range, filtering and sorting the displayed list, and returning feedback. Calls used only to construct the date
predicate or format the feedback are omitted.

![Interactions for the `upcoming` Command](images/UpcomingSequenceDiagram.png)

### Listing engagements and sorting by event date

`ListCommandParser` accepts `list` and `list sort/date`, rejecting extra positional arguments,
unsupported/repeated prefixes, empty values and invalid sort options before changing the model.
List-specific parsing is case-insensitive and preserves raw arguments to reject embedded control characters.
Stage filtering is deferred; `stage/` is currently an unsupported list parameter.

`ListCommand` calls `Model#showAllPersons` to clear the current filter and select an ordering.
`ModelManager` exposes a `SortedList<Person>` over its `FilteredList<Person>` to the UI and index-based commands.
For date sorting, it compares the existing `EventDate.value` (`LocalDate`) and breaks ties by the record's
position in the underlying address book. Plain listing removes the comparator, restoring insertion order.
The stored list itself is never sorted. Updating a filter removes date sorting so a later search is in
insertion order. Deleting from a date-sorted view resets that view after selecting the displayed engagement.

`LogicManager` skips storage writes for `ListCommand`: viewing engagements does not require write access to the
JSON file. Other commands retain their existing saving behaviour. `Model#getPersonListHeading` supplies the
heading and count through `Logic`; `MainWindow` refreshes a simple label in `PersonListPanel` after successful
commands. A filtered view uses the neutral placeholder `Contacts (N)` for future search-UI refinement.

Tests cover empty and single-record lists, past/future dates, ties, filter/sort resets, displayed-index
edit/advance/delete integration, failed-input view preservation and unchanged persisted order across restarts.

### Managing engagement stages

`Stage` is a required enum field of every `Person`. `Stage#fromString` accepts the five display values
case-insensitively after trimming surrounding whitespace: `Enquiry`, `Booked`, `Shot Completed`,
`Awaiting Payment`, and `Delivered`. JSON loading is strict: a missing or invalid `stage` makes the entire data file
invalid rather than assigning a default.

`AddCommandParser` accepts an optional `s/STAGE`; when it is absent, the parser constructs the engagement with
`Stage.ENQUIRY`. `EditCommandParser` stores a supplied stage in `EditPersonDescriptor`. The descriptor uses
`Optional<Stage>` to distinguish “stage not supplied in this edit” from a stage value; `Person#getStage` itself
returns a non-null `Stage`.

`AdvanceStageCommand` takes the engagement at the displayed index and asks `Stage#getNextStage` for its successor.
It constructs a new immutable `Person` with the same client and event fields, replaces the old object through
`Model#setPerson`, and returns to the complete insertion-ordered list. `Delivered` has no successor and produces a
command error without modifying the model or view. Direct editing can move a stage either forward or backward.

Stage is deliberately excluded from `Person#isSameEngagement`, so changing only the stage cannot create a distinct
duplicate. Engagement identity uses name, normalized phone, normalized email, event type, and event date; name,
email, and event type comparisons are case-insensitive.

### Finding engagements

`FindCommandParser` requires exactly one of `n/`, `p/`, `e/`, or `t/` with a non-blank search value. It does not
validate the search text as a complete field value. `PersonSearchPredicate` performs a case-insensitive substring
comparison against the selected stored field. Consequently, `find e/alice@` is valid and can match
`alice@example.com`. Phone values are stored without spaces or hyphens, but search text is not normalized, so users
should normally search phone numbers using digits only.

Executing `find` removes date sorting, replaces the previous predicate, and assigns indexes to the matching view.
Index-based commands operate on that view. A successful add, edit, or advance restores the complete list; deleting
from a search leaves the remaining search matches visible.

### Persistence after commands

`LogicManager#execute` parses and executes a command before deciding whether to save. `ListCommand` and
`UpcomingCommand` are the only commands that skip `Storage#saveAddressBook`, because they change only the view.
All other successfully executed commands currently attempt a write, including `find`, `help`, and `exit`.

If a storage write fails, `LogicManager` returns a `CommandException`. The command has already executed, however, so
an add, edit, advance, delete, or clear remains applied to the in-memory model for the current session. There is no
rollback; the data file retains its last successfully written state. On startup, a missing file loads sample data,
whereas a malformed or otherwise unreadable file loads an empty address book and leaves the bad file untouched until
a later command successfully writes data.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` to delete the fifth engagement. The `delete` command calls
`Model#commitAddressBook()`, causing the modified address book state to be saved in the `addressBookStateList`, and
the `currentStatePointer` is shifted to the newly inserted state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes
`add n/David Li p/91234567 e/david@example.com t/Wedding d/2027-01-15` to add a new engagement. The `add` command
also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the
`addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user decides that adding the engagement was a mistake and executes `undo`. The `undo` command calls
`Model#undoAddressBook()`, shifts the `currentStatePointer` once to the left, and restores the previous address book
state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X), but due to a limitation of PlantUML, it continues to the end of the diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not
pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be
purged. It no longer makes sense to redo the earlier `add` command. This is the behavior that most modern desktop
applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo execute:**

* **Alternative 1 (proposed choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, save only the engagement being deleted).
  * Cons: We must ensure that the implementation of each individual command is correct.

### \[Proposed\] Data archiving

Data archiving is not implemented in the current version. The proposed feature hides completed engagements from the
active list without deleting their client or event history. An `archive INDEX` command would accept the index in the
current displayed list and archive only an engagement at the `Delivered` stage. A separate archived view would allow
the user to inspect archived engagements and restore one that was archived accidentally.
