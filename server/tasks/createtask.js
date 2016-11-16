/* Implemented by: Simon */
/* 'Navigated' by Chris */
/* Refactored by Kaleb & Chris 11/14/2016 */
var task = require('../models/task.js');
var jwtDecode = require('jwt-decode');
var Household = require('../models/household.js');
var utilities = require('../utilities.js');

/*
  Notes: 11/1/2016 3:17pm

  TODO:
  Implement Description
*/

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;

  var name = req.body.name;
  var type = req.body.type;
  var assigned = req.body.currentlyAssigned;
  var recurring = req.body.recurring;
  var recurringIntervalDays = req.body.recurringIntervalDays;

  if( !name || name === "" ) {
    res.json({
      success: false,
      message: "Give the task a name"
    });
  } else if (!type || !(type === "Rotating" ||
               type === "Voluntary" || type === "Assigned"))
  {
    res.json({
      success: false,
      message: "Specify type as 'Rotating', 'Voluntary', or 'Assigned'"
    });
  } else if ( (!assigned || assigned === "" ) && type === "Assigned" ) {
    res.json({
      success: false,
      message: "Specify who the task is assigned to."
    });
  } else if ( recurring === undefined ){
    res.json({
      success: false,
      message: "Set recurring to true or false."
    });
  } else if ( recurring === true && ( !recurringIntervalDays || recurringIntervalDays < 0)) {
    res.json({
      success: false,
      message: "Recurring: True -> error: recurringIntervalDays < 0"
    });
  } else {
    utilities.getHouseholdFromUserId(currentUserId, function(hh) {
      if (!hh) {
        res.json({
          success: false,
          message: 'User is not a member of a Household.'
        });
        return;
      }
      var sameNameIndex = hh.taskList.find(function(e) {
        return (e.name === name);
      });
      if (sameNameIndex !== undefined) {
        res.json({
          success: false,
          message: 'Task names must be unique.'
        });
        return;
      }

      var taskObject = new task({
        name: name,
        recurring: recurring,
        recurringIntervalDays: recurringIntervalDays || 0,
        type: type,
        currentlyAssigned: assigned || "0"
      });

      Household.update( { '_id' : hh._id }, { $push: {'taskList': taskObject} }, function(err) {
        if( err ) {
          throw err;
        } res.json({
          success: true,
          message: "Good job!"
        });
      });
    });
  }
};
