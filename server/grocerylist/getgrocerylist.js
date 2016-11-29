// Implemented by Kaleb
var jwtDecode = require('jwt-decode');
var utilities = require('../utilities.js');

module.exports = function(req, res) {
  var token = req.query.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;

    utilities.getHouseholdFromUserId(currentUserId, function(hh) {
      if (!hh) {
        res.json({
          success: false,
          message: "Current user is not in a household."
        });
      }
      else {
        res.json({
          success: true,
          groceryList: hh.groceryList
        });
      }
    });

};
