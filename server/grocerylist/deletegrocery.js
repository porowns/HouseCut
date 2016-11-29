var task = require('../models/task.js');
var jwtDecode = require('jwt-decode');
var Household = require('../models/household.js');
var utilities = require('../utilities.js');

module.exports = function(req,res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;

  var itemName = req.body.itemName;

  if (!itemName || itemName === "") {
    res.json({
      success: false,
      message: "Please enter a grocery item."
    });
  }

    utilities.getHouseholdFromUserId(currentUserId, function(hh) {
      if (!hh) {
        res.json({
          success: false,
          message: "User is not a member of a household."
        });
      }

      Household.update({ '_id': hh._id },
                       { $pull: { groceryList: { name: itemName } } },
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
           message: "No grocery with that name found."
          });
          return;
        }
        res.json({
          success: true,
          message: "Grocery successfully deleted."
        });
      });

    });
};
