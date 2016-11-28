/* Implemented by: Chris */
/*
  GET /household/roommates?token=X(&userId=Y)

  Returns the current user's roommates.

  If you supply a userId, will return only that user.
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
    if (userId) {
      utilities.getRoommatesFromHousehold(hh, function(roommates) {
        res.json({
          success: true,
          roommates: roommates
        });
        return;
      });
    }
    else {
      var userIndex = hh.HouseholdMembers.indexOf(userId);
      if (userIndex == -1) {
        res.json({
          success: false,
          message: "User not found in household."
        });
        return;
      }
      else {
        utilities.getRoommateFromHousehold(hh, userId, function(roommate) {
          res.json({
            success: true,
            roommate: roommate
          });
          return;
        });
      }
    }
  });
};
