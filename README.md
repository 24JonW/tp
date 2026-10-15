[![CI Status](https://github.com/AY2627S1-CS2103T-T08-3/tp/actions/workflows/gradle.yml/badge.svg)](https://github.com/AY2627S1-CS2103T-T08-3/tp/actions/workflows/gradle.yml)

![Ui](docs/images/Ui.png)

# ShutterLink

ShutterLink is a desktop engagement manager for independent event photographers. Each engagement combines a client's
contact details with an event and its workflow stage, helping photographers manage work from the initial enquiry
through post-event delivery.

## Features

- Add client engagements at any stage, defaulting to `Enquiry` when no stage is supplied.
- Edit client, event, and engagement-stage details.
- Advance an engagement to its next workflow stage with one command.
- Delete engagements immediately when they are no longer needed.
- Find engagements using a partial, case-insensitive name, phone number, email address, or event type.
- List all engagements or sort them by event date.
- View events taking place within the next seven calendar days.
- Automatically save engagements and restore them when the application starts.
- View supported commands through the in-app help panel.

## Getting started

1. Install Java 25 or later and download the latest `.jar` file from the project releases page.
2. Run it with:

   ```bash
   java -jar addressbook.jar
   ```

## Example commands

```text
add n/John Tan p/+65 9123 4567 e/john.tan@example.com t/Wedding d/2026-12-20 s/Booked
edit 1 d/2026-12-25 s/Enquiry
advance 1
find t/Wedding
list sort/date
upcoming
help
```

## Documentation

- [User Guide](docs/UserGuide.md)
- [Developer Guide](docs/DeveloperGuide.md)
- [About Us](docs/AboutUs.md)
- [ShutterLink Product Website](https://ay2627s1-cs2103t-t08-3.github.io/tp/)

## Acknowledgements

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
