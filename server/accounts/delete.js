/* Implemented by: Chris */

/*
  POST /deleteaccount

  with body: { token: String }
*/

var jwtDecode = require('jwt-decode');
var utilities = require('./../utilities.js');

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;

  utilities.deleteAccount(currentUserId, function(success) {
    if (success) {
      res.json({
        success: true,
        message: 'Account successfully deleted.'
      });
    }
    else {
      res.json({
        success: true,
        message: 'Failed to delete account.'
      });
    }
  });
};
