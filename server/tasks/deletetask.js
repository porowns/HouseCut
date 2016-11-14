/* Implemented by Chris and Kaleb */

/*
POST /household/deletetask
data: {
  token: [String],
  taskName: [String]
}
*/

var task = require('../models/task.js');
var jwtDecode = require('jwt-decode');
var Household = require('../models/household.js');
var utilities = require('../utilities.js');

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;

  var taskName = req.body.taskName;

  if (!taskName || taskName === "") {
    res.json({
      success: false,
      message: "Please enter a task name to delete (taskName)."
    });
    return;
  }

  utilities.getHouseholdFromUserId(currentUserId, function(hh) {
    if (!hh) {
      res.json({
        success: false,
        message: 'No Household for that user.'
      });
      return;
    }

    Household.update({ '_id': hh._id },
                     { $pull: { taskList: { name: taskName } } },
                     { safe: true },
    function(err, data) {
      if (err) {
        throw err;
      }
      console.log("num: " + data);
      console.log(JSON.parse(JSON.stringify(data)));
      if (data.nModified === 0) {
        res.json({
         success: false,
         message: "No task with that name found."
        });
        return;
      }
      res.json({
        success: true,
        message: "Task successfully deleted."
      });
    });

  });
};
