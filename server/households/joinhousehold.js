var Household = require('../models/household');
var User = require('../models/user.js');
var utilities = require('./../utilities.js');

// To Depcryt Household Password
var jwt = require('jsonwebtoken');
var jwtDecode = require('jwt-decode');
var crypto = require('crypto');
var rand = require('csprng');

module.exports = function (req, res) {

var token = req.body.token;
var houseHoldName = req.body.houseHoldName;
var houseHoldPassword = req.body.houseHoldPassword;

// Getting User ID from Input
var decoded = jwtDecode(token);
var currentUserId = decoded.id;

// Access User
User.findOne({ '_id' : currentUserId}, function(err, user) {
  if (err) {
    throw err;
  }
  if (user) {
    if (user.householdId != 0) {
      // If user has a household...
      res.json({
        success: false,
        message: "User already has a household"
      });
    }
    else {
      // If user does not have household...
      Household.findone({ "_houseHoldName" : houseHoldName}, function (err, household) {
        if (household) {
          res.json({
            success: true,
            message: "Household exists! Let's get you in there!"
          });
        }
        else {
          res.json({
            success:false,
            message: "Household does not exist.."
          });
        }
      });
    }
  }
  else {
    res.json({
      success: false,
      message: "Whoops! User doesn't exist..."
    });
  }
});
}
