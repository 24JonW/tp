---
layout: page
title: Le Xuan Son's Project Portfolio Page
---

### Project: ShutterLink

ShutterLink is a desktop application that helps freelance event photographers manage client engagements, from the
first enquiry to final delivery. The user interacts with it using a CLI, and it has a GUI created with JavaFX. It is
written in Java and is based on AddressBook-Level3.

Given below are my contributions to the project.

* **New Feature**: Duplicate detection and possible-typo warnings
  ([\#63](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/63), [\#78](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/78)).
  * What it does: rejects an engagement whose name, phone, email, event type and event date all match an existing one,
    and warns, without blocking, when a new or edited engagement shares a phone number or email with a different
    client. The warning lists the affected records by their displayed indexes and says what they share.
  * Justification: photographers record several engagements per client and type contact details by hand, so exact
    duplicates and typos are common, while shared family or business contacts are legitimate.
  * Highlights: separates three notions of sameness (same client, same engagement, similar contact details). The
    warning lists each client once, refers to indexes that stay valid after the command, and does not repeat when an
    edit leaves the client's details unchanged.

* **New Feature**: Validation and normalization of client details
  ([\#64](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/64), [\#81](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/81), [\#83](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/83), [\#128](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/128)).
  * What it does: accepts international phone numbers with spaces and hyphens, validates email domains, accepts
    accented letters, apostrophes, hyphens and periods in names, and normalizes values so that formatting differences
    do not defeat duplicate detection.
  * Highlights: the same rules apply to typed commands and to the data file, so a hand-edited file cannot load values
    that a command would reject.

* **Enhancement**: Names containing relation markers such as `s/o` and `d/o`
  ([\#169](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/169), [\#171](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/171)).
  * What it does: `n/Raj s/o Kumar` keeps `s/o` in the name instead of reading it as the stage prefix `s/`, and
    `find n/Raj s/o` also works.
  * Highlights: changing the tokenizer was risky because every command depends on it, so markers are recognized only
    where they cannot be a real prefix, and a marker after any other prefix is still read as that prefix.

* **Enhancements to existing features**: clearer error messages that name unsupported and missing parameters
  ([\#126](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/126)), ShutterLink wording instead of AB3 wording in result messages ([\#85](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/85), [\#127](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/127)),
  and wrapping of long feedback in the result display ([\#130](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/130)).

* **Code contributed**:
  [RepoSense link](https://nus-cs2103-ay2627-s1.github.io/tp-dashboard/?search=sonlexuan3000&breakdown=true)

* **Project management**:
  * Team lead of a five-person team.
  * Opened 23 pull requests and 25 issues in the team repository.
  * Shared agent instructions so that AI-assisted changes follow the coding standard ([\#67](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/67)).

* **Documentation**:
  * User Guide: documented edits that change nothing ([\#129](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/129)) and the possible-typo warning.
  * Developer Guide:
    * Wrote the use cases ([\#40](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/40)).
    * Updated the Logic, Model, Storage and persistence sections, with the add and persistence sequence diagrams
      ([\#97](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/97)), and regenerated the model and storage class diagrams ([\#62](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/62)).
    * Wrote the implementation section on duplicate detection and possible-typo warnings, with its activity diagram.

* **Community**:
  * PRs reviewed: [\#60](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/60), [\#105](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/105), [\#106](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/106), [\#134](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/134), [\#139](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/139),
    [\#144](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/144).

* **UI**: designed the UI mockups ([\#28](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/28), [\#31](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/31)) and replaced `Ui.png` with a screenshot of the
  current product ([\#176](https://github.com/AY2627S1-CS2103T-T08-3/tp/pull/176)).
