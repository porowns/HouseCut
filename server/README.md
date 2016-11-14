This server exposes a REST API for the HouseCut backend.

URL: http://housecut-145314.appspot.com/

# API Documentation

## Schemas

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

## Endpoints

| Title | URL | Method | URL Params | Data Params | Success Response | Error Response | Notes |
| ----- | --- | ------ | ---------- | ----------- | ---------------- | -------------- | ----- |
| Register | `/register` | `POST` | None | { username: [String], email: [String], password: [String] } | { success: true } | { success: false, message: "Explanation" } | |
| Login | `/login` | `POST` | None | { email: [String], password: [String] } | { success: true, id: [String], token: [String] } | { success: false, message: "Explanation" } | |
| Delete account | `/deleteaccount` | `POST` | None | { token: [String] } | { success: true } | { success: false, message: "Explanation" } | |
| Create Household | `/createhousehold` | `POST` | None | { token: [String], houseHoldName : [String], houseHoldPassword: [String] } | { success: true } | { success: false, message: "Explanation" } | |
| Join Household | `/joinhousehold` | `POST` | None | { token: [String], houseHoldName : [String], houseHoldPassword: [String] } | { success: true } | { success: false, message: "Explanation" } | |
| Delete Household | `/deletehousehold` | `POST` | None | { token: [String]} | { success: true } | { success: false, message: "Explanation" } | Only admin can delete the household. |
| Set admin privilege | `/setadmin` | `POST` | None | { userId: [String] OPTIONAL (defaults to current user's id), setAdmin: ('true'&#124;'false'), token: [String] } | { success: true } | { success: false, message: "Explanation" } | userId will default to the calling user |
| Get tasklist (for a user or a household) | `/household/tasklist` | `GET` | token=[String] and optional userId=[String] | None | { success: true, tasklist: [Array of Tasks] } | { success: false, message: "Explanation" } | |
| Get all roommates | `/household/roommates` | `GET` | token=[String] | None | { success: true, roommates: [Array of Users] } | { success: false, message: "Explanation" } |  |
| Get single roommate/user | `/household/roommates` | `GET` | token=[String]&userId=[String] | None | { success: true, roommate: [User] }| { success: false, message: "Explanation" } | |
| Add roommate | `/household/roommates`| `POST` | None | { operation: 'add', userId: [String] [will default to current user's id], householdName: [String] OPTIONAL, householdPassword: [String] OPTIONAL } | { success: true, householdId: [String] } | { success: false, message: "Explanation" } | HH name and pass NOT needed if an admin is calling this. |
| Remove roommate | `/household/roommates`| `POST` | None | { operation: 'remove', userId: [String] [will default to current user's id] } | { success: true } | { success: false, message: "Explanation" } | Sole admins of households cannot remove themselves until they appoint a new admin first. |
| Create task | `/household/createtask` | `POST` | None | { token: [String], name: [String], type: [String]:(one of: `Rotating`, `Voluntary`, `Unassigned`), assigned: [String]:(only required if type is `Assigned`), recurring: [Boolean], recurringIntervalDays: [Integer]:(only required if `recurring` is true) } | { success: true } | { success: false, message: "Explanation" } | |

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
