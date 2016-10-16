// Implemented by Kaleb
// Creates a household, adds the user to the household list


var Household = require('../models/household');

// Hashed Password for houseHoldPassword
var jwt = require('jsonwebtoken');
var jwtDecode = require('jwt-decode');
var crypto = require('crypto');
var rand = require('csprng');
var User = require('../models/user.js');
var utilities = require('./../utilities.js');



module.exports = function(req, res) {
	var token = req.body.token;
	var houseHoldName = req.body.houseHoldName;
	var houseHoldPassword = req.body.houseHoldPassword;

	var decoded = jwtDecode(token);
	var currentUserId = decoded.id;
	console.log(currentUserId)
	var cancelprogram = 0;

	// Check if User has Household
	User.findOne({ '_id' : currentUserId}, function (err, user) {
		if (err) {
			throw err;
		}

		if (user) {
			if (user.householdId != 0)
			{
				cancelprogram = 1;
			}

			// Checks to see if user input a household name
			if (houseHoldName == "" || !houseHoldName)
			{
				console.log("hi");
				res.json({
					success: false,
					message: "Please enter a household name."
				});
			}

			else if (cancelprogram != 0)
			{
				res.json({
					success: false,
					message: "User has a household already."
				});
			}
			// Checks to see if user input a password
			else if (houseHoldPassword == "" || !houseHoldPassword)
			{
				res.json({
					success: false,
					message: "Please enter a household password."
				});
			}

			// Ensures that the password is of length 6
			else if (houseHoldPassword.length < 6) {
				res.json({
					success:false,
					message: "Please enter a password at least 6 characters long"
				});
			}

			// If all the above cases are true,
			// Create the Household
			else {
				// Check if user has household

				// Check if household exists
				Household.findOne({ 'houseHoldName' : houseHoldName}, function(err, household) {
					if (err) {
						throw (err);
					}
					// If it exists
					if (household) {
						res.json({
							success: false,
							message: "Household already exists."
						});
					}

					else {
						// Hash Password
						var salt = rand(160, 36);
						var newpass = salt + houseHoldPassword;
						var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex")
						// Changing Password
						var id = crypto.createHash('sha512').update(currentUserId+rand).digest("hex");

						// Create Household
						var household = new Household({
							houseHoldName: houseHoldName,
							houseHoldPassword: hashed_password,
							salt: salt,
							id: id,
							HouseholdMembers: currentUserId
						});

						// Update User ID
						User.update({ '_id': currentUserId }, {
							householdId: houseHoldName
						}, function (err) {
							if (err)
							{
								throw (err);
							}
						});

						// Add to Database
						household.save(function(err) {
							if (err)
								throw err;
							console.log('Household created');
							res.json({
								success: true,
								message: 'Congratulations! You made a household!'
							});
						});
					}
				});
			}




		}
	});


}
