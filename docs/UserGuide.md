---
layout: page
title: User Guide
---

ShutterLink is a **desktop application for managing photography client engagements, optimized for use through a Command Line Interface (CLI)** while retaining the benefits of a Graphical User Interface (GUI). If you type quickly, ShutterLink can help you manage engagements faster than traditional GUI applications.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure that Java `25` or later is installed on your computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2627S1-CS2103T-T08-3/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for ShutterLink.

1. Open a terminal, `cd` to the folder containing the JAR file, and run `java -jar addressbook.jar`.<br>
   A GUI similar to the one below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type a command in the command box and press Enter to execute it. For example, type **`help`** and press Enter to open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com t/Wedding d/2026-12-20` : Adds an engagement for `John Doe` at the `Enquiry` stage.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) section below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  For example, in `add n/NAME`, replace `NAME` with a value such as `John Doe`.

* Items in square brackets are optional.<br>
  For example, `edit INDEX [n/NAME]` can be used as `edit 1 n/John Doe` or with another editable field.

* Parameters can be in any order.<br>
  For example, if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Commands that take no parameters reject unexpected input unless their command section states otherwise.<br>
  For example, `help 123` is rejected because `help` takes no parameters.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a client engagement to ShutterLink. The stage is optional and defaults to `Enquiry`.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL t/EVENT_TYPE d/EVENT_DATE [s/STAGE]`

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com t/Wedding d/2026-12-20`
* `add n/Betsy Crowe p/+65 9123 4567 e/betsycrowe@example.com t/Portrait d/2027-01-15 s/Booked`

Supported stages are `Enquiry`, `Booked`, `Shot Completed`, `Awaiting Payment`, and `Delivered`.

### Listing and sorting contacts: `list`

Use `list` to return to all client engagements after a search or sorted view.
Use `list sort/date` to review all engagements from the earliest event date to the latest.
Past events are included. Engagements on the same date stay in the order they were added.

Formats:
* `list` — all contacts in insertion order.
* `list sort/date` — all contacts by ascending event date.

The heading is `All contacts (N)` or `All contacts by event date (N)`, respectively.
The result panel reports `Showing N contacts.` With no saved contacts, it reports `No contacts to display.`
Here, `N` is the number of displayed contacts.

Examples:
* `list sort/date` brings earlier shoots to the top, including completed dates.
* `list` restores insertion order after sorting.
* `LIST SORT/DATE` also works: the list command, prefix and sort value are case-insensitive.

Each command starts from all records, replacing the previous search or sort.
Displayed indexes are recalculated; subsequent edit, advance, and delete commands use the indexes currently shown.
Sorting does not change the saved records or their insertion order. Restarting restores insertion order.

Only the `sort/` prefix and the value `date` are supported. Spaces and tabs may separate arguments,
but the command must occupy one line. Stage filtering is not available in v1.2.

| Invalid input | Feedback |
| --- | --- |
| `list sort/name` | `Invalid sort option. Use list sort/date.` |
| `list sort/` | `Empty value for sort/.` |
| `list sort/date sort/date` | `Duplicate parameter: sort/.` |
| `list stage/Booked` | `Unsupported parameter: stage/.` |
| `list 3` or `list sort/date extra` | `Invalid command format. Usage: list [sort/date]` |

Unsupported prefixes are rejected. Invalid list commands leave both the data and current view unchanged.

### Viewing upcoming events: `upcoming`

Shows all client engagements scheduled for **today and the following six calendar dates**, inclusive.
This is seven calendar days in total, using your computer's local date and timezone when you run the command.

Format: `upcoming`

* Results start from all saved contacts, replacing any previous search or sort.
* Events are sorted from earliest to latest; events on the same date retain insertion order.
* Past events and events seven or more days after today are excluded.
* Displayed indexes start at 1. Edit and delete use the indexes in this view.
* The heading shows `Upcoming: START_DATE to END_DATE (N)` and feedback shows `Showing N upcoming contacts.`
* With no matching events, the list is empty and feedback is `No events scheduled from START_DATE to END_DATE.`
* Records are not changed or saved by this command. Use `list` to restore all contacts in insertion order.

For example, running `upcoming` on **2026-10-14** includes dates **2026-10-14 through 2026-10-20**.
An event on 2026-10-21 is outside that window. With two matches, the heading is
`Upcoming: 2026-10-14 to 2026-10-20 (2)` and the feedback is `Showing 2 upcoming contacts.`

The command is case-insensitive and takes no arguments: `UPCOMING` works, but `upcoming 7` and
`upcoming sort/date` return `Invalid command format. Usage: upcoming` without changing the current view.
The date window does not advance automatically at midnight; run `upcoming` again to refresh it.
If the date window cannot be calculated, ShutterLink asks you to check the system date and leaves the view unchanged.

### Editing an engagement: `edit`

Edits one or more details of an existing engagement. Use `s/STAGE` to correct or directly set its stage.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [t/EVENT_TYPE] [d/EVENT_DATE] [s/STAGE]`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* The stage may be moved forward or backward to any supported value.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/Corporate d/2027-03-01 s/Awaiting Payment` edits the name, event details,
   and stage of the 2nd engagement.

### Advancing an engagement stage: `advance`

Advances an engagement to the next workflow stage.

Format: `advance INDEX`

The stage progression is `Enquiry` → `Booked` → `Shot Completed` → `Awaiting Payment` → `Delivered`.
An engagement already at `Delivered` cannot be advanced. Use `edit INDEX s/STAGE` when a stage needs correction.

Example: `advance 2` advances the second displayed engagement by one stage.

### Finding contacts: `find`

Find contacts by name, phone number, email address, or event type.

Format:

```text
find n/SEARCH_TEXT
find p/SEARCH_TEXT
find e/SEARCH_TEXT
find t/SEARCH_TEXT
```
Where:
* n/ searches contact names.
* p/ searches phone numbers.
* e/ searches email addresses.
* t/ searches event types.

Examples:
* find n/Jonathan finds `Jonathan Wong` and `Jonathan Tan`.
* find p/9123 finds phone numbers containing `9123`.
* find e/jonathan finds email addresses containing `jonathan`.
* find t/Wed finds event types containing `Wed`.

| Invalid input | Reason |
| --- | --- |
| `find` | No search field was provided |
| `find n/` | The search value is empty |
| `find n/Jonathan p/9123` | More than one search field was provided |
| `find n/Jonathan n/Tan` | The same search field was repeated |

### Deleting a person: `delete`

Deletes the specified person from the address book.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Clearing all entries: `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

ShutterLink automatically saves data after every command. You do not need to save manually.

### Editing the data file

ShutterLink data is saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file. Every engagement must include a `stage` whose value is `Enquiry`, `Booked`, `Shot Completed`, `Awaiting Payment`, or `Delivered`.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes make the data file invalid, ShutterLink starts with an empty address book at the next run. The invalid file remains on disk until you run a command (ShutterLink saves after every command). Still, we recommend backing up the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Install the app on the other computer and overwrite the data file it creates with the data file from your previous ShutterLink home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL t/EVENT_TYPE d/EVENT_DATE [s/STAGE]` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com t/Wedding d/2026-12-20 s/Booked`
**Advance stage** | `advance INDEX`<br> e.g., `advance 2`
**Clear** | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit** | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [t/EVENT_TYPE] [d/EVENT_DATE] [s/STAGE]`<br> e.g., `edit 2 n/James Lee s/Booked`
**Find** | `find n/SEARCH_TEXT`, `find p/SEARCH_TEXT`, `find e/SEARCH_TEXT`, or `find t/SEARCH_TEXT`<br> e.g., `find n/James`
**List** | `list` or `list sort/date`
**Upcoming events** | `upcoming`
**Help** | `help`
