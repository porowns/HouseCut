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

var jwtDecode = require('jwt-decode');
var Household = require('./../models/household.js');
var utilities = require('./../utilities.js');

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;
  var userId = req.body.userId || currentUserId;
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

  utilities.getHouseholdFromUserId(currentUserId, function(hh) {
    utilities.checkUserIsAdmin(currentUserId, function(isAdmin) {
      if (isAdmin) {
        /* currentUser is an admin */
        if (currentUserId == userId && operation == "remove") {
          /* need to check if admin is the sole admin */
          utilities.getNumAdmins(hh, function(num) {
            if (num == 1) {
              res.json({
                success: false,
                message: "Sole admin of a household can't remove themselves from household."
              });
              return;
            }
            else {
              addOrRemoveUserFromHousehold(operation, currentUserId, hh,
                undefined, function(r) {
                res.json(r);
                return;
              });
            }
          });
        }
        else {
          /*
            admins always allowed to attempt to add/remove other users
            or attempt to add themselves
          */
          addOrRemoveUserFromHousehold(operation, userId, hh,
            undefined, function(r) {
            res.json(r);
            return;
          });
        }
      }
      else {
        /* currentUser is not an admin */
        if (currentUserId == userId) {
          /* users always allowed to attempt to add/remove themselves */
          addOrRemoveUserFromHousehold(operation, currentUserId, hh,
            { hhName: hhName, hhPass: hhPass }, function(r) {
            res.json(r);
            return;
          });
        }
        else {
          /* users never allowed to attempt to add/remove other users */
          res.json({
            success: false,
            message: "Non-admin household member cannot add/remove users other than themselves."
          });
          return;
        }
      }
    });
  });
};

var addOrRemoveUserFromHousehold = function(operation, userId, hh, data, callback) {
  if (operation == "remove") {
    utilities.removeUserFromHousehold(userId, hh._id, function(r) {
      if (callback)
        callback(r);
    });
  }
  else if (operation == "add"){
    if (hh) {
      /* no duplicate households allowed */
      if (callback)
        callback({
          success: false,
          message: "User is already in a household named " +
                    hh.houseHoldName + " with id " + hh._id
        });
    }
    else {
      /* adding to new household */
      utilities.addUserToHousehold(userId, data.hhName, data.hhPass, function(r) {
        if (callback)
          callback(r);
      });
    }
  }
};
