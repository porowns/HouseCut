/* Implemented by: Chris */
/*
  POST /displayname

  with body:
  {
    token: [String],
    name: [String]
  }
*/

var jwtDecode = require('jwt-decode');
var User = require('./../models/user.js');
var utilities = require('./../utilities.js');

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;
  var name = req.body.name;

  if (!name || name == "") {
    res.json({
      success: false,
      message: "Enter a display name."
    });
    return;
  }

  User.update( { '_id' : currentUserId}, { $set: {'displayName': name} }, function(err) {
    if (err) {
      throw err;
    }
    res.json ({
      success: true,
      message: "Display name changed."
    });
    return;
  });
};
