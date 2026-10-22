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
Stage filtering is not supported; `stage/` is rejected as an unsupported list parameter.

`ListCommand` calls `Model#showAllPersons` to clear the current filter and select an ordering.
`ModelManager` exposes a `SortedList<Person>` over its `FilteredList<Person>` to the UI and index-based commands.
For date sorting, it compares the existing `EventDate.value` (`LocalDate`) and breaks ties by the record's
position in the underlying address book. Plain listing removes the comparator, restoring insertion order.
The stored list itself is never sorted. Updating a filter removes date sorting so a later search is in
insertion order. Deleting from a date-sorted view resets that view after selecting the displayed engagement.

`LogicManager` skips storage writes for `ListCommand`: viewing engagements does not require write access to the
JSON file. Other commands retain their existing saving behaviour. `Model#getPersonListHeading` supplies the
heading and count through `Logic`; `MainWindow` refreshes a simple label in `PersonListPanel` after successful
commands. A filtered view uses the neutral heading `Contacts (N)`.

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

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a freelance photographer who handles event-based jobs
* has a need to manage a significant number of clients and photography jobs
* needs to track client contact details, event details, and job progress from enquiry to delivery
* prefers desktop apps over other types of applications
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Manage photography engagements from enquiry to delivery faster than with a typical
mouse-driven GUI application.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

In these stories, an **engagement** is the record being added, edited, searched, displayed, and deleted. A **client**
is the person associated with that engagement, identified in the current model by the combination of name, normalized
phone number, and normalized email address. Clients are not stored as separate objects, so multiple engagements for
the same client repeat those three fields.

| Priority | User story |
| -------- | ---------- |
| `***` | As a photographer, I can add an engagement with client contact details, event details, and an optional initial stage, so that I can record work at any point in its lifecycle. |
| `***` | As a photographer, I can set an engagement's stage, such as enquiry, booked, shot completed, awaiting payment, or delivered, so that I always know what needs to happen next. |
| `***` | As a photographer, I can edit an engagement's client, event, or stage details, so that I can keep its information accurate as things change. |
| `***` | As a photographer, I can delete an engagement, so that my engagement list does not fill up with irrelevant work. |
| `***` | As a photographer, I can search engagements by client phone number or email address, so that I can find a client's work even when I do not remember their name. |
| `***` | As a photographer, I can search engagements by event type, so that I can quickly locate specific types of shoots. |
| `***` | As a photographer, I can view engagements sorted by event date, including past events, so that I can review them chronologically. |
| `***` | As a photographer, I can view all engagements with events happening within the next seven days, so that I can prepare logistics in advance. |
| `***` | As a fast typist, I can add, edit, and find engagements by typing commands, so that I can manage work faster than with a form. |
| `***` | As a photographer, I can type `help` to open a concise command reference, so that I can recall common syntax without leaving the application. |
| `**` | As a photographer, I can record multiple engagements for the same repeat client, so that their separate events remain available. |
| `*` | As a photographer, I receive a possible-typo warning when an added or edited engagement shares a phone number or email address with a different client identity. |


### Use cases

(For all use cases below, the **System** is `ShutterLink` and the **Actor** is the `user`, a freelance event photographer, unless specified otherwise)

**Use case: UC01 - Add an engagement**

**MSS**

1.  User requests to add an engagement, giving the client's name, phone number, email address, event type and event
    date, and optionally an engagement stage.
2.  ShutterLink adds the engagement at the supplied stage, or at _Enquiry_ when no stage is supplied, and shows the
    full engagement list in insertion order.

    Use case ends.

**Extensions**

* 1a. A required detail (name, phone number, email address, event type or event date) is missing, or a supplied detail
  (including stage) is invalid.

    * 1a1. ShutterLink shows an error message describing the first problem found.
    * 1a2. User requests to add the engagement again with corrected details.

      Steps 1a1-1a2 are repeated until the details are valid.<br>
      Use case resumes from step 2.

* 1b. An engagement with the same name, normalized phone number, email address, event type, and event date already
  exists, using case-insensitive comparison for name, email, and event type. Stage is not part of duplicate identity.

    * 1b1. ShutterLink informs the user that the engagement already exists and does not add it.

      Use case ends.

* 1c. ShutterLink is unable to save the data.

    * 1c1. ShutterLink shows an error message. The engagement remains in memory for the current session, but the data
      file retains its last successfully saved state.

      Use case ends.
