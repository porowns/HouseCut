/* Implemented by: Simon */

//database user
var User = require('../models/user.js');

var task = require('../models/task.js');
var jwtDecode = require('jwt-decode');
var Household = require('../models/household.js');
var utilities = require('../utilities.js');

/*
  Notes: 11/1/2016 3:17pm

  TODO:
  Implement Description
  Check name unique-ness

  When adding a task, check if taskName exists in list. <- Name unique-ness
  going to be in the call back function. database and come back

*/

module.exports = function(req, res) {
  var token = req.query.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;
  var householdId = decoded.householdId;

  var name = req.body.name;
  var type = req.body.type;
  var assigned = req.body.currentlyAssigned;
  var recurring = req.body.recurring;
  var recurringIntervalDays = req.body.recurringIntervalDays

  if( !name || name === "" ) {
    res.json({
      success: false,
      message: "Give the task a name"
    });
  } else if ( !type || type != "Rotating" ||
               type != "Voluntary" || type != "Assigned")
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
  } else if ( recurring === true && ( !recurringIntervalDays || recurringIntervalDays < 0) {
    res.json({
      success: false,
      message: "Recurring: True -> error: recurringIntervalDays < 0"
    })
  } else {

    var taskObject = new task({
      name: name,
      recurring: recurring,
      recurringIntervalDays: recurringIntervalDays || 0,
      type: type,
      currentlyAssigned: assigned || "0"
    });

    Household.update( { '_id' : householdId }, { $push: {'taskList': taskObject} }, function(err) {
      if( err ) {
        throw err;
      } res.json({
        success: true,
        message: "Good job!"
      });
    });

  }
}
