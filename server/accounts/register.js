/* Implemented by: Chris */

/*
  POST /register

  with body:
  {
    email: String,
    password: String,
    username: String
  }
*/

var User = require('../models/user.js');
var crypto = require('crypto');
var rand = require('csprng');

module.exports = function(req, res) {
  var username = req.body.username;
  var email = req.body.email;
  var password = req.body.password;

  if (username === "" || !username) {
    res.json({
      success: false,
      message: 'Please enter a username.'
    });
  }
  else if (email === "" || !email ||
            email.indexOf('@') === -1 ||
            email.indexOf('@') === email.length-1) {
    res.json({
      success: false,
      message: 'Please enter a valid e-mail.'
    });
  }
  else if (password === "" || !password) {
    res.json({
      success: false,
      message: 'Please enter a password.'
    });
  }
  else if (password.length < 6) {
    res.json({
      success: false,
      message: 'Please enter a password at least 6 characters long.'
    });
  }
  else {
    /* check if user with that email already exists */
    User.findOne({ 'email': email }, function(err, user) {
      if (err) {
        throw (err);
      }
      if (user) {
        res.json({
          success: false,
          message:'Email already registered.'});
        return;
      }
      else {
        /* hash the password */
        var salt = rand(160, 36);
        var newpass = salt + password;
        var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex");
        /* used for changing password */
        var id = crypto.createHash('sha512').update(email+rand).digest("hex");

        /* create user */
        var newUser = new User({
          displayName: username,
          email: email,
          hashed_password: hashed_password,
          salt: salt,
          id: id,
          householdId: "0"
        });

        /* add user to database */
        newUser.save(function(err) {
          if (err) throw err;

          console.log('User saved successfully');
          res.json({
            success: true,
            message: 'Register success'
          });
        });
      }
    });
  }
};
