/* Implemented by: Chris */
/*
  GET /tasklist?token=X&(userId=Y|householdId=Z)
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
  var householdId = req.query.householdId;

  /*
    currentUserId has to be in the same household as householdId or
    the household that userId is in
  */
  User.findOne({ '_id': currentUserId }, function(err, user) {
    if (err) {
      throw err;
    }
    if (user) {
      if (userId) {
        /* get user tasklist */
        utilities.checkUserIsInHousehold(userId, user.householdId, function(household) {
          if (household) {
            var userTasklist = household.taskList.filter(function(e, i, a) {
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
              message: 'userId wasn\'t in the same household as currentUserId.'
            });
          }
        });
      }
      else if (householdId) {
        /* get household tasklist */
        Household.findOne({ '_id': user.householdId }, function(err, hh) {
          res.json({
            success: true,
            tasklist: hh.taskList /* TODO: make sure this field is right */
          });
        });
      }
      else {
        /* no userId or householdId */
        res.json({
          success: false,
          message: 'Please supply either a userId or householdId.'
        });
      }
    }
    else {
      /* no user with currentUserId found */
      res.json({
        success: false,
        message: 'No user with that id found.'
      });
    }
  });
}
