This server exposes a REST API for the HouseCut backend.

URL: http://housecut-145314.appspot.com/

# API Documentation

Users have the following schema:
~~~
	{
		displayName: [String],
		id: [String],
		admin: [Boolean]
	}
~~~

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
| Get tasklist (for a user or a household) | `/household/tasklist` | `GET` | token=[String] and optional userId=[String] | None | { success: true, tasklist: [Array of Tasks] } | { success: false, message: "Explanation" } | |
| Get all roommates | `/household/roommates` | `GET` | token=[String] | None | { success: true, roommates: [Array of Users] } | { success: false, message: "Explanation" } |  |
| Get single roommate/user | `/household/roommates` | `GET` | token=[String]&userId=[String] | None | { success: true, roommate: [User] }| { success: false, message: "Explanation" } | |
| Add roommate | `/household/roommates`| `POST` | None | { operation: 'add', userId: [String] [will default to current user's id], householdName: [String] OPTIONAL, householdPassword: [String] OPTIONAL } | { success: true, householdId: [String] [only if joined a new household]} | { success: false, message: "Explanation" } | HH name and pass NOT needed if an admin is calling this. |
| Remove roommate | `/household/roommates`| `POST` | None | { operation: 'remove', userId: [String] [will default to current user's id] } | { success: true } | { success: false, message: "Explanation" } | Sole admins of households cannot remove themselves until they appoint a new admin first. |

# Starting the server

To start the server locally, run the command `npm start`.

The server is running live at the URL at the top of this README.

# Testing

Tests are held in the test/ directory. To run the tests, start the server
locally with the command `npm start`. Then, in a separate terminal, run the
command `npm test`. Note that there is no test database set up (yet), so all
tests run on the real database. This doesn't matter, though, as they clean up
their junk data when they finish.

# Backend Structure (notes)

/register (POST)
/login (POST)
/deleteaccount (POST)
/setadmin (POST)

/createhousehold (POST)

******************************

/household

... /tasklist (GET, POST)

... /whiteboard

... /roommates (GET, POST)
