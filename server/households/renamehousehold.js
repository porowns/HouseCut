/* Implemented by: Chris */
/*
  POST /household/renamehousehold

  with body:
  {
    token: [String],
    name: [String]
  }
*/

var jwtDecode = require('jwt-decode');
var Household = require('./../models/household.js');
var utilities = require('./../utilities.js');

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;
  var name = req.body.name;

  if (!name || name == "") {
    res.json({
      success: false,
      message: "Give a new name for the household."
    });
    return;
  }

  utilities.getHouseholdFromUserId(currentUserId, function(hh) {
    utilities.checkUserIsAdmin(currentUserId, function(isAdmin) {
      if (isAdmin) {
        /* currentUser is an admin */
        Household.count({ 'houseHoldName': name }, function(err, count) {
          if (err) {
            throw err;
          }
          if (count > 0) {
            res.json ({
              success: false,
              message: "That Household name is already taken."
            });
            return;
          }
          Household.update( { '_id' : hh._id}, { $set: {'houseHoldName': name} }, function(err) {
            if (err) {
              throw err;
            }
            res.json ({
              success: true,
              message: "Household name changed."
            });
            return;
          });
        });
      }
      else {
        /* currentUser is not an admin */
        res.json({
          success: false,
          message: "Only Household admins can change the Household name."
        });
        return;
      }
    });
  });
};
