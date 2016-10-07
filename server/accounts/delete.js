/* Implemented by: Chris */

/*
  POST /deleteaccount

  with body: { email: String, password: String, token: String }
*/

var User = require('../models/user.js');
var jwt = require('jsonwebtoken');
var crypto = require('crypto');
var rand = require('csprng');

module.exports = function(req, res) {
  var email = req.body.email;
  var password = req.body.password;

  if (!email || email == "") {
    res.json({
      success: false,
      message: 'Please enter an email.'
    });
  }
  else if (!password || password == "") {
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

          user.remove({ 'email': email }, function(err) {
            if (err) {
              res.json({
                success: false,
                message: err
              });
            }
            else {
              /* TODO: remove userId from Household member list and update all
              chores that were that user's turn to the next person / unassigned */
              res.json({
                success: true,
                message: 'Delete account success'
              });
            }
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
          message: 'No user with that email is registered.'
        });
      }
    });
  }
}
