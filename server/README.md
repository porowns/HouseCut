This server exposes a REST API for the HouseCut backend.


# API Documentation

Tasks have the following schema:
~~~
{
	name: { type: String, minLength: 1 },
  recurring: Boolean,
  recurringIntervalDays: Number,
  type: { type: String, minLength: 1, enum: [ 'Rotating', 'Assigned', 'Voluntary' ] },
  currentlyAssigned: { type: String, minLength: 1 } // user ID
}
~~~

| Title | URL | Method | URL Params | Data Params | Success Response | Error Response | Notes |
| ----- | --- | ------ | ---------- | ----------- | ---------------- | -------------- | ----- |
| Register | `/register` | `POST` | None | { username: [String], email: [String], password: [String] } | { success: true } | { success: false, message: "Explanation" } | |
| Login | `/login` | `POST` | None | { email: [String], password: [String] } | { success: true, id: [String], token: [String] } | { success: false, message: "Explanation" } | |
| Delete account | `/deleteaccount` | `POST` | None | { email: [String], password: [String], token: [String] } | { success: true } | { success: false, message: "Explanation" } | |
| Set admin privilege | `/setadmin` | `POST` | None | { userId: [String], setAdmin: (1&#124;0)&#124;('true'&#124;'false'), token: [String] } | { success: true } | { success: false, message: "Explanation" } | |
| Get tasklist (for a user or a household) | `/tasklist` | `GET` | token=[String] and optional userId=[String] | None | { success: true, tasklist: [Array of Tasks] } | { success: false, message: "Explanation" } | |
| Get roommates | `/household/roommates` | `GET` | token=[String] | None | { success: true, roommates: [Array of user IDs] } | { success: false, message: "Explanation" } | Will update in the future to return user objects instead of only ids. |
| Add/remove roommate | `/household/roommates`| `POST` | None | { operation: ('add'&#124;'remove'), userId: [String] (will default to current user's id), householdName: [String], householdPassword: [String] (HH name and pass only needed if self joining a new household) } | { success: true, householdId: [String] (only if joined a new household) } | { success: false, message: "Explanation" } | |

# Starting the server

To start the server locally, run the command `npm start`.

# Testing

Tests are held in the test/ directory. To run the tests, start the server
locally with the command `npm start`. Then, in a separate terminal, run the
command `npm test`. Note that there is no test database set up (yet), so all
tests run on the real database. This doesn't matter, though, as they clean up
their junk data when they finish.

# Backend Structure (notes)

/register

/login

/deleteaccount

/createhousehold
	houseHoldName
	houseHoldPassword

	res = success!
/joinhousehold
	HouseHoldName
	houseHoldPassword

	res = true/false

/leavehousehold
	user id

******************************

/household

... /tasklist

	... task "id"

	... task "id"

... /whiteboard

... /roommates
