This server exposes a REST API for the HouseCut backend.


# API Documentation

| Title | URL | Method | URL Params | Data Params | Success Response | Error Response | Notes |
| ----- | --- | ------ | ---------- | ----------- | ---------------- | -------------- | ----- |
| Register | `/register` | `POST` | None | { username: [String], email: [String], password: [String] } | { success: true } | { success: false, message: "Explanation" } | |
| Login | `/login` | `POST` | None | { email: [String], password: [String] } | { success: true, id: [String], token: [String] } | { success: false, message: "Explanation" } | |
| Delete account | `/deleteaccount` | `POST` | None | { email: [String], password: [String], token: [String] } | { success: true } | { success: false, message: "Explanation" } | |

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
