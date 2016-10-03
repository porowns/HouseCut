var Household = require('../models/household');

module.exports = function(req, res) {
	var houseHoldName = req.body.houseHoldName;
	var houseHoldPassword = req.body.houseHoldPassword;

	if (houseHoldName == "" || !houseHoldName)
	{
		res.json({
			success: false,
			message: "Please enter a household name."
		});
	}

	else if (houseHoldPassword == "" || !houseHoldPassword)
	{
		res.json({
			success: false,
			message: "Please enter a household password."
		});
	}
	else if (houseHoldPassword.length < 6) {
		res.json({
			success:false,
			message: "Please enter a password at least 6 characters long"
		});
	}
	else {
		// We're good to go, add the household to the DATABASE

		// Check if household exists
		Household.findOne({ 'houseHoldName' : houseHoldName}, function(err, household) {
			if (err) {
				throw (err);
			}

			if (household) {
				res.json({
					success: false,
					message: "Household already exists."
				});
			}

			else {
				// Create Household
				var household = new Household({
					'houseHoldName': houseHoldName,
					'houseHoldPassword': houseHoldPassword
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
