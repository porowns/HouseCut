/* Implemented by: Chris */

/*
  POST /login

  with body:
  {
   email: String,
  password: String
  }
*/

var User = require('../models/user.js');
var crypto = require('crypto');
var rand = require('csprng');
var utilities = require('./../utilities.js');

module.exports = function(req, res) {
  var email = req.body.email;
  var password = req.body.password;

  if (!email || email === "") {
    res.json({
      success: false,
      message: 'Please enter an email.'
    });
  }
  else if (!password || password === "") {
    res.json({
      success: false,
      message: 'Please enter a password.'
    });
  }
  else {
    User.findOne({ 'email': email }, function(err, user) {
      if (err) {
        throw err;
      }
      if (user) {
        var hash_pw = crypto.createHash('sha512').update(user.salt + password).digest("hex");
        if (hash_pw == user.hashed_password) {
          var token = utilities.getToken(user);
          res.json({
            success: true,
            message: 'Login success',
            id: user._id,
            householdId: user.householdId,
            admin: user.admin,
            token: token,
            displayName: user.displayName
          });
        }
        else {
          res.json({
            success: false,
            message: 'Wrong password.'
          });
        }
      }
      else {
        res.json({
          success: false,
          message: 'Email not registered.'
        });
      }
    });
  }
}
