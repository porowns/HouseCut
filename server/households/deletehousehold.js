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

    }
    else {
      res.json ({
        success: false,
        message: "User not found."
      });
    }
  });
}
