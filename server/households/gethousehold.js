/*
  Implemented by: Simon
  Navigated by: Chris

  GET /household/name?token={ string }
  Returns the current user's household name.

*/

var jwtDecode = require('jwt-decode');
var Household = require('./../models/household.js');
var utilities = require('./../utilities.js');

module.exports = function(req, res) {
  var token = req.query.token;
  var decoded = jwtDecode(token);
  var userId = decoded.id;

  utilities.getHouseholdFromUserId(userId, function(hh) {
    if (!hh) {
      res.json({
        success: false,
        message: "Current user is not in a household."
      });
      return;
    }
    res.json({
      success: true,
      name: houseHoldName
    });

  });
};
