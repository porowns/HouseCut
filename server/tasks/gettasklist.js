/* Implemented by: Chris */
/*
  GET /household/tasklist?token=X&(userId=Y)

  Returns the user's household tasklist.
  If you supply a userId, it will return only the tasks
  that are currently assigned to that user.
*/

var User = require('../models/user.js');
var jwtDecode = require('jwt-decode');
var Household = require('../models/household.js');
var utilities = require('../utilities.js');

module.exports = function(req, res) {
  var token = req.query.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;
  var userId = req.query.userId;
  var householdId = decoded.householdId;

  if (userId) {
    utilities.checkUsersAreInSameHousehold(currentUserId, userId, function(hh) {
      if (hh) {
        var userTasklist = hh.taskList.filter(function(e, i, a) {
          return (e.currentlyAssigned == userId);
        });
        res.json({
          success: true,
          tasklist: userTasklist
        });
      }
      else {
        res.json({
          success: false,
          message: "Current user wasn't in the same household as userId."
        });
      }
    });
  }
  else {
    utilities.checkUserIsInHousehold(currentUserId, householdId, function(hh) {
      if (hh) {
        res.json({
          success: true,
          tasklist: hh.taskList
        });
      }
      else {
        res.json({
          success: false,
          message: "Current user wasn't in that household."
        });
      }
    });
  }
}
