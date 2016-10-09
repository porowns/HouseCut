/* Implemented by: Chris */
/*
  POST /household/roommates

  with body:
  {
    operation: (add|remove),
    userId: String, (if not given, will be set to the currentUser's id)
    token: String,
    householdName: String, // only necessary if self joining new household
    householdPassword: String // only necessary if self joining new household
   }

   Adds or removes users from households.

  Admins can always add any user IF that user isn't in a household already.
  Admins can remove any user except if they're the sole admin and they're
  trying to remove themselves.

  A user an add themselves to a new household if they supply a valid household
  name and password, AND they aren't already in a household.
  A user can always remove themselves.
*/

var User = require('../models/user.js');
var jwtDecode = require('jwt-decode');
var Household = require('../models/household.js');
var utilities = require('../utilities.js');

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;
  var currentUserIsAdmin = decoded.admin;
  var userId = req.body.userId || currentUserId;
  var householdId = decoded.householdId;
  var operation = req.body.operation;
  var hhName = req.body.houseHoldName;
  var hhPass = req.body.houseHoldPassword;

  if (operation != "add" && operation != "remove") {
    res.json({
      success: false,
      message: "Field 'operation' must be 'add' or 'remove'."
    });
    return;
  }

  if (currentUserIsAdmin) {
    if (currentUserId == userId && operation == "remove") {
      /* need to check if admin is the sole admin */
      Household.findOne({ '_id': householdId }, function(err, hh) {
        utilities.getNumAdmins(hh, function(num) {
          if (num == 1) {
            res.json({
              success: false,
              message: "Sole admin of a household can't remove themselves from household."
            });
          }
          else {
            addOrRemoveUserFromHousehold(operation, currentUserId, hh, function(r) {
              res.json(r);
            });
          }
        });
      });
    }
    else {
      /*
        admins always allowed to attempt to add/remove other users
        or attempt to add themselves
      */
      utilities.getHouseholdFromUserId(currentUserId, function(hh) {
        addOrRemoveUserFromHousehold(operation, userId, hh, function(r) {
          res.json(r);
        });
      });
    }
  }
  else {
    if (currentUserId == userId) {
      /* users always allowed to attempt to add/remove themselves */
      utilities.getHouseholdFromUserId(currentUserId, function(hh) {
        addOrRemoveUserFromHousehold(operation, currentUserId, hh, function(r) {
          res.json(r);
        });
      });
    }
    else {
      /* users never allowed to attempt to add/remove other users */
      res.json({
        success: false,
        message: "Non-admin household member cannot add/remove users other than themselves."
      });
    }
  }
}

var addOrRemoveUserFromHousehold = function(operation, userId, hh, callback) {
  if (operation == "remove") {
    utilities.removeUserFromHousehold(userId, hh._id, function(r) {
      callback(r);
    });
  }
  else if (operation == "add"){
    if (hh) {
      /* no duplicate households allowed */
      callback({
        success: false,
        message: "User is already in a household named " +
                  hh.houseHoldName + " with id " + hh._id
      });
    }
    else {
      /* adding to new household */
      utilities.addUserToHousehold(userId, hhName, hhPass, function(r) {
        callback(r);
      });
    }
  }
}
