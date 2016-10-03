This server exposes a REST API for the Housecut backend.


# API Documentation

| Title | URL | Method | URL Params | Data Params | Success Response | Error Response | Notes |
| ----- | --- | ------ | ---------- | ----------- | ---------------- | -------------- | ----- |
| Register | `/register` | `PUT` | None | { username: [string], email: [string], password: [string] } | { success: true } | { success: false, message: "Explanation" } | |
| Login | `/login` | `PUT` | None | { email: [string], password: [string] } | { success: true, token: [string] } | { success: false, message: "Explanation" } | |
| Delete account | `/deleteaccount` | `PUT` | None | { email: [string], password: [string] } | { success: true } | { success: false, message: "Explanation" } | |

# Backend Structure
/register

/login

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
