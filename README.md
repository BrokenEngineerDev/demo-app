# Goal
## Build a small demo application using Kotlin, Jetpack Compose, and MVVM to demonstrate your understanding of Compose basics, state handling, and navigation.
## Requirements

### 1. Login Screen
The app should allow login with predefined credentials (kept as constants).
Validation:Name input: only letters or underscores, length 6–16.
Password input: only digits or underscores, length 8–12.Password must be masked (dots/asterisks).

Behavior:Disable the Login button if inputs are invalid.
Show a Snackbar error if login credentials are wrong.Simulate login delay: delay(3000).
After successful login, navigating back to the login screen should not be possible with a simple back button press (but don’t block back navigation entirely).

---

### 2. List Screen
Display a scrollable list of at least 30 items.Each item should:
Contain text (simple values like "1", "2", "3", etc. are fine).
Have a checkable checkbox.
Additional requirements:Include a special “Check it all” item that toggles all checkboxes at once:
It should support three states:
Unchecked → no items selected.Checked → all items selected.
Indeterminate → some but not all items selected.
This item should be sticky (pinned) at the top or bottom of the list. Support logout via a toolbar action button.
Preserve scroll position when navigating back from the details screen.

---

### 3. Details Screen
When clicking on a list item:Navigate to a Details screen where:
Item text is editable.
Checkbox is checkable.
Save behavior:
Enable the Save button only if changes are made.
On Save, update the item and navigate back to the list.
Navigation:
Support going back without saving (system back or toolbar back button).Support logout via a toolbar action button.
Validation:
Item text: only letters, digits, or underscore, length 1–100.
Technical Notes
Single-Activity app.
Pure Compose navigation (no fragments).All state/data should be stored in memory (no database, no persistence).
Clean Architecture (repositories/data sources) is not required.Keep ViewModels clean (avoid unnecessary logic).
UI structure per screen:Toolbar at the top.
Action button at the bottom.Scrollable content in the middle.

---

Bonus Points
Usage of derivedStateOf, snapshotFlow, and side effects.
Optimization of recompositions.Creating custom reusable UI components (e.g., MyTextField, MyButton).
Applying stateful/stateless Compose patterns.Applying basic theming and styling.
Objective
We don’t expect a production-ready app, but rather a clear demonstration of Compose fundamentals and your ability to learn quickly. This test helps us see if you’re ready for further growth with our team.
