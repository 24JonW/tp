[![CI Status](https://github.com/AY2627S1-CS2103T-T08-3/tp/actions/workflows/gradle.yml/badge.svg)](https://github.com/AY2627S1-CS2103T-T08-3/tp/actions/workflows/gradle.yml)

![Ui](docs/images/Ui.png)

# ShutterLink

ShutterLink is a desktop contact and engagement manager for independent event photographers. It keeps each client connected to their event details and engagement stage, helping photographers manage relationships from the initial enquiry through post-event follow-up.

## Features

- Add client engagements with contact information, event type, and event date.
- Edit contact and event details while preserving the engagement stage.
- Track stages such as `Enquiry`, `Booked`, and `Awaiting Payment`.
- Delete contacts immediately when they are no longer needed.
- Find engagements by exact name, phone number, email address, or event type.
- List all contacts, sort them by event date, or filter them by stage.
- View events taking place within the next seven calendar days.
- Automatically save contacts and restore them when the application starts.
- View supported commands through the in-app help panel.

## Getting started

1. Download the latest `.jar` file from the project releases page.
2. Run it with:

   ```bash
   java -jar shutterlink.jar
   ```

## Example commands

```text
add n/John Tan p/+65 9123 4567 e/john.tan@example.com t/Wedding d/2026-12-20
stage 1 s/Booked
edit 1 d/2026-12-25
find t/Wedding
list sort/date
list stage/Awaiting Payment
upcoming
help
```

## Documentation

- [User Guide](docs/UserGuide.md)
- [Developer Guide](docs/DeveloperGuide.md)
- [About Us](docs/AboutUs.md)
- [Address Book Product Website](https://se-education.org/addressbook-level3)

## Acknowledgements

This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org).
