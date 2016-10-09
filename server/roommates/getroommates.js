/* Implemented by: Chris */
/*
  GET /household/roommates?token=X

  Returns the current user's roommates.
*/

var User = require('../models/user.js');
var jwtDecode = require('jwt-decode');
var Household = require('../models/household.js');
var utilities = require('../utilities.js');

module.exports = function(req, res) {
  var token = req.query.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;
  var householdId = decoded.householdId;

  Household.findOne({ '_id': householdId }, function(err, hh) {
    if (err) {
      throw err;
    }
    if (hh) {
      res.json({
        success: true,
        roommates: hh.HouseholdMembers
      });
    }
    else {
      res.json({
        success: false,
        message: "Current user's household wasn't found."
      });
    }
  });
}
