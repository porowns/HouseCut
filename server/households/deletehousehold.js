// Implemented by Kaleb
var Household = require('../models/household');
var User = require('../models/user.js');

// To Depcryt Household Password
var jwt = require('jsonwebtoken');
var jwtDecode = require('jwt-decode');
var crypto = require('crypto');
var rand = require('csprng');

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;

  User.findOne({ '_id' : currentUserId}, function(err, user) {
    if (err)
      throw err;
    if (user) {
      if (user.admin == true) {
        Household.findOne({ '_id' : user.householdId}, function(err, household) {
          if (household) {
            household.HouseholdMembers.forEach(function(user) {
              User.update({ '_id' : user}, {$set: {'householdId' : 0}}, function(err, user) {
                console.log("User ID updated");
              });
            });

            household.remove(function(err) {
              if (err)
                throw err;
              else {
                res.json ({
                  success : true,
                  message : "Household deleted."
                });
              }
            });
          }
          else {
            res.json({
              success: false,
              message: "Users household ID is incorrect. Uh oh!"
            });
          }
        });
      }
      else {
        res.json ({
          success: false,
          message: "User is not admin."
        });
      }
    }
    else {
      res.json ({
        success: false,
        message: "User not found."
      });
    }
  });
}
