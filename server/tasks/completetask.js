/* Implemented by: Simon */
/* 'Navigated' by Chris */

var task = require('../models/task.js');
var jwtDecode = require('jwt-decode');
var Household = require('../models/household.js');
var utilities = require('../utilities.js');

/*
  Notes:

  3:49 PM 11/22/16
  TODO: Doesnt handle recurring tasks

*/

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;

  var name = req.body.name;

  if( !name || name === "" ) {
    res.json({
      success: false,
      message: "Provide a name"
    });
    return;
  }
  utilities.getHouseholdFromUserId(currentUserId, function(hh) {
    if (!hh) {
      res.json({
        success: false,
        message: 'User is not a member of a Household.'
      });
      return;
    }
    var sameNameTask = hh.taskList.find(function(e) {
      return (e.name === name);
    });
    if (sameNameTask === undefined) {
      res.json({
        success: false,
        message: 'Task not found.'
      });
      return;
    }
    var checkType = sameNameTask.type;
    switch (checkType) {
      case 'Voluntary':
        utilities.deleteTask(hh._id, name, function(){
          res.json({
            success: true,
            message: 'Task Complete -> Thanks for Volunteering!'
          });
          return;
        })
        break;
      case 'Rotating':
        var whosturn = hh.HouseholdMembers.find(function(e) {
          return (e === sameNameTask.currentlyAssigned );
        });
        if ( sameNameTask.currentlyAssigned === currentUserId ) {
          whosturn++;
          sameNameTask.currentlyAssigned = hh.HouseholdMembers[whosturn];
          res.json({
            success: true,
            message: 'Thanks! Assigning next user in line...'
          })
          return;
        } else {
          res.json({
            success: false,
            message: 'Not currently the passed users turn'
          });
          return;
        }
        break;
      case 'Assigned':
        if( sameNameTask.currentlyAssigned === currentUserId ) {
          utilities.deleteTask(hh._id, name, function(){
            res.json({
              success: true,
              message: 'Task Complete -> Thanks for Volunteering!'
            });
          })
          return;
        } else {
          res.json({
            success: false,
            message: "You aren't assigned to complete this task."
          })
          return;
        }
        break;
    }
  });
};
