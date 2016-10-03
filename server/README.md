This server exposes a REST API for the Housecut backend.


# API Documentation

| Title | URL | Method | URL Params | Data Params | Success Response | Error Response | Notes |
| ----- | --- | ------ | ---------- | ----------- | ---------------- | -------------- | ----- |
| Register | `/register` | `PUT` | None | { username: [string], email: [string], password: [string] } | { success: true } | { success: false, message: "Explanation" } | |
| Login | `/login` | `PUT` | None | { email: [string], password: [string] } | { success: true, token: [string] } | { success: false, message: "Explanation" } | |
